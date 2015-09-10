/*--------This file contains the class Buffer whilch performs teh normal operations of a queue i.e. push and pop. It has been implemented using array------------*/

#include <iostream>

using namespace std;

template<class T>
class Buffer
{
 int front, rear;
 T *array;
 int size;
 public:
 
 Buffer();
 Buffer(int);
 void push(T x);
 T pop();
 T pop(int);
 T peek(int);
 bool isElement(int);
 int numofelements();
 //void display();
 inline bool isEmpty(){return(front==-1);}
 inline bool isFull(){return(rear+1)%size==front;}
};

template <class T>
int Buffer<T> :: numofelements()
{
 if(front==-1)
	return 0;
 else if(front==rear)
	return 1;
 else if(front<rear)
	return(rear-front+1);
 else return(size-front+rear+1);
}

template<class T>
bool Buffer<T>::isElement(int n)
{
 int count=numofelements();
 if(count==0 || count<n || n==0)
	return false;
 return true;
}

template <class T>
T Buffer<T> :: peek(int n)
{
return(array[(front+n-1)%size]);
}

template <class T>
Buffer<T> :: Buffer()							//initialized the front and rear values
{
 front=-1;
 rear=-1;
 array=new T[10];
 size=10;
}

template <class T>
Buffer<T> :: Buffer(int l)							//initialized the front and rear values
{
 front=-1;
 rear=-1;
 array=new T[l];
 size=l;
}

template <class T>
void Buffer<T> :: push(T x)						//performs the push operation
{
 if(front==-1)							//if the queue is empty both front and rear are incremented else if the queue's limit is not reached, rear 
	{array[++front]=x;						//is incremented, and the value is inserted
	 ++rear;
	}
 else if((rear+1)%size!=front)
	{
	 rear=(rear+1)%size;
	 array[rear]=x;
	}
}

template <class T>
T Buffer<T> :: pop(int index)							
{
 T temp;
 int i;
 if(front==-1)							
	{//return NULL;					
	}
 else {temp=array[index-1];
	i=index-1;
	if(front==rear)	
		{front=-1;rear=-1;return temp;}
	while(i!=rear)
		{array[i]=array[(i+1)%size];
		 i=(i+1)%size;
		}
	if(i==0)
		rear=size-1;
	else rear=i-1;
	return temp;
	}
}
 
template <class T>
T Buffer<T> :: pop()							//performs the pop operation
{
 T temp;
 if(front==-1)							//if the queue is non-empty then the value pointed by rear is returned and rear is decremented.
	{//return NULL;					
	}
 else {temp=array[front];
	if(front==rear)	
		{front=-1;rear=-1;}					//in case the queue becomes empty the front and rear are set to -1
	else front=(front+1)%size;
	return temp;
	}
}



/*template <class T>
void Buffer<T> :: display()
{
 int i=front, j=rear;
 if(i==-1)
	return;
 else do
	{cout << array[i] << " ";
	 i=(i+1)%size;
	}while(i!=(j+1)%size);
 cout << endl;
}*/

