package Machine;

/**
 * Устройство управления
 * @author Ponomarev
 *
 */

enum Cycle {
	INSTRFETCH, ADDRFETCH, EXECUTION, ANOTHER
}
 
public class ManagerDevice
{
	public ManagerDevice(RegisterFactory reg_factory, ChannelFactory channels, ALU alu, FlagFactory flag_factory, DeviceFactory dev)
	{
		this.instr_pointer = reg_factory.getMicroInstructionPointer();
		this.reg_factory = reg_factory;
		this.flag_factory = flag_factory;
		this.channels = channels;
		this.alu = alu;
		this.dev = dev;
	}
	
	private boolean checkBit(int bits, int number)
	{
		if ((bits & (int) StrictMath.pow(2, number)) != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void timeStep()
	{
		alu.resetALU(); // Отключение инверторов и инкрементора
		
		channels.CloseAllChannels(); // Закрыть все каналы
		dev.closeAllChannels(); // Закрыть все каналы ВУ
		
		channels.MicroCommandToRMC().open(); // Пересылаем микрокоманду в регистр микрокоманд

		// Установка/сброс битов РС
		updateStateBits();
		
		flag_factory.refreshStateCounter();
		
		reg_factory.getMicroInstructionPointer().setValue(reg_factory.getMicroInstructionPointer().getValue()+1);
		int command = reg_factory.getMicroCommandRegister().getValue();
		if (checkBit(command, 15))
		{
			////////////////////////////////////
			// Управляющая микрокоманда (УМК) //
			////////////////////////////////////
			
			// Поле (бит сравнения)
			boolean compare_bit = checkBit(command, 14);
			
			Register compare_reg = null;
			
			// РС - проверяемый регистр
			if (!checkBit(command, 13) && !checkBit(command, 12)) compare_reg = reg_factory.getStateCounter();
			
			// РД - проверяемый регистр
			if (!checkBit(command, 13) && checkBit(command, 12)) compare_reg = reg_factory.getDataRegister();
			
			// РК - проверяемый регистр
			if (checkBit(command, 13) && !checkBit(command, 12)) compare_reg = reg_factory.getCommandRegister();
			
			// А - проверяемый регистр
			if (checkBit(command, 13) && checkBit(command, 12)) compare_reg = reg_factory.getAccumulator();
			
			// Проверяемый бит
			int choose_bit =(command & 0xf00)>>8;
			
			// Сравнение
			if ( checkBit(compare_reg.getValue(), choose_bit) == compare_bit )
			{
				instr_pointer.setValue(command & 0xff);
			}
			
		}
		else
		{
			if (!checkBit(command, 14))
			{
			/////////////////////////////////////
			// Операционая микрокоманда (ОМК0) //
			/////////////////////////////////////
				
			// Выбираем левый вход
				
				// На левый вход ноль
				if (!checkBit(command, 13) && !checkBit(command, 12)) reg_factory.getLeftALUInput().setValue(0);
				
				// На левый вход аккумулятор
				if (!checkBit(command, 13) && checkBit(command, 12)) channels.FromAcc().open();
				
				// На левый вход регистр состояния
				if (checkBit(command, 13) && !checkBit(command, 12)) channels.FromSC().open();
				
				// На левый вход клавишный регистр
				if (checkBit(command, 13) && checkBit(command, 12)) channels.FromIR().open();
				
			// Выбираем правый вход
				
				// На правый вход ноль
				if (!checkBit(command, 9) && !checkBit(command, 8)) reg_factory.getRightALUInput().setValue(0);
				
				// На правый вход регистр данных
				if (!checkBit(command, 9) && checkBit(command, 8)) channels.FromDR().open();

				// На правый вход регистр команд
				if (checkBit(command, 9) && !checkBit(command, 8)) channels.FromCR().open();

				// На правый вход счетчик команд
				if (checkBit(command, 9) && checkBit(command, 8)) channels.FromIP().open();
				
			// Обратный код
				
				// Правый вход
				if (checkBit(command, 7)) alu.setRightReverse();

				// Левый вход
				if (checkBit(command, 6)) alu.setLeftReverse();
				
			// Операция
				
				// Лев.вх + Прав.вх
				if (!checkBit(command, 5) && !checkBit(command, 4)) alu.add(); 

				// Лев.вх + Прав.вх + 1
				if (!checkBit(command, 5) && checkBit(command, 4)) 
				{
					alu.setIncrement();
					alu.add();
				}
				
				// Лев.вх & Прав.вх
				if (checkBit(command, 5) && !checkBit(command, 4)) alu.and();
				
			//Сдвиги
				
				// Сдвиг вправо
				if (!checkBit(command, 3) && checkBit(command, 2)) alu.ror();
				
				// Сдвиг влево
				if (checkBit(command, 3) && !checkBit(command, 2)) alu.rol();

			// Работа с памятью
				// Чтение
				if (!checkBit(command, 1) && checkBit(command, 0)) channels.ReadFromMem().open();
				
				// Запись
				if (checkBit(command, 1) && !checkBit(command, 0)) channels.WriteToMem().open();				
			}
			else
			{
			/////////////////////////////////////
			// Операционая микрокоманда (ОМК1) //
			/////////////////////////////////////
				
			//Управление обменом с ВУ
				
				// Разрешение прерывания
				if (checkBit(command, 11))
				{
					flag_factory.getInterruptEnable().setFlag();
					flag_factory.refreshStateCounter();
				}
				
				// Запрещение прерывания
				if (checkBit(command, 10))
				{
					flag_factory.getInterruptEnable().clearFlag();
					flag_factory.refreshStateCounter();
				}
				
				// Сброс флагов ВУ
				if (checkBit(command, 9)) dev.clearAllFlags();
				
				// Организация связей с ВУ
				if (checkBit(command, 8))
				{
					int cmd = (reg_factory.getCommandRegister().getValue()&0x0f00)>>8;
					int dev_adr = reg_factory.getCommandRegister().getValue()&0x00ff;
					
					flag_factory.getInputOutput().setFlag();

					switch(cmd)
					{
						case 0:
							// clf B
							if (dev.getInternalDevice(dev_adr) != null) dev.getInternalDevice(dev_adr).getStateFlag().clearFlag();
							break;
						case 1:
							// tsf B
							if (dev.getInternalDevice(dev_adr) != null)
							{
								dev.getInternalDevice(dev_adr).getStateFlagChannel().open();
								if ( dev.getInternalDevice(dev_adr).getStateFlag().getValue() == 1)
								{
									reg_factory.getInstructionPointer().setValue(reg_factory.getInstructionPointer().getValue()+1);
								}
							}
							break;
						case 3:
							// OUT B
							if ((dev_adr == 1) || (dev_adr == 3))
							{
								dev.getInternalDevice(1).getAddressChannel().open();
								dev.getInternalDevice(1).getIORequestChannel().open();
								dev.getInternalDevice(2).getAddressChannel().open();
								dev.getInternalDevice(2).getIORequestChannel().open();
								dev.getInternalDevice(3).getAddressChannel().open();
								dev.getInternalDevice(3).getIORequestChannel().open();
								
								
								dev.getInternalDevice(dev_adr).getOutputChannel().open();
							}
							break;
						case 2:
							// IN B
							if ((dev_adr == 2) || (dev_adr == 3))
							{
								dev.getInternalDevice(1).getAddressChannel().open();
								dev.getInternalDevice(1).getIORequestChannel().open();
								dev.getInternalDevice(2).getAddressChannel().open();
								dev.getInternalDevice(2).getIORequestChannel().open();
								dev.getInternalDevice(3).getAddressChannel().open();
								dev.getInternalDevice(3).getIORequestChannel().open();
								
								dev.getInternalDevice(dev_adr).getInputChannel().open();
							}
							break;
						default: break;
					}	
				}
				
			// Регистр C	
				
				// Перенос
				if (!checkBit(command, 7) && checkBit(command, 6)) alu.setCIfExist();

				// Сброс
				if (checkBit(command, 7) && !checkBit(command, 6)) alu.clearC();
				
				 // Установка
				if (checkBit(command, 7) && checkBit(command, 6)) alu.setC();
				
			// Установка регистра N
				if (checkBit(command, 5)) alu.setN();
				
			// Установка регистра Z
				if (checkBit(command, 4)) alu.setZ();
				
			// Остановка ЭВМ
				if (checkBit(command, 3)) {
					flag_factory.getAddressSelection().clearFlag();
					flag_factory.getInstructionFetch().clearFlag();
					flag_factory.getExecution().clearFlag();
					flag_factory.getProgram().clearFlag();
				}
				
			// Выход АЛУ (Содержимое БР)
				// в РА
				if (!checkBit(command, 2) && !checkBit(command, 1) && checkBit(command, 0)) channels.ToAR().open();

				// в РД
				if (!checkBit(command, 2) && checkBit(command, 1) && !checkBit(command, 0)) channels.ToDR().open();
				
				// в РК
				if (!checkBit(command, 2) && checkBit(command, 1) && checkBit(command, 0)) channels.ToCR().open();
				
				// в СК
				if (checkBit(command, 2) && !checkBit(command, 1) && !checkBit(command, 0)) channels.ToIP().open();

				// в A
				if (checkBit(command, 2) && !checkBit(command, 1) && checkBit(command, 0)) channels.ToAcc().open();
				
				// в РА, РД, РК, А
				if (checkBit(command, 2) && checkBit(command, 1) && checkBit(command, 0)) 
				{
					channels.ToAR(). open();
					channels.ToDR(). open();
					channels.ToCR(). open();
					channels.ToAcc().open();
				}
			}
		}
	}
	
	private void updateStateBits()
	{
		// В/В осуществляется только в определённых микрокомандах
		flag_factory.getInputOutput().clearFlag();

		// Установка PC(5)
		if ((flag_factory.getInterruptEnable().getValue() == 1) && dev.deviceRequest())
			flag_factory.getInterruption().setFlag();
		else
			flag_factory.getInterruption().clearFlag();

		int n = reg_factory.getMicroInstructionPointer().getValue();
		Cycle cycle;

		if ((n <= 0xC) || ((n >= 0x5E) && (n <= 8D)) || (n >= 0xE0))
			cycle = Cycle.INSTRFETCH;
		else if ((n >= 0xD) && (n <= 0x1C))
			cycle = Cycle.ADDRFETCH;
		else if (((n >= 0x1D) && (n <= 0x5B)) || ((n >= 0xB0) && (n <= 0xDF)))
			cycle = Cycle.EXECUTION;
		else
			cycle = Cycle.ANOTHER;

		flag_factory.getInstructionFetch().setValue(cycle == Cycle.INSTRFETCH);
		flag_factory.getAddressSelection().setValue(cycle == Cycle.ADDRFETCH);
		flag_factory.getExecution().setValue(cycle == Cycle.EXECUTION);
		flag_factory.getProgram().setValue(cycle != Cycle.ANOTHER);
	}
	
	private Register			instr_pointer;
	private RegisterFactory	reg_factory;
	private ChannelFactory		channels;
	private ALU				alu;
	private FlagFactory		flag_factory;
	private DeviceFactory		dev;
}
