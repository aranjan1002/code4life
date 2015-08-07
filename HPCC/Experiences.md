Overall Experience and Recommendations

I came into this project because I had some experiences with database. But as it turned out the skills which I actually utilized here was c++ and debugging skills. It is easy to understand the overall goal of the project: improve running time of certain queries by improving the optimization process. But there was were many details about the project which I came to realize very later on in the project but were necessary to make progress. Now that I have some confidence about the knowledge of the project, I can outline some steps which could have been much more productive and may act as a reference for future interns or employees. I will like to point out that these are from my perspective and some other person may have behaved differently based upon his expertise and experience. 

Originally the plan went something like this: I will be able to master ECL to some extent, run the queries which exploits the relevant problems in the code generation. Then, look at the code generated and suggest changes to improve the optimization process. But it turned out that the first step of learning ECL was taking too much time for me. I was thinking that it should not be much more difficult than learning SQL which I believe I got grasp of pretty quickly when I learnt in school. But ECL is much different from SQL and is also much more powerful. Many aspects which are explicit in ECL are on an abstract level in SQL. For example, normalization, distribution among clusters, performing operations per record (using transform) etc. If I was given the task for implementing certain algorithm in ECL, I believe I could have done it in a shorter amount of time. But I was expected to create ECL queries with the target of creating some generated code which exploses problems with child queries. At that point I was not clear about what is the process of creating a child query. I felt myself stuck in understanding which operators to use from the long list and what exactly do these operators do? But this was realized shortly and I was now given the task of making some well guided changes to the code generator instead. 

Now that I can see the project from a broader perspective I think I can say what would have been the most productive steps for me. 

Overall, it would have been ideal if at each step I was asked to achieve something by changing the code no matter how I do it. This would make me  navigate through different classes and ask about what they do when I get stuck. This will give me a hands on experience with the code generator. Feedback about my changes would have helped me make my changes better with time.

After finishing the initial setup, I believe we could have started with the mustAssignInline function as I did in the Week 4 in this commit:
https://github.com/aranjan1002/HPCC-Platform/commit/16cdbbf4814a4f8bc1a519a395e74904bb5ad370

This step was easy because I was explicitly told as to how to add the option and also that I should do some debugging to make gsoc 1 to 6 run by adding cases to the function mustAssignInline. Once I have done this, I know the following
* How to add options to ecl or eclcc
* How to check for options (using queryOptions)
* markHoist function is always called for each activity to figure out if an operation to be assigned inline or not
* canAssignInline function is the main function which makes this judgement

Next, some more operations are needed to be added to the function mustAssignInline to make sure that the regression tests run properly. Fortunately, Gavin sent me the list himself without me looking further into this. The changes that he made to the code here was probably the most important thing in the learning process:

https://github.com/hpcc-systems/HPCC-Platform/compare/master...ghalliday:gsoc?expand=1

After getting some rough idea about what the different classes and functions used in this changes do, I made some changes to see if I can make the following steps working for gsoc1 to 6
* i) Return back all the subgraphs and attributes in outoflinesubgraphs and make it run for gsoc 1 to 6
* ii) Selectively inline childgraph only if all the activities in the childgraph can be inlined and make it work for gsoc 1 to 6

I am at the step ii) currently. Debugging for step 2 by hacking through the code made me learn a lot of things like these:
* The work is not done once canProcessInline is called in markHoist function. Later on generateGraph function is called in the ChildGraphBuilder class to actually build the corresponding child graphs
* Resourcing the graph essentially means that all optimizations have been applied to the graph. This term is used only in the context of child graph
* A child graph is resourced by calling the function getResourcedChildGraph
* The structure of a resourced graph looks something like this: it has children either with the activity no_subgraph or is an attribute. Each no_subgraph has a bunch of children each with activity no_setgraphresult
* The other activities of a child query is traversed recursively in canProcessInline function
* getgraphresult often (if not always) has a tag externalAtom which tell it that where should it look for the result
* Some ideas about what are the uses of classes like CHqlBoundExpr and HqlExpressionArray 
* Some ideas about the use of LINK and Owned classes
* ctx contains some important information about the current context. It can be updated with result of subgraph and later on retrieved.

I must also say that having a couple of deadlines towards my PhD (June 24: PhD proposal and July 18: Journal submission) mandated me to work towards it after 5 pm or so (other than weekends) and also kept me mentally occupied over two different things. Additionally, I could have been more expressive in stating my views so that my mentors had a better idea about where I am at. This is something I will consider as an important learning experience. Working remotely did not help either.

I appreciate the patience of my mentors in helping me throughout the project and their readiness to make changes accordingly. I would very much like to finish the project by working on it in my free time (given it is not cancelled). 
