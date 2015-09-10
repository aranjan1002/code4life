/* File eval.h */
/* All program by Steven V. Walstra (svw@cis.ufl.edu)
   Fri Apr 21 00:21:12 EDT 1995

   This header file includes the definitions specific to eval.c/functions.c,
   as well as several functions which act on the many structures defined 
   within. I should have written this in C++. . .
*/


/* Aug,  Lambda, Gamma, YStar, and COND, COMMA, and TAU defined already */
#define GENERAL	0   /* General things which stack by CSE Rule 1 */
#define BINOPA	100 /* Arithmatic BINOP */
#define BINOPL  101 /* Logical BINOP */
#define UNOP	110 /* Unary operators  */
#define BETA	200 /* The beta structure (for conditionals) */
#define ETA	300 /* The eta structure (for recursion) */
#define ENVIRO	400 /* The environment marker type */
#define TUPLE	500 /* The tuple type */
#define EMPTY	-1  /* Signfies empty stack */
#define UNKNOWN -2  /* Unknown variable */

/* Instrinsic commands (also Aug) */
#define ISINTEGER	600
#define ISTRUTHVALUE	601
#define ISSTRING	602
#define ISTUPLE		603
#define ISFUNCTION	604
#define ISDUMMY		605
#define	PRINT		606
#define CONC		607
#define ORDER		608
#define ITOS		609
#define STEM		610
#define STERN		611
#define XNULL		612

struct next_en { /* Used exclusively within struct environment */
	struct environment *here;
	struct next_en *next;
};


struct environment { /* Environment linked n-ary tree */
	int number;
	struct environment *prev; /* Previous */
	struct next_en *next; /* Next (can have any number of children)*/
	struct item *from; /* Variable to match */
	struct item *to;   /* Replacement value */
};


struct leta { /* Lambda and eta */
	int k; /* index */
	struct environment *c; /* current environment */
	struct item *x; /* variable */
};


struct beta { /* Beta structure -- simulates df dt beta */
	struct delta *T;
	struct delta *F;
	};


struct tuple { /* Tuple type */
	int number;
	struct item *storage;
	};

struct delta { /* The control-structure structure ! */
	int number;
	struct item *structure;
	struct delta *next;
};


struct item { /* The item structure is the CSE stacks' basic building block */
	int type;
	struct item *next;
	union {
		struct leta *l;
		struct environment *epsilon; 
		struct beta *b;
		struct tree *t;
		struct tuple *tup;
		int tau;
	} val;
};


/* Find_env returns the value of the environmnent via the nearest enviroment 
   marker*/
struct environment *find_env(s) 
struct item *s;
{  
  while (s->type!=ENVIRO) s=s->next;
  return (s->val.epsilon);
};


struct item *make_cse_stack(type) /* Makes room for an item */
int type;
{
	struct item *c = (struct item *) 
			malloc(sizeof(struct item));
	if (c==NULL) oom();
	c->type=type;
	return(c);
}


struct leta *make_lambda(k,x) /* Creates a new lambda/eta structure */
int k; /* Index */
struct tree *x; /* variable */
{	
	struct item *it=make_cse_stack(NULL);
	struct leta *l = (struct leta *) malloc(sizeof(struct leta));
	if (l==NULL) oom();
	it->val.t=x; /* Put tree into an item */
	l->k=k;
	l->x=it;
	l->c=NULL;
	return(l);
}


struct beta *make_beta()  /* Creates a beta node -- paired deltas */
{
	struct beta *b = (struct beta *) malloc(sizeof(struct beta));
	if (b==NULL) oom();
	b->T = NULL;
	b->F = NULL;
	return(b);
}


struct delta *make_delta() /* Creates and initializes a delta node */
{
	struct delta *d = (struct delta *) malloc(sizeof(struct delta));
	if (d==NULL) oom();
	d->structure=NULL;
	d->number=0;
	d->next=NULL;
	return(d);
}


struct item *wind(list) /* Wind up the list -- return the last member */
struct item *list;
{ 
 if (list==NULL) return(NULL);
 while(list->next!=NULL) list=list->next;
 return(list);
}


struct item *wind2(list) /* Return the second to last member (or NULL) */
struct item *list;
{ 
 if (list->next==NULL) return(NULL);
 while(list->next->next!=NULL) list=list->next;
 return(list);
}


struct delta *wind_d(list)  /* Return the last member on the delta list */
struct delta *list;
{ 
 if (list==NULL) return(NULL);
 while(list->next!=NULL) list=list->next;
 return(list);
}


/* Make_env returns a pointer to a newly created environment marker */
struct item *make_env(x)
struct environment *x;
{
 struct item *temp = make_cse_stack(ENVIRO);
 temp->val.epsilon=x;
 return(temp);
}


struct item *copy(x) /* Copies the contents of item x to new stack item */
struct item *x;      /* (used for copying values from environment lookups) */ 
{
 struct item *temp=make_cse_stack(x->type);
 if ( (x->type==LAMBDA)||(x->type==ETA)) {
	temp->val.l=(struct leta *) malloc(sizeof (struct leta));
	if ((temp->val.l)==NULL) oom();
	temp->val.l->k=x->val.l->k;
	temp->val.l->c=x->val.l->c;
	temp->val.l->x=copy(x->val.l->x);
 }

 else temp->val=x->val;
 return(temp);
}


struct item *copy_all(x) /* Copies everything in the list of items in x */
struct item *x;
{
 struct item *temp=copy(x);
 struct item *head=temp;
 while(x->next!=NULL) { 
	temp->next=copy(x->next);
	temp=temp->next;
	x=x->next;
 }
 temp->next=NULL;
 return(head);
}


/* Searches the control structure for the given number control structure */
struct item *d_scan(d,number)
int number;
struct delta *d;
{struct delta *d_temp=d;
 while(d_temp!=NULL) {
	if (d_temp->number==number) return(copy_all(d_temp->structure));
	d_temp=d_temp->next;
 }
 printf("Unable to locate delta #%d!\n",number);
 return(NULL);
}


struct item *create_item(type,v) /* Used by binops and unops */
int type,v;
{
 struct item *temp_s=make_cse_stack(GENERAL);
 struct tree *temp_t=(struct tree *) malloc (sizeof(struct tree));
 if (temp_t==NULL) oom();
 temp_t->type=type;
 temp_t->location=0;
 temp_t->value=v;
 temp_s->val.t=temp_t;
 return(temp_s);
}


struct item *create_boolean(v) /* Used by binops and unops */
int v;
{
 struct item *temp_s=make_cse_stack(GENERAL);
 struct tree *tree=(struct tree *) malloc (sizeof(struct tree));
 if (tree==NULL) oom();

 tree->location=0;
 tree->value=v;
 tree->type = (v ? TRUE : FALSE );
 temp_s->val.t=tree;
 return(temp_s);
}


void new_head(nh,oh) /* Tags 'oh' onto the end of the one-element 'nh' */
 struct item *nh,**oh;
{
 nh->next=*oh;
 (*oh)=nh;
}


int pow(x,y) /* Integer exponentiation routine. . . */
int x,y;
{int t,total=1;
 if (y<0) return(0); /* For integers, y cannot be negative. . .*/
 if (y==0) return(1);
 for(t=0;t<y;t++) total *= x;
 return(total);
}


int boolean(x) /* Returns the defined TRUE or FALSE depending on x */
int x;
{return((x==TRUE)||(x==FALSE));}


int true_type(x) /* Returns the 'type' of an item */
struct item *x;
{
 switch (x->type) {
 	case GENERAL:
	case UNOP: 
	case BINOPA:
	case BINOPL: return(x->val.t->type);
	default: return(x->type);
 }
}


void snip(c) /* Drops the last element of list 'c' */
struct item *c;
{struct item *temp;
 temp=wind2(c);
 if (temp!=NULL) temp->next=NULL;
 else c->type=EMPTY;
}


/* Add_env adds enviroment 'here' to the environment tree after 'ptr' */
void add_env(ptr,here)
struct environment *ptr, *here;
{
 struct next_en *head;
 struct next_en *temp = (struct next_en *) malloc(sizeof(struct next_en));
 if (temp==NULL) oom();
 temp->here=here;
 temp->next=NULL;
 head=ptr->next;
 while(head->next!=NULL) head=head->next; /* Go to the end of the structure */
 head->next=temp;
};


/* ** Used only for debugging ** prints out details on a given item */
void examine_cse(c)
struct item *c;
{
	if (c==NULL) {printf("NULL "); return;}
	switch (c->type) {
	case GENERAL: 
	case BINOPA:
	case BINOPL:
	case AUG:
	case UNOP : name_dump(c->val.t); break;
	case GAMMA: printf("<G> ");break;
	case BETA : printf("<D>%d <D>%d <B> ",c->val.b->T->number,
		c->val.b->F->number); break;
	case LAMBDA : 
			printf("<L,%d,%d",c->val.l->k,
				c->val.l->c==NULL ? -1 : c->val.l->c->number);
	 		if (true_type(c->val.l->x)==1)  
				printf("%s",c->val.l->x->val.t->id_name);
	 		else printf("Comma node "); 
			printf("> ");
			break;
	case TAU : printf("<T>%d ",c->val.tau);break;
	case COMMA: printf("<,> "); break;
	case TUPLE : printf("Tuple:%d ",c->val.tup->number); break;
	case ENVIRO: printf("e%d ",c->val.epsilon->number);
				break;
	case ETA : printf("<E,%d,%d> ",c->val.l->k, c->val.l->c->number); 
			examine_cse(c->val.l->x);break;
	default : printf("error!?\n unexpected type %d",c->type);
	}
}
 

/* Make_env_stack adds an element to the environment tree
   - the environment number is e_num, with e_to substituted for e_from. 
   - 'where' is the parent after which we are to add the new element */
struct environment *make_env_stack(e_num,e_from,e_to,where)
struct environment *where;
struct item *e_from, *e_to;
int e_num;
{
 struct environment *temp = (struct environment *) 
					malloc(sizeof(struct environment));
 struct next_en *temp2 = (struct next_en *) malloc(sizeof(struct next_en));
 if ( (temp==NULL) || (temp2==NULL)) oom();

 if (debug2) {
	printf("Creating environment #%d\n",e_num);
 	printf("Substituting: "); examine_cse(e_to);
	printf("For         : "); examine_cse(e_from);
	(where==NULL)?printf("Under PE\n"):printf("Under %d\n",where->number);
 }

 temp2->next=NULL; /* Set up the 'next' structure */
 temp2->here=NULL;
 temp->next=temp2; /* Added the 'next' structure */
 temp->prev=where; /* This environment's parent is 'where' */
 temp->number=e_num; /* enviroment # */
 temp->from=e_from; /* Sub 'from' for 'to' */
 temp->to=e_to;

 if (where==NULL) return(temp); /* PE case */

 if (where->next->here==NULL) 
	where->next->here=temp;
 else 
	add_env(where,temp); 
 return (temp);
}


/* Dumps the environment list -- Only used for debugging */
void tree_dump(e,n)
struct environment *e;
int n;
{
 struct next_en *te;
 if (e==NULL) return;
 printf("       ");
 dots(n);
 printf("Environment #%d",e->number);
 (e->prev!=NULL) ? printf(" (previous %d)\n",e->prev->number) : printf("\n");
 printf("       ");
 dots(n);
 examine_cse(e->from);
 printf("\n");
 printf("       ");
 dots(n);
 examine_cse(e->to);
 printf("\n");
 te=e->next;
 while (te->here != NULL) {
		tree_dump(te->here,n+1);
		te=te->next;
		if (te==NULL) return;
 }
}


/* ** Used only for debugging ** Dumps the stack pointed to by c */
void dump_cse(c)
struct item *c;
{
 while(c!=NULL) {
	examine_cse(c);
	c=c->next;
 }
}


/* ** Used only for debugging ** Dumps the control structure list (deltas) */
void dump_delta(d)
struct delta *d;
{
 while (d!=NULL) {
	printf("Delta #%d -----------------\n",d->number);
	dump_cse(d->structure);
	d=d->next;
  }
}
