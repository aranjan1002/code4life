#include <iostream>

using namespace std;

int main()
{char temp[1243];
 while (cin.getline(temp, 1000))
	{
	 cout << "insert into Airport values('" << temp << "')" <<endl;
	}
}