/* File eval.c */
/*   Thu Apr 20 23:45:56 EDT 1995

   These programs, together with eval.h and functions.c, handle the evaluation
   of the CSE machine.

   Make_stack_part recursively makes a list of control structures (delta
   stacks), which then put into the CSE machine in exactly the way described
   in Dr. Bermudez's notes. 'cse_engine' simply runs through the control
   stack and calls the appropriate CSE rules.

   Note: The lines which are commented 'Change Me' are the lines which I had
   to alter to match the weaker error reporting.
*/

#include "eval.h"
#include "functions.c" /* Internal functions (Print, Istuple, etc) */

/* Make_stack_part returns a stackable 'item' from an AST -- This program 
   recursively creates the control structure (delta stacks) */
struct item *make_stack_part(st,i,d)
struct delta *d;
struct tree *st;
int *i;
{
 struct item *i_temp;
 struct item *m;
 int counter=0;

 if (st==NULL) return(NULL);

 m=make_cse_stack(GENERAL);
 switch (st->type) {
	case ID: 
	case INTEGER:
	case STRING:
	case TRUE:
	case FALSE: 
	case XNIL:
	case YSTAR:
	case DUMMY : { /* These are all 'general' items -- rule 1 type things */
		m->val.t = st;
		m->next=NULL;
		return(m);
	}
	case LAMBDA : { /* Lambda function */ 
		(*i)++; /* Update counter */ 
		m->val.l = make_lambda(*i,st->left); /* Note: l->c undefined */
		m->type=LAMBDA;
		d=wind_d(d); /* Go to the end of the current delta */
		d->next=make_delta(); 
		d=d->next;
		d->number=*i; /* Tag this on the end */
		d->structure=make_stack_part(st->right,i,d);
		return(m);
	}
	case NEG:
	case NOT : {/* Uniary operators */
		m->val.t = st;
		m->type=UNOP;
		m->next=make_stack_part(st->left,i,d);
		return(m);
	}
	case OR:
	case AND: {/* Logical binops */
		m->val.t = st;
		m->type=BINOPL;
		m->next=make_stack_part(st->left,i,d);
		i_temp=m;
		m=wind(m);
		m->next=make_stack_part(st->right,i,d);
		return(i_temp);
	}
	case GR:
	case GE:
	case LS:
	case LE:
	case EQ:
	case NE: 
	case PLUS:
	case MINUS:
	case TIMES:
	case DIVIDE:
	case EXPO : {/* Arithmatic binops */
		m->val.t = st;
		m->type=BINOPA;
		m->next=make_stack_part(st->left,i,d);
		i_temp=m;
		m=wind(m);
		m->next=make_stack_part(st->right,i,d);
		return(i_temp);
	}
	case AUG : { /* The aug function, which is really a binop. . .*/
		m->val.t = st;
		m->type=AUG;
		m->next=make_stack_part(st->left,i,d);
		i_temp=m;
		m=wind(m);
		m->next=make_stack_part(st->right,i,d);
		return(i_temp);
	}
	case CONDITIONAL : { /* The conditional */
		m->type = BETA;
		m->val.b=make_beta(); /* The beta will contain the deltas */
		i_temp=m;
		(*i)++; /* Next . . . */
		d=wind_d(d);
		d->next=make_delta();
		d=d->next;
		d->number=*i;
		d->structure=make_stack_part(st->left->extra,i,d);
		m->val.b->T = d; /* True result delta */
		(*i)++; /* Next . . .*/
		d=wind_d(d);
		d->next=make_delta();
		d=d->next;
		d->number=*i;
		d->structure=make_stack_part(st->left->left->extra,i,d);
		m->val.b->F = d; /* False result delta */
		m->next=make_stack_part(st->extra,i,d);
		return(i_temp);
	}
	case GAMMA : { /* The multifunctional gamma */
		m->val.t = st;
		m->type=GAMMA;
		m->next=make_stack_part(st->left,i,d);
		i_temp=m;
		m=wind(m);
		m->next=make_stack_part(st->right,i,d);
		return(i_temp);
	}
	case TAU :  { /* Create a tau item */
		m->type=TAU;
		i_temp=m;
		while (st!=PNULL) {
			counter++; /* Counts the number of items in the tau */
			m->next=make_stack_part(st->extra,i,d);
			m=wind(m);
			st=st->left;
		}
		i_temp->val.tau=counter;
		return(i_temp);
	}
	default: printf("Don't know what do with %s\n",names[st->type]);
		 exit(-150);
	}
 return(NULL); /* This will never be reached */
}


/* Finds the appropriate values to substitute from the environment stack */
void lookup(c,s)
struct item **c,*s;
{
 struct environment *e_temp=find_env(s);
 while (e_temp!=NULL) {
	if (e_temp->from==NULL) break; /* This is the PE */
	
	if (debug2) printf("%s==%s? (Enviro %d)\n",
		e_temp->from->val.t->id_name, 
		(*c)->val.t->id_name,e_temp->number);

	if (strcmp(e_temp->from->val.t->id_name,(*c)->val.t->id_name)==0) {
		if (debug2) {
			printf("Found match in environ #%d = ",e_temp->number);
			Print_piece(e_temp->to); printf("\n");
		}
		*c=copy(e_temp->to);
		return; /* Matched! */
	}
 e_temp=e_temp->prev; /* Traverse the environment tree upward */
 }

 if (funct_check(*c)!=UNKNOWN) return; /* This is an intrinsic function */

 printf("Undeclared Identifier <%s> in line %d\n",(*c)->val.t->id_name,
 (*c)->val.t->location); 
 exit(50);
}
 

void rule_1(c,s) /* Simple stacking */
struct item *c, **s;
{
 struct item *temp_c=wind(c); /* get the item from the control */
 if (debug2) {
	printf("Rule 1 - stacking ");
	name_dump(temp_c->val.t);
 }
 snip(c);  /* Remove control control referecne */
 if (true_type(temp_c)==ID) 
	lookup(&temp_c,*s);  /* Look it up */
 temp_c->next=*s; /* Put it on the top of the stack */
 *s=temp_c;
}


void rule_2(c,s) /* Lambda stacking */
struct item *c, **s;
{
 struct item *temp_c=wind(c);
 struct item *x=make_cse_stack(LAMBDA);
 x->val.l=make_lambda(temp_c->val.l->k,temp_c->val.l->x->val.t);
 x->val.l->c=find_env(*s); /* Add the enviroment value and location */
 if (debug2) 
	printf("Rule 2 - stacking lambda in enviroment %d\n",
		x->val.l->c->number);
 snip(c); /* Drop the ol' lambda */
 new_head(x,s);
}


void rule_3_etc(c,s,d,env) /* All the gamma possibilities */
struct item *c,**s;
struct delta *d;
int *env;
{
 int funct_type=funct_check(*s); /* Checks all valid gamma applications */
 snip(c); /* Drop gamma from control */
 switch (funct_type) { 
	case LAMBDA: rule_3(c,s,d,env); break;
	case PRINT: Print(s); break;
	case XNULL : 
	case ISINTEGER :
	case ISTUPLE : 
	case ISDUMMY :
	case ISTRUTHVALUE:
	case ISFUNCTION:
	case ISSTRING: IsSomething(funct_type,s); break;
	case CONC: conc(c,s); break;
	case ORDER: order(s); break;
	case TUPLE: tuple_select(s); break;
	case YSTAR: rule_12(s);break;
	case STEM: Stem(s); break;
	case ITOS: ItoS(s); break;
	case STERN: Stern(s); break;
	case ETA: rule_13(c,s); break;
	default : 
		if ((*s)->type==GENERAL) 
/*			printf("Error in line %d: ",(*s)->val.t->location); */
		printf("Error: Illegal Function Application.\n"); /*Change me*/
/* 		examine_cse(*s);*/
		exit(50);
 }
}


void rule_5(c,s) /* Exit environment */
struct item *c,**s;
{
 struct item *temp_s=*s;
 struct item *temp2_s=*s;
 struct item *temp_c=wind(c);
 if (debug2) printf("Rule 5 - closing environment #%d\n",
				temp_c->val.epsilon->number);
 snip(c); /* Drop the environment variable from the control stack */

 if (temp_s->type==ENVIRO) *s=(*s)->next; 
 else {
 	while (temp_s->type!=ENVIRO) temp_s=temp_s->next;
	while (temp2_s->next!=temp_s) temp2_s=temp2_s->next;
	temp2_s->next=temp_s->next; /*Drops the environment out of the middle*/
 }
}


void rule_6_5(c,s) /* Aug */
struct item *c,**s;
{
 struct tuple *tup;
 struct item *temp,*temp2;
 int type=true_type(*s);
 
 if ( (type!=TUPLE) && (type!=XNIL)) { 
	printf("Cannot augment a non tuple (%d).\n",type); 
	exit(33);
 } 

 snip(c); /* Drop the aug */ 

 if (type==TUPLE) {
	tup=(*s)->val.tup;	/* Snag the tuple information */
	tup->number++; 		/* Increament the tuple counter */
	temp=tup->storage; 
	while(temp->next!=NULL) temp=temp->next; /* Go to end of the list */
	temp->next=((*s)->next); /* Add the item */
	temp2=temp;
	temp=copy(*s);
	temp->val.tup=tup;
 	(*s)=(*s)->next->next;
	temp2->next->next=NULL; /* Terminate the list */
	}
 else { 
	tup=(struct tuple *) malloc(sizeof(struct tuple)); /* Make a tuple */
	if (tup==NULL) oom();
	tup->storage=((*s)->next); /* Add the one-tuple */
	tup->number=1;
	temp=make_cse_stack(TUPLE);
	temp->val.tup=tup;
 	(*s)=(*s)->next->next;
	tup->storage->next=NULL; /* Terminate the list */
	}
 new_head(temp,s);
}


void rule_6(c,s) /* Binops -- Mostly error checking code! */
struct item *c,**s;
{
 struct item *temp_s=*s;
 struct item *temp_c=wind(c);
 int i1,i2;
 if (debug2) printf("Rule 6 - Binop (%d)\n",temp_c->type);
 if (temp_c->type==BINOPA) {
	i1=temp_s->val.t->type;
	i2=temp_s->next->val.t->type;
	if (i1!=i2) {
/*	    printf("Incompatible operands for '%s'.\n",
		names[temp_c->val.t->type]);*/
	    printf("Illegal Operands for '%s'\n",  /* Change me (add .,too) */
		names[temp_c->val.t->type]);
	    exit(10);
	} 
	if ((i1!=STRING)&&(i1!=INTEGER)) {
/*	   printf ("Invalid value used in expression '%s'\n",
		 names[temp_c->val.t->type]);
	   printf("Offending value is "); 
	   examine_cse(temp_s); */
	    printf("Illegal Operands for '%s'\n",names[temp_c->val.t->type]);
	   exit(10);	/* Change me */
	}
	if ((i2!=STRING)&&(i2!=INTEGER)) {
	   printf ("Invalid value used in expression '%s'.",
		names[temp_c->val.t->type]); 
	   printf("\n. Offending value is ");
	   examine_cse(temp_s->next); 
	   exit(10);
	} 
	if (i1==STRING) {
		switch (temp_c->val.t->type) {
		case PLUS:
		case MINUS:
		case TIMES:
		case DIVIDE: printf("Incompatible operands for '%s'.",
		 names[temp_c->val.t->type]);
	   exit(10);
	   	}	
	}

	if (i1==INTEGER) { /* Get the actual values */
		i1=(*s)->val.t->value;
		i2=(*s)->next->val.t->value;
	} /* Do only one string comparison for everything !*/
	else i1=strcmp((*s)->val.t->pname,(*s)->next->val.t->pname);
	*s=(*s)->next->next; /* Drop the two values */
	snip(c); /* Drop the binop from the control */
	if (temp_s->val.t->type==INTEGER) {

	switch (temp_c->val.t->type) { /* Arithmatic integer calculations */
		case PLUS  :new_head(create_item(INTEGER,i1+i2),s); break;
		case MINUS :new_head(create_item(INTEGER,i1-i2),s); break;
		case TIMES :new_head(create_item(INTEGER,i1*i2),s); break;
		case DIVIDE:new_head(create_item(INTEGER,i1/i2),s); break;
		case EXPO  :new_head(create_item(INTEGER,pow(i1,i2)),s); break;
		case GR :	new_head(create_boolean(i1>i2),s);  break;
		case GE : 	new_head(create_boolean(i1>=i2),s); break;
		case LS : 	new_head(create_boolean(i1<i2),s);  break;
		case LE : 	new_head(create_boolean(i1<=i2),s); break;
		case EQ : 	new_head(create_boolean(i1==i2),s); break;
		case NE : 	new_head(create_boolean(i1!=i2),s); break;
		default : printf("What the hell is %d!?\n",temp_c->val.t->type);
			exit(10);
		}
	} /* Integer section */
	else {

	switch (temp_c->val.t->type) { /* String comarisons */ 
		case GR : new_head(create_boolean(i1>0),s);  break;
		case GE : new_head(create_boolean(i1>=0),s); break;
		case LS : new_head(create_boolean(i1<0),s);  break;
		case LE : new_head(create_boolean(i1<=0),s); break;
		case EQ : new_head(create_boolean(i1==0),s); break;
		case NE : new_head(create_boolean(i1!=0),s); break;
		default : printf("What is %d!?\n",temp_c->val.t->type);
			exit(10);
		}
	} /* String section */
 } 
 else {/* Logical operations */ 

	i1=temp_s->val.t->type; 
	i2=temp_s->next->val.t->type; 
	if ((!boolean(temp_s->val.t->type))||(!boolean(temp_s->val.t->type))){
	   	printf ("Invalid value used in logical expression '%s'\n",
	   	names[temp_c->val.t->type]);
	   	exit(10);
	}
 
	i1=(*s)->val.t->value;
	i2=(*s)->next->val.t->value;

	*s=(*s)->next->next; /* Drop the two values */
	snip(c); /* Drop the binop from the control */

	switch (temp_c->val.t->type) {
		case OR : new_head(create_boolean(i1 || i2),s); break;
		case AND: new_head(create_boolean(i1 && i2),s); break;
		default : printf("What the hell is %d!?\n",temp_c->val.t->type);
			  exit(10);
	}
 }
}


void rule_7(c,s) /* Unops. . . */
struct item *c,**s;
{
 struct item *temp_s=*s;
 struct item *temp_c=wind(c);
 if (debug2) {
	printf("Rule 7 - Unop - "); 
	name_dump(temp_c->val.t);
 }

 if (temp_c->val.t->type==NOT) {
	if (!boolean(true_type(temp_s))) {
		printf(
		"Non-boolean for 'not' application in line %d\n",
		temp_c->val.t->location);
		exit(10);
	}
	(*s)=(*s)->next; /* Skip first stack item */
	new_head(create_boolean(! temp_s->val.t->value),s);
 }
 else {
	if (true_type(temp_s)!=INTEGER) {
		printf(
		"Cannot negate non-integer value in line %d\n",
		temp_c->val.t->location);
		exit(10);
	}
	(*s)=(*s)->next; /* Skip first stack item */
	new_head(create_item(INTEGER,- temp_s->val.t->value),s);
 }
 snip(c);  /* Remove Neg or Not from control */
}


void rule_8(c,s) /* Conditionals */
struct item *c,**s;
{
 struct item *temp_s=*s;
 struct item *temp_c=wind(c);
 struct beta *temp_b;
 if (debug2) printf("Rule 8 - Conditional \n");

 if (!(boolean(temp_s->val.t->type))) {
	printf("Non-boolean result used in conditional\n");
	exit(20);
 }
 temp_b=temp_c->val.b;
 snip(c); /* Drop the beta */
 temp_c=wind(c);
 temp_c->next = copy_all ((temp_s->val.t->value) ? temp_b->T->structure : 
					 temp_b->F->structure);
 (*s)=(*s)->next; /* Drop the boolean value */
}


void rule_9(c,s) /* Tuple formation */
struct item *c,**s;
{
 struct item *temp_c=wind(c);
 struct tuple *tup = (struct tuple *) malloc(sizeof(struct tuple)); 
 struct item *temp_s=(*s);
 int number=temp_c->val.tau;
 if (tup==NULL) oom();

 if (debug2) printf("Rule 9 - Tuple formation\n");
 while(temp_c->val.tau > 1) { 
	if (debug2) printf("Getting element #%d\n",temp_c->val.tau);
	if (temp_s==NULL) {
		printf("Tuple formation attempted on empty stack\n");
		exit(33); /* Impossible ! */
	}
	temp_s=temp_s->next;
	temp_c->val.tau--;
 }
 snip(c); /* Drop the tau */
 tup->storage=(*s);  /* Now we have a nice list */
 (*s)=temp_s->next; /* Stack pointer now beyond tupalized values */
 temp_s->next=NULL;  /* The tupled stuff is now tagged with a NULL */
 temp_c=make_cse_stack(TUPLE); /* Make a new item */
 temp_c->val.tup=tup; /* Here's the tuple! */
 temp_c->val.tup->number=number;
 new_head(temp_c,s);
}


/* The cse_engine simply looks on the control stack and calls the appropriate
   rules, all of which are above except for functions involving gammas, which
   are in function.c */
struct environment * cse_engine(d_head)
struct delta *d_head;
{
 struct item *control, *c_head, *s_head; 
 int enviro=0; /* PE */ 
 struct environment *e_head=make_env_stack(enviro,NULL,NULL,NULL); 
 c_head=control=make_env(e_head); /* Add environment markers to stacks */
 s_head=make_env(e_head); 
 control->next=d_head->structure; /* Start with d0 */

 while (c_head->type!=EMPTY) {
	if (enviro>10000) {
		printf("Error: Unable to Allocate More Memory\n");
		exit(10);
	/* The above is a kludge -- it would take hours for efficient linked
	   lists to run out of memory! */
	}
	control=wind(c_head);
  	if (debug3) { 
		printf("| "); dump_cse(c_head);
		printf("---- "); dump_cse(s_head); 
		printf("|\n");
/*		printf("Environ------------\n"); tree_dump(e_head,1); */
	}
	switch(control->type) { /* Rules */
		case TUPLE:
		case GENERAL: rule_1(c_head,&s_head); break;
		case LAMBDA: rule_2(c_head,&s_head); break;
		case GAMMA: rule_3_etc(c_head,&s_head,d_head,&enviro); 
			break;
		case ENVIRO: rule_5(c_head,&s_head);break;
		case BINOPA: 
		case BINOPL: rule_6(c_head,&s_head); break;
		case UNOP: rule_7(c_head,&s_head); break;
		case BETA: rule_8(c_head,&s_head); break;
		case TAU: rule_9(c_head,&s_head); break;
		case AUG: rule_6_5(c_head,&s_head); break; 
		default: printf("Unknown type %d\n",control->type); exit(99);
	}
 }
 if (debug2) {
	printf("\nDone - Items remaining on stack:\n");
 	dump_cse(s_head);
	printf("\nThe stack is as follows:");
	tree_dump(e_head,1);
 }
 printf("\n");
 return e_head;
}

/* 'Eval' creates the struct list and runs it through the cse_engine */
void eval(st) 
struct tree *st;
{
 struct delta *d_temp=make_delta();
 struct delta *d_head=d_temp;
 struct environment *e_head;
 int counter=0;
 d_head->number=counter;
 d_head->structure=make_stack_part(st,&counter,d_temp);
 if (debug2) dump_delta(d_head);
 e_head = cse_engine(d_head);
 if (debug3) {
      printf("Environment tree:\n");
      tree_dump(e_head,1); 
 }
}

