#include "Node.h"

void Node :: setdata(int val)
{
 this->data=val;
}

void Node :: setptr(Node *ptr)
{
 this->next = ptr;
}

int Node :: getdata()
{
 return(this->data);
}

Node* Node :: getptr()
{
 return(this->next);
}
