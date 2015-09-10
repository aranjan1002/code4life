#include <iostream>
#include <cstring>

using namespace std;

void strrev(char *a)
{
 int len,i;
 for(i=0; a[i]; i++)
	len=i;
 for(i=0; len>i; i++)
	swap(a[i],a[len--]);
}

int main()
{
 int val[50][50][4], i, j, test, testnum, dim, req,cnt, red, blue;
 char chr[51][51], temp[51], ans[10];
 cin >> test;
 testnum=test;
 while (test--)
	{red =0 ;
 	 blue = 0;
	 cin >> dim >> req;
	 memset (chr, 0, sizeof(chr));
	 for(i=0; i<dim; i++)
		{cnt=0;
		 cin >> temp;
		 for(j=0; j<dim; j++)
			if(temp[j]!='.')
				chr[i][cnt++]=temp[j];
	 	 chr[i][cnt]=0;
		 strrev(chr[i]);
		 //cout << chr[i] << endl;
		}
	 memset(val, 0, sizeof(val));
	 for (i=0; i<dim; i++)
		{j=0;
		 while (chr[i][j])
			{
			 if(j>0)
				if(chr[i][j] && chr[i][j-1]==chr[i][j])
					{
					 val[i][j][0] = val[i][j-1][0]+1;
					 if(val[i][j][0] == req-1)	
						if(chr[i][j]=='R')	
							red=1;
						else blue=1;
					}
			 if(i>0)
				if(chr[i][j] && chr[i-1][j]==chr[i][j])
					{
					 val[i][j][1] = val[i-1][j][1]+1;
					 if(val[i][j][1] == req-1)	
						if(chr[i][j]=='R')	
							red=1;
						else blue=1;
					}
			 if(i>0 && j>0)
				if(chr[i][j] && chr[i-1][j-1]==chr[i][j])
					{
					 val[i][j][2] = val[i-1][j-1][2]+1;
					 if(val[i][j][2] == req-1)	
						if(chr[i][j]=='R')	
							red=1;
						else blue=1;
					}
			 if(i>0 && j<50)
				if(chr[i][j] && chr[i-1][j+1]==chr[i][j])
					{
					 val[i][j][3] = val[i-1][j+1][3]+1;
					 if(val[i][j][3] == req-1)	
						if(chr[i][j]=='R')	
							red=1;
						else blue=1;
					}
			 j++;
			}
		}
	 if(red==1 && blue==1)
		strcpy(ans,"Both");
	 else if(red==1)
		strcpy(ans,"Red");
	 else if(blue==1)
		strcpy(ans,"Blue");
	 else strcpy(ans,"Neither");
	 //cout << red << " " << blue << endl;
	 cout << "Case #" << testnum-test << ": " << ans << endl;
	}
}