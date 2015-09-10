#include <iostream>
#include <cstring>
using namespace std;

char letters[]="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
char digits[]="0123456789";
char operators[]="+-*<>&.|@:=˜$!#%ˆ_[]{}\"‘?`";
char others[]="() ;,/";
char keywords[][10]={"let", "in", "where", "aug", "or", "not", "fn", "gr", "ge", "ls", "le", "eq", "ne", "in", "where", "aug", "or", "not", "fn", "true", "false", "nil", "dummy", "within", "and", "rec","1"};

int checkKeyword(char *str)
{
 int i=0;
 while(keywords[i][0]!='1')
	if(strcmp(keywords[i++], str)==0)
		return 1;
 return 0;
}

int checkLetter(char ch)
{
 int i=0;
 while(letters[i])
	if(ch==letters[i++])
		return 1;
 return 0;
}

int checkDigit(char ch)
{
 int i=0;
 while(digits[i])
	if(ch==digits[i++])
		return 1;
 return 0;
}

int checkOperator(char ch)
{
 int i=0;
 while(operators[i])
	if(ch==operators[i++])
		{
		 return 1;
		}
 return 0;
}

int checkOthers(char ch)
{
 int i=0;
 while(others[i])
	if(ch==others[i++])
		return 1;
 return 0;
}

char* getNext(int x)
{
 static int cnt, flag=0, same=0;
 static char temp[300], temp1[300];
 static char chr;
 //cout << "same=" << same;
 if(same==1)
	{same=x;
	 //cout << temp << endl;
	 return(temp);
	}
 if(flag==1 || (cin.get(chr)))
	{//cout << chr << flag << "\t";
	 flag=0;
 	 if(checkLetter(chr))
		{temp[0]=0;
		 strcat(temp, "<ID:");
		 cnt=0;
		 while(checkLetter(chr) || checkDigit(chr) || chr=='_')
			{temp1[cnt++]=chr;
			 cin.get(chr);
			}
		 temp1[cnt]=0;
		 if(checkKeyword(temp1)==0)
			{strcat(temp, temp1);
			 cnt=strlen(temp);
		 	 temp[cnt++]='>';
			 temp[cnt]=0;}
		 else strcpy(temp, temp1);

		 //cout << "Identifier: " << temp << endl;
		 flag=1;
		}
	 else if(checkDigit(chr))
		{temp[0]=0;
		 strcat(temp, "<INT:");
		 cnt=strlen(temp);
		 while(checkDigit(chr))
			{temp[cnt++]=chr;
			 cin.get(chr);
			}
		 temp[cnt++]='>';
		 temp[cnt]=0;
		 //cout << "Integer: " << temp << endl;
		 flag=1;
		}
	 else if(checkOperator(chr))
		{//cout << "Operator: " << chr;
		 temp[0]=chr;
		 if(chr=='<' || chr=='>')
			{
			 cin.get(chr);
			 if(chr=='=')
				{temp[1]=chr; temp[2]=0;}
			 else {flag=1; temp[1]=0;}
			}
		 else if(chr=='-')
			{
			 cin.get(chr);
			 if(chr=='>')
				{temp[1]=chr; temp[2]=0;}//cout << chr << endl;
			 else {flag=1; temp[1]=0;}
			}
		 else if(chr=='*')
			{
			 cin.get(chr);
			 if(chr=='*')
				{temp[1]=chr; temp[2]=0;}//cout << chr << endl;
			 else {flag=1; temp[1]=0;}
			}
		 else temp[1]=0;//cout << endl;
	 	}
	 else if(chr == '\'')
			{
			 temp[0]=0;
			 strcat(temp, "<STR:\'");
			 cnt=strlen(temp);
			 	while(cin.get(chr))
				{if(flag==0 && chr=='\\')
					{flag=1;
					 temp[cnt++]=chr;
					}
				 else if(flag==0 && (checkLetter(chr) || checkDigit(chr) || checkOperator(chr) || checkOthers(chr)))
				 	temp[cnt++]=chr;
				 else if(flag==1)
					{
					 if(chr != 't' && chr != 'n' && chr != '\\' && chr!='\'')
						{cout << "Invalid Token: Escape sequence not recognized" << endl;
						 flag=2;
						 break;
						}
					 else {temp[cnt++]=chr;
						flag=0;
						}
					}
				 else if(chr=='\'')
					{temp[cnt++]=chr;
					 temp[cnt++]='>';
					 temp[cnt]=0;
					 //cout << "String: " << temp << endl;
					 flag = 3;
					 break;
					}
				 else {cout << "Invalid Token: Not a legal character" << endl;
					flag=2;
					break;
					}
				}
			 if(flag==2)
				return(NULL); 	//break;
			 else if(flag!=3)
				{cout << "Invalid Token: String did not end" << endl;
				 return(NULL); 	//break;
				}
			 else flag=0;
			}
	 else if(chr=='/')
		{
		 cin.get(chr);
		 if(chr!='/')
			{temp[0]='/'; temp[1]=0;
			 flag=1;
			}
		 else  {
			cin.getline(temp,1000);
			return(getNext(x));
			}
		}
	 else if(chr=='(' || chr == ')' || chr == ';' || chr==',')
		{temp[0]=chr; temp[1]=0;}//cout << "Punctuation: " << chr << endl;
	 else if(chr!='\n' && chr!='\t' && chr!=' ' && chr!='\r')
		{cout << "Invalid Token: Not a legal character: " << chr << endl;
	 	 return((char *)("\0")); 	//break;
		}
	 else return(getNext(x));
	//cout << flag << "\t";
	//cout << temp << endl;
	same=x;
	return(temp);
	}
 return((char *)"\0");
}



