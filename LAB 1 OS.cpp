#include<iostream>
#include<fstream>
#include<string>
#include<windows.h>

using namespace std;

const int maxNameLength = 10;

struct employee {
	int num;
	char name[maxNameLength];
	double hours;
};

void createBinFile(string file, int numRecords) {
	ofstream fout(file, ios::binary);
	employee human;

	for (int i = 0; i < numRecords; i++) {
		cout << "Enter the data about employee number " << i + 1 << "\n";
		cout << "Number : ";
		cin >> human.num;
		cout << "Name : ";
		cin >> human.name;
		cout << "Number of hours worked : ";
		cin >> human.hours;
		fout.write((char*)&human, sizeof(human));
	}
}

void generateReport(string binFileName, string reportFileName, double payPerHour) {
	ifstream fin(binFileName, ios::binary);
	ofstream fout(reportFileName);

	fout << "File report : \t" << binFileName << "\n";
	fout << "Employee number\tName\tHours\tSalary\n";

	employee human;

	while (fin.read((char*)&human, sizeof(human))) {
		double salary = human.hours * payPerHour;

		fout << "\t" << human.num << "\t" << human.name << "\t" << human.hours << "\t" << salary << "\n";
	}
}

int main() {
	string binFileName;
	string reportFileName;
	int numRecords;
	double payPerHour;

	cout << "Enter the name of binary file : ";
	cin >> binFileName;
	cout << "Enter the num of records : ";
	cin >> numRecords;

	createBinFile(binFileName, numRecords);
	string createCommand = "Creator " + binFileName + " " + to_string(numRecords);
	STARTUPINFO siCreator = { sizeof(siCreator) };
	PROCESS_INFORMATION piCreator;

	CreateProcess(NULL, (LPWSTR)createCommand.c_str(), NULL, NULL, FALSE, 0, NULL, NULL, &siCreator, &piCreator);

	WaitForSingleObject(piCreator.hProcess, INFINITY);

	ifstream finBin(binFileName, ios::binary);
	employee human;

	while (finBin.read((char*)&human, sizeof(human))) {
		cout << human.num << "\t" << human.name << "\t" << human.hours << "\n";
	}

	cout << "Enter the name report file ";
	cin >> reportFileName;
	cout << "Enter pay per hour : ";
	cin >> payPerHour;

	generateReport(binFileName, reportFileName, payPerHour);
	string reporterCommand = "Reporter " + binFileName + " " + reportFileName + " " + to_string(payPerHour);
	STARTUPINFO siReporter;
	PROCESS_INFORMATION piReporter;

	CreateProcess(NULL, (LPWSTR)reporterCommand.c_str(), NULL, NULL, FALSE, 0, NULL, NULL, &siReporter, &piReporter);

	WaitForSingleObject(piReporter.hProcess, INFINITY);

	ifstream finRep(reportFileName);
	string line;

	while (getline(finRep, line)) {
		cout << line << "\n";
	}

	CloseHandle(piCreator.hProcess);
	CloseHandle(piReporter.hProcess);

	finBin.close();
	finRep.close();
	return 0;
}