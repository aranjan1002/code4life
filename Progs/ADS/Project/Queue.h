/*--------This file contains the class Queue whilch performs teh normal operations of a queue i.e. push and pop. It has been implemented using array------------*/

#include <iostream>

using namespace std;

#define SIZE 50000							//maximum size of the queue

template<class T>
class Queue
{
 int front, rear;
 T array[SIZE];
 public:
 Queue();
 void push(T x);
 T pop();
 //void display();
 inline bool isEmpty(){return(front==-1);}
};

template <class T>
Queue<T> :: Queue()							//initialized the front and rear values
{
 front=-1;
 rear=-1;
}

template <class T>
void Queue<T> :: push(T x)						//performs the push operation
{
 if(front==-1)							//if the queue is empty both front and rear are incremented else if the queue's limit is not reached, rear 
	{array[++front]=x;						//is incremented, and the value is inserted
	 ++rear;
	}
 else if((rear+1)%SIZE!=front)
	{
	 rear=(rear+1)%SIZE;
	 array[rear]=x;
	}
 else cout << "Queue limit exceeded" << endl;
}

template <class T>
T Queue<T> :: pop()							//performs the pop operation
{
 T temp;
 if(front==-1)							//if the queue is non-empty then the value pointed by rear is returned and rear is decremented.
	{cout << "Queue Empty";					
	}
 else {temp=array[front];
	if(front==rear)	
		{front=-1;rear=-1;}					//in case the queue becomes empty the front and rear are set to -1
	else front=(front+1)%SIZE;
	return temp;
	}
}
 
/*template <class T>
void Queue<T> :: display()
{
 int i=front, j=rear;
 if(i==-1)
	return;
 else do
	{cout << array[i] << " ";
	 i=(i+1)%SIZE;
	}while(i!=(j+1)%SIZE);
 cout << endl;
}*/

