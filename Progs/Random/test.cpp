#include <iostream>
#include<cstdio>
#include<cstring>

using namespace std;

int main(int argc, char *argv[])
{
  char    abc[27];
  char    *ptr = abc;
  strcpy(abc, "abcdefgxyz");
  /*
   * What are the types and values of expressions:
   */
  cout << abc << endl << *abc << &abc[3] << endl;
  cout << abc + 4 << endl << *(abc + 5) + 20000 << endl << abc[10] << endl << abc[12] << endl << &ptr << endl;
  /* 4. &abc[3] //&abc[3] = abc + 3*sizeof(char)
   * 5. abc+4
   * 6. *(abc+5) + 20000  //h  long long x = int * 1LL * int
   * 7. abc[10] //'\0'
   * 8. abc[12] //memset ()
   * 9. &ptr //char**
   */
  return 0;
}
