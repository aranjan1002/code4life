#include <iostream>
#include <cstring>
#include "bignum.h"

using namespace std;

int main()
{
 bignum a;
 char b[2032];
 cin >> b;
 a = b;
 cout << (a.power(100)).str();
}