/*--------This file conatains the defintion of MinleftistTree class, which encompasses all the functions required to perform delelte and insert operation in a Min Leftist tree.--------*/

#include <iostream>
#include "FibonacciHeap.h"

using namespace std;
class LeftistNode								//defines the structure of a node of Leftist Tree
{
 public:
 int data;
 LeftistNode *leftchild, *rightchild;
 int shortest;
 int level;
 LeftistNode(){};
 LeftistNode(int x){data=x; leftchild=NULL; rightchild=NULL;shortest=1;};	//constructor initializing the data to value x, and making other initializations
};
 
class MinLeftistTree
{
 private:
 
 LeftistNode *root;
 LeftistNode * Meld(LeftistNode *, LeftistNode *);			//Melds two trees recursively by examining their nodes  

 public:
 int nodecount;
 MinLeftistTree(){root=NULL;nodecount=0;};				//root is initizlized to Null if constructor is not given a parameter
 MinLeftistTree(LeftistNode *x) : root(x) {};				//root is node passed as parameter
 int getMin();								//returns the minimum value of the tree(root->data)
 void DeleteMin();
 void insert(int);								//inserts a new node with value as the passed the parameter and no children
 void Meld(MinLeftistTree *);						//melds two trees
 bool isEmpty();								//returns true if root is Null else false
 LeftistNode* getRoot();							//returns the address of the root node
 void swap(LeftistNode**, LeftistNode**);					//swaps two value held by two node pointer variables
 void display(ofstream &);
};

void displaytree(LeftistNode *node, ofstream &outputfile)		//dispalys the structure of the tree by performing level order traversal
{
 static Queue<LeftistNode *> q;						//a queue is used for the traversal
 static int currentlevel=0;
 if(node->level > currentlevel)						//updates the currently traversing level accroding to the current node's level
	{outputfile << endl;
	 currentlevel++;
	}
 outputfile << node->data << " ";						
 if(node->leftchild)								//sends the children to the queue and updates their level values
	{node->leftchild->level=(node->level)+1;
	 q.push(node->leftchild);
	}
 if(node->rightchild)
	{node->rightchild->level=(node->level)+1;
	 q.push(node->rightchild);
	}
 if(!q.isEmpty())								//calls recursively if the queue is not empty
	 displaytree(q.pop(), outputfile);
 else currentlevel=0;
}

/*void display2(LeftistNode *root, int i)
{
int temp=i;
 while(temp--)std::cout << ".";
 std::cout << root->data << std::endl;
 if(root->leftchild)
 display2(root->leftchild, i+1);
 if(root->rightchild)
 display2(root->rightchild, i+1);
}*/

void MinLeftistTree :: display(ofstream& outputfile)			//calls the displaytree function using appropriate parameters
{
 if(root)
 {root->level=0;
 //display2(root,0);
 displaytree(root, outputfile);
 }
}

LeftistNode* MinLeftistTree :: getRoot()					//returns the address of root
{
 return root;
}

bool MinLeftistTree :: isEmpty()						//checks whether the tree is empty or not
{
 if(root==NULL)
	return 1;
 return 0;
}

int MinLeftistTree :: getMin()									
{
 if(!isEmpty())								//checks whether root exists or not. If it does not, then returns -1. It is assumed that the values in thre tree are positive
	return(root->data);
 else return(-1);
}

void MinLeftistTree :: DeleteMin()						//performs the deltemin function
{LeftistNode *temp=new LeftistNode();
 if(!isEmpty())								//this block sets the root of the tree by either melding its two children or setting it to one of its children or to NULL
	{temp=root;								//as per the situation
	 nodecount--;
	 if(root->leftchild==NULL && root->rightchild==NULL)
		root=NULL;
	 else if(root->leftchild==NULL)
		root=root->rightchild;
	 else if(root->rightchild==NULL)
		root=root->leftchild;
	 else root=Meld(root->leftchild, root->rightchild);
 	 if(temp)delete temp;
	}
 //else cout << "Nothing to delete in leftist tree" << endl;
}

void MinLeftistTree :: insert(int x)					//inserts a new node with value x in the tree by melding it with the original 
{
 LeftistNode *temp=new LeftistNode(x);
 if(root==NULL)
	root=temp;
 else root=Meld(root, temp);
 nodecount++;
}

void MinLeftistTree :: Meld(MinLeftistTree *x)				//melds two MinLeftist Trees
{
 if(root==NULL)
	root=x->getRoot();
 else if(x->getRoot()!=NULL)
	root=Meld(root, x->getRoot());
}

LeftistNode* MinLeftistTree :: Meld(LeftistNode* a, LeftistNode* b)	//melds two non empty MinLeftist Subtrees whose roots have been passed as parameter
{
 LeftistNode *temp;
 if(a->data > b->data)							//ensures that a holds the node with less value
	swap(&a, &b);
 if(a->rightchild!=NULL)							//recursively traverses the rightmost path of the subtree a, until a's right is equal to NULL
	a->rightchild=Meld(a->rightchild, b);
 else {a->rightchild=b;}							//in case a's right child is null, it is set to b
 if(a->leftchild==NULL || a->leftchild->shortest < a->rightchild->shortest)	//after each recursive call, the shortest values of a's left child and right child are 
	{swap(&a->leftchild, &a->rightchild);}					//compared and appropriate swapping is done if required
 if(a->rightchild==NULL) a->shortest=1;						//a's shortest values is updated
 else
 a->shortest=a->rightchild->shortest+1;
 return(a);
}

void MinLeftistTree :: swap(LeftistNode **x, LeftistNode **y)		//swaps the values held by two node pointers variables
{//cout << x->data << endl;
 LeftistNode *temp;
 temp=*x;
 *x=*y;
 *y=temp;
 //cout << "swap" << x->data << endl;
}
