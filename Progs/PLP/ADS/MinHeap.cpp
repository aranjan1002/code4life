#include <iostream>
//#include "minPQ.h"

#define size 100

using namespace std;

template <class T>
class minPQ{
public:
	virtual ~minPQ() {}
	virtual bool IsEmpty() const = 0;
	virtual const T& Top() const=0;
	virtual void Push(const T&)=0;
	virtual void Pop()=0;
};

template <class T>
class MinHeap : public minPQ<T>
{
private:
	T heap[size+1];
	int heapsize;
public:
	MinHeap();
	bool IsEmpty() const;
	const T& Top() const;
	void Push(const T&);
	void Pop();
	void Display();
};

template <class T>
MinHeap<T> :: MinHeap()
{ heapsize=0;
 }

template <class T>
bool MinHeap<T> :: IsEmpty() const
{
 if(heapsize==0)
	return 1;
 else return 0;
}

template <class T>
const T& MinHeap<T> :: Top() const
{
 if(!IsEmpty())
	return(heap[1]);
 else return(heap[0]);
}

template <class T>
void MinHeap<T> :: Push(const T& val)
{
 int currnode = heapsize + 1;
 while(heap[currnode/2]>val && currnode!=1)
	{heap[currnode] = heap[currnode/2];
	 currnode = currnode/2;
	}
 heap[currnode]=val;
 heapsize++;
}

template <class T>
void MinHeap<T> :: Pop()
 {
 int temp, currnode;
  if(!IsEmpty())
	{
	 if(heapsize==1)
		heap[1].~T();
	 else 
		{temp=2;
		 currnode=1;
		 while(temp<heapsize)
			{if(heap[temp]>heap[temp+1])
				temp++;
			 if(heap[temp]>=heap[heapsize])
				break;
			 heap[currnode]=heap[temp];
			 currnode=temp;
			 temp=temp*2;
			}
		 heap[currnode]=heap[heapsize--];
		}
	}
  else cout << "Heap is already empty" << endl;
 }	        

template <class T>
void MinHeap<T> :: Display()
 {
  int i=0;
  while(++i <= heapsize)
	cout << i << ". " << heap[i] << "\t";
 }

		 