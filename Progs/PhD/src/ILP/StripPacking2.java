import ilog.concert.*;
import ilog.cplex.*;

import java.io.*;
import java.lang.*;

public class StripPacking2 {
    public static final int NUM_RECT = 25;
    public static final int MAX_WIDTH = 100;
    public static final String INPUT_FILENAME = "Input.txt";

    public static void main(String[] args) throws IloException, IOException {
        new StripPacking2().start();
    }

    void start() throws IloException, IOException {
        IloCplex cplex = new IloCplex();
        readInput(INPUT_FILENAME);

        IloIntVar[][] beta = new IloIntVar[MAX_WIDTH][NUM_RECT];

        for (int i = 0; i < MAX_WIDTH; i++) {
            beta[i] = cplex.boolVarArray(NUM_RECT);
        }

        // constraint: total number of slots alloted to a
        // rectangle equals its width
        for (int i = 0; i < NUM_RECT; i++) {
            IloLinearNumExpr expr = cplex.linearNumExpr();
            for (int j = 0; j < MAX_WIDTH; j++) {
                expr.addTerm(1, beta[j][i]);
            }
            cplex.addEq(expr, height[i]);
        }

        // constraint: overall height
        IloIntVar H = cplex.intVar(0, Integer.MAX_VALUE);
        for (int i = 0; i < MAX_WIDTH; i++) {
            cplex.addGe(H,
                        cplex.scalProd(beta[i],
                                       height));
        }

        // objective function
        cplex.addMinimize(H);

        // print result
        if (cplex.solve()) {
            System.out.println("Minimum Height - " + H);
        }
    }

    //static void populateByRow(IloMPModeler model,
    public void readInput(String inputFile)
        throws IOException {
         InputStreamReader is =
             new InputStreamReader(
                                   new FileInputStream(inputFile));
         BufferedReader br = new BufferedReader(is);
         String line;
         line = br.readLine(); // ignore the first line
         int cnt = 0;
         while ((line = br.readLine()) != null) {
             //System.out.println(line);
             if (line.startsWith(",")) {
                 continue;
             }
             String[] split = line.split(",");
             height[cnt] = Integer.parseInt(split[1]);
             width[cnt++] = Integer.parseInt(split[2]);
	     System.out.println(height[cnt-1] + " " + width[cnt - 1]);
         }
    }

    int[] height = new int[NUM_RECT];
    int[] width = new int[NUM_RECT];
}


