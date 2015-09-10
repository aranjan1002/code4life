#include <iostream>
#include <cstring>
#include <cmath>

using namespace std;

int min_of(int a, int b, int c, int d)
{int min;
 if(a>=0)
	min = a;
 else if (b>=0)
	min = b;
 else if(c>=0)
	min = c;
 else min = d;
 if(b>=0 && b<min)
 	min=b;
 if(c>=0 && c<min)
	min=c;
 if(d>=0 && d<min)
	min=d;
 return(min);
}
 
int find (int a[][100], int i, int j, int c)
{
 if (a[i][j] == i*c+j+1)
	return(i*c+j+1);
 else return(find(a, ceil(a[i][j]/(c*1.0))-1, (a[i][j]%c?(a[i][j]%c)-1:c-1), c));
}

int main()
{
 int altitude[100][100], basin[100][100], r, c, min, n, s, e , w, testno;
 int alphabets[10000], i, j, count, test;
 cin >> test;
 testno = test;
 while (test--)
 {
 cin >> r >> c;
 memset (alphabets, 0, sizeof(alphabets));
 for (i=0 ; i<r ; i++)
 	for (j=0 ; j<c ; j++)
		cin >> altitude[i][j];

 for (i=0 ; i<r ; i++)
	for (j=0 ; j<c ; j++)
		basin[i][j] = i*c+j+1;

 for (i=0 ; i<r ; i++)
	{for (j=0 ; j<c ; j++)
		{//if(i>0 && i<r-1 && j>0 && j<c-1)
		 {
		 n=s=e=w=-1;
		 if(i>0)
			n=altitude[i-1][j];
		 if(j>0)
			w=altitude[i][j-1];
		 if(j<c-1)
			e=altitude[i][j+1];
		 if(i<r-1)
			s=altitude[i+1][j];
		 min = min_of(n,s,e,w);
		 //cout << min << endl;
		 if(altitude[i][j] > min)
			{
			 if(i>0 && altitude[i-1][j]==min)
				basin[i][j]=basin[i-1][j];
		 	 else if(j>0 && altitude[i][j-1]==min)
				basin[i][j]=basin[i][j-1];
			 else if(j<c-1 && altitude[i][j+1]==min)
				basin[i][j]=basin[i][j+1];
			 else if(i<r-1 && altitude[i+1][j]==min)
				basin[i][j]=basin[i+1][j];
			}
		 }
		}
	}
 count = 97;
 for (i=0 ; i<r ; i++)
	for (j=0 ; j<c ; j++)
		{basin[i][j]=find(basin, i, j, c);
		 //cout << basin[i][j] << endl;
		 if (alphabets[basin[i][j]])
			altitude[i][j]=alphabets[basin[i][j]];
		 else {alphabets[basin[i][j]]=count++;
			altitude[i][j]=alphabets[basin[i][j]];
			}
		}
		  
 cout << "Case #" << testno-test << ": ";
 /*for (i=0 ; i<r ; i++)
	{for (j=0 ; j<c ; j++)
		cout << basin[i][j] << " ";
	 cout << endl;  
	}*/
 cout << endl;
 for (i=0 ; i<r ; i++)
	{for (j=0 ; j<c ; j++)
		cout << (char)altitude[i][j] << " ";
	 cout << endl;   
	}
 }
}