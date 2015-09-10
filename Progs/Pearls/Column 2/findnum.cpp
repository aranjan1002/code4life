#include <iostream>
using namespace std;

int main()
{
 int a[65535], b[65535],i, cnt2, cnt1, bit, temp=1, count1, count2, flag;
 
 for(i=0; i<65535; i++)
	{cin >> a[i];
	 //cout << i << endl;
	}
 
 cnt1=0;
 cnt2=0;
 bit=15;
 temp = temp << bit;
 for(i=0; i<65535; i++)
	if(a[i] & (temp))
		a[cnt1++]=a[i];
	else {b[cnt2++]=a[i];
	      //cout << a[cnt2-1] << endl;
	     }
 //cout << cnt1 << " " << cnt2 << endl;
 while(!(cnt1 == 1 || cnt2 == 1))
 {
 if(cnt1 < cnt2)
	flag = 1;
 else flag = 2;
 bit--;
 count1 = cnt1;
 count2 = cnt2;
 cnt1 = 0;
 cnt2 = 0;
 //temp = temp >> 1;
 if (flag == 1)
	{for(i=0; i<count1; i++)
		if(a[i] & (1<<bit))
			a[cnt1++] = a[i];
		else b[cnt2++] = a[i];}
 else {for(i=0; i<count2; i++)
		if(b[i] & (1<<bit))
			a[cnt1++] = b[i];
		else b[cnt2++] = b[i];}
 //cout << cnt1 << " " << cnt2 << endl;
 }
 /*for (i=0; i<cnt1; i++)
	cout << a[i] << endl;
 cout << endl;
 for (i=0; i<cnt2; i++)
	cout << b[i] << endl;*/
 if(cnt1==1)
	{if(a[0]%2)
		cout << "Missing Number is " << a[0]-1;
	 else cout << "Missing Number is " << a[0]+1;
	}
 else {
 	if(b[0]%2)
		cout << "Missing Number is " << b[0]-1;
	 else cout << "Missing Number is " << b[0]+1;
	} 
 cout << endl;
}