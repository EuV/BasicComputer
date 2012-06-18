/**
 * $Id$
 */

package ru.ifmo.it.elements;

public class Comparer extends DataHandler {
	private DataSource input;
	private int startbit;

	public Comparer(DataSource input, int startbit, DataSource ... ctrls) {
		super(1, ctrls);

		this.input = input;
		this.startbit = startbit;
	}

	public void setValue(int ctrl) {
		super.setValue(((input.getValue() >> startbit) & 1) == ctrl ? 1 : 0);
	}
}
