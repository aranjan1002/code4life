#include<time.h>
#include<stdio.h>
#include<stdlib.h>

int main(void)
{
  clock_t ticks1, ticks2;
  int i=1;
  for(i=0; i<100; i++)
	{system("ioreg -c IOHIDSystem | perl -ane 'if (/Idle/) {$idle=(pop @F)/1000000000; print $idle,\"\\n\"; last}'");
	 sleep(1);
	}
  return 0;
}
