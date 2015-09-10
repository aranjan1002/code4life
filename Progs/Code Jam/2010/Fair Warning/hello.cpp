#include "bignum.h"
#include <iostream>

using namespace std;

int main()
{
 bignum i;
 int j = 3;
 i = j;
 
 cout << i.str();
}