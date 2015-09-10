#include <iostream>
#include <cmath>
using namespace std;

int convert (int base, int num)
{
 long sum1=1, sum2=0;
 while (num>0)
	{
	 sum1 = (sum1*10) + (num % base);
	 num /= base;
	}
 while (sum1>9)
	{
	 sum2 = (sum2*10)+(sum1 % 10);
	 sum1 /= 10;
	}
 return(sum2);
}

int toten (int base, int num)
{ 
 long sum = 0, cnt = 0;
 while (num)
	{
	 sum += num%10 * pow(base, cnt++);
	 num /= 10;
	}
 return (sum);
}

int square (int base, int num)
{
 long sum=0;
 //num = convert(base, num);
 while(num)
	{
	 sum = convert(base, (toten(base, sum) + (num%10) * (num%10)));
	 num /= 10;
	}
 return (sum);
}

int main()
{
 long count = 0, temp = 14, num, base, i, cycle[50], j;
 cin >> base;
 for ( i=2 ; i<=1000 ; i++)
	{
	 count = 0;
	 temp = convert(base,i);
	 //cout << i << " F " << temp << " f ";
	 do
	 {
	  temp = square (base, temp);
	  cycle[count++] = temp;
	  //cout << temp << " ";
	  for ( j=0 ; j < count-1 ; j++)
		if (temp == cycle[j])
			temp = 0;
	  if (temp == 0)
		break;
	 }
	 while (temp != 1);
        if (temp == 1)
		cout << i << " ";
	} 
 
}
