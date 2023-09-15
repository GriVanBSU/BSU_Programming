#include <iostream>
#include <thread>
#include<vector>
#include<chrono>
#include<algorithm>

using namespace std;

void min_max(int& min, int& max, vector<int>& vec) {

	min = vec[0];
	max = vec[0];
	for (int i = 1; i < vec.size(); i++) {
		if (min > vec[i]) {
			min = vec[i];
		}

		this_thread::sleep_for(chrono::milliseconds(7));

		if (max < vec[i]) {
			max = vec[i];
		}

		this_thread::sleep_for(chrono::milliseconds(7));
	}

	cout << "minimun is " << min << "\n";
	cout << "maximum is " << max << "\n";
}

void findAvarage(int& average, vector<int>& vec) {
	int sum = 0;
	for (int i = 0; i < vec.size(); i++) {
		sum += vec[i];
		this_thread::sleep_for(chrono::milliseconds(12));
	}

	average = sum / vec.size();

	cout << "average is " << average << "\n";
}

int main() {
	setlocale(LC_ALL, "ru");

	vector<int> numbers;
	int size = 0;
	int min, max = 0;
	int average = 0;
	int num = 0;

	cout << "Введите размер массива : ";
	cin >> size;


	cout << "Введите элементы массива : ";
	for (int i = 0; i < size; i++) {
		cin >> num;
		numbers.push_back(num);
	}

	thread minMaxThread(min_max, ref(min), ref(max), ref(numbers));
	thread avarageThread(findAvarage, ref(average), ref(numbers));

	avarageThread.join();
	minMaxThread.join();

	replace(numbers.begin(), numbers.end(), min, average);
	replace(numbers.begin(), numbers.end(), max, average);

	cout << "Результат:";
	for (int i = 0; i < numbers.size(); i++) {
		cout << numbers[i] << " ";
	}

	return 0;
}