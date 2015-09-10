#include "Node.h"
#include "Lexer.cpp"
#include "Token.h"
#include "TokenList.h"
#include "Environs.h"
#include "Para.h"
#include "StackToken.h"
#include "StackTokenList.h"
#include <iostream> 
#include <cstring>
#include <cstdlib>
#include <cstdio>
#include <cmath>

void E();
void Ew();
void T();
void Ta();
void Tc();
void B();
void Bt();
void Bs();
void Bp();
void A();
void At();
void Af();
void Ap();
void R();
void Rn();
void D();
void Da();
void Dr();
void Db();
void Vb();
void Vl();
void Read(char []);
char* trim(char []);
void BuildTree(char *, int);
void ShowTree(Node*, int);
void Standardize(Node*);
Node* BuildTree2(char *n, Node *l, Node *r);
void ShowTree2(Node*, int, int, int);
void copy(Node *t, Node *s);
char *toString(int);
void addParamater(Node *temp, int index);
void ShowTree3(Node *temp, int a);
void Eval(int n);
Environs *env = new Environs();
char *str1 = new char();

Node *arr[100];
TokenList delta[305];
Para parameter[305];
StackTokenList CS;
StackTokenList stack;
int cnt=0;
int treeindex=0;
int count=0;
int envCount=0;
char tmp[400];
int flag1=0, flag2=0, flag3=0;
//int maxi[130];

int main(int argc, char **argv)
{char *temp = new char();
 int i=0,j;
 freopen(argv[--argc], "r", stdin);
 while(argc>0)
	{
	 if(strcmp(argv[argc], "-ast") == 0)
		flag1=1;
	 else if(strcmp(argv[argc], "-noout")==0)
		flag2=1;
	 else if(strcmp(argv[argc], "-st")==0)
		flag3=3;
	 argc--;
	}
 //while(strcmp(getNext(1),"\0")) {cout << getNext(1) << " ";  strcpy(temp, getNext(0)); cout << (int)temp[0] << endl;}
 E();
 if(flag1)
 ShowTree(arr[0], 0);
 Standardize(arr[0]);
 //cout << "Standardized" << endl;
 ShowTree2(arr[0], 0, 0, 0);
 /*for(i=0; i<=cnt; i++)
	{j=0;
	 cout << "delta" << i << ": ";
	 delta[i].setIndex(0);
	 while(delta[i].isEmpty()!=1)//strcmp(delta[i][j].getVal(),0))
		{cout << delta[i].pop() << " " ;
		 j++;
		}
	 delta[i].reset(j);
	 cout << endl << j << endl;
	 j=0;
	 while(parameter[i].isEmpty()!=1)//strcmp(delta[i][j].getVal(),0))
		{cout << parameter[i].pop() << " " ;
		 j++;
		}
	 parameter[i].reset(j);
	 cout << endl;
	}*/
 env->setCurrEnv(envCount++);
 env->setValues(0, (char *) "<ID:Conc>", (char *) "Conc");
 env->setValues(0, (char *) "<ID:Stern>", (char *) "Stern");
 //env->setValues(0, (char *) "<ID:Stem>", (char *) "Stem");
 //env->setValues(0, (char *) "<ID:Isstring>", (char *) "Isstring");
 //env->setValues(0, (char *) "<ID:Istuple>", (char *) "Istuple");
 if(flag3)
 	ShowTree3(arr[0], 0); 
 //cout << "St completed" << endl;
 if(flag2==0)
 Eval(0);
 if(flag2==0)
 cout << endl;
 //cout << envCount;
}

void Move(TokenList x)
{
 x.setIndex(0);
 while(x.getIndex() <= x.getMaxi())
	CS.push(x.getNext());
 //CS.show();
}

int length(char *temp)
{
 int i;
 for(i=0; temp[i]; i++);
 return i;
}

int checkLambda(char *temp)
{
 char temp2[250];
 //cout << "checking lambda";
 if(length(temp) < 7)
	return 0;
 else {
	strcpy(temp2, temp);
	temp2[6]=0;
	return(!(strcmp(temp2,(char *)"lambda")));
	}
}

int checkEta(char *temp)
{
 char temp2[250];
 //cout << "check eta" << endl;
 if(length(temp) < 7)
	return 0;
 else {
	strcpy(temp2, temp);
	temp2[3]=0;
	return(!(strcmp(temp2,(char *)"eta")));
	}
}

	
int getDelta(char *temp)
{
 char *t = &temp[7];
 return(atoi(t));
}

int getEnv(char *temp)
{
 char *t = &temp[length(toString(getDelta(temp)))+8];
 //cout << atoi(t) << "link env" << endl;
 return(atoi(t));
}

char* trim2(char str[])
{
 if(str[0]=='<' && str[2]=='D')
	{str[length(str)-1]=0;
	 return(&str[4]);
	}
 else if(str[0]=='<' && str[2]=='N')
	{
	 str[length(str)-1]=0;
	 return(&str[5]);
	}
 else if(str[0]=='<' && str[2]=='T')
	{str[length(str)-1]=0;
	 return(&str[5]);
	}
 else return (str);
}

char* mergeTau(char *temp)
{
 int i=atoi(&temp[3]);
 tmp[0]=0;
 //cout << tmp << "before merging" << flag2 << endl;
 strcpy(tmp, "(");
 while(i--)
 	{strcat(tmp, trim2(stack.pop()));
	 strcat(tmp, (char *)", ");
	}
 tmp[length(tmp)-2]=')';
 tmp[length(tmp)-1]=0;
 //cout << tmp << "after merging" << flag2 << endl;
 return(tmp);
}
	
char* findTauVal(char *temp, int i)
{
 char flag=0;
 int j=1, cnt=0, k=0;
 //cout << temp << "dsf" << temp[j]<< endl;
 while(temp[j]!='\0')
	{//cout << temp[j-1];
	 if(temp[j]=='\'')
		{tmp[k++]=temp[j++];
		 while(temp[j]!='\'')
			tmp[k++]=temp[j++];
		 tmp[k++]=temp[j++];
		}
	 else if(temp[j]=='(')
		{tmp[k++]=temp[j++];
		 flag++;
		 while(flag)
			{if(temp[j]=='(')
				flag++;
			 else if(temp[j]==')')
				flag--;
			 tmp[k++]=temp[j++];
			}
		}
	 else if(temp[j]!=',' && temp[j]!=')' && temp[j]!=' ')
		tmp[k++] = temp[j++];
	 else if(temp[j]==' ' && temp[j-1]==',') j++;
	 else if(temp[j]==' ') tmp[k++]=temp[j++];
	 else {cnt++;
		j++;
		if(cnt==i)
			{tmp[k]=0;
			 //cout << temp1 << "returned";
			 return(tmp);
			}
		else k=0;
		}
	 
	}
 cout << "Error: Index not found" << endl;
 return(NULL);
}

void addParameterValues(int n)
{int j=1;
 char temp[250];
 //strcpy(temp, stack.pop());
 //cout << "n=" << n << endl;
 if(n==1)
	{strcpy(temp, stack.pop());
	 env->setValues(envCount-1, parameter[300].pop(), temp);
	}
 else
 {
 strcpy(temp, stack.pop());
 //cout << temp << "parameter" << temp[0]<<endl;
 
 if(temp[0]=='(')
 	 while(n--)
		{
	 	 env->setValues(envCount-1, parameter[300].pop(), findTauVal(temp, j++));
		}
 else 
	{//cout << "df";
	 env->setValues(envCount-1, parameter[300].pop(), temp);
   	 n--;
	 while(n--)
		{env->setValues(envCount-1, parameter[300].pop(), stack.pop());
		}
	}
 }
}
		 


int countTau()
{
 char temp[250], flag=0;;
 int j=1, cnt=0, k=0;
 strcpy(temp, stack.pop());
 //cout << temp << "dsf" << temp[j]<< endl;
 while(temp[j]!='\0')
	{//cout << temp[j-1];
	 if(temp[j]=='\'')
		{j++;
		 while(temp[j]!='\'')
			j++;
		 j++;
		}
	 else if(temp[j]=='(')
		{j++;
		 flag++;
		 while(flag)
			{if(temp[j]=='(')
				flag++;
			 else if(temp[j]==')')
				flag--;
			 j++;
			}
		}
	 else if(temp[j]!=',' && temp[j]!=')' && temp[j]!=' ')
		j++;
	 else if(temp[j]==' ' && temp[j-1]==',') j++;
	 else if(temp[j]==' ') j++;
	 else {cnt++;
		j++;
		}
	}

 //cout << cnt1 << "returned";
 return (cnt);
}

void apply()
{
 char temp[250];
 char temp1[250];
 int x=0, newn, curr;
 strcpy(temp, stack.pop());
			 //cout << "apply" << temp;
			 /*cout << stack.pop();
			 cout << stack.pop();
			 cout << stack.pop();*/
			 //cout << stack.pop();
			 //stack.reset(3);
		 	 if(checkLambda(temp))
				{
				 newn = getDelta(temp);
				 curr = getEnv(temp);
				 //if(envCount<20)
				 env->setCurrEnv(envCount++);
				 env->setLink(envCount-1, curr);
				 //cout << "in lambda" << endl;
 				 while(parameter[newn].isEmpty()!=1)	//strcmp(delta[i][j].getVal(),0))
					{//env->setValues(envCount-1, parameter[newn].pop(), stack.pop());
					 //cout << parameter[newn].pop() << endl;
					 //cout << x << endl;
					 parameter[300].push(parameter[newn].pop());
					 x++;
					}
				 addParameterValues(x);
				 parameter[newn].reset(x);
				 /*strcpy(temp, (char *)("e"));
				 strcat(temp, toString(env->getCurrEnv()));
				 stack.push(temp);*/
				 CS.push((char *)"env");
				 Move(delta[newn]);
				}
			 else if(strcmp(temp, (char *)"Conc")==0)
				{strcpy(temp, trim2(stack.pop()));
				 strcpy(temp1, trim2(stack.pop()));
				 temp[length(temp)-1]=0;
			 	 stack.push(strcat(temp, &temp1[1]));
				}
			 else if(strcmp(temp, (char *)"<ID:Isstring>")==0)
				{strcpy(temp, trim2(stack.pop()));
				 if(temp[0]=='\'')
			 	 	stack.push((char *)"true");
				 else stack.push((char *)"false");
				}
			 else if(strcmp(temp, (char *)"<ID:Isinteger>")==0)
				{strcpy(temp, trim2(stack.pop()));
				 if(temp[0]>=48 && temp[0]<=57)
			 	 	stack.push((char *)"true");
				 else stack.push((char *)"false");
				}
			  else if(strcmp(temp, (char *)"<ID:Istruthvalue>")==0)
				{strcpy(temp, trim2(stack.pop()));
				 if(strcmp(temp, "<true>")==0 || strcmp(temp, "true")==0 || strcmp(temp, "<false>")==0 || strcmp(temp, "false")==0)
			 	 	stack.push((char *)"true");
				 else stack.push((char *)"false");
				}
			 else if(strcmp(temp, (char *)"<ID:Istuple>")==0)
				{strcpy(temp, trim2(stack.pop()));
				 if(temp[0]=='(' || strcmp(temp, "<nil>")==0)
			 	 	stack.push((char *)"true");
				 else stack.push((char *)"false");
				}
			 else if(strcmp(temp, (char *)"Stern")==0)
				{strcpy(temp, trim2(stack.pop()));
				 temp[1]='\'';
			 	 stack.push(&temp[1]);
				}
			 else if(strcmp(temp, (char *)"<ID:Stem>")==0)
				{strcpy(temp, trim2(stack.pop()));
				 temp[2]='\'';
				 temp[3]=0;
			 	 stack.push(temp);
				}
			 else if(strcmp(temp, (char *)"<Y*>")==0)
				{strcpy(temp1, stack.pop());
				 if(!(checkLambda(temp1)))
					cout << "Error: Expected lambda in stack" << endl;
				 else
					{strcpy(temp, (char *)"eta ");
					 strcat(temp, toString(getDelta(temp1)));
					 strcat(temp, " ");
					 strcat(temp, toString(getEnv(temp1)));
					 stack.push(temp);
					}
				}
			 else if(checkEta(temp))
				{
				 stack.push(temp);
				 strcpy(temp1, (char *)"lambda");
				 strcat(temp1, &temp[3]);
				 stack.push(temp1);
				 CS.push((char *)"gamma");
				 CS.push((char *)"gamma");
				}
			 else if(temp[0]=='(')
				{strcpy(temp1, stack.pop());
				 stack.push(findTauVal(temp, atoi(trim2(temp1))));
				}
			 else if(strcmp(temp, (char *)"<ID:Order>")==0)
				{stack.push(toString(countTau()));}
			 else if(strcmp(temp, (char *)"<ID:Print>")==0 || strcmp(temp, (char *)"<ID:print>")==0)
				{strcpy(temp, trim2(stack.pop()));
				 x=0;
				 curr=0;
				 //cout << "in gamma " << flag2 << endl;
				 if(flag2==0)
				 while(temp[x])
					if(checkLambda(temp))
						{cout << "[lambda closure: x: " << getDelta(temp) << "]";
						 break;
						}
					else if(temp[x]=='\'')
						{x++;
		 				 while(temp[x]!='\'')
							{
							 if(temp[x]=='\\')
								{x++;
								 if(temp[x] == 'n')
									cout << "\n";
								 else if(temp[x]=='t')
									cout << "\t";
								 else cout << temp[x];
								 x++;
								}
							 else cout << temp[x++];
							}
						 x++;
						}
					else if(temp[x]!='<' && temp[x]!='>') cout << temp[x++];
					else x++;
					//cout << checkLambda(temp) << "hello" << endl;
				 stack.push((char *)"dummy");
				}
			 else if(strcmp(temp, (char *)"<ID:ItoS>")==0)
			{
			 strcpy(temp, stack.pop());
			 strcpy(temp1, (char *)"\'");
			 strcat(temp1, temp);
			 strcat(temp1, (char *)"\'");
			 stack.push(temp1);
			}
			 else(stack.push(temp));
}

void Eval(int n)
{char temp[250];
 char temp1[250];
 CS.push((char *)"env");
 Move(delta[n]);
 //delta[n].reset(maxi[n] - delta[n].getMaxi() - 1);
 while(CS.isEmpty()!=1)
		{//cout << "flag2=" << flag2 << endl;
		 strcpy(temp, CS.pop());
		 //cout << "\t" << temp << cnt << " " << envCount<<endl;
	 	 if(strcmp(temp, (char *)"gamma")==0)
			{//cout << "gamma found";
			 apply();
			}
		 else if(strcmp(temp, (char *)"+")==0)
			{
			 stack.push(toString(atoi(trim2(stack.pop())) + atoi(trim2(stack.pop()))));
			}
		 else if(strcmp(temp, (char *)"+")==0)
			{
			 stack.push(toString(pow(atoi(trim2(stack.pop())), (atoi(trim2(stack.pop()))))));
			}
		 else if(strcmp(temp, (char *)"-")==0)
			{
			 stack.push(toString(atoi(trim2(stack.pop())) - atoi(trim2(stack.pop()))));
			}
		 else if(strcmp(temp, (char *)"*")==0)
			{
			 stack.push(toString(atoi(trim2(stack.pop())) * atoi(trim2(stack.pop()))));
			}
		 else if(strcmp(temp, (char *)"/")==0)
			{
			 stack.push(toString(atoi(trim2(stack.pop())) / atoi(trim2(stack.pop()))));
			}
		 else if(strcmp(temp, (char *)"eq")==0)
			{
			 strcpy(temp1, stack.pop());
			 strcpy(temp, stack.pop());
			 if(strcmp(trim2(temp), trim2(temp1))==0)
				stack.push((char *)"true");
			 else stack.push((char *)"false");
			}
		 else if(strcmp(temp, (char *)"ne")==0)
			{
			 strcpy(temp1, stack.pop());
			 strcpy(temp, stack.pop());
			 if(strcmp(trim2(temp), trim2(temp1))==0)
				stack.push((char *)"false");
			 else stack.push((char *)"true");
			}
		 else if(strcmp(temp, (char *)"not")==0)
			{
			 strcpy(temp, stack.pop());
			 if(strcmp(trim2(temp), (char *)"true")==0 || strcmp(trim2(temp), (char *)"<true>")==0)
				stack.push((char *)"false");
			 else stack.push((char *)"true");
			 //cout << temp << "tested" << endl;
			}
		 else if(strcmp(temp, (char *)"le")==0 || strcmp(temp, (char *)"<=")==0)
			{
			 strcpy(temp1, stack.pop());
			 strcpy(temp, stack.pop());
			 if(atoi(trim2(temp1)) <= atoi(trim2(temp)))
				stack.push((char *)"true");
			 else stack.push((char *)"false");
			}
		 else if(strcmp(temp, (char *)"ge")==0 || strcmp(temp, (char *)">=")==0)
			{
			 strcpy(temp1, stack.pop());
			 strcpy(temp, stack.pop());
			 if(atoi(trim2(temp1)) >= atoi(trim2(temp)))
				stack.push((char *)"true");
			 else stack.push((char *)"false");
			}
		 else if(strcmp(temp, (char *)"gr")==0 || strcmp(temp, (char *)">")==0)
			{
			 strcpy(temp1, stack.pop());
			 strcpy(temp, stack.pop());
			 if(atoi(trim2(temp1)) > atoi(trim2(temp)))
				stack.push((char *)"true");
			 else stack.push((char *)"false");
			}
		 else if(strcmp(temp, (char *)"neg")==0)
			{
			 strcpy(temp, stack.pop());
			 strcpy(temp1, (char *)"-");
			 stack.push(strcat(temp1, toString(atoi(trim2(temp)))));
			}
		 else if(strcmp(temp, (char *)"ls")==0 || strcmp(temp, (char *)"<")==0)
			{
			 strcpy(temp1, stack.pop());
			 strcpy(temp, stack.pop());
			 if(atoi(trim2(temp1)) < atoi(trim2(temp)))
				stack.push((char *)"true");
			 else stack.push((char *)"false");
			}
		 else if(strcmp(temp, (char *)"&")==0)
			{
			 strcpy(temp1, stack.pop());
			 strcpy(temp, stack.pop());
			 if((strcmp(temp, (char *)"true")==0 || strcmp(temp, (char *)"<true>")==0) && (strcmp(temp1, (char *)"true")==0 || strcmp(temp1, (char *)"<true>")==0))
				stack.push((char *)"true");
			 else stack.push((char *)"false");
			}
		 else if(strcmp(temp, (char *)"or")==0)
			{
			 strcpy(temp1, stack.pop());
			 strcpy(temp, stack.pop());
			 if((strcmp(temp, (char *)"false")==0 || strcmp(temp, (char *)"<false>")==0) && (strcmp(temp1, (char *)"false")==0 || strcmp(temp1, (char *)"<false>")==0))
				stack.push((char *)"false");
			 else stack.push((char *)"true");
			}
		 else if(strcmp(temp, (char *)"beta")==0)
			{
			 strcpy(temp1, stack.pop());
			 if(strcmp(temp1, "true") == 0 || strcmp(temp1, "<true>") == 0)
				{strcpy(temp1, CS.pop());
				 strcpy(temp1, CS.pop());
				 Move(delta[atoi(temp1)]);
				}
			 else{
			      strcpy(temp1, CS.pop());
			      strcpy(temp, CS.pop());
			      //cout << "going to " << temp1<< endl;
			      Move(delta[atoi(temp1)]);
			     }
			}
		 else if(strcmp(temp, (char *)"aug")==0)
				{strcpy(temp, stack.pop());
				 strcpy(temp1, stack.pop());
				 if(strcmp(temp, "<nil>")==0)
					{strcpy(temp, "(");
					 strcat(temp1, ")");
					 strcat(temp, temp1);
					 stack.push(trim2(temp));}
				 else {
					temp[length(temp)-1]=',';
					strcat(temp, (char *) " ");
					stack.push(strcat(strcat(temp, trim2(temp1)), ")"));
					}
				}
		 else if(strcmp(temp, (char *)"env")==0)
			{
			 env->popEnv();
			 //envCount--;
			}
		 else if(env->check(temp)!=NULL)
			{stack.push(env->check(temp));
			 //cout << env->check(temp) << "found" << endl;
			}		 
		 else if(checkLambda(temp))
			{strcat(temp, " ");
			 strcat(temp, toString(env->getCurrEnv()));
			 stack.push(temp);
			}
		 else if(temp[0]=='t' && temp[1]=='a' && temp[2]=='u')
			stack.push(mergeTau(temp));
		 else
		 	{stack.push(temp);
			 //cout << temp << "pushed" << endl;
			}
		 cout << "\n--STACK--------"<<endl;
		 stack.show();
		 cout << "\n--CS--------"<<endl;
		 CS.show();
		 cout << "Env Count=" << envCount << endl;
		}
 //cout << n << " complete" << endl;
 /*while(stack.isEmpty()!=1)
		cout << stack.pop() << " ";*/
}


void E()
{
 if (strcmp(getNext(1), "let")==0)
	{
	 Read((char*)"let");
	 D();
	 Read((char*)"in");
	 E();
	 //cout << "E -> let D in E" << endl;
	 BuildTree((char *)"let", 2);
	}
 else if(strcmp(getNext(1), "fn")==0)
	{
	 int N=0;
	 Read((char *)"fn");
	 do
	 {
 	  Vb();
	  N++;
	 }while(strcmp(getNext(1), "<ID:")==0 || strcmp(getNext(1), "(")==0);
 	 Read((char *)".");
	 E();
	 //cout << "e -> fn Vb+. E" << endl;
	 BuildTree((char *)"lambda", N+1);
	}
 else Ew();
}

void Ew()
{
 T();
 if(strcmp(getNext(1), "where")==0)
	{Read((char *)"where");
 	 Dr();
	 //cout << "Ew -> T where Dr" << endl;
	 BuildTree((char *)"where", 2);
	}
}

void T()
{
 int N=1;
 Ta();
 if(strcmp(getNext(1), ",")==0)
  {while(strcmp(getNext(1), ",")==0)
	{
	 Read((char *)",");
	 Ta();
	 N++;
	}
   //cout << "T -> Ta, Ta" << endl;
   BuildTree((char *)"tau", N);
  }
}

void Ta()
{
 Tc();
 while(strcmp(getNext(1), "aug")==0)
	{
	 Read((char *)"aug");
	 Tc();
	 //cout << "Ta -> Ta aug Tc" << endl;
	 BuildTree((char *)"aug", 2);
	}
}

void Tc()
{
 B();
 if(strcmp(getNext(1), "->")==0)
	{
	 Read((char*)"->");
	 Tc();
	 Read((char*)"|");
	 Tc();
	 //cout << "Tc -> B-> Tc|Tc" << endl;
	 BuildTree((char *)"->", 3);
	}
}

void B()
{
 Bt();
 while(strcmp(getNext(1), "or")==0)
	{
	 Read((char *)"or");
	 Bt();
	 //cout << "B-> B or Bt" << endl;
	 BuildTree((char *)"or", 2);
	}
}

void Bt()
{
 Bs();
 while(strcmp(getNext(1), "&")==0)
	{
	 Read((char *)"&");
	 Bs();
	 //cout << "Bt-> Bt & Bs" << endl;
	 BuildTree((char *)"&", 2);
	}
}

void Bs()
{
 if(strcmp(getNext(1), "not")==0)
	{
	 Read((char *)"not");
	 Bp();
 	 //cout << "Bs -> not Bp" << endl;
	 BuildTree((char *)"not", 1);
	}
 else Bp();
}

void Bp()
{
 A();
 if(strcmp(getNext(1), "gr")==0 || strcmp(getNext(1), ">")==0)
	{
 	 if(strcmp(getNext(1), "gr")==0)
	 	Read((char *)"gr");
	 else if(strcmp(getNext(1), ">")==0)
		Read((char *)">");
	 A();
	 //cout << "Bp -> A gr A" << endl;
	 BuildTree((char *)"gr", 2);
	}
 else if(strcmp(getNext(1), "ge")==0 || strcmp(getNext(1), ">=")==0)
	{
	 if(strcmp(getNext(1), "ge")==0)
		Read((char *)"ge");
	 else Read((char *)">=");
	 A();
	 //cout << "Bp -> A ge A" << endl;
	 BuildTree((char *)"ge", 2);
	}
 else if(strcmp(getNext(1), "ls")==0 || strcmp(getNext(1), "<")==0)
	{
	 if(strcmp(getNext(1), "ls")==0)
		Read((char *)"ls");
	 else Read((char *)"<");
	 A();
	 //cout << "Bp -> A ls A" << endl;
	 BuildTree((char *)"ls", 2);
	}
 else if(strcmp(getNext(1), "le")==0 || strcmp(getNext(1), "<=")==0)
	{
	 if(strcmp(getNext(1), "le")==0)
		Read((char *)"le");
	 else Read((char *)"<=");
	 A();
	 //cout << "Bp -> A le A" << endl;
	 BuildTree((char *)"le", 2);
	}
 else if(strcmp(getNext(1), "eq")==0)
	{
	 Read((char*)"eq");
	 A();
	 //cout << "Bp-> A eq A" << endl;
	 BuildTree((char *)"eq", 2);
	}
 else if(strcmp(getNext(1), "ne")==0)
	{
	 Read((char*)"ne");
	 A();
	 //cout << "Bp-> A ne A" << endl;
	 BuildTree((char *)"ne", 2);
	}
}

void A()
{
 char *temp = new char();
 if(strcmp(getNext(1), "-")==0)
	{
	 Read((char *)"-");
	 At();
	 //cout << "A -> -At" << endl;
	 BuildTree((char *)"neg", 1);
	}
 else if(strcmp(getNext(1), "+")==0)
	{
	 Read((char *)"+");
	 At();
	 //cout << "A -> +At" << endl;
	}
 else At();
 while(strcmp(getNext(1), "-")==0 || strcmp(getNext(1), "+")==0)
	{strcpy(temp, getNext(1));
	 Read(temp);
	 At();
	 //cout << "A-> A " << temp << " At" << endl;
	 BuildTree((char *)temp, 2);
	}
}

void At()
{
 char *temp = new char();
 Af();
 while(strcmp(getNext(1), "*")==0 || strcmp(getNext(1), "/")==0)
	{strcpy(temp, getNext(1));
	 Read(temp);
	 Af();
	 //cout << "At-> At " << temp << " Af" << endl;
	 BuildTree((char *)temp, 2);
	}
}

void Af()
{
 Ap();
 if(strcmp(getNext(1), "**")==0)
	{
	 Read((char *)"**");
	 Af();
	 //cout << "Af -> Ap ** Af" << endl;
	 BuildTree((char *)"**", 2);
	}
}

void Ap()
{
 R();
 while(strcmp(getNext(1), "@")==0)
	{
	 Read((char *)"@");
	 Read((char *)"<ID:");	
	 R();
	 //cout << "Ap -> Ap @ id R" << endl;
	 BuildTree((char *)"@", 3);
	}
}

void R()
{
  Rn();
  //cout << "Rand()" << endl;
  while((strcmp(trim(getNext(1)), "<ID:") && strcmp(trim(getNext(1)), "<INT:") && strcmp(trim(getNext(1)), "<STR:") && strcmp(getNext(1), "true") && strcmp(getNext(1), "false") && strcmp(getNext(1), "(") && strcmp(getNext(1), "nil") && strcmp(getNext(1), "dummy"))==0)
  {Rn();
   //cout << "R-> R Rn" << endl;
   BuildTree((char *)"gamma", 2);
  }
}

void Rn()
{int i;
 //cin >> i;
 if(strcmp(trim(getNext(1)), "<ID:")==0)
	{Read((char*)"<ID:"); 
	 //cout << "Rn -> Id" << endl;
	}
 else if (strcmp(trim(getNext(1)), "<INT:")==0)
	{Read((char*)"<INT:");
	 //cout << "Rn -> INT" << endl;
	}
 else if (strcmp(trim(getNext(1)), "<STR:")==0)
	{Read((char*)"<STR:");
	 //cout << "Rn -> STR" << endl;
	}
 else if(strcmp(getNext(1), "true")==0)
	{Read((char*)"true");
	 //cout << "Rn -> true" << endl;
	 BuildTree((char *)"<true>", 0);}
 else if(strcmp(getNext(1), "false")==0)
	{Read((char*)"false");
	 //cout << "Rn -> false" << endl;
	 BuildTree((char *)"<false>", 0);}
 else if(strcmp(getNext(1), "(")==0)
	{Read((char*)"(");
 	 E();
	 Read((char*)")");
	 //cout << "Rn -> (E)" << endl;
	}
 else if(strcmp(getNext(1), "nil")==0)
	{Read((char*)"nil"); 
	 //cout << "Rn -> nil" << endl;
	 BuildTree((char *)"<nil>", 0);}
 else if(strcmp(getNext(1), "dummy")==0)
	{Read((char*)"dummy");
	 //cout << "Rn -> dummy" << endl;
	 BuildTree((char *)"<dummy>", 0);}
 else cout << "Parse Error: Expected an identifier but found '" << getNext(1);
}

void D()
{
 Da();
 if(strcmp(getNext(1), "within")==0)
	{Read((char *)"within");
 	 D();
	 //cout << "D -> Da 'within' D" << endl;
	 BuildTree((char *)"within", 2);
	}
}

void Da()
{
 Dr();
 int N=1;
 if(strcmp(getNext(1), "and")==0)
 {
 while(strcmp(getNext(1), "and")==0)
	{Read((char *)"and");
 	 N++;
	 Dr();
	 //cout << "Da -> Dr and Dr" << endl;
	}
 BuildTree((char *)"and", N);
 }
}

void Dr()
{
 if(strcmp(getNext(1), "rec")==0)
	{
	 Read((char *) "rec");
	 Db();
	 //cout << "Dr -> rec Db" << endl;
	 BuildTree((char *)"rec", 1);
	}
 else Db();
}

void Db()
{
 int N=0;
 if(strcmp(getNext(1), "(")==0)
	{
	 Read((char *) "(");
	 D();
	 Read((char *) ")");
	 //cout << "Db -> (D)";
	}
 else if (strcmp(trim(getNext(1)), "<ID:")==0)
	{
	 Read((char *)"<ID:");
	 if(strcmp(trim(getNext(1)), "<ID:")==0 || strcmp(getNext(1), "(")==0)
		{while(strcmp(trim(getNext(1)), "<ID:")==0 || strcmp(getNext(1), "(")==0)
			{N++;
			 Vb();
			}
		 Read((char *)"=");
		 E();
		 //cout << "Db -> <IDENTIFIER> Vb+ = E" << endl; // Use N+2
		 BuildTree((char *)"function_form", N+2);
		}
	 else if(strcmp(getNext(1), "=")==0)
		{
		 Read((char *)"=");
		 E();
		 //cout << "Db -> V1=E" << endl;
		 BuildTree((char *)"=", 2);
		}
	 else if(strcmp(getNext(1), ",")==0)
		{
		 Read((char *)",");
		 Vl();
		 Read((char *)"=");
		 E();
		 BuildTree((char *)"=", 2);
		 //cout << "V1=E" << endl;
		}
	 else cout <<"Parsing Error: Expected an identifier or '=' or ',' but found '"<<getNext(1)<<"'";
	}
 else cout << "Parsing Error: Expected an identifier of a '(' but found '" << getNext(1) << "'";
} 

void Vb()
{
 if(strcmp(trim(getNext(1)), "<ID:")==0)
	{Read((char *)"<ID:");
	 //cout << "Vb -> <identifier>" << endl;
	}
 else if(strcmp(getNext(1), "(")==0)
	{Read((char *)"(");
	if(strcmp(trim(getNext(1)), "<ID:")==0)
		{Read((char *)"<ID:");
		 if(strcmp(getNext(1), ",")==0)
		 	{Read((char *)",");
			 Vl();
			}
		 Read((char *)")");
		 //cout << "Vb -> (Vl)" << endl;
		}
	else if(strcmp(getNext(1), ")")==0)
		{
		 Read((char *)")");
		 //cout << "Vb -> ()" << endl;
		 BuildTree((char *)"<()>", 0);
		}
	else cout << "Parsing Error: Expected an identifier or ')' but found '" << getNext(1) << "'" << endl;
	}
 else cout << "Parsing Error: Expected an identifier or '(' but found'" << getNext(1) << "'" << endl;
}

void Vl()
{
 int N=2;
 Read((char *)"<ID:");
 while(strcmp(getNext(1), ",")==0)
	{Read((char *)",");
	 Read((char *)"<ID:");
	 N++;
	}
 //cout << "Vl -> <Identifier> list," << endl;
 BuildTree((char *)",", N);
}
			 
char* trim(char str[])
{
 if(str[0]=='<' && str[2]=='D' && str[1]=='I')
	return((char*)"<ID:");
 else if(str[0]=='<' && str[2]=='N')
	return((char*)"<INT:");
 else if(str[0]=='<' && str[2]=='T')
	return((char*)"<STR:");
 else return (str);
}

void Read(char str[])
{
 char *temp=getNext(0);
 //cout << str << " " << temp << endl;
 if(str[3]==':' || str[4]==':')
 	{if(strcmp(str, trim(temp)))
		cout << "Parse Error: Expected " << str << " but found " << temp;
	 else BuildTree(temp, 0);
	 //cout << temp << endl;
	}
 else if(strcmp(str, temp))
		cout << "Parse Error: Expected " << str << " but found " << temp;
 //else cout << temp << "read\n";
}

void BuildTree(char *temp, int N)
{int i=0;
 Node *p = new Node();
 p->setVal(temp);
 //cout << p->getVal()  << treeindex<< N <<temp << endl;
 if(N>0)
	{p->setLeft(arr[treeindex-N]);
	 for(i=treeindex-N; i<treeindex-1; i++)
		arr[i]->setRight(arr[i+1]);
	}
 arr[treeindex-N]=p;
 if(N==0)
	treeindex++;
 else treeindex -= (N-1);
 /*if(strcmp(temp,"let")==0)
	{
	 cout << "Showing Tree" << endl;
	 ShowTree(arr[treeindex-1], 0);
	}*/
}

void ShowTree(Node *temp, int a)
{
 int i;
 for(i=0; i<a; i++, cout <<".");
 cout << temp->getVal() << endl;
 if(temp->getLeft()!= NULL)
	ShowTree(temp->getLeft(), a+1);
 if(temp->getRight()!= NULL)
	ShowTree(temp->getRight(), a);
}


void addParameter(Node *temp, int index)
{
 if(temp->getLeft()!= NULL)
	addParameter(temp->getLeft(), index);
 if(strcmp(temp->getVal(), " ") && strcmp(temp->getVal(), ",") && strcmp(temp->getVal(), "<()>"))
 	parameter[index].push(temp->getVal());
 if(temp->getRight()!= NULL)
	addParameter(temp->getRight(), index);
}

void ShowTree3(Node *temp, int a)
{
 int i;
 char temp2[250];
 //cout << index << temp;
 /*if(strcmp(temp->getVal(), "lambda")==0)
	{//cout << "in lambda:" << index << endl;
	 strcpy(temp2, (char *)"lambda ");
	 delta[index].add(strcat(temp2, toString(cnt+1)));
	 addParameter(temp->getLeft(), index);
	 cnt++;
	 //cout << (temp->getLeft()->getVal()) << " " << temp->getRight() << endl;
	 if(temp->getRight()!=NULL)
	 ShowTree2(temp->getRight(), a+1, cnt, 0);
	 return;
	}
 else if(strcmp(temp->getVal(), "->")==0)
	{count++;
	 cout << cnt << endl;
	 delta[index].add(toString(cnt+1));
	 delta[index].add(toString(cnt+2));
	 delta[index].add((char *)"beta");
 	 cout << "->" << endl;
	 if(temp->getLeft()!=NULL)
	 ShowTree2(temp->getLeft(), a+1, cnt, l+1);
	 if(temp->getRight()!=NULL)
	 {if(temp->getRight()->getLeft()!=NULL)
	   {cnt++;
 	    ShowTree2(temp->getRight()->getLeft(), a+1, cnt, 0);}
	 if(temp->getRight()->getRight()!=NULL)
	   {cnt++;
	    ShowTree2(temp->getRight()->getRight(), a+1, cnt, 0);}
	 }
	 return;
	}	
 delta[index].add(temp->getVal());*/ 
 if(strcmp(temp->getVal(), " ") && strcmp(temp->getVal(), "beta"))
 {
 for(i=0; i<a; i++, cout <<".");
 if(temp->getVal()[0]=='t' && temp->getVal()[1]=='a' && temp->getVal()[2]=='u')
 	cout << "tau" << endl;
 else cout << temp->getVal() << endl;
 if(temp->getLeft()!= NULL)
	ShowTree3(temp->getLeft(), a+1);
 if(temp->getRight()!= NULL)
	ShowTree3(temp->getRight(), a+1);
 }
 else 
 {if(temp->getLeft()!= NULL)
	ShowTree3(temp->getLeft(), a);
 if(temp->getRight()!= NULL)
	ShowTree3(temp->getRight(), a);
 }
 //free(temp);
}


void ShowTree2(Node *temp, int a, int index, int l)
{
 int i;
 char temp2[250];
 //cout << index << temp;
 /*for(i=0; i<a; i++, cout <<".");
 cout << temp->getVal() << endl;
 cout << cnt << endl;*/
 if(strcmp(temp->getVal(), "lambda")==0)
	{//cout << "in lambda:" << index << endl;
	 strcpy(temp2, (char *)"lambda ");
	 delta[index].add(strcat(temp2, toString(cnt+1)));
	 addParameter(temp->getLeft(), cnt+1);
	 cnt++;
	 //cout << (temp->getLeft()->getVal()) << " " << temp->getRight() << endl;
	 if(temp->getRight()!=NULL)
	 ShowTree2(temp->getRight(), a+1, cnt, 0);
	 return;
	}
 else if(strcmp(temp->getVal(), "->")==0)
	{count++;
	 delta[index].add(toString(cnt+1));
	 i=index;
	 //cout << i << endl;
	 if(temp->getRight()!=NULL)
	 {if(temp->getRight()->getLeft()!=NULL)
	   {cnt++;
 	    ShowTree2(temp->getRight()->getLeft(), a+1, cnt, 0);}
	 delta[index].add(toString(cnt+1));
	 delta[index].add((char *)"beta");
	 if(temp->getRight()->getRight()!=NULL)
	   {cnt++;
	    ShowTree2(temp->getRight()->getRight(), a+1, cnt, 0);}
	 }
	 if(temp->getLeft()!=NULL)
	 ShowTree2(temp->getLeft(), a+1, i, l+1);
	 return;
	}	 
 if(strcmp(temp->getVal(), (char *)" "))
 	delta[index].add(temp->getVal());
 if(temp->getLeft()!= NULL)
	ShowTree2(temp->getLeft(), a+1, index, l+1);
 if(temp->getRight()!= NULL)
	ShowTree2(temp->getRight(), a+1, index, l+2);
 //free(temp);
}


void Standardize(Node *n)
{
 Node *temp = new Node(), *temp1 = new Node();
 Node *a[250];
 int i=0, j;
 if(n->getLeft()==NULL)
	{n->setRight(NULL);
	 return;
	}
 a[i++]=n->getLeft();
 temp = n->getLeft();
 while(temp->getRight()!=NULL)
 	{
	 temp=temp->getRight();
	 a[i++]=temp;
	}
 if(strcmp(n->getVal(), (char *)"let")==0)
	{
	 Standardize(a[0]);
	 Standardize(a[1]);
	 n->setVal((char *)"gamma");
	 temp = BuildTree2((char *)"lambda", n->getLeft()->getLeft(), a[1]);
	 n->setRight(n->getLeft()->getRight());
	 n->setLeft(temp);
	 //temp->getLeft()->setRight(NULL);
	 }
 else if(strcmp(n->getVal(), (char *)"where")==0)
	{
	 Standardize(a[0]);
	 Standardize(a[1]);
	 n->setVal((char *)"gamma");
	 temp = BuildTree2((char *)"lambda", a[1]->getLeft(), n->getLeft());
	 n->setRight(a[1]->getRight());
	 n->setLeft(temp);
	 //cout << n->getVal() << " " << n->getLeft()->getVal() << " " << n->getLeft()->getLeft()->getVal() << " " << n->getLeft()->getRight()->getVal() << " " << n->getRight()->getVal() << endl;
	 //temp->getLeft()->setRight(NULL);
	 //cout << "Where\n"; 
	 //ShowTree2(n, 0);
	 //cout << endl;
	 }
 else if(strcmp(n->getVal(), (char *)"rec")==0)
	{
	 Standardize(a[0]);
	 n->setVal((char *)"=");
	 temp = BuildTree2((char *)"lambda", a[0]->getLeft(), a[0]->getRight());
	 temp1 = BuildTree2((char *)"gamma", NULL, temp);
	 temp = BuildTree2((char *)"<Y*>", NULL, NULL);
	 temp1->setLeft(temp);
	 n->setRight(temp1);
	 temp = BuildTree2(a[0]->getLeft()->getVal(), NULL, NULL);
	 n->setLeft(temp);
	 /*cout << "Rec\n"; 
	 ShowTree2(n, 0);
	 cout << endl;*/
	 //Standardize(n);
	 //temp->getLeft()->setRight(NULL);
	}
 else if(strcmp(n->getVal(), (char *)"gamma")==0 || strcmp(n->getVal(), (char *)"lambda")==0 || strcmp(n->getVal(), (char *)"*")==0 || strcmp(n->getVal(), (char *)"/")==0 || strcmp(n->getVal(), (char *)"-")==0 || strcmp(n->getVal(), (char *)"=")==0 || strcmp(n->getVal(), (char *)"+")==0 || strcmp(n->getVal(), (char *)"eq")==0 || strcmp(n->getVal(), (char *)"aug")==0 || strcmp(n->getVal(), (char *)"gr")==0 || strcmp(n->getVal(), (char *)"ge")==0 || strcmp(n->getVal(), (char *)"ls")==0 || strcmp(n->getVal(), (char *)"le")==0 || strcmp(n->getVal(), (char *)"ne")==0 || strcmp(n->getVal(), (char *)"or")==0 || strcmp(n->getVal(), (char *)"&")==0 || strcmp(n->getVal(), (char *)"**")==0 || strcmp(n->getVal(), (char *)"aug")==0)
	{
	 Standardize(a[0]);
	 Standardize(a[1]);
	 n->setLeft(a[0]);
	 n->setRight(a[1]);
	}

 else if(strcmp(n->getVal(), (char *)"function_form")==0)
	{
	 for(j=0; j<i; j++)
		Standardize(a[j]);
	 n->setVal((char *)"=");
	 temp = BuildTree2((char *)"lambda", a[1], NULL);
 	 n->setRight(temp);
 	 for(j=2; j<i-1; j++)
		{
		 temp1 = BuildTree2((char *)"lambda", a[j], NULL);
	 	 temp->setRight(temp1);
		 temp = temp1;
		}
	 temp->setRight(a[j]);
	 //cout << "Function Form\n"; 
	 //ShowTree2(n, 0,0,0);
	 //cout << endl;
	}
 else if(strcmp(n->getVal(), (char *)"->")==0)
	{
	 a[0]->setRight(NULL);
	 Standardize(a[0]);
	 Standardize(a[1]);
	 Standardize(a[2]);
	 //copy(n, a[0]);
	 n->setVal((char *)"->");
	 temp1 = BuildTree2((char *)"beta", NULL, NULL);
	 n->setRight(temp1);
	 temp1->setLeft(a[1]);
	 temp1->setRight(a[2]);
	}
 else if(strcmp(n->getVal(), (char *)",")==0)
	{
	 for(j=0; j<i; j++)
		Standardize(a[j]);
	 if(i==2)
		{
		 n->setRight(a[1]);
		}
	 else
	 {
	 temp=n;
	 for(j=1; j<=i-3; j++)
		{temp1 = BuildTree2((char *)" ", a[j], NULL);
		 temp->setRight(temp1);
		 temp=temp1;
		}
	 temp1 = BuildTree2((char *)" ", a[j], a[j+1]);
	 temp->setRight(temp1);
	 }
	} 
 else if(strcmp(n->getVal(), (char *)"tau")==0) 	 
	{char *t = new char();
	 Standardize(a[0]);
	 strcpy(t,n->getVal());
	 strcat(t, toString(i));
	 //cout << "tau count = " << i << t << endl;
	 n->setVal(t);
	 temp=n;
	 for(j=1; j<i; j++)
		{Standardize(a[j]);
		 temp1 = BuildTree2((char *)" ", a[j], NULL);
		 temp->setRight(temp1);
		 temp=temp1;
		}
	}
 else if(strcmp(n->getVal(), (char *)"and")==0) 	 
	{ char *t = new char();
	 n->setVal((char *)"=");
	 
	 for(j=0; j<i; j++)
		Standardize(a[j]);
	 temp = BuildTree2((char *)",", a[0]->getLeft(), NULL);
	 temp1 = BuildTree2((char *)"tau", a[0]->getRight(), NULL);
	 n->setLeft(temp);
	 n->setRight(temp1);
 	 if(i==2)
		{
		 temp->setRight(a[1]->getLeft());
		}
	 else
	 {
	 for(j=1; j<=i-3; j++)
		{
	 	 temp1 = BuildTree2((char *)" ", a[j]->getLeft(), NULL);
		 temp->setRight(temp1);
		 temp=temp1;
		}
	 temp1 = BuildTree2((char *)" ", a[j]->getLeft(), a[j+1]->getLeft());
	 temp->setRight(temp1);
	 }
	 temp=n->getRight();
	 t=temp->getVal();
	 strcat(t, toString(i));
	 //cout << "tau count = " << i << t << endl;
	 n->getRight()->setVal(t);
	 //cout << i << endl;
	 for(j=1; j<i; j++)
		{
		 temp1 = BuildTree2((char *)" ", a[j]->getRight(), NULL);
		 temp->setRight(temp1);
		 temp=temp1;
		}
	 /*ShowTree3(n, 0);
	 cout << endl;*/
	}
 else if(strcmp(n->getVal(), (char *)"@")==0) 	 
	{temp = BuildTree2((char *)"gamma", a[1], a[0]);
	 Standardize(a[0]);
	 Standardize(a[1]);
	 Standardize(a[2]);
 	 n->setLeft(temp);
	 n->setVal((char *)"gamma");
	 n->setRight(a[2]);
	}
 else if(strcmp(n->getVal(), (char *)"within")==0) 	 
	{
 	 Standardize(a[0]);
	 Standardize(a[1]);
	 temp = BuildTree2((char *)"lambda", a[0]->getLeft(), a[1]->getRight());
	 temp1 = BuildTree2((char *)"gamma", temp, a[0]->getRight());
 	 n->setLeft(a[1]->getLeft());
	 n->setVal((char *)"=");
	 n->setRight(temp1);
	}
 else if(strcmp(n->getVal(), (char *)"not")==0) 	 
	{
	 n->setRight(NULL);
	 Standardize(a[0]);
	}	
 else Standardize(n->getLeft());
}

Node* BuildTree2(char *n, Node *l, Node *r)
{
 //Node *temp = new Node();
 arr[45] = new Node();
 arr[45]->setVal(n);
 arr[45]->setLeft(l);
 arr[45]->setRight(r);
 return(arr[45]);
}

void copy(Node *t, Node *s)
{
 t->setVal(s->getVal());
 t->setLeft(s->getLeft());
 t->setRight(s->getRight());
}

char* strrev(char *t)
{
 char t1[250];
 int l2, l;  
 l2=l=strlen(t);
 while(l2>0)
	{t1[l-l2] = t[l2 - 1];
	 l2--;
	}
 t1[l]=0;
 strcpy(t, t1);
 return t;
}

char *toString(int i)
{//cout << i << "passed" << endl;
 int c=0;
 do
	{str1[c++]=(i%10)+48;
	 //cout << str1[c-1] << endl;
	 i /= 10;
	}
 while(i);
 str1[c]=0;
 strrev(str1);
 return(str1);
}


