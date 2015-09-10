#include <iostream>
#include <cstdlib>
#include <cstring>

using namespace std;

int main(int argc, char** argv)
{
 int flag1=0, flag2=0;
 int i;
 char str[100];
 system("g++ -o parser Parser.cpp");
 strcpy(str, "./parser < ");
 strcat(str, argv[--argc]);
 while(argc>0)
	{
	 if(strcmp(argv[argc], "-ast") == 0)
		flag1=1;
	 else if(strcmp(argv[argc], "-noout")==0)
		flag2=1;
	 argc--;
	}
 cout << str;
 if(flag1==1)
	system(str);
}

