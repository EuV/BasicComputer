/////////////////////////////////////////////////////////////////////////////////////////////
// ���������� �������-������� ��� �������-������
/////////////////////////////////////////////////////////////////////////////////////////////
abstract class EChanellRegToReg implements IChanell 
{
	IRegister destination;  // ����������
	IRegister source;       // ��������
	boolean visible;        // "���������" ������ 
	boolean sending;        // ���������
	
	EChanellRegToReg(IRegister dst, IRegister src)
	{
		destination = dst;
		source = src;
	}
	
	public void Send()
	{
		
	}
	
	public void Close()
	{
		
	}
	
}
