#include <iostream>
#include <cstdlib>
using namespace std;

class test {
  int x;
public:
  void print() {
    cout << "Hello world";
  }
};

int main()
{
  test t;
  t.print();
}



