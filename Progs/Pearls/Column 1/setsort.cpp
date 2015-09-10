#include <iostream>
#include <set>

using namespace std;

int main()
{
 set <int> s;
 long int i;
 set <int> :: iterator j;
 while (cin >> i)
	s.insert(i);
 for (j=s.begin(); j!=s.end(); j++)
	cout << *j << "\n";
 cout << (i=9999999999999);
}
