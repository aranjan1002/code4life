#include "Node.h"
#include "Lexer.cpp"
#include <cstdio>
#include <iostream> //let rec fact x = x eq 1 -> 1 | x * fact (x-1)
			//in print (fact 2)

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

Node *arr[40];
int treeindex=0;

int main(int argc, char **argv)
{char *temp = new char();
 //strcpy(temp, argv[1]);
 freopen(argv[1], "r", stdin);
 //while(cout << getNext(0));
 E();
 ShowTree(arr[0], 0);
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
		 //cout << "Db -> ’<IDENTIFIER>’ Vb+ ’=’ E" << endl; // Use N+2
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
		 BuildTree((char *)"()", 0);
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
