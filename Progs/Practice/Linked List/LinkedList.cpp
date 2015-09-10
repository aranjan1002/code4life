#include "LinkedList.h"
#include <iostream>

using namespace std;

LinkedList :: LinkedList()
{
 start = new Node();
 start->setdata(-1);
 start->setptr(NULL);
}

void LinkedList :: insert(int val)
{
 Node *temp;
 if(start->getdata()==-1)
 	start->setdata(val);
 else {
 	temp = start;
 	while(temp->getptr() != NULL)
		temp=temp->getptr();
	temp->setptr(new Node());
	temp->getptr()->setdata(val);
	temp->getptr()->setptr(NULL);
	}
}

void LinkedList :: deletenode()
{
 Node *temp;
 if(isEmpty())
	cout << "List is empty" << endl;
 else if(start->getptr()==NULL)
	start->setdata(-1);
 else {
 	temp = start;
	while(temp->getptr()->getptr() != NULL)
		temp=temp->getptr();
	delete temp->getptr();
	temp->setptr(NULL);
	}
}

int LinkedList :: isEmpty()
{
 if(start->getdata()==-1)
	return 1;
 else return 0;
}	

void LinkedList :: display()
{
 Node *temp;
 if(isEmpty())
	cout << "List is empty" << endl;
 else {
 	temp=start;
 	while(temp!=NULL)
		{cout << temp->getdata() << endl;
		 temp=temp->getptr();
		}
	}
}

void LinkedList :: reverse()
{
 if(start->getptr()!=NULL)
 	reverselist(start, start->getptr());
}

void LinkedList :: reverselist(Node *p1, Node *p2)
{
 /*if(t2->getptr()!=NULL)
	reverselist(t1->getptr(), t2->getptr());
 else {
	start->setptr(NULL);
	start=t2;
	}
 t2->setptr(t1);*/
 Node *t, *t1, *t2;
 t1=start;
 t=start->getptr();
 start->setptr(NULL);
 while(t!=NULL)
	{
	 t2=t->getptr();
	 t->setptr(t1);
	 t1=t;
	 t=t2;
	}
 start=t1;
}
