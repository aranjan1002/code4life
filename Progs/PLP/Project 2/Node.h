#include <iostream>
#include <cstring>

using namespace std;

class Node
{
 char val[50];
 Node *left;
 Node *right;
 
 public:
 
 Node()
 	{
	 val[0] = 0;
	 left = NULL;
	 right = NULL;
	}
 char* getVal()
	{//cout << val << "returned" << strlen(val) << endl;
	 return(val);
	}
 Node* getLeft()
	{
 	 return(left);
	}
 Node* getRight()
	{
	 return(right);
	}
 void setVal(char *temp)
	{//cout << "j " << temp << endl;
	 strcpy(val, temp);
	 //cout << "j " << val << endl;
	}
 void setLeft(Node *temp)
	{
	 left = temp;
	}
 void setRight(Node *temp)
	{
	 right = temp;
	}
};
