#ifndef DEFS_H
#define DEFS_H

#include <string>

#define MAX_ANDS 20
#define MAX_ORS 20

#define PAGE_SIZE 131072

using namespace std;

enum Target {Left, Right, Literal};
enum CompOperator {LessThan, GreaterThan, Equals};
enum Type {Int, Double, String};

unsigned int Random_Generate();

string ToString(Type&);

#endif

