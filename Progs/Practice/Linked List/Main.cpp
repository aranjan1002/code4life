#include "LinkedList.h"
#include <iostream>
#include <cstdlib>

using namespace std;

int main()
{
 int val, choice;
 LinkedList *list = new LinkedList();
 
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
		list->insert(val);
		break;
	 case 2:
		list->deletenode();
		break;
	 case 3:
		list->display();
		break;
	 case 4:
		list->reverse();
		break;
	 default:
		cout << "Invalid choice" << endl;
	}
 }
}
