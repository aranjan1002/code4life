/* File functions.c */
/*   Fri Apr 21 00:05:57 EDT 1995

   These are additional supplements for the CSE RPAL evaluation -- Mostly
   gamma-related functions.  */

void Print_t(); /* Forward declaration. . .*/

/* Funct_check is a list of all the functions a gamma can 'activate', and
   returns the defined name of what should be done (defined in eval.h). It is
   also used by lookup to screen values that do not need to be looked up in
   the environment */
int funct_check(it)
struct item *it;
{
 char *id_name;
 switch (it->type) {
 	case LAMBDA:	return(LAMBDA); /* Lambda closure candidate */
	case ETA   : 	return(ETA);    /* Recursion candidate */
	case TUPLE : 	return(TUPLE);  /* Tuple selection candidate */
 }
 if (it->type==GENERAL) {
	if (it->val.t->type==ID) {
		id_name=it->val.t->id_name;
		if (!strcmp("Print",id_name)) return(PRINT);
		if (!strcmp("print",id_name)) return(PRINT); /* For typos */
		if (!strcmp("Istuple",id_name)) return(ISTUPLE);
		if (!strcmp("Isfunction",id_name)) return(ISFUNCTION);
		if (!strcmp("Isdummy",id_name)) return(ISDUMMY);
		if (!strcmp("Isstring",id_name)) return(ISSTRING);
		if (!strcmp("Isinteger",id_name)) return(ISINTEGER);
		if (!strcmp("Istruthvalue",id_name)) return(ISTRUTHVALUE);
		if (!strcmp("Conc",id_name)) return(CONC);
		if (!strcmp("conc",id_name)) return(CONC); /* For typos */
		if (!strcmp("ItoS",id_name)) return(ITOS);
		if (!strcmp("Order",id_name)) return(ORDER);
		if (!strcmp("Stern",id_name)) return(STERN);
		if (!strcmp("Stem",id_name)) return(STEM);
		if (!strcmp("Null",id_name)) return(XNULL);
	}
	if (it->val.t->type==YSTAR) return(YSTAR); 
 }
 return(UNKNOWN);
}


/* Rule_3 deals with lambda closure, which is pretty much the most important
   thing to do in the CSE machine. */
void rule_3(c,s,d,env)
struct item *c,**s;
struct delta *d;
int *env;
{
 struct item *temp_c;
 struct item *s_top=*s;
 struct item *s_temp;
 struct tree *temp_t; /* For decomposing tuple lists */
 struct item *temp_x;
 int close_hold=s_top->val.l->k;
 struct environment *close_env=s_top->val.l->c;
 if (debug2) printf("Rule 3 - Lambda closure\n");

 /* It's easier to just break up commas than deal with them during look-up! */
 if (true_type(s_top->val.l->x)==COMMA) {
	if (true_type(s_top->next)!=TUPLE) { 
		printf("Variable mismatch - trying to bind non-tuple to");
		printf(" comma node in line %d\n",
					s_top->val.l->x->val.t->location);
		exit(33);
	}

	temp_t=s_top->val.l->x->val.t; 
	s_top=s_top->next->val.tup->storage; /* Tuple list */
	*s=(*s)->next->next; /* Drop the two pieces from the stack */ 

	while(temp_t!=NULL) {
		if (s_top==NULL) {
		   printf("Variable mismatch - Tuple smaller than comma\n");
		   exit(33);
		}
		(*env)++; /* New evironment*/
		temp_x=create_item(ID,0);
		temp_x->val.t->id_name=temp_t->extra->id_name; /* Fake ID :) */
 		close_env=make_env_stack(*env,temp_x,s_top,close_env);
		s_temp=make_env(close_env); /* Create new enviro marker */
 		new_head(s_temp,s);
 		temp_c=wind(c);
 		temp_c->next=make_env(close_env);
		temp_t=temp_t->left;
		s_top=s_top->next;
	}
 }
 else { /* Normal one-variable binding */
  	if (s_top->next!=NULL) {
		*s=(*s)->next->next; /* Drop the two pieces from the stack */ 
	  	(*env)++;
 	  	close_env=make_env_stack(*env,
			copy(s_top->val.l->x),copy(s_top->next),close_env);
		s_temp=make_env(close_env); /* Create new enviro marker */
 		new_head(s_temp,s);
 		temp_c=wind(c);
 		temp_c->next=make_env(close_env);
	}
 	else
	  	printf("Stack error \n"); /* Impossible */
	}
 temp_c=wind(c);
 temp_c->next=d_scan(d,close_hold); /* Add delta to the control */
}


/* This is the print workhorse, which knows how to deal with all the printable
   things the 'Print' function can handle. It is called by Print and Print_t */
void Print_piece(piece)
struct item *piece;
{
 char *temp,*temp2;
 switch (true_type(piece)) {
	case INTEGER :	printf("%d",piece->val.t->value);break;
	case STRING :	temp=piece->val.t->pname;
			temp2=temp;
			while((temp2=strchr(temp,13))!=NULL) {
				*temp2=NULL;
				printf("%s\n",temp); /* ASCII 13 is CR */
				*temp2=13;
				temp=temp2+1;
			}
			printf("%s",temp);
			break;
	case DUMMY :	printf("dummy");break;
	case TRUE :	printf("true");break;
	case FALSE : 	printf("false");break;
	case LAMBDA: 	printf("[lambda closure: %s: %d]",
				piece->val.l->x->val.t->id_name,
				piece->val.l->k); break;
	case ETA:	printf("[eta closure: %s: %d]", /*Probably impossible*/
			piece->val.l->x->val.t->id_name,
			piece->val.l->k); break; 
	case XNIL : 	printf("nil"); break;
	case TUPLE:	Print_t(piece);break;
	default : printf("I don't know what to do with %d",true_type(piece));
		name_dump(piece->val.t);
 }
}


/* Print_t handles tuples -- it calls Print_piece for the elements, and 
   Print_piece can call call Print_t when it encounters nested tuples */
void Print_t(s) 
struct item *s;
{ struct item *temp;
 printf("(");
 temp=s->val.tup->storage;
 while(temp!=NULL) {
	Print_piece(temp);
	temp=temp->next;
	if (temp!=NULL) printf(", ");
 }
 printf(")");
}


void Print(s) /* General print routine */
struct item **s;
{ 
 *s=(*s)->next;
 if ((*s)->type!=TUPLE) Print_piece(*s);
 else Print_t(*s);
 (*s)=(*s)->next; /* Skip printed item */
 new_head(create_item(DUMMY,0),s); /* Leave a dummy behind */
}


void IsSomething(funct,s) /* Handles all the Is* functions, plus Null */
int funct;
struct item **s;
{
 int type;
 *s=(*s)->next; /* Skip the 'IsSomething' */
 type = true_type(*s);
 *s=(*s)->next; /* Skip whatever it was */
 switch (funct) {
	case ISINTEGER : new_head(create_boolean(type==INTEGER),s); break;
	case ISTUPLE : new_head(create_boolean(type==TUPLE||type==XNIL),s);
							 break;
	case ISSTRING : new_head(create_boolean(type==STRING),s); break;
	case ISDUMMY : new_head(create_boolean(type==DUMMY),s); break;
	case ISTRUTHVALUE : new_head(create_boolean(boolean(type)),s); break;
	case ISFUNCTION : new_head(create_boolean(type==LAMBDA||type==ETA),s);
									break;
	case XNULL : new_head(create_boolean(type==XNIL),s); break;
	default : printf("This point should never be reached (IsSomething)\n");
 }
}


void conc(c,s) /* Handles the Conc call */
struct item **s,*c;
{
 struct item *x=create_item(STRING,0);
 char *s1,*s2;
 snip(c); /* Get rid of the extra gamma. . .*/
 *s=(*s)->next; /* Skip the "conc" */
 if ( (true_type(*s)!=STRING) || (true_type((*s)->next)!=STRING)) {
	printf("Non-strings used in conc call\n");
	free(x);
	exit(33);
 }
 s1=(*s)->val.t->pname;
 s2=(*s)->next->val.t->pname;

 x->val.t->pname=create_string(s1,strlen(s1)+strlen(s2));
 x->val.t->pname=strcat(x->val.t->pname,s2);
 (*s)=(*s)->next->next; /* Skip the two orignal strings */
 new_head(x,s);
}


void order(s) /* Handles the Order call */
struct item **s;
{
 struct item *x;
 int i;
 i=true_type((*s)->next);
 if (! ( (i==TUPLE) ||  (i==XNIL) ) ) {
	printf("Attempt to find the order of a non-tuple in line %d\n",
	(*s)->val.t->location);
	exit(33);
 }
 *s=(*s)->next; /* Skip the 'Order' */
 i = ( (i==XNIL) ? 0 : (*s)->val.tup->number);
 x=create_item(INTEGER,i);
 *s=(*s)->next; /* Skip the tuple */
 new_head(x,s); /* Put the answer on the stack */
}


void tuple_select(s) /* Handles tuple selection */
struct item **s;
{
 struct item *x;
 int val;
 if (true_type((*s)->next)!=INTEGER) {
	printf("Invalid tuple selection (non-integer)\n");
	exit(33);
 }
 val=(*s)->next->val.t->value;
 if ( (val <= 0) || (val > (*s)->val.tup->number) ) {
	printf("The tuple selection value %d out of range\n",val);
	exit(33);
 }

 x=(*s)->val.tup->storage;
 while(val-->1) x=x->next;
 x=copy(x);
 (*s)=(*s)->next->next; /* Skip over tuple and integer */
 new_head(x,s);
}


void rule_13(c,s) /* The dreaded recursion is very easy! */
struct item *c,**s;
{
 struct leta *l=(struct leta *) malloc (sizeof(struct leta));
 struct item *gamma_1=make_cse_stack(GAMMA);
 struct item *gamma_2=make_cse_stack(GAMMA);
 struct item *lambda_1=make_cse_stack(LAMBDA); 
 if (l==NULL) oom();
 if (debug2) printf("Rule 13: Gamma acting on eta\n");
 c=wind(c);
 c->next=gamma_1;
 gamma_1->next=gamma_2; /* Add the two gammas to the control */
 lambda_1->val.l=l;
 l->k=(*s)->val.l->k;
 l->c=(*s)->val.l->c;
 l->x=copy((*s)->val.l->x);
 new_head(lambda_1,s); /* Put the eta on the stack */
}


void rule_12(s)  /* Creates the eta node from Y* */
struct item **s;
{
 struct leta *e=(struct leta *) malloc (sizeof(struct leta));
 struct item *x=make_cse_stack(ETA);
 if (e==NULL) oom();
 if (debug2) printf("Rule 12: Gamma acting on Y*\n");
 *s=(*s)->next; /* Skip the ystar */

 if (true_type(*s)!=LAMBDA) {
	printf("Invalid attempt to apply Y* to non-lambda closure (%d)",
		true_type(*s));
	exit(33);
 }
 
 x->val.l=e;
 e->k=(*s)->val.l->k; /* Index */
 e->c=(*s)->val.l->c; /* Environment */
 e->x=copy((*s)->val.l->x); /* Variable */
 *s=(*s)->next; /* Skip over the lambda */
 new_head(x,s); /* Put the eta on the stack */
}


void Stem(s) /* Handles the Stem call */
struct item **s;
{
 struct item *temp;
 char *t;

 if (true_type((*s)->next)!=STRING) {
	printf("Invalid Stem use in line %d - Argument is not a string\n",
		(*s)->val.t->location);
	exit(33);
 }

 temp=create_item(STRING,0);
 t=(char *) malloc( (sizeof(char)*2) );
 t[0]=(*s)->next->val.t->pname[0];
 t[1]='\0';
 temp->val.t->pname=t;
 (*s)=(*s)->next->next;
 new_head(temp,s);
} 


void Stern(s)  /* Handles the Stern call */
struct item **s;
{
 struct item *temp;
 char *t,*x;
 int len;
 if (true_type((*s)->next)!=STRING) {
	printf("Invalid Stem use in line %d - Argument is not a string\n",
		(*s)->val.t->location);
	exit(33);
 }
 x=(*s)->next->val.t->pname;
 if (debug2) printf("in Stern %s\n",x);

 temp=create_item(STRING,0);
 len=strlen(x);

 t=(char *) malloc( (sizeof(char) * len) );
 strcpy(t,x+1);
 temp->val.t->pname=t;
 (*s)=(*s)->next->next;
 new_head(temp,s);
}

 
void ItoS(s)  /* Handles the very annoying Integer to String call */
struct item **s;
{
 struct item *temp;
 int value,negative,rem,digit=20;  /* Maximum number of digits */
 char *t=(char *) malloc(digit);
 if (true_type((*s)->next)!=INTEGER) {
	printf("Invalid ItoS use in line %d - Argument is not an Integer\n",
		(*s)->val.t->location);
	exit(33);
 }
 value=(*s)->next->val.t->value;
 temp=create_item(STRING,0);
 t[--digit]=NULL;
 t[--digit]='0';
 if (value!=0) {
 	negative=(value<0);
 	value=abs(value);
 	while(value>0) {
		rem=value % 10;
		t[digit--]='0'+rem;
		value=(value-rem)/10;
 	}
 	if (negative) t[digit--]='-';
 	t+=(digit+1);
 }
 else 
	t+=digit;

 (*s)=(*s)->next->next;
 temp->val.t->pname=t;
 new_head(temp,s);
}
