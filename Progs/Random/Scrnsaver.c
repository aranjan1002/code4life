#include <cstdio>
#include <cstdlib>
#include <ctime>

/*int main()
{
 system("w");
}*/
#include <stdio.h>
#include <time.h>

void wait ( int seconds )
{
  clock_t endwait;
  endwait = clock () + seconds * CLOCKS_PER_SEC ;
  while (clock() < endwait) {}
}

int main ()
{
  int n;
  printf ("Starting countdown...\n");
  for (n=5; n>0; n--)
  {
    printf ("%d\n",n);
    system("w");
    wait (1);
  }
  printf ("FIRE!!!\n");
  return 0;
}
