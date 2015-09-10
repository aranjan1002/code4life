#include <cstring>
using namespace std;

class StackToken
{
 char tok[500];
 public:
	StackToken()
		{
		 tok[0]=0;
		}
	void insertVal(char *temp)
		{//cout << "inserting" << temp << endl;
		 strcpy(tok,temp);}
	char* getVal()
		{//cout << tok << "returned" << endl;
		 return(tok);}
	void concatenate(char *temp)
		{
		 int l, i=0;
		 l=strlen(tok);
		 while(temp[i])
			tok[l++]=temp[i++];
		 tok[i]=0;
		}
};
