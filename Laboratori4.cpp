#include<string>
#include<conio.h>
#include<Windows.h>
#include<fstream>
#include <iostream>
#include<vector>

std::string lineReader(std::string name) {

	std::ifstream in(name, std::ios::binary);
	std::vector<std::string> vectorOfString;

	while (!in.eof()) {
		std::string temp;
		std::getline(in, temp);
		vectorOfString.push_back(temp);
	}

	in.close();
	std::ofstream out(name, std::ios::binary);

	for (size_t i = 1; i < vectorOfString.size(); i++) {
		out << vectorOfString[i] << "\n";
	}

	return vectorOfString[0];
}


int main()
{
	setlocale(LC_ALL, "ru");
	std::cout << "Введите имя файла ввода : ";
	std::string inputFileName;
	std::cin >> inputFileName;

	std::ofstream fin(inputFileName, std::ios::binary);
	fin.close();
	std::cout << "Введите количество записей : ";
	int numOfRecords = 0;
	std::cin >> numOfRecords;
	std::cout << "Введите количество процессов sender: ";
	int numOfSenders = 0;
	std::cin >> numOfSenders;

	HANDLE hInputSemaphore = CreateSemaphore(NULL, 0, numOfRecords, L"Input Semaphore started");
	if (hInputSemaphore == NULL) {
		return GetLastError();
	}
	HANDLE hOutputSemaphore = CreateSemaphore(NULL, 0, numOfRecords, L"Output Semaphore started");
	if (hOutputSemaphore == NULL) {
		return GetLastError();
	}

	HANDLE* hEventStarted = new HANDLE[numOfRecords];	
	for (int i = 0; i < numOfSenders; i++) {
		STARTUPINFO si = { 0 };
		PROCESS_INFORMATION piApp = { 0 };
		std::wstring CommandLine = (L"Sender.exe " + std::wstring(inputFileName.begin(), inputFileName.end()) + L" " + std::to_wstring(numOfRecords) + L" ") + std::to_wstring(i + 1);
		LPWSTR lpszCommandLine = &CommandLine[0];

		if (!CreateProcess(NULL, lpszCommandLine, NULL, NULL, TRUE, CREATE_NEW_CONSOLE, NULL, NULL, &si, &piApp)) {
			std::cout << "Обломись!\n";
			return (int)GetLastError();
		}

		hEventStarted[i] = CreateEvent(NULL, FALSE, FALSE, L"StartProcess");
	}

	WaitForMultipleObjects(numOfSenders, hEventStarted, TRUE, INFINITY);

	int userChoice;
	std::cout << "Введите 1 - хотите получить сообшение, 0 - хотите закрыть программу : ";
	std::cin >> userChoice;


	while (true) {
		switch (userChoice) {
		case 1:
			WaitForSingleObject(hInputSemaphore, INFINITY);
			std::cout << "Полученное сообщение : " << lineReader(inputFileName) << "\n";
			ReleaseSemaphore(hOutputSemaphore, 1, NULL);
			std::cout << "Введите 1 - хотите получить сообшение, 0 - хотите закрыть программу : ";
			std::cin >> userChoice;
			continue;
		case 0:
			return 0;
		default:
			std::cout << "Ящерский ввод, пробуй еще!";
			std::cout << "Введите 1 - хотите получить сообшение, 0 - хотите закрыть программу : ";
			std::cin >> userChoice;
		}
	}

	for (size_t i; i < numOfSenders; i++) {
		CloseHandle(hEventStarted[i]);
	}
	return 0;
}