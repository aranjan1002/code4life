package pa1;

import pa.InputFileReader;
import pa.ThreeIntegers;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Pa1_q3 {
    private static int nodesCount = 0;

    public static void main(String[] args) throws IOException {
        ArrayList<ThreeIntegers> input = new InputFileReader().
            readThreeIntegersPerLine(args[0]);
	int vertices_count = new InputFileReader().
	    readTwoIntegersFirstLine(args[0]).int1;
	Collections.sort(input, new Comparator<ThreeIntegers>() {
		public int compare(ThreeIntegers a, ThreeIntegers b) {
		    return a.int3 - b.int3;
		}
	    });
	ArrayList<Integer> visited_vertices = new ArrayList<Integer>();
	visited_vertices.add(input.get(0).int1);
	int total_cost = 0;
	while(visited_vertices.size() < vertices_count) {
	    int i = 0;
	    for (i=0; i < input.size(); i++) {
		ThreeIntegers temp = input.get(i);
		Integer u = new Integer(temp.int1);
		Integer v = new Integer(temp.int2);
		int edge_cost = temp.int3;
		
		if (visited_vertices.contains(u) ||
		    visited_vertices.contains(v)) {
		    input.remove(temp);
		    if ((visited_vertices.contains(u) &&
			 visited_vertices.contains(v) == false)) {
			visited_vertices.add(v);
			total_cost += edge_cost;
			break;
		    }
		    else if(visited_vertices.contains(v) &&
			    visited_vertices.contains(u) == false) {
			visited_vertices.add(u);
			total_cost += edge_cost;
			break;
		    }
		    System.out.println(visited_vertices.size());
		}
	    }     
	}
	System.out.println(total_cost);
    }
}