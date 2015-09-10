package edu.strippacking.NFDH;

import java.util.ArrayList;
import java.io.IOException;

public class RunNFDH {
    public static void main(String[] args) 
	throws IllegalArgumentException, IOException {
	new RunNFDH().start(args);
    }

    public void start(String[] args) 
	throws IllegalArgumentException, IOException {
	readArgs(args);
	rectangleList = InputFileReader.readFile(inputFile);
	double res = npNFDH.applyNFDHAndGetHeight(rectangleList);
	System.out.print(rectangleList.size() + " " +
			 res + " ");
    }

    private void readArgs(String[] args) 
	throws IllegalArgumentException {
	if (args.length != 1) {
	    throw new IllegalArgumentException("Expected 1 arg");
	} else {
	    inputFile = args[0];
	}
    }

    private String inputFile = new String();
    private ArrayList<Rectangle> rectangleList =
	new ArrayList<Rectangle>();
    NonPreemptiveNFDH npNFDH = new NonPreemptiveNFDH();
}