/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import ru.ifmo.cs.bcomp.Assembler;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class AssemblerView extends ActivateblePanel {
	private GUI gui;
	private CPU cpu;
	private ComponentManager cmanager;
	private Assembler asm;
	private JTextArea text;

	public AssemblerView(GUI gui) {
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();

		asm = new Assembler(cpu.getInstructionSet());

		text = new JTextArea();
		text.setFont(FONT_COURIER_BOLD_21);
		JScrollPane scroll = new JScrollPane(text);
		scroll.setBounds(TEXTAREA_X, TEXTAREA_Y, TEXTAREA_WIDTH, TEXTAREA_HEIGHT);
		add(scroll);

		JButton button = new JButton("Компилировать");
		button.setForeground(Color.BLACK);
		button.setFont(FONT_COURIER_PLAIN_12);
		button.setBounds(625, 1, 200, BUTTONS_HEIGHT);
		button.setFocusable(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cmanager.getRunningState()) {
					showError("Для компиляции остановите выполняющуюся программу");
					return;
				}

				boolean clock = cpu.getClockState();
				cpu.setClockState(true);

				try {
					asm.compileProgram(text.getText());
					asm.loadProgram(cpu);
				} catch (Exception ex) {
					showError(ex.getMessage());
				}

				cpu.setClockState(clock);
			}
		});
		add(button);
	}

	@Override
	public void panelActivate() {
		text.requestFocus();
	}

	@Override
	public void panelDeactivate() { }

	@Override
	public String getPanelName() {
		return "Ассемблер";
	}

	private void showError(String msg) {
		JOptionPane.showMessageDialog(gui, msg, "Ошибка", JOptionPane.ERROR_MESSAGE);
	}
}
