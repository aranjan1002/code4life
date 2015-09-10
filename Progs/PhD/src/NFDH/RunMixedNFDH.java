package edu.strippacking.NFDH;

import java.util.ArrayList;
import java.io.IOException;
import java.lang.Double;

public class RunMixedNFDH {
    public static void main(String[] args)
        throws IllegalArgumentException, IOException {
        new RunMixedNFDH().start(args);
    }

    public void start(String[] args)
        throws IllegalArgumentException, IOException {
        readArgs(args);
        rectangleList = InputFileReader.
	    readFile(inputFile,
		     expectedPreemptiveFraction);
        double res = mixedNFDH.getHeightAfterPacking(rectangleList);
        System.out.print(rectangleList.size() + " " +
			 res + " ");
    }

    private void readArgs(String[] args)
        throws IllegalArgumentException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 args");
        } else {
            inputFile = args[0];
	    expectedPreemptiveFraction = 
		Double.parseDouble(args[1]);
        }
    }

    private String inputFile = new String();
    private ArrayList<Rectangle> rectangleList =
        new ArrayList<Rectangle>();
    private MixedNFDH mixedNFDH = new MixedNFDH();
    private double expectedPreemptiveFraction;
}
