Questions regarding code generator
* The functions which start with doBuild.. are they supposed to return the value of the expression? Or the c++ translation?
* I need to look back at it to come up with a better framed question. But as of now this is the question. Where is the instance of ChildGraphExprBuilder lost before generatedGraph function of ChildGraphBuilder is called? I needed that instance to assign externalAtom attribute to the getGraphResult operator.
* The following questions are related to xml file (gsoc4.xml) generated with gsoc4 using branch childquery-2.6.4 with option optimizeInlineOperations
  1. There is a child graph inside a child graph (node id = 30). It looks like it was not needed to be created. It would have fine if it was within the outside child graph. From what I understand child graph is better than inline because they are resourced. I wonder if a child graph inside a child graph would serve any purpose. 
  2. I will try to make an interpretation of the graphical representation (gsoc4.png file) and the xml. With this some questions will come up
    * Graph name="graph1" type="activities"- I suppose this is the outer blue box in the graph. In that case what is graph wfid="1"? 
    * I see that node id = 1 has an att element inside but no attribute is mentioned. It ends with </att>. Does it need to be there?
    * What do I call the the graph inside node = 1? Is it called a child graph? If so, what is the graph corresponding to the child query is called?
    * Am I correct with this generic representation of any graph?
```
<Graphs>
 <Graph name=..>
  <xgmml>
   <graph wfid=..>
    <edge> and <att>
    .
    .
    .
    <node id=...>
     <att>
      <graph>
       <edge>
       <node id (an activity)>
        <att> (details about activity)
        .
        .
        .
       </node>
       <node id (another activity)>
       .
       .
       .
       </node>
      </graph>
     </att>
    </node>
    <node id=..>
     <att..>
      <graph child=>
       <edge..>
       .
       .
       .
       <node id (an activity)>
        <att> (details about activity)
        .
        .
        .
       </node>
       <node id (another activity)>
        .
        .
        .
       </node>
      </graph>
     </att>
    </node>
   </graph>
  </xgmml>
 <Graph>
<Graphs>
```
* I think I was not able to use graph control in firefox. Though it was generating a graph it was not the same as Jamie saw in his own instance. Is there some instuction on how to use it with firefox?
* I think this was talked about before sometime, but I did not quite get the idea. What is an active dataset?
* It will be a good idea to have some more information about the content of the variable ctx. I suppose it contains information like the graphs to be evaluated, the expression already evaluated etc. Some small documentation about this would help.
* A rather basic question. I am not sure what does ROW operation do. In the documentation it says it creates a single data record. Does it mean it creates a single record with one column? Or something else?
* When I dump an Ihqlexpression, I see a lot of information which I cannot quite understand. What does %e %c or %t represent on the left side of each line? Example:
It is kind of obvious that they stand for element, constant and type. But what are these things really? I suppose element is similar to no_operator but not exactly.
```
00000177 2015-07-20 19:37:50 14131 14131 %t11 = type table(%t10);
0000017A 2015-07-20 19:37:50 14131 14131 %e14 = record(%e2,%e13);
0000018A 2015-07-20 19:37:50 14131 14131 %c35 = constant 4 : int8;
```
* I see that there a few terms which mean the same thing. For examle dataset and recordset and table (I guess?), field and column, operation and activity. If possible, it would be better address them as one unique term.
* It would probably be a good idea to generate an error if an option is not available but used in command line. For example if I use something like -fanyrandomthing, it does not generate an error.
* Regarding the following function, I think it evaluates the result of expr. Why is it called TempExpr? What is temporary about it?
```
buildTempExpr(BuildCtx & ctx, IHqlExpression * expr, CHqlBoundExpr & tgt, ExpressionFormat format)
```

