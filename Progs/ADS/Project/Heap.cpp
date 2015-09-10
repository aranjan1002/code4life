/*---------This is the file coontaining the main() function. It handles user input and make appropriate calls to other functions--------------*/

#include <vector>
#include <iostream>
#include <string>
#include <cstring>
#include <fstream>
#include <cstdlib>
#include <ctime>
#include "MinLeftistTree.h" 

#define M 5000							//count of randomly generated numbers
#define N 5000							//count of numbers with which the tree is initialized in random mode

using namespace std;

void display(vector<FibonacciHeapNode *> x)				//for testing purpose, i used this function to see the values currently in the reference vector
{
 vector<FibonacciHeapNode *> :: iterator i;
 i=x.begin();
 while(i!=x.end())
 	{cout << (*i)->data << " ";
	 i++;
	}
 cout << endl;
}

int findmin(vector <FibonacciHeapNode *> x, FibonacciHeapNode *y)	//it returns the index value of reference vector in which the address of node containing the minimum value of 
{										//the whole heap is contained. Later on it is used to delete the corresponding index from vector(in deletemin operation)
 int count=0;
 while(count<x.size())
	{if(x[count++]==y)
		return count-1;
	}
 return count;
}

void exchange(int *x, int *y)						//used for random generation of unique numbers between 0 and N
{
 int temp=*x;
 *x=*y;
 *y=temp;
}

int main(int argc, char **argv)
{
 FibonacciHeap tree;								//Declarations
 MinLeftistTree leftisttree;
 int ins=0, del=0;
 int cnt=0, operation=0, fibflag=0, leftflag=0, bothflag=0;
 vector <FibonacciHeapNode *> listofnodes;				//contains addresses of nodes currently present in the fibonacci heap
 //vector <FibonacciHeapNode *> :: iterator i;
 int i;
 char inputfile[100];
 int randominit[N];
 string input;
 int value, index;
 int count=0;
 clock_t start1, start2, duration;


 if(argc!=2 && argc!=3)							//checking the validity of number of arguments passed
	{
	 cout << "Invalid number of arguments" << endl;
	 exit(0);
	}
 if(strcmp(argv[1], "-r")==0 && argc==2)					//checking whether random mode is requested
	{bothflag=1;
	 fibflag=0;
	 leftflag=0;
	 ofstream operations("operations.txt", ios::out);		//an output file is generated to store the random operations
	 for(i=0; i<N; i++)
		randominit[i]=i;
	 for(i=N-1; i>=0; i--)						//in this loop, the tree is initialized with unique N randomly generated values ranging from 0 to N-1
		{count=rand()%(i+1);
		 exchange(&randominit[count], &randominit[i]);
		 tree.Insert(randominit[i]);
		 leftisttree.insert(randominit[i]);
		}
	 cnt=0;
 	 while(cnt<M)								//the random operations (M) are generated and stored in this loop
		{operation=rand()%2;
	 	 if(operation==0)
			{operations << "D" << endl;
			 if(ins>0)ins--;
			}
		 else {operations << "I " << rand()%M << endl; ins++;}
	 	 cnt++;
		}
	 strcpy(inputfile, "operations.txt");				//the generated file would act as inputfile
	}
 else if(argc==3 && strcmp(argv[1], "-il")==0)				//checking whether its user input mode
	{leftflag=1;
	 fibflag=0;
	 strcpy(inputfile, argv[2]);
	}
 else if(argc==3 && strcmp(argv[1], "-if")==0)
	{leftflag=0;
	 fibflag=1;
	 strcpy(inputfile, argv[2]);
	}
 else {cout << "Invalid syntax" << endl;
	exit(0);
	}
 ifstream inputs(inputfile, ios::in);					//the inputfile contains the name of the input file, and its file pointer is stored in 'inputs'
 if(bothflag)									//checks for random mode
 {										
 start1=clock();								//clock is started
 while(inputs >> input)							//receiving inputs 
	{
	 //cout << "input : " << input << endl;
	 if(input=="D")
		{
		 tree.removeMin();						//calling removemin if the operation is D
		}
	 else if(input=="I")							//calling insert if the operation is I
		{inputs >> value;
		 tree.Insert(value);
		}
	}
 duration=clock()-start1;
 cout << "Fibonacci\nDuration: " << duration << endl;			//outputs the difference in standard output
 cout << "Time per instruction: " << duration/5000.0 << endl << endl;
 inputs.close();
 }
 if(fibflag)									//in case the switch is -if
 {
  while(inputs >> input)
	{
	 //cout << "input : " << input << endl;
	 if(input=="D")
		{
		 if(listofnodes.size()>0)					//updates the reference vector
		 	{listofnodes.erase(listofnodes.begin() + findmin(listofnodes, tree.getMin()));
			}
		 tree.removeMin();						//calls removemin function
		}
	 else if(input=="I")
		{inputs >> value;
		 //cout << " " << value << endl;
		 listofnodes.push_back(tree.Insert(value));		//calls insert function
		}
	 else if(input=="R")							//checks for remove operation
		{inputs >> index;						
		 if(index<listofnodes.size())
		 	{tree.Remove(listofnodes[index]);			//calls remove function
			 listofnodes.erase(listofnodes.begin()+index);	//updates the refernce vector
			}
		 //cout << "trying to remove " << *(listofnodes.begin()+index)->data << endl;
		}
	 else if(input=="DK")						//checks for decrease key operation
		{inputs >> index >> value;
		 //cout << " " << index << " " << value << endl;
		 if(index<listofnodes.size())
			tree.decreaseKey(listofnodes[index], value);	//calls decreasekey function if the given index is valid
		}
	 //tree.display();
	 //cout << endl;
	}
 ofstream output("result_bino.txt", ios::out);				//the structure of the final tree is displayed in the file "result_bino.txt"
 tree.display(output);
 }
 else if(bothflag || leftflag)						//the block runs the operations on leftist tree if its random more or if the user has selected the switch "-il"
 {
 ifstream inputs2(inputfile, ios::in);					
 start2=clock();								//clock started
 while(inputs2 >> input)							//this block runs operations on the leftist tree by calling appropriate functions
	{//cout << input << endl;
	 if(input=="D")
		{leftisttree.DeleteMin();
		}
	 else if(input=="I")
		{inputs2 >> value;
		 leftisttree.insert(value);
		}
	 //leftisttree.display();
	}
 duration=clock()-start2;							//difference in time is calculated
 if(bothflag)
 	{
 	 cout << "LeftistTree\nDuration: " << duration << endl;		//difference of time is output if the user has selected random mode
	 cout << "Time per instruction: " << duration/5000.0 << endl;
	}
 else {ofstream output("result_left.txt", ios::out);
	leftisttree.display(output);					//final structure of the tree is written in the file "result_left.txt" if the user has selected the switch "-il"
	}
 }
 /*ofstream output("result_left.txt", ios::out);
 ofstream output2("result_bino.txt", ios::out);
 tree.display(output2);
 cout << endl << endl;
 leftisttree.display(output);
 cout << del << " " << ins << endl;
 cout << tree.nodecount << " " << leftisttree.nodecount << endl;*/
}
