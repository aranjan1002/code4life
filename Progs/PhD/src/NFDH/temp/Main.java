package edu.strippacking.NFDH;

import java.util.ArrayList;
import java.util.Collections;
import java.lang.Integer;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.*;

public class Main {
    public static void main(String[] args) 
	throws IOException {
	if (args.length != 1) {
	    System.err.println("Error: required one parameter " +
			       "of dir path to input files");
	}
	new Main().start(args);
    }
    
    public void start(String[] args) 
	throws IOException {
	System.out.println("FileName\tNon-Pre\t\tPre\t\tPreDW" +
			   "\t\tPreIW\t\tMixed");
	File dir = new File(args[0]);
	
	for (File file : dir.listFiles()) {
	    String file_name = file.getName();
	    double opt_height = Constants.getOptHeight(file_name);
	    try {
		if (opt_height == -1) {
		    throw new Exception("Opt height unknown");
		}
		readInputFile(file.getPath());
		
		System.out.print(file_name + "\t");
		printResult(nonPreemptiveNFDH.
			    applyNFDHAndGetHeight(rectangleList),
			    opt_height);
		printResult(preemptiveNFDH.
			    applyNFDHAndGetHeight(rectangleList),
			    opt_height);
		printResult(preemptiveNFDH.
			    applyNFDHDWAndGetHeight(rectangleList),
			    opt_height);
		printResult(preemptiveNFDH.
			    applyNFDHIWAndGetHeight(rectangleList),
			    opt_height);
		printResult(mixedNFDH.
			    applyNFDHAndGetHeight(rectangleList),
			    opt_height);
		    
	    }
	    catch (Exception e) {
		System.out.println("Error in file: " + file_name 
				   + e.getMessage());
		e.printStackTrace();
	    }
	}
    }

    public void readInputFile(String file_name) 
	throws IOException {
	id = 0;
	InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream(file_name));
        BufferedReader br = new BufferedReader(is);
	String line;
	rectangleList.clear();
	boolean first_row = true;
	line = br.readLine(); // ignore the first line
	while ((line = br.readLine()) != null) {
	    //System.out.println(line);
	    line = line.trim().replaceAll(" +", ",");
	    if (line.startsWith(",")) {
		continue;
	    }
	    String[] split = line.split(",");
	    if (first_row == true) {
		Constants.W = Double.parseDouble(split[3]);
		first_row = false;
		continue;
	    }
	    double h = Double.parseDouble(split[1]);
	    double w = Double.parseDouble(split[2]);
	    boolean isPreemptive = decidePreemptive();
	    Rectangle rect = new Rectangle(id++, w, h, isPreemptive);
	    rectangleList.add(rect);
	}
    }

    private boolean decidePreemptive() {
	double rand = Math.random();
	if (rand < threshold) {
	    return true;
	}
	return false;
    }

    private void printResult(double height, double opt_height) {
	System.out.print(String.format("%.3f %.3f\t", 
				       height, 
				       height / opt_height));
    }
    
    private ArrayList<Rectangle> rectangleList =
	new ArrayList<Rectangle>();
    private NonPreemptiveNFDH nonPreemptiveNFDH = new NonPreemptiveNFDH();
    private PreemptiveNFDH preemptiveNFDH = new PreemptiveNFDH();
    private MixedNFDH mixedNFDH = new MixedNFDH();
    private int id;

    private final double threshold = 0.5;
}
