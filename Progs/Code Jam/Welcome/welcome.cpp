#include <iostream>
#include <cstdio>
#include <string>
#include <cstring>

using namespace std;


int main()
{string a;
 int i, j, index[501][20], test, temp;
 
 cin >> test;
 getline(cin, a);
 temp = test;
 //cout << a << endl;
 while (test--)
 {
 memset (index, 0, sizeof(index));
 getline(cin, a);
 //cout << a << endl;
 i=0; 
 index[0][0]=1;
 while(a[i])
	{//a[i] = tolower(a[i]);
 	 for (j=0; j<20; j++)
		{index[i+1][j] = index[i][j]%100000;
		 cout << index[i][j] << " ";
		}
	 cout << endl << a[i] << endl;
	 if(a[i]=='w')
		index[i+1][1]+=index[i][0];
	 else if(a[i]=='e')
		{index[i+1][2]+=index[i][1];
		 index[i+1][7]+=index[i][6];
		 index[i+1][15]+=index[i][14];}
	 else if(a[i]=='l')
		index[i+1][3]+=index[i][2];
	 else if(a[i]=='c')
		{index[i+1][4]+=index[i][3];
		 index[i+1][12]+=index[i][11];}
	 else if(a[i]=='o')
		{index[i+1][5]+=index[i][4];
		 index[i+1][10]+=index[i][9];
		 index[i+1][13]+=index[i][12];}
	 else if(a[i]=='m')
		{index[i+1][6]+=index[i][5];
		 index[i+1][19]+=index[i][18];}
	 else if(a[i]==' ')
		{index[i+1][8]+=index[i][7];
		 index[i+1][11]+=index[i][10];
		 index[i+1][16]+=index[i][15];}
	 else if(a[i]=='t')
		index[i+1][9]+=index[i][8];
	 else if(a[i]=='d')
		index[i+1][14]+=index[i][13];
	 else if(a[i]=='j')
		index[i+1][17]+=index[i][16];
	 else if(a[i]=='a')
		index[i+1][18]+=index[i][17];
	 i++;
	 }
 printf ("Case #%d: %04d\n", temp-test, index[i][19]%10000); 
 //cout << "Case #" << temp-test<< ": " << setw(2) << index[i][19]%10000 << endl;
 }
}

