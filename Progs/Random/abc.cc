#include <iostream>
#include <cstdio>

using namespace std;

int main () {
  int x[2][4];
  cout << x << endl;
  printf("%d\n", (int) &x[0][1]);
  cout << &x[1][1] << endl;
}
