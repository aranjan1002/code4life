#include <iostream>
#include <cstring>

#define x(y) 4+y
#define GCD1(x, y) GCD(x, y);		//while(x!=0 && y!=0){if(x>y) x=x%y;  else y=y%x;}


using namespace std;

int GCD(int x, int y)
{
 if(x==0)
	return(y);
 else if(y==0)
	return(x);
 else if(x>y)
	return(GCD(x%y, y));
 else return(GCD(x, y%x));
}

/*int GCD1(int x, int y)
{
 while(x!=0 && y!=0)
	{
	 if(x>y)
		x=x%y;
 	 else y=y%x;
	}
 if(x==0)
 	return(y);
 else return(x);
}*/


int main()
{int z;
 //cout << GCD1(15, 18);
 cout << GCD(15, 18) << " " << GCD1(15,18);
}

