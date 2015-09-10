#include <iostream>
#include <cstring>
using namespace std;


char* reverse_words(char *str, int size)
{
 char *temp=new char[size];
 char *newarray=new char[size];
 int countnew, counttemp, i;
 size--;
 countnew=counttemp=0;
 while(size>=0)
	{
	 temp[counttemp++]=str[size--];
	 if(str[size]==' ' || size==-1)
		{
		 for(i=counttemp-1; i>=0; i--)
			newarray[countnew++]=temp[i];
		 counttemp=0;
		 if(str[size]==' ')
			newarray[countnew++]=str[size--];
		}
	}
 return(newarray);
}

void fibonacci(int a, int b, int n)
{
 
}

void append(char *str, char t)
{
 int i;
 for(i=0; str[i]; i++);
 str[i]=t; str[i+1]=0;
}

int length(char *str)
{
 int i;
 for(i=0; str[i]; i++);
 return i;
}

int no=1;

void permute(char *str, bool* used, char* result, int level)
{
 int i;
 if(level==length(str))
	{cout << no++ << " " << result << endl;
	 return;
	}
 //cout << "new recursion" << endl;
 for(i=0; i<length(str); i++)
	{//cout << i << endl;
	 if(used[i]) { continue;}
	 used[i]=true;
	 append(result, str[i]);
	 //cout << result << endl;
	 permute(str, used, result, level+1);
	 //cout << i << " end" << length(str) << endl;
	 result[length(result)-1]=0;
	 used[i]=false;
	}
}

void test(int i)
{cout << endl;
 for(int j=0; j<5; j++)
	{cout << j << " ";
	 if(i<5)
	 test(i+1);
	}
}

void bits(int x, int cnt, int i)
{int temp;
 if(i&x)
	temp=1;
 else temp=0;
 if(cnt<=31)
	{bits(x, cnt+1, (i<<1));
	cout << temp;}
}

int main()
{
 int x=5;
 bits(x, 0, 1);
 //test(0);
}
