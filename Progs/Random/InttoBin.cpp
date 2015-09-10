#include <iostream>
using namespace std;

void inttobin(int n)
{
 if(n!=0)
	inttobin(n>>1);
 cout << (n&1);
}

int main()
{
 int i;
 cin >> i;
 inttobin(i);
}