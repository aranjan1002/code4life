#include<cstring>

using namespace std;

class Env_Node
{
 char var[5][14];
 char val[5][181];
 Env_Node *link;
 //int index;
 public:
	Env_Node()
		{
		  link=NULL;
		  var[0][0]=0;
		  var[1][0]=0;
		  var[2][0]=0;
		  var[3][0]=0;
		  var[4][0]=0;
		}
	void insert_pair(char *v, char *c)
		{//val[index] = (char *) malloc (150);
		 int i=0, j=0, index;
		 if(var[0][0]==0)
			index=0;
		 else if(var[1][0]==0)
			index=1;
		 else if(var[2][0]==0)
			index=2;
		 else if(var[3][0]==0)
			index=3;
		 else if(var[4][0]==0)
			index=4;
		 else cout<<"Parameters Exceeded max" << endl;
		 strcpy(var[index], v);
		 if(strlen(c)>180)
			{val[index++][j++]=0;
			 while(c[i])
				if(c[i]==' ' && c[i-1]==',')
					i++;
				else val[index-1][j++]=c[i++];
			}
		 else
		 strcpy(val[index++], c);
		 //cout << v << "=" << c << endl;
		}
	char* check_var(char *v)
		{int i, c1=0,c2=0;
		 char temp[250];
	 	 //cout << "in "; 
		 for(i=0; i<5; i++)
			{//cout << var[i] << endl;
			 if(var[i][0]==0)
				break;
			 if(strcmp(v, var[i])==0)
				{if(val[i][0]==0)
					{c1=1;
					 while(val[i][c1])
						{if(val[i][c1]==',')
							{temp[c2++] = val[i][c1++];
							 temp[c2++] = ' ';
							}
						 else temp[c2++]=val[i][c1++];
						}
					 temp[c2]=0;
					 strcpy(val[i], temp);
					 return(val[i]);
					}					 
				 else 
				return(val[i]);
				}
			 }
		 return(NULL);
		}
	void insert_link(Env_Node *p)
		{link=p;
		}
	Env_Node* get_link(){return link;}
};

