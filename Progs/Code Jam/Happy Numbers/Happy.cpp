#include <iostream>
#include <cstring>
#include <cmath>
using namespace std;

int change_base(int num, int base)
{
 int sum=0;
 while (num)
	{cout<<num%base;
	 sum+=pow((num%base),2);
	 num/=base;
	}
 cout<<endl;
 return sum;
}

int main()
{
 int happy[10][1001], base, no, temp, sum;
 //cin >> no >> base;
 memset (happy, 2, sizeof(happy));
 base = 3;
 cin >> no;
 	//for ( no=2; no<1001; no++)
 		{sum=0;
		 temp=no;
		 //temp=change_base(temp, base);
		 if(happy[base][no])
		 while(1)
		 {
		  cout << (temp=change_base(temp, base));
		  cout <<endl;
		  if(temp==1)
			{happy[base][no]=1;
			 break;
			}
		  if(temp==no || happy[base][temp]==0)
			{happy[base][no]=0;
	 		 break;
			}
		  cin>>happy[6][7];
		 }
		sum=0;
		}
 //cout << (temp = change_base(no, base));
} 
 