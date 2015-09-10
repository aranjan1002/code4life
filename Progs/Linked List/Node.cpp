#include "Node.h"


Node :: Node()
{next = NULL;}

void Node :: setVal(int num)
{
 val = num;
}

void Node :: setNext(Node *p)
{
 this->next = p;
}

int Node :: getVal()
{
 return this->val;
}

Node* Node :: getNext()
{
 return this->next;
}

