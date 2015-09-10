#include <iostream>
#include <cstring>
#include <string>

using namespace std;

int main()
{
 string words[5000], temp;
 int letters_num, words_num, tests_num, i, j, k, l, count, tests[15][26];
 
 cin >> letters_num >> words_num >> tests_num;
 for (i=0; i<words_num; i++)
	cin >> words[i];

 for (i=0; i<tests_num; i++)
	{cin >> temp;
	 //cout << i << "# " << endl;
	 j=0;
	 count=0;
	 //cout << temp << endl;
	 l=0;
	 while (temp[j])
		{memset (tests[l], 0, 26*sizeof(int));
		 if(temp[j]=='(')
			{j++;
			 while(temp[j]!=')')
				tests[l][temp[j++]-97]=1;
			}
		 else tests[l][temp[j]-97]=1;
		 j++;
		 l++;
		}
	 for (j=0; j<words_num; j++)
		{
	 	 for (k=0; k<letters_num; k++)
			if(tests[k][words[j][k]-97]!=1)
				break;
		 if (k==letters_num)
			count++;
		}
	 /*for (k=0; k<letters_num; k++)
		{for (j=0; j<26; j++)
			if(tests[k][j])
				cout << char(j+97);
		 cout << " ";
		}*/
	 cout << "Case #" << i+1 << ": " << count <<endl;
	}

 /*for (i=0; i<words_num; i++)
	{cout << words[i] << endl;}*/
 
}
