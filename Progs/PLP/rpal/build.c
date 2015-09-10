/* Last modified (commented) Mon Mar 13 21:46:17 EST 1995 */

/* Build.c contains all the routines for generating an AST from a token list -
   The AST is build using a recursive decent parser using a stack to store
   nodes for later construction. Pretty neat. . . */

void E(), DD(), AT(); /* Forward declarations */

struct tree *pop() {/* Returns the tree node from the top of the stack */
 struct tree *member;
 if (top==NULL) panic("Parse error -- empty stack!\n"); 
 member=top->member;
 top=top->next;
 return(member);
}

void push(limb) /* Pushs a tree node on to the stack */
struct tree *limb; 
{ 
 struct t_stack *new_top = (struct t_stack *) malloc (sizeof(struct t_stack));
 if (new_top==NULL) panic("No more memory for stack!\n"); 
 new_top->member=limb;
 new_top->next=top;
 top=new_top;
 if (debug) name_dump(limb);
}

void build_tree(name)  /* Builds unary and binary trees */
 int name;
{
 struct tree *new_node=(struct tree *) malloc (sizeof(struct tree));
 if (debug) printf("Build (2)"); 
 if (new_node==PNULL) panic("No more memory for tree!");
 if (binary(name)) new_node->right=pop();
 new_node->left=pop();
 new_node->type=name;
 new_node->extra=PNULL;
 new_node->location=0;
 push(new_node);
}

/* Nodes with more than two children will be constructed as a 
   doubly linked list with the left node being the previous member, the
   right node being the next member, and the extra node pointing to the actual
   node information */

void build_tree_polyary(name,number) /* Builds n-ary trees */
int name,number;
{
 struct tree *new_node, *prev_node=PNULL;
 int left=number;
 if (debug) printf("build_tree_%d",number);
 while (left > 0) {
	new_node = (struct tree*) malloc (sizeof(struct tree));
 	if (new_node==PNULL) panic("No more memory for tree!");
	if (prev_node!=PNULL) {prev_node->right=new_node;}
 	new_node->extra=pop();
	new_node->left=prev_node;
	new_node->type=name;
	new_node->location=0; 
	prev_node=new_node;
	left--;
 }
 new_node->right=PNULL;
 push(new_node);
}


int next_token() { /* Returns the type of token on the token list */
 return(head->type);
}

void advance() {  /* Gobles up the current token */
 head=head->next;
}

void terminal() { /* Creates terminal nodes */
 struct tree *new_node = (struct tree*) malloc (sizeof(struct tree));
 if (new_node==PNULL) panic("No more memory for tree!");
 new_node->id_name=create_string(head->name,head->n_length);
 new_node->extra=PNULL;
 new_node->left=PNULL;
 new_node->right=PNULL;
 new_node->type=next_token();
 new_node->location=head->location;
 if (head->type==INTEGER) new_node->value=atoi(new_node->id_name);
 if (head->type==TRUE) new_node->value=1;
 if (head->type==FALSE) new_node->value=0;
 if (head->type==STRING) new_node->pname=strip(new_node->id_name);
 push(new_node); /* Put the new leaf on the stack */
 advance(); /* Free up some space */
}

void empty_terminal() {/* Creates the special empty_terminal node "()" */
 struct tree *new_node = (struct tree*) malloc (sizeof(struct tree));
 if (new_node==PNULL) panic("No more memory for tree!");
 new_node->id_name=create_string("<()>",4);
 new_node->extra=PNULL;
 new_node->left=PNULL;
 new_node->right=PNULL;
 new_node->type=EMPTY_PAREN;
 push(new_node); /* Put the new leaf on the stack */
}


/* The following several routines simply parse the token stack into the
   AST--the follow EXACTLY from pages 38 and 39 of the lecture notes, right
   down to the variable names! */

int VL() { /* Returns 1 if it was able to create a variable node */
 int n=0;
 if (next_token()==ID) {
	terminal(); /* Put ID on the stack */
	while(next_token()==COMMA) { /* It's a comma node! */
		advance();
		if (next_token()!=ID) parse_error("an identifier");
		terminal();
		n++;
	}
	if (n>0) build_tree_polyary(COMMA,n+1);
	return(1);
 }
 return(0);
}

int VB() { /* Returns 1 if it was able to create a variable node */
 switch (next_token()) {
	case ID : {
		terminal();
		return(1);
	}
	case L_PAREN : {
		advance();
		if (VL()==1) {
			if (next_token()!=R_PAREN) 
				parse_error(")");
			advance(); /* remove the right parenthesis */
			return(1);
		}
		if (next_token()!=R_PAREN) parse_error(")");
		advance();
		empty_terminal(); /* Makes the '()' terminal node */
		return(1);
	}
	default : return(0);
 }
}

void DB() {
 int n=0;
 if (VL()==1) 
	{if (next_token()==EQUALS) {
		advance(); /* Consume equal sign */
		E();
		build_tree(EQUALS);
		return;
	}
	while (VB()==1) n++;
	if (next_token()!=EQUALS) parse_error("=");
	advance(); /* Consume equal sign */
	E();
	build_tree_polyary(FUNCT_FORM,n+2);
	return;
	}
 if (next_token()==L_PAREN) {
	advance();
	DD();
	if (next_token()!=R_PAREN) parse_error(")");
	advance();
	return;
	}
 parse_error("Nothing left to parse"); 
 }	
	
void DR() {
 if (next_token()==REC) {
	advance();
 	DB();
	build_tree(REC);
 } 
 else DB();
}

void DA() {
 int n=1;
 DR();
 if (next_token()==AND2) {
 	while (next_token()==AND2) {
		advance(); /* Consume 'and' node */
		 DR();
		 n++;
	}
	build_tree_polyary(AND2,n);
 }
}

void DD() {
 DA();
 if (next_token()==WITHIN) {
	advance();
	DD();
	build_tree(WITHIN);
 }
}

int RN() { /* Returns 1 if able to create a rand node */
 switch (next_token()) {
	case ID :
	case INTEGER:
	case STRING:
	case TRUE:
	case FALSE:
	case XNIL: {terminal(); return(1);}
	case L_PAREN: {
		advance();
		E();
		if (next_token()!=R_PAREN) parse_error(")");
		advance();
		return(1);
 	}
 }
 if (next_token()==DUMMY) {
	terminal();
	return(1);
 }
 return(0);
} 

void R() {
 if (RN()==1) while(RN()==1) build_tree(GAMMA);
}

void AP() {
 R();
 while (next_token()==ATX) {
	advance();
	/* MEB: 10/23/95.  Wasn't checking for this.   */
	if (next_token()!=ID) parse_error("an identifier");
	terminal();
	R();
	build_tree_polyary(ATX,3);
 }
}

void AF() { 
 int n=0;
 AP();
 while (next_token()==EXPO) {
	advance(); /* Eat exponent */
	AP();
	n++;
 }
 while(--n >= 0) build_tree(EXPO);
}

void AT() {
 int relational;
 AF();
 relational=next_token();
 while ( (relational==TIMES) || (relational==DIVIDE) ) {
	advance();
	AF();
	build_tree(relational);
	relational=next_token();
 }
}

void AA() {
 int relational;
 if (next_token()==MINUS) {
	advance(); /* Remove the negative sign */
	AT();
	build_tree(NEG);
 }
else  if (next_token()==PLUS) {advance(); AT(); }
else AT();

 relational=next_token();
 while ((relational==PLUS) || (relational==MINUS)) {
	advance(); /* Eat the operator */
	AT();
	build_tree(relational);
	relational=next_token();
 }
}

void BP() { 
 int relational;
 AA();
 relational=next_token();
 if ( (relational>=GR) && (relational<=NE) ) { /* It's a valid relational */
	advance();
	AA();
	build_tree(relational);
 }
}

void BS() {
 if (next_token()==NOT) {
	advance();
	BP();
	build_tree(NOT);
 }
 BP();
}

void BT() {
 BS();
 while (next_token()==AND) { /* Chage while to if! */
	advance();
	BS();
	build_tree(AND);
 }
}

void B() {
 BT();
 while (next_token()==OR) { /* Same story */
	advance();
	BT();
	build_tree(OR);
 }
}

void TC() {
 B();
 if (next_token()==CONDITIONAL) {
	advance();
	TC();
	if (next_token()!=BAR) parse_error ("|");
	advance();
	TC();
	build_tree_polyary(CONDITIONAL,3);
 }
}

void TA() {
 TC();
 while(next_token()==AUG) {
	advance(); 
	TC(); 
	build_tree(AUG);
 }
}

void T() {
 int n=1;
 TA();
 if (next_token()==COMMA) { /* We are building a tau node */ 
 	while(next_token()==COMMA) {
		advance(); /* Eat comma */
		TA(); 
		n++;
	}
	build_tree_polyary(TAU,n);
 }
}

void EW() {
 T();
 if (next_token()==WHERE) { 
	 advance(); /* Eat the 'where' token */
	 DR();
	 build_tree(WHERE); /* Done */
 }
}

void E() {
 int n=1;
 switch (next_token()) {
 case LET : {
		advance(); /* Eat let */
		DD();
		if (next_token()!=IN) parse_error("in");
		advance(); /* Eat in */
		E();
		build_tree(LET);
		break; /* Done */
	     }
 case FN : {
		advance(); /* Eat FN */
		VB();
		while(next_token()!=PERIOD) {
			n++; 
			VB();
		}
		advance(); /* Eat period */
		E();
		if (n==1) build_tree(LAMBDA);
		else build_tree_polyary(LAMBDA,n+1);
		break; /* Done */
 	   }
 default : EW();
 }
}
	
struct tree *rpal() { /* Recursive decent parser entry...returns AST */
 E();
 if (head->next!=TNULL) {
	printf("Potential parse problem--tokens remain.\n");
	printf("Remaining tokens begin at line %d.\n",head->location);
 }
 return(top->member);
}
