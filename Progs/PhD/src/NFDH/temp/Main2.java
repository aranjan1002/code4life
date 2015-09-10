package edu.strippacking.NFDH;

import java.util.ArrayList;
import java.util.Collections;
import java.lang.Integer;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.*;

public class Main2 {
    public static void main(String[] args) 
	throws IOException {
	new Main2().start(args);
    }
    
    public void start(String[] args) 
	throws IOException {
	readInput(args);
	System.out.println(args[0]);
	//System.out.println("FileName\tNon-Pre\t\tPre\t\tPreDW\t\tPreIW\t\tMixed");
	File dir = new File(myDirectoryPath);
	double pr_sum = 0;
	int dir_size = dir.listFiles().length;
	double[] h_array_nfdh = new double[dir_size];
	double[] h_array_dw_nfdh = new double[dir_size];
	double[] h_array_iw_nfdh = new double[dir_size];
	double[] h_array_pre_nfdh = new double[dir_size];
	double[] h_array_pre_dw_nfdh = new double[dir_size];
	double[] h_array_pre_iw_nfdh = new double[dir_size];
	double[] h_array_mixed_nfdh = new double[dir_size];
	for (File file : dir.listFiles()) {
	    double pr = 0;
	    String fileName = file.getName();
	    if (fileName.endsWith("csv")) {
		try {
		    readInputAndFillRectangleList(file.getPath());
		    // System.out.print(fileName + "\t");
		    h_array_nfdh[fileCount] = nonPreemptiveNFDH.
			applyNFDHAndGetHeight(rectangleList);
                    h_array_dw_nfdh[fileCount] = nonPreemptiveNFDH.
                        applyNFDHDWAndGetHeight(rectangleList);
                    h_array_iw_nfdh[fileCount] = nonPreemptiveNFDH.
                        applyNFDHIWAndGetHeight(rectangleList);
                    h_array_pre_nfdh[fileCount] = preemptiveNFDH.
                        applyNFDHAndGetHeight(rectangleList);
                    h_array_pre_dw_nfdh[fileCount] = preemptiveNFDH.
                        applyNFDHDWAndGetHeight(rectangleList);
                    h_array_pre_iw_nfdh[fileCount] = preemptiveNFDH.
                        applyNFDHIWAndGetHeight(rectangleList);
                    h_array_mixed_nfdh[fileCount] = mixedNFDH.
                        applyNFDHAndGetHeight(rectangleList);

		    // System.out.print(String.format("%.6f\t", 
		    // 				   h_array_nfdh[fileCount]));
		    // System.out.print(" " + String.format("%.6f\t", 
		    // 					 h_array_nfdh[fileCount] /
		    // optHeight));
		    // System.out.print(String.format("%.6f\t",
		    // 				   h_array_pre_nfdh[fileCount]));
		    // System.out.print(String.format("%.6f\t",
		    // 				   h_array_pre_dw_nfdh[fileCount]));
		    // System.out.print(String.format("%.6f\t",
		    // 				   h_array_pre_iw_nfdh[fileCount]));
		    // System.out.print(String.format("%.6f\t",
		    //h_array_mixed_nfdh[fileCount]));
		    if (h_array_pre_dw_nfdh[fileCount] != 
			h_array_pre_iw_nfdh[fileCount]) {
			System.out.println("Check! " + fileName);
		    }
		    // System.out.println();
		    
		    prNfdhSum += h_array_nfdh[fileCount] / optHeight;
		    // System.out.println(h_array_nfdh[fileCount] / optHeight);
		    prDwNfdhSum += h_array_dw_nfdh[fileCount] / optHeight;
		    prIwNfdhSum += h_array_dw_nfdh[fileCount] / optHeight;
		    prPreNfdhSum += h_array_pre_nfdh[fileCount] / optHeight;
		    prPreDwNfdhSum += h_array_pre_dw_nfdh[fileCount] / optHeight;
		    prPreIwNfdhSum += h_array_pre_iw_nfdh[fileCount] / optHeight;
		    prMixedNfdhSum += h_array_mixed_nfdh[fileCount] / optHeight;
		    fileCount++;
		}
		catch (Exception e) {
		    System.out.println("Error in file: " + fileName);
		    e.printStackTrace();
		}
	    }
	}

	if (fileCount > 0) {
	    prNfdh = prNfdhSum / fileCount;
	    prDwNfdh = prDwNfdhSum / fileCount;
	    prIwNfdh = prIwNfdhSum / fileCount;
	    prPreNfdh = prPreNfdhSum / fileCount;
	    prPreDwNfdh = prPreDwNfdhSum / fileCount;
	    prPreIwNfdh = prPreIwNfdhSum / fileCount;
	    prMixedNfdh = prMixedNfdhSum / fileCount;
	    // System.out.println(prNfdh + " " + prNfdhSum + " " + fileCount);
	    System.out.print("PR/stdevPR\t");
	    printResult(prNfdh, getStdDev(h_array_nfdh, prNfdh)); 
	    printResult(prDwNfdh, getStdDev(h_array_dw_nfdh, prDwNfdh));
	    printResult(prIwNfdh, getStdDev(h_array_iw_nfdh, prIwNfdh));
	    printResult(prPreNfdh, getStdDev(h_array_pre_nfdh, prPreNfdh));
	    printResult(prPreDwNfdh, getStdDev(h_array_pre_dw_nfdh, prPreDwNfdh));
	    printResult(prPreIwNfdh, getStdDev(h_array_pre_iw_nfdh, prPreIwNfdh));
	    printResult(prMixedNfdh, getStdDev(h_array_mixed_nfdh,  prMixedNfdh));
	    System.out.println();
	    System.out.println("Total files: " + fileCount);
	}
    }

    public double getStdDev(double[] array, double mean) {
	double sum = 0;
	for (int i = 0; i < fileCount; i++) {
	    double pr = array[i] / optHeight;
	    sum += Math.pow(pr - mean, 2);
	}
	return Math.sqrt(sum / fileCount);
    }

    public void readInputAndFillRectangleList(String fileName) 
	throws IOException {
	id = 0;
	InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream(fileName));
        BufferedReader br = new BufferedReader(is);
	String line;
	rectangleList.clear();
	boolean firstRow = true;
	line = br.readLine(); // ignore the first line
	while ((line = br.readLine()) != null) {
	    //System.out.println(line);
	    line = line.trim().replaceAll(" +", ",");
	    //System.out.println(line);
	    if (line.startsWith(",")) {
		continue;
	    }
	    String[] split = line.split(",");
	    double h = Double.parseDouble(split[1]);
	    double w = Double.parseDouble(split[2]);
	    boolean isPreemptive = decidePreemptive();
	    Rectangle rect = new Rectangle(id++, w, h, isPreemptive);
	    rectangleList.add(rect);
	    if (firstRow == true) {
                Constants.W = Double.parseDouble(split[3]);
                firstRow = false;
                continue;
            }
	    //System.out.println(rect);
	}
	//System.out.println(rectangleList.size() + " rectangles in the input");
    }

    private void readInput(String[] args) 
	throws IOException {
	myDirectoryPath = args[0];
	optHeight = Double.parseDouble(args[1]);
    }

    private boolean decidePreemptive() {
	double rand = Math.random();
	if (rand < threshold) {
	    return true;
	}
	return false;
    }

    private void printResult(double pr, double prStdDev) {
	System.out.print(String.format("%.3f %.3f\t", pr, prStdDev));
    }
    
    private ArrayList<Rectangle> rectangleList =
	new ArrayList<Rectangle>();
    private NonPreemptiveNFDH nonPreemptiveNFDH = new NonPreemptiveNFDH();
    private PreemptiveNFDH preemptiveNFDH = new PreemptiveNFDH();
    private MixedNFDH mixedNFDH = new MixedNFDH();
    private double optHeight;
    private String myDirectoryPath;
    private double prNfdhSum, prDwNfdhSum, prIwNfdhSum,
	prPreNfdhSum, prPreDwNfdhSum, 
	prPreIwNfdhSum, prMixedNfdhSum;
    private double prNfdh, prDwNfdh, prIwNfdh,
	prPreNfdh, prPreDwNfdh, prPreIwNfdh, prMixedNfdh;
    private int fileCount = 0;
    private int id;

    private final double threshold = 0.5;
    private final String TAB = "\t";
}
