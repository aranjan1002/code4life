#include <iostream>
#include <fstream>
#include <cstdlib>
#include <string>

using namespace std;

long int randint(long int l, long int u)
{
 return l + rand()% (u-l+1);
}

int main()
{
 long int i, temp, flag=0, count, temp1;
 int num[1000000];
 fstream file1, file2;
 char line[1000];
 for (i=1; i<=1000000; i++)
	{count = 1;
	 temp = randint(i, 10000000);
	 cout << "temp=" << temp << endl;
	 if (flag == 0)
		{
		 file1.open("out.o", ios::in);
		 file2.open("out1.o", ios::out);
		 while(!file1.eof())
			{file1.getline(line, 100);
			 if (count == i)
			 	temp1 = atol(line);
			 if (count == temp)
				{num[i-1] = atol(line);
				 cout << num[i-1] << endl;
				 file2 << temp1 << endl;
				}
			 else 
				file2 << line << endl;
			 count++;
			}
		 flag = 1;	 
		 file1.close();
		 file2.close();
		}
	 else 
		{
		 flag = 0;
		 file1.open("out1.o", ios::in);
		 file2.open("out.o", ios::out);
		 while(!file1.eof())
			{file1.getline(line, 100);
			 if (count == i)
			 	temp1 = atol(line);
			 if (count == temp)
				{num[i-1] = atol(line);
				 cout << num[i-1] << endl;
				 file2 << temp1 << endl;
				}
			 else 
				file2 << line << endl;
			 count++;
			}
		 file1.close();
		 file2.close();
		}			 
	}
}

