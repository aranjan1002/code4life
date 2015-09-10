#include "Env_Node.h"
#include <cstdlib>
#include <iostream>
#include <cstring>
using namespace std;

class Environs
{
 int curr_env[435];
 Env_Node *env_list[2000];
 int  curr_index;
 public:
	 Environs()
	 	{int i;
		 curr_index=0;
		 for(i=0; i<2000; i++)
			{env_list[i]=new Env_Node();
			}
		}
	 void setCurrEnv(int i)
		{
		 pushEnv(i);
		}
	 void pushEnv(int i)
		{if(!(isFull()))
			{curr_env[curr_index++]=i;
			 //cout << i << " pushed" << endl;
			 }
		 else cout << "Stack Full" << curr_index << endl;
		}
 	 int isFull()
		{if(curr_index>200)
			return 1;
		 else return 0;
		}
	 int isEmpty()
		{if(curr_index<=0)
			return 1;
		 else return 0;
		}
	 void popEnv()
		{if(!(isEmpty()))
			{curr_index--;
			 //cout << curr_env[curr_index] << " popped" << endl;
			 //free(env_list[curr_index]);
			}
		 else cout << "Stack Empty" << endl;
		}
 	 int getCurrEnv()
		{return (curr_env[curr_index-1]);
		}

	 void setLink(int i, int j)
		{env_list[i]->insert_link(env_list[j]);
		 //cout << "link between " << i << " and " << j << endl;
		}

	 char* check(char *temp)
		{
		 Env_Node *t=env_list[curr_env[curr_index-1]];
		 do
			{if(t->check_var(temp)!=NULL)
				return(t->check_var(temp));
			 else t=t->get_link();
			}
		 while(t != NULL);
		 return(NULL);
		}

	 void setValues(int i, char *v, char *c)
		{int j;
		 env_list[i]->insert_pair(v, c);
		}

};
