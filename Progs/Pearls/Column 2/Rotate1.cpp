#include <iostream>
#include <cstring>
using namespace std;

int main()
{int t, r;
 char temp;
 char *str = new char(), *temp2 = new char();
 cin >> str;
 cin >> r;
 temp = str[r];
 str[r]=0;
 strcpy(temp2, str);
 str[r]=temp;
 t=r;
 while(str[t])
	str[t-r]=str[t++];
 strcpy(&str[t-r], temp2);
 cout << str << endl;
}
