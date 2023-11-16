#include<string>
#include<conio.h>
#include<Windows.h>
#include<fstream>
#include <iostream>
#include<vector>

const int MAX_MESSAGE_SIZE = 20;

int main(int argc, char* argv[])
{
	setlocale(LC_ALL, "ru");
	std::string fileName = argv[1];
	HANDLE hStartEvent = OpenEvent(EVENT_MODIFY_STATE, FALSE, L"StartProcess");
	if (hStartEvent == NULL)
	{
		std::cout << "Окаянные ящеры сломали компьютер!\n";
		std::cin.get();
		return GetLastError();
	}

	HANDLE hInputSemaphore = OpenSemaphore(EVENT_ALL_ACCESS, FALSE, L"Input Semaphore started");
	if (hInputSemaphore == NULL) {
		return GetLastError();
	}
	HANDLE hOutputSemaphore = OpenSemaphore(EVENT_ALL_ACCESS, FALSE, L"Output Semaphore started");
	if (hOutputSemaphore == NULL) {
		return GetLastError();
	}

	int userChoice;
	std::cout << "Введите 1 - хотите отправить сообшение, 0 - хотите закрыть программу : ";
	std::cin >> userChoice;


	while (true) {
		if (userChoice == 1) {
			std::cout << "Сюда глаголь : ";
			std::string message;
			std::cin >> message;

			if (message.size() > MAX_MESSAGE_SIZE) {
				std::cout << "Ах ты, ящер окаянный, глаголь да не заглогаливайся(максимальный размер сообщения : 20 символов\n";
				std::cout << "Введите 1 - хотите отправить сообшение, 0 - хотите закрыть программу : ";
				std::cin >> userChoice;
				continue;
			}

			std::ofstream out(fileName, std::ios::binary | std::ios::app);
			WaitForSingleObject(hInputSemaphore, INFINITY);
			out << message << "\n";
			out.close();

			ReleaseSemaphore(hOutputSemaphore, 1, NULL);
			std::cout << "Введите 1 - хотите отправить сообшение, 0 - хотите закрыть программу : ";
			std::cin >> userChoice;
		}
		else if (userChoice == 0) {
			break;
		}
		else
		{
			std::cout << "Ящерский ввод, пробуй еще!";
			std::cout << "Введите 1 - хотите отправить сообшение, 0 - хотите закрыть программу : ";
			std::cin >> userChoice;
		}
	}

	return 0;
}