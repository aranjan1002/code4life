/* Last modified (commented) Mon Mar 13 22:18:03 EST 1995 */

/* Token.c is a collection of routines used in tokenizing the input */

void fill(t,here,test,type) /* Fills up a token element with data */
struct token *t;
char *here, *test;
int type;
{
 t->n_length=here - test + 1;
 t->name=(type==STRING) ? create_string(test+1,t->n_length-2) : /* Remove ''''*/
			  create_string(test,t->n_length);
 t->type=type;
 t->location=line_num;
 t->next=TNULL;
}

void fill_sugar(t,name,type) /* Same as fill, but allows 'name change'*/
struct token *t;
char *name;
int type;
{
 t->n_length=2;
 t->name=create_string(name,2);
 t->type=type;
 t->location=line_num;
 t->next=TNULL;
}

int isoperator(test) /* Returns non-zero when pointer points to an operator */
char *test;
{static char *operators="\"+-*<>&.@/:=~|$!#%^_[]{}`?";
 return(strchr(operators,*test)!=NULL);
}

int ispunc(test) /* Returns non-zero when point points to a 'punctuation' */
char *test; 
{static char *punct="();,";
 return(strchr(punct,*test)!=NULL);
}

int makespace(test,t) /* Returns 1 if able to make a space token */
char *test;
struct token *t;
{
 char *here=test;
 if (isspace(*here)) {
	while(isspace(*++here));
	here--;
	if (*here==10) --here; /*If last character is EOL, then just leave it*/
 	fill(t,here,test,DELETE);
	return(1);
 }
 return(0);
}

void reserved_symbols(t) /* List of symbols known by RPAL - used for defining */
struct token *t;
{
 if (t->n_length==1) {
	switch (*t->name) {
		case '.' : t->type=PERIOD; break;
		case '&' : t->type=AND; break;
		case '*' : t->type=TIMES; break;
		case '+' : t->type=PLUS; break;
		case '-' : t->type=MINUS; break;
		case '/' : t->type=DIVIDE; break;
		case '@' : t->type=ATX; break;
		case '|' : t->type=BAR; break;
		case '=' : t->type=EQUALS; break;
	}
 }
 else if (t->n_length==2) {
	if (strncmp("->",t->name,2)==0) t->type=CONDITIONAL;
	else
	if (strncmp("**",t->name,2)==0) t->type=EXPO;
	else
	if (strncmp(">=",t->name,2)==0) { /* Desugar */
		t->type=GE;
		t->name=create_string("ge",2);
	}
	else
	if (strncmp("<=",t->name,2)==0) { /* Unsweeten */
		t->type=LE;
		t->name=create_string("le",2);
	}
  }
}

void reserved_words(t) /* List of words used by RPAL */
struct token *t;
{
 if (t->n_length==2) {
	if (strncmp("in",t->name,2)==0) t->type=IN;
	else
	if (strncmp("gr",t->name,2)==0) t->type=GR;
	else
	if (strncmp("ge",t->name,2)==0) t->type=GE;
	else
	if (strncmp("ls",t->name,2)==0) t->type=LS;
	else
	if (strncmp("le",t->name,2)==0) t->type=LE;
	else
	if (strncmp("eq",t->name,2)==0) t->type=EQ;
	else
	if (strncmp("ne",t->name,2)==0) t->type=NE;
	else
	if (strncmp("or",t->name,2)==0) t->type=OR;
	else
	if (strncmp("fn",t->name,2)==0) t->type=FN;
 }
 else 
 if (t->n_length==3) {
	if (strncmp("let",t->name,3)==0) t->type=LET;
	else
	if (strncmp("not",t->name,3)==0) t->type=NOT;
	else
	if (strncmp("neg",t->name,3)==0) t->type=NEG;
	else
	if (strncmp("nil",t->name,3)==0) t->type=XNIL;
	else
	if (strncmp("and",t->name,3)==0) t->type=AND2;
	else
	if (strncmp("rec",t->name,3)==0) t->type=REC;
	else
	if (strncmp("aug",t->name,3)==0) t->type=AUG;

 }
 else 
 if ((t->n_length==4) && (strncmp("true",t->name,4)==0)) t->type=TRUE;
 else
 if (t->n_length==5) {
	if (strncmp("where",t->name,5)==0) t->type=WHERE;
	else
	if (strncmp("false",t->name,5)==0) t->type=FALSE;
	else
	if (strncmp("dummy",t->name,5)==0) t->type=DUMMY;
 }
 if ((t->n_length==6) && (strncmp("within",t->name,6)==0)) t->type=WITHIN;
}


int makeidentifier(test,t) /* Returns 1 if able to create an identifier */
char *test;
struct token *t;
{
 static char *under="_";
 char *here=test;
 if (isalpha(*here++)) {
	 while(isalnum(*here) || (strchr(under,*here)!=NULL)) here++;
	 here--;
	 fill(t,here,test,ID);
	 reserved_words(t);
	 return(1);
 }
 return(0);
}

int makecomment(test,t) /* Returns 1 if able to make a comment */
char *test;
struct token *t;
{
 char *here=test;
 if ( (*here=='/') && (here[1]=='/') ) {
	here+=(strlen(here)-2); /* Leave the EOL character on the line */
	fill(t,here,test,DELETE);
	return(1);
 }
 return(0);
}


int makeinteger(test,t) /* Returns 1 if able to make an integer token */
char *test;
struct token *t;
{
 char *here=test;
 if (isdigit(*here++)) {
	 while(isdigit(*here)) here++; 	/* Run through the digits */
	 here--;			/* Went one too far */
	 fill(t,here,test,INTEGER);
	 return(1);
 }
 return(0);
}

int makedelimiter(test,t) /* Returns 1 if able to make a delimiter ();, */
char *test;
struct token *t;
{
 char *here=test;
 if (ispunc(here)) {
   switch(*here) {
	case '(' : fill(t,here,test,L_PAREN); break;
	case ')' : fill(t,here,test,R_PAREN); break;
	case ';' : fill(t,here,test,SEMI); break;
	case ',' : fill(t,here,test,COMMA); break;
	default: printf("Error in makedelimiter\n");
   }
   return(1);
 }
 return(0);
}

int twocheck(here,first,second) /* Simple one character look-ahead routine */
 char **here;
 int first;
 char *second;
{
 char *ptr=*here;
 if (*ptr==first) { 
	if (strchr(second,ptr[1])!=NULL) {
		return(1);
	}
 }
 return(0);
}

int valid_escape(here) /* Returns 1 if the two-character sequence is a valid
			  escape sequence (\t,\\,\',\n) */
char **here;
{
 char *temp=*here;
 if (*temp==92) {
	switch (*(temp+1)) {
	  	case 92 :
	  	case 39 : /* break; */ 
		case 't': /* temp[1]=9; break; */
		case 'n': break; /* temp[1]=13; break; */
		default : return(0); /* Invalid escape */
	}
/* while(*temp!=10) {temp[0]=temp[1]; temp++;} */
 (*here)++;
 return(1);
 }
 return(0);
}

int makestring(test,t) /* Returns 1 if able to create a string token */
char *test;
struct token *t;
{
 char *here=test;
 if (*here==39) {/* 39 is the apostrophe */
	here++;
	while(isalnum(*here) || isspace (*here) || isoperator(here) || 
	ispunc(here) || valid_escape(&here) ? 1 : 0) here++;
	if (*here==39) { /* End apostrophe */
		fill(t,here,test,STRING);
		return(1); /* Success */
	}
 }
 return(0); /* Not a string */
	
}

int makeoperator(test,t) /* Note: According to the RPAL lexicon, things like */
char *test;		 /* ".-" are valid operators -- however, we know they*/
struct token *t;	 /* are invalid, so we'll split any unknown 'long'   */
{ 			 /* operators into single operators */ 
 char *here=test;
 if (isoperator(test)) {
	 	switch(*here) { 
			case '>' : if (twocheck(&here,62,"=")) { /* >= */
					fill_sugar(t,"ge",GE);
					return(1);
				   } 
				   else {
					fill_sugar(t,"gr",GR); 
					return(1);
				   }
			case '<' : if (twocheck(&here,60,"=")) { /* <= */
					fill_sugar(t,"le",LE);
					return(1);
				   } 
				   else {
					fill_sugar(t,"ls",LS); 
					return(1);
				   }
			case '-' : if (twocheck(&here,45,">")) { /* -> */
				    	fill(t,++here,test,CONDITIONAL);
				    	return(1);
				   }
				   break;
			case '*' : if (twocheck(&here,42,"*")) { /* ** */
				    	fill(t,++here,test,EXPO);
	 			    	return(1);
				   }
				   break;
	 	}
	 fill(t,here,test,OP);
	 reserved_symbols(t); /* All the symbols with useful names */
	 return(1);
 } 
 return(0);
}

struct token *make_token() /* Mallocs memory for a token */
{
 return( (struct token *) malloc (sizeof(struct token)));
}

 char *process(teststring,head, current, incoming) /* Builds the token list */
 char *teststring;
 struct token **head, **current, **incoming;
 {struct token *temp=*incoming;
	if (*current==*head) { /* Then this is the first node on the list */
		*head=*incoming;
		*current=*incoming;
	}
	if ((*incoming=make_token())==TNULL)  /* Make new node */
			panic("No more memory for tokens!");
	(*incoming)->next=TNULL;
	
	(*current)->next=*incoming;
	*current=*incoming;
  return(&teststring[temp->n_length]);
}


struct token *trim(head) /* Removes all the delete nodes from the list */
struct token *head;
{
 struct token *temp=head, *prev=TNULL;
 while(temp!=TNULL) {
	while (temp->type==DELETE) {
		if (temp==head) /* First node */ {
			head=head->next;
			free(temp); 
			temp=head; }
		else {  prev->next=temp->next;
			temp=temp->next;
		}
	}	
	prev=temp;
	temp=temp->next;
 }
 return(head);}
