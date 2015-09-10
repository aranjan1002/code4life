#include "Node.h"

class LinkedList
{
 Node *start;
 
 public:
 LinkedList();
 void insert(int);
 void deletenode();
 int isEmpty();
 void reverse();
 void reverselist(Node*, Node*);
 void display();
};

  