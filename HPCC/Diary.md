The aim of this documentation is to conserve my internship experiences and serve as an informative source for someone 
who is just starting to learn the details of HPCC systems from scratch.

The overall goal of my intersnhip is to improve the efficiency of execution of child queries. What is a child query and how can we go about improving it is explained in later parts of the document.

#Before the internship
Since, I applied for this using Google Summer of Code, I had to develop a good idea about the project to develop a proposal. The first goal was to get an understanding of what HPCC system is and how does it work. The links on this page does this job pretty well:

    http://hpccsystems.com/community/training-videos
    
I went through all the links under these topics as they were very much related to my work. 
* HPCC
* ECL 
* Installing HPCC

In brief, HPCC is a platform to execute powerful queries on huge amounts of data (also called Big Data). The queries are written in an in-house declarative language called ECL. 

HPCC is a direct competitor of MapReduce. A major advantage that HPCC has over MapReduce is that its query language is much more powerful and allows a variety of complex operations. On the other hand, it is also a bit of a drawback, because the user needs to understand how to use ECL and its various tools which may be a bit of a learning curve.

Quite often I came across the terms: thor, hthor and roxie. I found it best to consider them as different physical clusters of computers. They vary in the way they execute certain aspects of an ECL query. It is my guess that thor is used for large datasets and roxie for small ones.

The next step was to understand some ECL queries and see how it worked. Each ECL query is first converted to xml and c++ code. The execution engine uses these two execute the query. Gavin sent me some basic example queries. In addition, the following two links helped me quite a lot:

https://github.com/hpcc-systems/HPCC-Platform/blob/master/ecl/eclcc/DOCUMENTATION.rst

http://hpccsystems.com/download/docs/ecl-language-reference/html

The first link is to understand some fine details in the processing of an ECL query. Now that I look back at it, a lot more things seem to make sense compared to how it looked when I initially read it. The second link is useful to understand any particular line in the ECL code. Since, there could be many syntax of any given operation, this may get confusing. It is good to think of them as overloaded functions. 

With this I found that the best way to identify a child query is to look for the element: child="1" in the generated xml graph. Via email, I was told that what are the potential problems in the generation of child queries. I tried to come up with ECL queries to create those problems but was unable to do so. Eventually, Gavin sent me a bunch of examples ecl codes: gsoc1 to gsoc6, which explored the relevant problems. The ecl files can be found here:

https://github.com/hpcc-systems/HPCC-Platform/tree/master/testing/regress/ecl

Before getting to know HPCC, I have worked on mySQL and Oracle. In one of my courses, we had to develop a new database from scrach based on some C++ code framework. Until then, my view of a database system limited to some classic algorthms to execute database operations like select, sort, join etc and how to write functional queries in sql to get these done. The internship helped me explore to some extent a system in which all these features are already there. My work was to make some small tweaks and hope to have a favorable effect at the end. 

#During the internship
The initial steps involved forking the HPCC system on Github, building it locally and then installing it. There were a few glitches in the process, for example, at one point the ECL watch went blank. But they were solved after by talking with Gavin and Gordon. Then I went through the documentation of github to understand the basics. I believe my experience with github would be very productive for me in the future. I sent the pull request to the repo with the six examples that Gavin sent me. 

Thereafter I setup the eclipse CDT and started debugging the code. It took some time to understand well the details of how to work with eclipse CDT. I have been used to working with emacs which had much limited features. After some discussions with Gavin, I was finally able to setup the debug configuration. One thing which needs attention while setting up the configuration is the selection of process launcher. After clicking Debug configuration I clicked Select Other at the bottom and selected 'Use Legacy Create Process launcher'.  It also took sometime to make sure that the eclcc.ini had all the required paths. 

I started with debugging gsoc5. The part of this ecl code which was the point of concern is this:
```
 outRecord t1(idRecord l) := TRANSFORM
     f := l.children(cid % 2 = 1); // those children whose cid % 2 = 1
     SELF.child1:= ROW(makeNested(f(cid % 3 != 1)));
     SELF.child2:= ROW(makeNested(f(cid % 4 != 1)));
     SELF := l;
 END;
```

The filter operation 'f' is evaluated inline twice for child1 and child2 as shown here:

		for (;vQ--;) {
			rowO = *curP++;
			if ((long long)*((unsigned long long *)(rowO + 0U)) % 2LL == 1LL) {
				if ((long long)*((unsigned long long *)(rowO + 0U)) % 3LL != 1LL) {
					crM.append(rowO);
				}
			}
		}
	...........
	...........
		for (;vU--;) {
			rowS = *curT++;
			if ((long long)*((unsigned long long *)(rowS + 0U)) % 2LL == 1LL) {
				if ((long long)*((unsigned long long *)(rowS + 0U)) % 4LL != 1LL) {
					crR.append(rowS);
				}
			}
		}

My first impression was that I should be just debugging looking at the variables and try to figure out the place where the filter activity is generated inline. Then, I can try to change the code to see if I can make it evaluate only once. But as it turned out things are much more complex than that. Gavin told me to look at canProcessInline function to see which operators are evaluate inline and which not. The operator in my interest for this case was no\_filter. But it occurred to me that the function is called for multiple operators and I could not relate many of these operators with the actual ecl code. Even for the operators while look like they are relavant to the ECL code, I could not understand the expression associated with it. I was then introduced to the logging functions like EclIR::dump\_ir() and DBGLOG(). This helped me a bit. But the dump of EclIR::dump\_ir(IHqlExpression) was not very clear about which operator is contained in it. It seemed that it had a lot of other information. Eventually, I found that there is another function: EclIR::getOperatorIRText() to print exactly which operator is it working on. I tried to dig a bit deeper in canProcessInline function and went a few levels in the code. After some discussion with Gavin, I came to following conclusions:

1. newAtom is used to indicate that the parent dataset or row that the column/fields is being selected from is not active. If a no_select has no newAtom attribute then it is a column selected from an active dataset (SQL cursor might be clearer).  E.g., ds(ds.myField = 10) ds.myField does not have a new atom because ds is not active within the scope it is used.
2. The following function is checking for a filter condition that doesn’t relate to the dataset being filtered.
```
bool filterIsTableInvariant(IHqlExpression * expr)
{
    IHqlExpression * dsSelector = expr->queryChild(0)->queryNormalizedSelector();
    ForEachChildFrom(i, expr, 1)
    {
        IHqlExpression * cur = expr->queryChild(i);
        if (containsSelector(cur, dsSelector))
            return false;
    }
    return true;
}
```
3. Sort operations are not executed inline. It generates a child query. The following function contains more information about it. 
```
ABoundActivity * HqlCppTranslator::doBuildActivitySort(BuildCtx & ctx, IHqlExpression * expr)
```
At this point I thought that the code is too complex to really understand every little details about it. 

I now shifted my focus on making my own dataset and maybe go on to experiment with how can I run queries on it to discover the problem with child queries. For this I wanted to get a good understanding about some basics of dabatabse creation in ECL like indexing, normalizing etc. I decided to go through this example ecl file for the purpose:

https://github.com/hpcc-systems/ecl-samples/blob/master/bundles/CellFormatter/GenData/GenData.ecl

Going through this raised a lot of questions in my mind. I will go through some of the points which I figures out after discussion with Gavin via email:

1. Certains terms are used interchangeably in the ecl documentation like field and column, recordset and dataset and table.
2. The normalize function:
```
NORMALIZE(BlankKids,TotalChildren,CreateKids(LEFT,COUNTER))
```
is an outdated idiom. It just seemed to call the CreateKids function TotalChildren (an integer) number of times on the dataset of BlankKids. The actual normalization process is done using other function, which I have not explored yet. A replacement for the above functions is:
```
DATASET(TotalChildren, CreateKids(COUNTER))
```
After this, I created my own dataset and is shown here:

https://github.com/hpcc-systems/HPCC-Platform/commit/0bb3ebfdcb4ac16da41dd918ee5a044d54f7b374

The creation of the database was a bit of a hassle because there were lot of formatting like single quotes which I needed to correct in excel. I tried to create some examples next to figure out the problem of child query. I did come up with an example of a count query which could have been more efficient. But it looked like it will take me a long time to actually be able to master ECL and come up with examples which identifies the problems. I often felt lost in the immense number of operations to look at. I was told that I should concentrate on the operations which are assigned false by canProcessInline functions. But since I did not have full confidence that I understood the function properly, I could not feel like I could venture into making my own ECL queries. Also, I was still uncertain about the link between an ECL action and the node operator activity which is responsible to execute that action. 

Gavin was very understanding and he gave me an easier job of coming up with my own version of generated C++ code for gsoc5 and gsoc6 which would be efficient according to me. We were talking about making every activity in a child graph at this point. I went through the generated codes of gsoc5 and gsoc6 and came up with the following conclusions after discussion with Gavin:

1. There can be a node inside a graph and a graph inside a node. Nodes inside graphs are the activities within a graph.
2. Terms like cAc2 in c++ means activity in the node with id = 2 (note 2 at the end of cAc2) of the xml graph
3. The xml graph is generated using IHqlExpression class instance for the activites which return false from canProcessInline()  

I came up with a proposal of how should the generated C++ and xml files look like for gsoc5 and gsoc6 and can be seen here:


In summary, my idea was to store the result of every activity in a separate child graph and split the result to other child graphs who need it. My reasoning for the approach: From what I gathered so far, most of the optimizations are done in child graphs. So, why not keep every activity in a child graph. Then split its result accordingly to who needs it. Another suggestion was regarding the inline code of gsoc5. The relevant generated code is already shown above. I proposed the following instead:
```
   for (;vS--;) {
			rowQ = *curR++;
		        if ((long long)*((unsigned long long *)(rowQ + 0U)) % 2LL == 1LL) {
				if ((long long)*((unsigned long long *)(rowQ + 0U)) % 4LL != 1LL) {
					crP.append(rowQ);
				}
				if ((long long)*((unsigned long long *)(rowM + 0U)) % 3LL != 1LL) {
					crK.append(rowM);
				}
			}
		}
```
Gavin liked the second idea of making the two ifs inside one if and created a jira issue for the same here:

https://track.hpccsystems.com/browse/HPCC-13771

Regarding the first idea of assigning everything to a separate child graph I was told that the eventual goal is to ensure everything is evaluated as efficiently as possible rather than keeping everything in a child graph. As far as distributing the result of child graph to other child graph is considered, splitter is there to that job. I tried to justify my stand on sharing result on one child graph with other child graphs with this: Suppose it takes t amount of time to process r rows in an activity and it takes i amount of time to iterate through the processed result. If the activity is shared between two child graphs, time to process and fetch all the rows to the child graphs will be r + 2i. But if it not shared then the time will be at least 2r. To this Gavin said, that this sounds like a good idea and it could be a side-effect of implementing lazy child queries. So, I suppose my idea was related to something different from the current goal of the project ie. storing the result of a child query and splitting it among others.

Gavin sent me some changes to gsoc5 and gsoc6 and asked me to look at the generated code. 
```
gsoc6.ecl:
    SELF.child1:= ROW(makeNested(f));
to the following:
    SELF.child1:= ROW(makeNested(GRAPH(f)));

gsoc5.ecl
    f := l.children(cid % 2 = 1);
to
    f := __COMMON__(l.children(cid % 2 = 1));
```
This is when we realized that there was a miscommunication regarding the platform to use while generating code. The codes that I and Gavin were different. I was using hthor which was due to be deprecated. Luckily, it seemed to have worked fine for the gsoc examples. I started using thor from this point onwards. The above changes essentially solved the issues of sharing the filter operation but since it does not do the job automatically, some work is still to be done.

Next, Gavin sent me a new set of milestones to work on. They are pasted below:

0.       Investigate gsoc5 and gsoc6 and propose how the generated c++ and xml should look.
1.       Add an option to the code generator to generate minimal dataset operations inline.
Only generate inline operations that must be done inline, all else in child queries.
2.       Extend so it also generates inline operations that would be ridiculous to generate as a child query.
This could start simple and gradually be extended.\
3.       Write some more test cases that test that optimizations are being applied.  (The existing Regression suite queries may be sufficient.)
After those milestones, all non-trivial queries should be treated as child queries, and have all the normal optimization applied to it.  The problem with executing all queries as child queries is that it has an extra overhead, and tends to generate larger code.  The next milestones look at the resourced graph and work out which parts should be done inline after all.  Each of these stages should have at least one example query which is used to test the changes.
4.       Take an example ECL query that has a child query with a single result which can be executed inline. Implement code to unconditionally extract the dataset operation from the graph representation and execute it inline. 
(Use another #option to enable this behaviour.  At this stage the code takes no account of whether or not it is valid to execute the query inline – it performs it unconditionally if the option is set).
This requires understanding the graph representation.
5.       Extend (4) to cover the case where there are multiple independent results in the child query.
This will require extracting expressions from multiple subgraphs.
6.       Extend (5) to cover the case where there are multiple results that share part of the processing (e.g., share a filter, but then process it in two different ways)
This will require processing splitters – how should these be generated inline?
7.       Extend (6) to cover the situation where there are dependencies between the queries.  E.g., one branch counts, and another uses the value of that count in a filter.
This means that expressions are generated in the correct order, and that results are available for other dataset expressions.
8.       Add logic to (4) to automatically detect queries that are completely independent.
With this in place it should be possible to run the code generator on all existing queries, and only valid examples would be executed inline.
9.       Add logic to spot case (5) automatically
Successively improve the logic for spotting inline queries to cover more cases.
10.   Add logic to spot case (6) automatically
11.   Add logic to spot case (7) automatically.
12.   Add logic to check that dependencies of conditional datasets aren’t executed unconditionally.
Quite possibly only a small subset of conditional expressions should be evaluated inline.
13.   Add logic to spot the situation where a query that could be executed inline shares code with a query that must be executed out of line.
If part of a query is executed in a child query then it is likely to be worth executing all of the query in the child query.
14.   Add logic to allow some independent parts of the child query to be executed inline, but other parts inside the child query.
This requires some values to be retained within the graph, but others evaluated inline.

The first few steps were as follows:

1. Add an option (see hqlcpp.ipp HqlCppOptions and hqlcpp.cpp HqlCppTranslator::cacheOptions())
2. Add a new function to hqlinline mustProcessInline() or something similar
3. Check the option in NewChildDatasetSpotter can call the new function instead of the old one.
4. Start adding a minimal set of entries to alwaysProcessinline so that a) all the gsoc examples still work b) all the other examples still work.

While trying to implement the steps, I came across an error in eclipse which seemed to be tough to solve. The problem was in loading certain libraries at the end of the debug.
```
(0,0): error C6003: C++ link error: cannot find -leclrtl
(0,0): error C6003: C++ link error: cannot find -lhthor
a.out(0,0): error C3000: Compile/Link failed for a.out
```
I was told that it had a simple solution:
```
<path>/bin/eclcc <myfile.ecl> -L <path>/libs
```
It worked well.

I was finally able to make the changes to the code and figure out how it affects the eventual generated code. I added the option of mustAssignInline. 
To figure out which operations should be processed inline I put a breakpoint to find out which ones causes an error in the case of gsoc queries if I return false to each operators. But it does everything in child graph including creation of inline dataset. Unfortunately, the link with the changes are lost because I later on deleted my clone and forked the repo again. The changes were mainly these:
```
class NewChildDatasetSpotter : public ConditionalContextTransformer
{
void markHoistPoints(IHqlExpression * expr)
    {
        node_operator op = expr->getOperator();
        if (op == no_sizeof)
            return;

        if (expr->isDataset() || (expr->isDatarow() && (op != no_select)))
        {    
        	if (translator.queryOptions().minimalOperationsInline == true) {
        		if (!translator.mustAssignInline(&ctx, expr)) {
        			noteCandidate(expr);
        			return;
        		}
        	}
        	else
        		if (!translator.canAssignInline(&ctx, expr))
            {
                noteCandidate(expr);
                return;
            }
            if (!walkFurtherDownTree(expr))
                return;
        }

        doAnalyseExpr(expr);
    }
}

In hqlcppds.cpp

bool HqlCppTranslator::mustAssignInline(BuildCtx * ctx, IHqlExpression * expr)
{	node_operator op = expr->getOperator();
	switch(op) {
	//case no_getgraphresult:
	//case no_null:
	case no_createrow:
		return ::canAssignInline(ctx, expr);
	default:
		return false;
	}
}
```
As it can be seen, the mustAssignInline function returns true only for the case of no_createrow. This seems to have done the job of sharing filters in case of gsoc5 and gsoc6. Since I have not talked about gsoc6, I will paste the transform function below which is the currently not executed efficiently. The problem is explained in comments
```
//A filter definition is used within different conditions - one as is, one then sorted
//The code is generated inline in the transform for one, and in a child query for the
//second, but the filter code is not shared between them
outRecord t1(idRecord l) := TRANSFORM
    f := l.children(cid % 2 = 1);  // This could be an expensive filter operation...
    s := SORT(f, HASH(cid));
    SELF.child1:= ROW(makeNested(f));
    SELF.child2:= ROW(makeNested(s));
    SELF := l;
END;
```

Though the new option worked for all the gsoc queries without errors, it created errors for a few of the ecl files in ecl/regress. The details can be seen here:

filename: errors_runtimetests

After running regress.sh in testing/regress, I filtered those cpp files which had a difference in the sizes of cpp files. I sorted them in decreasing order of differences in file sizes and is shown here. The last column is the difference in size. 

filename: CppFileSizeDiff_testingregress.ods

It seems to create some large rectangles in the graph as for example gsoc6. I wonder if that is something that should be resolved.

filename: gsoc6_graph.png

Gavin said that the next step could be to make the source nodes not inline by adding the following lines to the mustAssignInline function at the beginning:
```
if (getNumActivityArguments(expr) == 0)
{
      return true;
}
```
I implemented this as a new option (inlineSourceOperations) and analyzed how it effected the size generated code in combination with the option mustAssignInline. The results can be seen in the sheet 5 and 6 of the file here:

filename: OptionsAnalysis.ods

In the excel sheet (in ods format) the file sizes of cpp and xml files of all compiler regression tests are compared in these cases:
1) no option 2) -fminimalOperationsInline 3) -finlineSourceOperations 4) both 2 and 3.

I found that the file sizes is very similar for the case of 1 and 3 which probably means that canProcessInline generally returns true for source nodes. In case of 4 the file sizes are similar to those in 2 which means that making source nodes inline did not have a huge effect on the generated code.

Gavin sent me a file which was an example of a case when a source node should not have been inline and can be found here:

filename:gsoc7.ecl

The whole idea of inlining or not inlining source node was later on removed from consideration as it seemed to be causing confusions. After the Friday's meeting of Jul 3, Gavin sent some updates about the current status of the project and future expectations:

First to clarify there are 3 different categories of expressions

1.      Operators that should always be evaluated inline.
2.      Operators that should always be evaluated in a child query
3.      Operators that can be evaluated in either, and the best location depends on the context.  This are the interesting ones.

As far as the final structure goes I would expect something along the following lines (I haven’t thought about this in detail).

* a)      NewChildDatasetSpotter is modified to call a new function mustAssignInline.  This is a simple function which only has a minimal number of operators (those in category A) that return true.  If there is any doubt if an operator belongs to category (A) it should added to the child query (treat them as category C). 
* b)      An expression graph for a child query is built up and resourced in the normal way.  E.g., using the code in ChildGraphBuilder
Note, at the end of this stage the graph will be fully optimized, and shared datasets will be marked appropriately.
* c)      Another *new* phase of processing is applied to the expression graph for the child query.  It decides which parts of that child graph would be more efficient to evaluate inline than in a child graph. This stage and the next stage deals with operators (C).
* d)      A new expression graph (IHqlExpression) is generated (from the input to stage (c)) which allows code to be generated with the expressions generated inline or in a child query as decided by stage (c).
* e)      As before the expressions are processed to generate the code (including the child query in the xml)

Milestones 1+2 are covered in stage (a), milestones (4) onwards are covered in stage (c) and (d).  There are several complications

* i)                    At the moment the results used for the values from child queries are replaced with operators to get results from the graph.  If the operations are no longer inline, how will the expressions be modified so they actually get generated inline?  The different modifications which could cause complications are outlined in milestones 4-7.
* ii)                   How do you determine whether something really should be generated inline.  This will require an improved equivalent to canProcessInline().  The different complications are outlined in milestones 8-14.

At this point 5 weeks were over and I had to leave for attending a conference for a week. Though it was a good experience, I lost my teeth crown towards the end of it which made the rest of the journey difficult. I had it replaced when I came back.

I returned back on Monday night of the next week. Gavin asked me to write up an overview of my future work. So, I turned back to the status update that Gavin sent me before. Looking at it made me ask quite a few questions which turned out to prove that I need to improve my knowledge about the code. But now that I look back at it, things seem to be much clearer. I will try to explain what it means to me now. Point (a) has already been implemented with the option of minimalOperationsInline. So, the initial graph is created after calling the mustAssignInline function so that most of the activities are assigned as child graphs. This is helpful because all the optimizations are applied to it in doing so. The child graphs are now resourced which is another way of saying that the child graphs are optimized. This is point (b). The next goal is to take out certain parts of the child graphs and assign them inline (in point (c)). Once this is done a new expression graph is generated which is basically the new xml after making the changes in point (c). This is done in point (d). The rest of the process is similar as before: code is generated for the xml activities and the inline operations and is executed. I think point i) is related to the problem I am currently facing: making sure that the result of a child graph can be accessed when it out context. I am yet to face the problem indicated in point ii)

After this, Gavin created a new branch with plenty of changes and which were supposed to act as a framework for future work. This can be accessed here:

https://github.com/hpcc-systems/HPCC-Platform/compare/master...ghalliday:gsoc?expand=1

The changes initially did not make a lot of sense to me. I probably needed some ideas about what is the flow of control of the code to properly understand the code. Also many of the classes were new to me. But at this point, I have a much better understanding of it and will try to explain it. 

The option minimalOperations is now replaced with optimizeInlineOperations. The first thing that the code does it to figure out which activities are to be evaluated inline in the function markHoist of class NewChildDatasetSpotter. This is done by calling the function mustAssignInline which has been updated with more operators. With this information, a list of child queries are built. Thereafter the function void ChildGraphBuilder::generateGraph is called to create the child graphs corresponding to the list of child queries. This function makes the child graphs resourced by calling the function getResourcedChildGraph. The child graphs are now fully optimized. The next step is to filter out certain parts of the child graph make them inline. This is done by calling optimizeInlineActivities function. The code (in the link above) makes all the child graphs inline. This is done by calling buildStmt function for each each child graph. Each child graph is supposed to have the first activity: no_setgraphresult. Each of the other activities are descendents (children or grandchildren and so on) of this activity. So a new case is added in the buildStmt function to handle this activity. This case is now handled by a new function doBuildStmtSetGraphResult. To understand this function, let us first look at what some of the classes and functions used in the function does:

OwnedHqlExpr – a smart pointer used to ensure the correct lifetime for the IHqlExpression objects

buildTempExpr() – the point that actually evaluates the result

createExprAttribute – creating an IHqlExpression that acts as a primary key to uniquely identify the result

CHqlBoundExpr – used to represent an expression that has been converted to C++

LINK - increase the link to an object by one (when the links become 0, it is automatically deleted like a garbage collector)

So basically the role of the function doBuildStmtSetGraphResult is to convert a child graph into c++ (or inline) code. For this it first stores some basic information about the child graph like dataset, graphId and resultNum (Not very sure what are these three exactly, but maybe not needed as of now). These three information helps in uniquely identifying the child graph and these identifier is stored in variable result by calling the function createExprAttribute. There is a tag (also called attribute) associated with this identifier called resultAtom. Basically all this tag does is say that the identifier is of type: 'result of a graph'. The translation to C++ code is actually done by calling the function buildTempExpr and stored in bound. At the end the current context is updated with the C++ code and the identifier associated with the code using ctx.associateExpr. Finally HqlCppTranslator::buildGetLocalResult is updated to search for that logical tag (resultAtom), and if found, return the dataset associated with it.  
gsoc5min.ecl in the link above is the file which runs properly with the changed code. The ecl file contains a single result in a single child query.

For gsoc1 to 6 it went into infinite recursion. The reason was that it was trying to inline certain activities which cannot be make inline, viz. sort and split. gsoc5 is the only example which does not have a sort operation. But it has a split operation when I use the option of optimizeInlineActivities. So it still created the infinite recursion.

I made a small change to the code and this worked for gsoc5.ecl. The branch can be seen here:

https://github.com/aranjan1002/HPCC-Platform/compare/childquery-2.1...aranjan1002:childquery-2.3

The only change is the addition of no_split case in the function doBuildDataset. With this change the filter operation was done only once and no child graph was created either. The code snippet for the filter operation is here:
```
        for (;vS--;) {
            rowQ = *curR++;
            if ((long long)*((unsigned long long *)(rowQ + 0U)) % 2LL == 1LL) {
                crP.append(rowQ);
            }
        }
        byte * rowT;
        byte * * curU = crP.queryrows();
        unsigned vV = crP.getcount();
        for (;vV--;) {
            rowT = *curU++;
            if ((long long)*((unsigned long long *)(rowT + 0U)) % 4LL != 1LL) {
                crN.append(rowT);
            }
        }
        vM.setown(crN.getcount(),crN.linkrows());
        rtlRowsAttr v01;
        RtlLinkedDatasetBuilder cr11(allocO);
        byte * row21;
        byte * * cur31 = crP.queryrows();
        unsigned v41 = crP.getcount();
        for (;v41--;) {
            row21 = *cur31++;
            if ((long long)*((unsigned long long *)(row21 + 0U)) % 3LL != 1LL) {
                cr11.append(row21);
            }
        }
```
It also runs properly generating the correct output.

It would be good to ask why this change works? What this change does is instead of doing anything for making split inline it goes on to make its first child inline. As can be seen in the graph representation of any split operation, it connects two nodes and hence probably has two children. Then why does making only child inline work? I am not very sure why this works and need to discuss with Gavin.

At this point Gavin left for a vacation and my co-mentor Jamie took up the charge. There were only 2 weeks left after this. Jamie asked to make changes in the function optimizeInlineActivities so that only such child graphs are inline in which all the activities can be inlined according to the function canProcessInline. The changes I made for the purpose can be seen here:

https://github.com/hpcc-systems/HPCC-Platform/compare/master...aranjan1002:childquery-2.4

With this gsoc6 was supposed to run properly because it should not have been inlined at all. But it turned to be giving this error for the platform thor after generating the code:
```
<Result>
 <Info><Code>4</Code><Source>master</Source><Message>4, Graph[13], workunitwrite[24]: MP link closed (10.227.162.168:20100), Master exception : Error aborting job, will cause thor restart</Message></Info>
 <Exception><Code>4</Code><Source>eclagent</Source><Message>System error: 4: Graph[13], workunitwrite[24]: MP link closed (10.227.162.168:20100), Master exception</Message></Exception>
</Result>
```
Interestingly, it worked well for the platform roxie and gave the correct output. For the platform hthor, it gave this error:
```
<Result>
 <Exception><Code>99</Code><Source>eclagent</Source><Message>System error: 99: Graph Result 1 accessed before it is created (in Project G13 E15)</Message></Exception>
</Result>
```
I created a new branch which does not call optimizeInlineActivities function and hence creates most the activities as a child graph. It runs properly for all the gsoc queries and cane be seen here:

https://github.com/hpcc-systems/HPCC-Platform/compare/master...aranjan1002:childquery-2.5

On comparing the xml files generated by this branch and the one with the error for gsoc6, I was able to identify the source of error. It was this line was missing in a child graph:
<att name="_numResults" value="2"/>

This is an attribute of subgraph which is required. So, I added the line by making the change on line 1388 of hqlccpds.cpp here:

https://github.com/hpcc-systems/HPCC-Platform/compare/master...aranjan1002:childquery-2.6

But I was not able to run the gsoc1 to 4 even after this change. I got the same error for each before the code could be generated:
error C4821: INTERNAL: Graph context not found

With quite a few hacks here and there, I was able to remove the error of gsoc1:

INTERNAL: Graph context not found

I was also able to generated xml and cpp of the code of gsoc1. But when I run it I get the following error:
```
<Result>
 <Exception><Code>3000</Code><Source>eclagent</Source><Message>System error: 3000: Graph[13], project[15]: SLAVE #1 [10.227.162.168:20100]: assert(tmpHandler.get()) failed - file: /home/aranjan/HPCC/HPCC-Platform/thorlcr/graph/thgraph.hpp, line 571, </Message></Exception>
</Result>
```
The changes to do this can be found here:

https://github.com/hpcc-systems/HPCC-Platform/compare/master...aranjan1002:childquery-2.6.3

In essence the changes were to save the getgraphresult expression with attribute externalAtom in a global variable: graphResult. Then, use this variable in the function buildGetLocalResult instead of the passed parameter. Also, instead of inlining the second child graph in the function optimizeInlineGraph, it is inlined at the end of the function generateGraph. I did that because I wanted to do the inline after the first child graph has been properly generated.

I think this was an important step because now we know that if the getgraphresult have the correct attribute to get the result from the child graph, it would do the job. The generated code can be seen here:
filename: gsoc1_childquery-2-6-3.cpp, gsoc1_childquery-2-6-3.xml

From what it looks like there may be a problem of out of order execution after looking at the xml and C++.

The next step is to figure out how to remove the error for gsoc2 to 4 and make it at least compile. Then we can look forward remove errors of running the code.

I was able to make the code work if all the subgraphs (including attributes) are allocated to outoflinesubgraphs. The code can be found here:

https://github.com/hpcc-systems/HPCC-Platform/compare/master...aranjan1002:childquery-2.6.4
