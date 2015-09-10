/* Last modified (commented) Mon Mar 13 22:19:11 EST 1995 */

/* Transform.c contains all the logic for transforming an AST to a ST, all of
   which can be found on page 40 of the lecture notes.

   Only 8 nodes need to be transformed, all of which are done in 
   'try_transform()'. The nodes are let; where; rec; @; within; and; 
   function_form; lambda.

 */

int examine(); /*Forward declarations */
void examine2();

struct tree *transform(ast) /* Transforms the AST to an ST */
struct tree *ast;
{
 struct tree *here=ast;
 static int xfrm=999; /* Number of transforms completed in this pass */
 while(xfrm) {
	xfrm=0;
	examine(here,&xfrm);
	if (debug) printf("%d transforms\n",xfrm);
	}
 return(here);
}

void tree_copy(src,dest) /* Copies the cotents of one tree node to another */
struct tree *src,*dest;
{
 dest->left=src->left;
 dest->right=src->right;
 dest->extra=src->extra;
 dest->id_name=src->id_name;
 dest->type=src->type;
}

struct tree *make_tree_node(type) /* Creates an empty tree node */
int type;
{
 struct tree *x= (struct tree *) malloc(sizeof(struct tree));
 if (x==PNULL) panic("Unable to malloc enough memory in make_tree_node()");
 x->extra=PNULL;
 x->left=PNULL;
 x->right=PNULL;
 x->id_name=NULL;
 x->type=type;
 return(x);
}

int try_transform(here) /* The acutal transforms are done here */
struct tree *here;
{
 struct tree *temp1, *temp2, *temp3, *temp4, *temp5;
 if (debug) {printf("Trying: "); name_dump(here); }
 switch (here->type) {
	case LET : if (here->left->type!=EQUALS) break; {
		temp1=here->left->right;
		here->left->right=here->right;
		here->right=temp1;
		here->left->type=LAMBDA;
		here->type=GAMMA;
		if (debug) printf("Finished LET\n");
		return(1);
		}
	case WHERE : if (here->right->type!=EQUALS) break; { 
		temp1=here->left; 	  /* P */
		temp2=here->right->right; /* E */
		here->left=here->right;
		here->right=temp2;
		here->left->right=temp1;
		here->type=GAMMA;
		here->left->type=LAMBDA;
		if (debug) printf("Finished WHERE\n");
		return(1);
		}
	case REC : if (here->left->type!=EQUALS) break; { 
		temp1=here->left->right;
		tree_copy(here->left,here);
		here->right=make_tree_node(GAMMA);	/* Gamma */
		here->right->left=make_tree_node(YSTAR);/* Ystar */
		here->right->right=make_tree_node(LAMBDA);/* Lambda */
		here->right->right->right=temp1; 	/* E */
		here->right->right->left=here->left;  /* X */
		if (debug) printf("Finished REC\n");
		return(1);
		}
	case ATX : { 
		temp1=here->extra; /* E1 */
		temp2=here->left->extra; /* Op */
		temp3=here->left->left->extra; /* E2 */
		here->type=GAMMA;
		here->left->right=here->left->left;
		here->right=temp3;
		here->left->right=temp1;
		here->left->left=temp2;
		here->extra=PNULL;
		here->left->extra=PNULL;
		here->left->type=GAMMA;
		if (debug) printf("Finished @\n");
		return(1);
		}
	case WITHIN : if (!((here->left->type==EQUALS) && 
			 (here->right->type==EQUALS))) break; {
		temp1=here->right->right;/* E2 */
		temp2=here->right->left; /* X2 */
		here->right->left=make_tree_node(0);
		here->right->right=here->left->right;
		here->right->left->left=here->left->left;
		here->left=temp2;			     /* X2 */
		here->right->left->right=temp1;		     /* E2 */
		here->type=EQUALS;
		here->right->type=GAMMA;
		here->right->left->type=LAMBDA;
		if (debug) printf("Finished WITHIN\n");
		return(1);
		}

	case AND2: {temp1=here; while(temp1!=PNULL) {/* Check for all = */
			if (temp1->extra->type!=EQUALS) return(0); 
			temp1=temp1->left;} 
		    temp1=here;
 		    temp4=temp2=make_tree_node(COMMA);
		    temp5=temp3=make_tree_node(TAU);
		    while(temp1->left!=PNULL) {
			temp2->extra=temp1->extra->left;
			temp3->extra=temp1->extra->right;
			temp2->left=make_tree_node(COMMA);
			temp3->left=make_tree_node(TAU);
			temp2=temp2->left;
			temp3=temp3->left;
			temp1=temp1->left;
		    }

		    temp2->extra=temp1->extra->left;
		    temp3->extra=temp1->extra->right;
		    here->left=temp4;
		    here->right=temp5;
		    here->extra=PNULL;
		    here->type=EQUALS;
		if (debug) printf("Finished AND\n");
		    return(1);
	}
	case FUNCT_FORM: case LAMBDA : {
		if (here->extra==PNULL) break;
		temp3=temp1=make_tree_node(LAMBDA);
		temp2=here->left; /* Beginning of variables */
		while(temp2->left!=PNULL) {
			temp1->right=make_tree_node(LAMBDA);
			temp1->left=temp2->extra;
			temp2=temp2->left;
			if (temp2->left==PNULL) temp1->right=temp2->extra;/*E*/
			temp1=temp1->right;
		}

		here->right=temp3; /* Tag the lambda chain back on */
		if (here->type==FUNCT_FORM) here->type=EQUALS;
		here->left=here->extra;
		here->extra=PNULL;
		if (debug) 
		   if (here->type==EQUALS) printf("Finished FUNCT_FORM\n");
                   else printf("Finished LAMBDA");
		return(1);
	}			

 }
 return(0);
}

int examine(here,xfrm) /* Recursively examines all the nodes */
struct tree *here;
int *xfrm;
{ 
 if (debug) {printf("examining: "); name_dump(here);}
 if (here!=PNULL) if (try_transform(here)) (*xfrm)++;  /* Transform here */
 if (here->extra!=PNULL) examine2(here,xfrm);
 else {
	if (here->left!=PNULL) examine(here->left,xfrm);
	if (here->right!=PNULL) examine(here->right,xfrm);
 }
 return(*xfrm);
}

void examine2(here,xfrm)  /* Helper function for n-ary nodes */
struct tree *here;
int *xfrm;
{	if (here->extra!= PNULL) examine(here->extra,xfrm);
	if (here->left!=PNULL) examine2(here->left,xfrm);
}
