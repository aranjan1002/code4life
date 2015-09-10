#include <iostream>
#include <cstring>
#include <cstdlib>

using namespace std;

int main()
{
 int i, j, test, testnum, ep, mp,cnt1, cnt2, temp1, temp2, count, max, ans=0;
 char *p, a[105][150][112], b[101][112], temp[300];
 cin >> test;
 testnum = test;
 p = (char *) malloc (120 * sizeof(char*));
 while (test--)
	{cin >> ep>> mp;
	 cnt1=0;
	 for (i=0; i<ep; i++)
 	 	{cin >> temp;
		 cnt1=0;
		p=strtok(temp,"//");
		while (p != NULL)
			{
			 strcpy(a[i][cnt1++], p);
			// cout << a[i][cnt1-1] << endl;
			 p = strtok(NULL, "//");
			} 
		 //strcpy(a[i][cnt1++],"0ing");
		 }
	 cnt2 = 0;
	 for (j=0; j<mp; j++)
		{cin >> temp;
		 cnt2=0;
		 p=strtok(temp,"//");
		 while (p != NULL)
			{
			 strcpy(b[cnt2++], p);
			// cout << b[i][cnt2-1] << endl;
			 p = strtok(NULL, "//");
			} 
		 strcpy(b[cnt2++],"0ing");
		
	ans=0;
	for(i=0; i<ep; i++)	
		{temp1 = 0;
		 //cout << a[i][temp1]<<a[0][0] <<"f";
		 while(!(strcmp(a[i][temp1], b[temp1])))
			{temp1++;
			 //cout << a[i][temp1-1] << "matched" << endl;
			}
		 if(temp1>max)
			max = temp1;
		}
	
	temp1=0;
	//cout << strcmp(b[temp1], "0ing") << "hell";
	while((strcmp(b[temp1], "0ing")!=0))
		{//cout << b[temp1] <<temp1<< endl;
		 strcpy(a[ep][temp1],b[temp1]);
		 temp1++;
		 //cout << ep << temp1-1 <<  a[ep][temp1-1]<<endl;
		 //cout << "copy";
		}
	//cout << a[0][0]<<"copy";
	ans = ans + temp1-max;
	ep++;
	}
	cout << "Case #" << testnum-test << ": " << ans << endl;
	}
}