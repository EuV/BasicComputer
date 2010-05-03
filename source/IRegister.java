/*-----------------------------------------------------------------------------
    Интерфейс регистра/памяти
-----------------------------------------------------------------------------*/
abstract interface IRegister
{
	int		SendData();              	// Метод выгрузки данных
	void	GetData(int input); 	// Метод загрузки данных
	int		Width();                 	// Разрядность регистра
}