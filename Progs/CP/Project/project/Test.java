package edu.cp.project;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Test {
	static int[][] mazeArray = { { 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			  { 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0 },
			  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			  { 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 },
			  { 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0 },
			  { 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0 },
			  { 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0 },
			  { 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0 },
			  { 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
			  { 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
			  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			  { 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0 },
			  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3 } };
	
	public static void main(String[] args) {
		
		String[] result1 = new String[MAZE_SIZE];
    	for (int i = 0; i < MAZE_SIZE; i++) {  //creating the board layout
			for (int j = 0; j < MAZE_SIZE; j++) {
				result1[i] = Arrays.toString(mazeArray[i]);
			}
    	}
    	String result = Arrays.toString(result1);
    	System.out.println(Arrays.toString(result1));
    	String[] str = result.split("], ");
    	str[0] = str[0].replaceAll("\\[", "").replaceAll("]]", "");
    	str = str[0].split(", ");
    	for (int i = 0; i < MAZE_SIZE; i++) {
    		mazeArray[0][i] = Integer.parseInt(str[i]);
    	}
    	
    	System.out.println(str[0]);
    	
	}
	static final int MAZE_SIZE = 13;
}
