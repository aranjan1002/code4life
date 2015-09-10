/*-------This file contains the declaration and definitions for all the functions of fibonacci heap. The file 'Queue.h' which it has included, contains a class for generating queue, which 
 is used for level order traversal of the tree------------*/

#include <cstring>
#include "Queue.h"

using namespace std;

#define MAXDEGREE 100												//defines the maximum degree that a node can have

class FibonacciHeapNode												//the basis structure of a node is defined
{
 public:
 	int degree, data;
	int level;
	bool childcut;
	FibonacciHeapNode *parent, *leftsibling, *rightsibling, *child;
	FibonacciHeapNode(){};
	FibonacciHeapNode(int x)											//the constructor initializes the values 
		{degree=0;
		 data=x;
		 child=NULL;
		 childcut=false;
		 parent=NULL;
		 leftsibling=this;
		 rightsibling=this;
		};
};

class FibonacciHeap
{
 private:
	FibonacciHeapNode *minptr;
	int maxdegree;

 public:
	int nodecount;
	FibonacciHeap(){minptr=NULL; maxdegree=MAXDEGREE; nodecount=0;}					//function description is given in function declaration part
	FibonacciHeapNode* Insert(int x);										
	void insertNode(FibonacciHeapNode *x);
	void removeMin();
	void Meld(FibonacciHeapNode *x);
	void Remove(FibonacciHeapNode *x);
	void decreaseKey(FibonacciHeapNode *x, int);
	void display(ofstream&);
	void displaytree(FibonacciHeapNode *node, ofstream &);
	void PairwiseCombine(FibonacciHeapNode *parenttobe, FibonacciHeapNode *childtobe);
 	void removeNode(FibonacciHeapNode *x);
	void cascadeCut(FibonacciHeapNode *x);
	inline FibonacciHeapNode* getMin(){return minptr;}
};

void FibonacciHeap :: decreaseKey(FibonacciHeapNode *x, int decreaseby)					//decreases the key value of the node x by amount decreaseby
{
 if(x->data>=decreaseby)
	x->data=x->data-decreaseby;											//value decreased
 else return;
 if(x->parent)		
	if(x->parent->data<x->data)											//checks whether the parent has larger value than the changed amount
		return;
	else
		{
		 removeNode(x);											//removes the current node from its subtree
		 cascadeCut(x->parent);										//cascade cuts starting from its parent
		 x->parent=NULL;											
		 insertNode(x);											//current node is inserted at the top level
		}
}

void FibonacciHeap :: cascadeCut(FibonacciHeapNode *x)								//start the cascade cut from the node x
{
 if(!x) return;
 while(x->childcut && x->parent)											//checks its childcut value and whether its parent exists
	{removeNode(x);												//if both are true, then, removes the node, insert in the top level and
	 insertNode(x);												//process is repeated for its parent node
	 x=x->parent;
	}
 x->childcut=true;													//the last node which has not been removed, its childcut value is set to true
}

void FibonacciHeap :: removeNode(FibonacciHeapNode *x)								//removes a node from its tree 
{
 if(!x) return; 
 x->leftsibling->rightsibling=x->rightsibling;									//adjust properties of its left and rightsiblings
 x->rightsibling->leftsibling=x->leftsibling;

 if(x->parent)
	if(x->parent->child==x)
		if(x->rightsibling==x)
			x->parent->child=NULL;									//if it has a parent whose child pointer points to itself and who has more than one child
		else x->parent->child=x->rightsibling;								//then, its parent child value is set to NULL else its parents child value is to its 
} 															//rightsibling

void FibonacciHeap :: Remove(FibonacciHeapNode *x)								//performs the remove operation
{
 FibonacciHeapNode *currentchild=x->child, *nextchild;
 if(x==minptr) {removeMin(); return;}										//removemin() called in case it is minptr
 if(currentchild)													//this block sets all its chilren's parent to NULL, and insert them at the top level
	do{																
	   currentchild->parent=NULL;
	   nextchild=currentchild->rightsibling;
	   insertNode(currentchild);
	   currentchild=nextchild;
	  }while(currentchild!=x->child);
 removeNode(x);													//the node is removed from its tree using removeNode() function
 if(x->parent) {cascadeCut(x->parent);}										//its parent undergoes cascade cut
 delete x;														//node is deleted
 nodecount--;
}

FibonacciHeapNode* FibonacciHeap :: Insert(int x)								//inserts a new node in the heap with value x
{
 FibonacciHeapNode *temp=new FibonacciHeapNode(x);								//constructs a new node
 
 if(!minptr)
	minptr=temp;													
 else {
	temp->leftsibling=minptr;											//inserts the node near the minptr
	temp->rightsibling=minptr->rightsibling;
	minptr->rightsibling->leftsibling=temp;
	minptr->rightsibling=temp;
	}
 if(minptr->data > temp->data)											//compares its value with that of minptr
	minptr=temp;
 nodecount++;
 return(temp);
}
 
void FibonacciHeap :: insertNode(FibonacciHeapNode *temp)							//inserts a node in the heap
{
 if(!minptr)
	minptr=temp;
 else {
	temp->leftsibling=minptr;											//inserts the node near the minptr
	temp->rightsibling=minptr->rightsibling;
	minptr->rightsibling->leftsibling=temp;
	minptr->rightsibling=temp;
	}
 if(minptr->data > temp->data)											//compares its value with that of minptr
	minptr=temp;
}

void FibonacciHeap :: PairwiseCombine(FibonacciHeapNode *parenttobe, FibonacciHeapNode *childtobe)	//pairwise combine two nodes by removing childnode from its subtree and making other
{															//adjustments
 if(parenttobe==childtobe)
	cout << "Exception: parent and child are same pointers" << endl;
 childtobe->leftsibling->rightsibling=childtobe->rightsibling;							//removes the child node from its subtree and adjusts its siblings' properties
 childtobe->rightsibling->leftsibling=childtobe->leftsibling;
 if(parenttobe->child)												//this block makes the combination of parent's existing children and the new child
	{childtobe->rightsibling=parenttobe->child;
	 parenttobe->child->leftsibling->rightsibling=childtobe;
 	 childtobe->leftsibling=parenttobe->child->leftsibling;
	 parenttobe->child->leftsibling=childtobe;
	}
 else {childtobe->leftsibling=childtobe;										//in case, the parent had no children, the child points to itself as its siblings
	childtobe->rightsibling=childtobe;	
	}
 parenttobe->child=childtobe;											//establishes parent-child relationship
 childtobe->parent=parenttobe;
 childtobe->childcut=false;												//childcut of child set to false
 parenttobe->degree=parenttobe->degree+1;										//parent's degree set to false
}

void FibonacciHeap :: removeMin()											//performs the removemin function
{
 FibonacciHeapNode *temp, *temp2;
 int flag;
 
 if(!minptr) return;													
 if(!minptr->child && minptr->rightsibling==minptr) {delete minptr; minptr=NULL; nodecount--; return;}	//if the min is the only node in the heap, it is deleted and function returns
 
 temp=minptr->child;
 
 if(temp)														//if min has one or more children, their parent is set to null
	{while(temp->parent)
		{temp->parent=NULL;
		 temp2=temp->rightsibling;
		 //insertNode(temp);
		 temp=temp2;
		}
	 if(minptr->leftsibling!=minptr)										//if min is not the only node at top level, its children is inserted into the top level
	 	{minptr->leftsibling->rightsibling=minptr->child->rightsibling;
	 	 minptr->child->rightsibling->leftsibling=minptr->leftsibling;
		 minptr->child->rightsibling=minptr->rightsibling;
	 	 minptr->rightsibling->leftsibling=minptr->child;}
	 else temp=minptr->child;											//temp holds node from where the comparison of degrees and pairwise combine would start
	 //display();
	}
 else {minptr->leftsibling->rightsibling=minptr->rightsibling;							//if min has no children, its siblings' properties are adjusted
	minptr->rightsibling->leftsibling=minptr->leftsibling;
	temp=minptr->rightsibling;
	}
 //cout << minptr << " to be freed " << endl;
 delete minptr;													//min is deleted
 minptr=temp;														//min randomly selected as temp
 nodecount--;
 FibonacciHeapNode *degreeindex[MAXDEGREE], *parenttobe, *childtobe, *flagnode;				//array degreeindex would store the node with degree equal to its index
 memset(degreeindex, 0, sizeof(degreeindex));
 temp2=temp;														//temp2 holds the current node under consideration in the loop of pairwise combining
 flagnode=temp2->leftsibling;											//flagnode helps in terminating the loop of pairwise combining
 flag=0;
 do
	{if(temp2->data <= minptr->data)										//checks for minimum value
		minptr=temp2;
	 if(temp2==flagnode)												//checks whether flagnode is reached or not
		flag=1;
	 if(degreeindex[temp2->degree]==NULL)									//if the degreeindex value for currentnode's degree is NULL, the currentnode is inserted
		{degreeindex[temp2->degree]=temp2;
		 //cout << temp2->degree << ". " << temp2->data << endl;
		 temp2=temp2->rightsibling;										//currentnode moves towards right
		}
	 else
		{parenttobe=temp2;											
		 temp2=temp2->rightsibling;
		 do													//in case of degreeindex has an entry for the currentnode's degree the block
			{if(degreeindex[parenttobe->degree]->data < parenttobe->data)				//selects a proper parent and child and then calls the pairwisecombine function
				{childtobe=parenttobe;								
				 parenttobe=degreeindex[parenttobe->degree];
				}
			 else if(degreeindex[parenttobe->degree]->data == parenttobe->data)			//this if block compares the values of the two nodes under consideration
				if(minptr==parenttobe || minptr==degreeindex[parenttobe->degree])		//and sets the value of child and parent node
					{if(parenttobe!=minptr)
						childtobe=parenttobe;
					 else childtobe=degreeindex[parenttobe->degree];
					 parenttobe=minptr;
					}
				else {childtobe=parenttobe;
				      parenttobe=degreeindex[parenttobe->degree];
				     }
			 else childtobe=degreeindex[parenttobe->degree];
			 //cout << "combining" << parenttobe->data << " and " << childtobe->data << endl;
			 PairwiseCombine(parenttobe, childtobe);							//calls the pairwise finction to make the parent child relationship
			 degreeindex[parenttobe->degree-1]=NULL;							//degreeindex is adjusted
			 //3display();
			 //cout << endl;
			}while(degreeindex[parenttobe->degree]!=NULL);						//loops continues untill the degree of the current parent does not match with any other node
		 degreeindex[parenttobe->degree]=parenttobe;					
		 //cout << parenttobe->degree << ". " << parenttobe->data << endl;
		}
	}while(!flag);												//loop terminates once flag is set
}

void FibonacciHeap :: displaytree(FibonacciHeapNode *node, ofstream &outputfile)				//displays the structure of a subtree using level order traversal
{static Queue<FibonacciHeapNode *> q;										//the queue is used to perform level order traversal
 static int currentlevel=0;
 if(node->level > currentlevel)
	{outputfile << endl;
	 currentlevel++;												//current level is adjusted according to the level property of the node
	}
 outputfile << node->data << " ";
 if(node->child)	
	{node->child->level=(node->level)+1;									//child's level value is set to one more than its parent's
	 q.push(node->child);											//the child is pushed in the queue
	}
 if(node->rightsibling->parent!=NULL && node->rightsibling->parent->child!=node->rightsibling)
	 {node->rightsibling->level=(node->parent->level)+1;							//each sibling's level value is set to one more of its parent
	  displaytree(node->rightsibling, outputfile);
	 }
 if(!q.isEmpty())
	 displaytree(q.pop(), outputfile);										
 else currentlevel=0;												//currentlevel is set to 0 for subsequent call to this funtions by another subtree
}
 
void FibonacciHeap :: display(ofstream& outputfile)								//traverses the top level and calls dispalytree for each node
{
 FibonacciHeapNode *temp;
 FibonacciHeapNode *arr[5000];
 int noofroots=0, i, j;
 //cout << "Display" << endl;
 if(minptr)
	{temp=minptr;
	 do
	   {arr[noofroots++]=temp;
	    temp=temp->rightsibling;
	   }while(temp!=minptr);
	 for(i=1; i<noofroots; i++)											//the top level nodes are stored in an array and are sorted in the ascending order
		{temp=arr[i];												//of theri degrees
		 j=i-1;
		 if(j>=0 && arr[j]->degree>temp->degree)
		 	{while(j>=0 && arr[j]->degree>temp->degree)
				arr[j+1]=arr[j--];
			 arr[j+1]=temp;
			}
		}
	 i=0;
 	 do
	    {outputfile << "###" << endl;
	     arr[i]->level=0;
	     displaytree(arr[i++], outputfile);									//for each node the function dispalytree is called
	     outputfile << endl;		
	     /*if(temp->child)
		displaytree(temp);
	     else cout << " " << temp->data << endl;
	     //else cout << "no child" << endl;
	     temp=temp->rightsibling;
		//cout << "minptr is " << minptr->data << endl;*/
	    }
	 while(i<noofroots);
	}
}
 