#include <iostream>
#include "MinHeap.cpp"

using namespace std;

MinHeap<int> *list = new MinHeap<int>();
int x;
x=5;

int main()
{
 
 int val, choice;

 while(1)
 {
  cout << "1. Enter Data" << endl;
  cout << "2. Delete Data" << endl;
  cout << "3. Display Data" << endl;
  cin >> choice;
  switch (choice)
	{
	 case 1:
		cout << "Enter the value: ";
		cin >> val;
		list->Push(val);
		break;
	 case 2:
		list->Pop();
		break;
	 case 3:
		list->Display();
		break;
	 case 4:
		cout << list->Top();
		break;
	 default:
		cout << "Invalid choice" << endl;
	}
 }
}
