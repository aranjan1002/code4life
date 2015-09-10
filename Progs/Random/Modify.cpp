#include <iostream>

using namespace std;

int main()
{
 char temp[1034];
 while (cin.getline(temp, 1000))
	{
 	 if(temp[0]!='A' && temp[1]!='b')
	 cout << temp << " MIA ORD" << endl;
	}
}
