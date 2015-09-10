/* Definitions.h, written by Steven V. Walstra for COP5555 */
/* Last modified: Fri Apr 21 00:56:11 EDT 1995	*/

/* This section includes integer definitions for all the valid RPAL types
   as well as a corresponding string array of the associated names.

   Also included are tree printing routines and all the structures used in
   the program (except those used in evaluation, which are in eval.h).
*/

#include <malloc.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>

#define ID 	1
#define INTEGER 2
#define STRING	3
#define OP	4
#define DELETE	5
#define L_PAREN	6
#define R_PAREN	7
#define SEMI	8
#define COMMA	9

#define LAMBDA 		10  /*10-16 -> Non-binary tree nodes*/
#define TAU    		11
#define CONDITIONAL 	12
#define ATX 		13
#define AND2 		14
#define FUNCT_FORM	15
#define FN		16

#define IN		18
#define PERIOD		19

#define LET		20
#define WHERE		21
#define AUG		22
#define OR		23
#define AND		24
#define NOT		25

#define GR		27
#define GE		28
#define LS		29
#define LE		30
#define EQ		31
#define NE		32

#define PLUS		35
#define MINUS		36
#define NEG		37
#define TIMES		38
#define DIVIDE		39
#define EXPO		40
#define BAR		41
#define GAMMA		45
#define TRUE		46
#define FALSE		47
#define XNIL		48
#define DUMMY		49
#define WITHIN		50
#define REC		51
#define EQUALS		52
#define EMPTY_PAREN	53
#define YSTAR		54

#define TNULL (struct token *) NULL
#define PNULL (struct tree *) NULL
#define SNULL (struct t_stack *) NULL
#define MAX_LINE_LENGTH 500

#define binary(x) ( (x!=REC)&&(x!=NOT)&&(x!=NEG) )

int debug=0; /* Debug state -- non_zero implies debugger mode */
int debug2=0; /* Evaluation debugger option */
int debug3=0; /* Evaluation debugger option */

char* names[55] = {"","","","","","","","","",",","lambda","tau","->","@","and",
		   "function_form","fn","","in",".","let","where","aug","or",
		   "&","not","rl","gr","ge","ls","le","eq","ne","","","+","-",
		   "neg","*","/","**","","","","","gamma","<true>","<false>",
		   "<nil>","<dummy>","within","rec","=","<()>","<Y*>"};

int line_num=0;				/* Global line number */
struct token {				/* Token structure */
		int type;
		char *name;
		int n_length;
		int location;
		struct token *next;
};

struct token *head; /* The first item on the token list */

struct tree {				/* Tree node structure */
		int type;
		struct tree *left, *right, *extra;
		char *id_name;
		char *pname;
		int value;
		int location;
};

struct t_stack {			/* Tree stack structure */ 
		struct tree *member; 
		struct t_stack *next;
		};

struct t_stack *top=NULL; /* Top of the stack */

void oom()
{
	printf("Error allocating memory\n");
	exit(-1);
};

void panic(string) /* Unclassy panic exit (out of memory, most likely) */
char *string;
{
	printf("%s \n",string);
	exit(-1);
}



void parse_error(string) /* Parse error exit with 'class' */
char *string;
{
       printf("Parse error: Expected \"%s\" in line %d, but \"%s\" was there\n",
		string,head->location,head->name);
	exit(-1);
}


char *create_string(ptr,length) /* Creates storage space for a string */
char *ptr;
int length;
{char *new;
 if ((new=(char *) malloc(length+1))==NULL)
				panic("Unable to malloc enough space");
 strncpy(new,ptr,length);
 new[length]=NULL;
 return(new);
}


void dots(n) /* Prints out n periods */
int n;
{ 
 while(n>0) {
	printf(".");
	n--;}
}


void name_dump(x) /* Prints the name of the node based on the passed pointer */
struct tree *x;
{
 if (x->type==ID) {printf("<ID:%s> \n",x->id_name);}
 else if (x->type==INTEGER) {printf("<INT:%d> \n",x->value);}
 else if (x->type==STRING) {printf("<STR:'%s'> \n",x->id_name);}
 else printf("%s \n",names[x->type]);
}


void traverse_2(); /* Forward declaration */

void traverse_tree(temp,n) /* Traverses a tree, printing nodes along the way */
struct tree *temp;
int n;
{
 dots(n); 		/* Prints out the leading dots */
 name_dump(temp);  	/* Prints the node name */
 if (temp->extra!=PNULL) 
	traverse_2(temp,n);
 else {
 	if (temp->left!=PNULL) traverse_tree(temp->left,n+1);
 	if (temp->right!=PNULL) traverse_tree(temp->right,n+1);
 }
}


void traverse_2(temp,n) /* Handles the n-ary nodes */
struct tree *temp;
int n; 
{ 	traverse_tree(temp->extra,n+1);
	if (temp->left!=PNULL) traverse_2(temp->left,n);
}


/* This function converts /t, //, /', and /n. . .It SHOULD be in the tokenizer
   section, but the fact that the AST and ST print out unconverted strings
   prevents this. . .
*/
char *strip(temp2)
char *temp2;
{
 int yes;
 char *temp=(char *) malloc (sizeof(char)*(strlen(temp2)+1));
 char *temp3=strcpy(temp,temp2); /* Copy string into new memory */
 while(*temp!=NULL) { 
        if (*temp==92) {
                yes=1;
                switch(temp[1]) {
                        case 92:
                        case 39: break;
                        case 't': temp[1]=9; break;
                        case 'n': temp[1]=13; break;
                        default : printf("Failure\n");yes=0; /*Invalid escape*/
                }
                if (yes==1) {
                        temp2=temp;
                        while(*temp!=NULL) {
                                temp[0]=temp[1];/* Shift the characters over */
                                temp++;
                        }
                        temp=temp2;
        
                }
        }
 temp++;
 }
 return(temp3);
}
