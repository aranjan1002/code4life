/*
Generated data sets with a fraction having widths just over half
of maximum width (between 51 to 60 percent of overall width)
Inputs
args[0] - fraction of rectangles whose widths will be just over half
args[1] - total num of rects
args[2] - overall width
args[3] - aspect ratio between width and height: 1/a.r. <= h/w <= a.r.\
args[4] - number of datasets (output files) to be produced
args[5] - path where the output files are to be generated
*/

package edu.strippacking.NFDH;

import java.util.Random;
import java.io.*;

public class DataGen {
    public static void main(String[] args) 
	throws IOException {
	new DataGen().start(args);
    }

    public void start(String[] args) 
    throws IOException {
	readInput(args);
	for (int i = 0; i < numDataSets; i++) {
	    OutputStreamWriter ow = 
		new OutputStreamWriter(
				       new FileOutputStream(outPath + i + DOTCSV));
	    BufferedWriter bw = 
		new BufferedWriter(ow);
	    bw.write("index,height,width\n");
	    int num_rect_just_over_half = (int) (numRect * frac);
	    int j;
	    for (j = 0; j < num_rect_just_over_half; j++) {
		double width = stripWidth / 2 + 
		    getDoubleBetween(0, stripWidth * 0.1);
		double height = getDoubleBetween(width / aspectRatio,
						 aspectRatio * width);
		bw.write(j + COMMA + height + COMMA + width);
		if (j == 0) {
		    bw.write(COMMA + stripWidth + NEWLINE);
		} else {
		    bw.write(NEWLINE);
		}
	    }
	    for (int k = j; k < numRect; k++) {
		double width = getDoubleBetween(0, stripWidth);
		double height = getDoubleBetween(width / aspectRatio,
						 aspectRatio * width);

		bw.write(k + COMMA + height + COMMA + width + NEWLINE);
	    }
	    bw.close();
	}

    }

    private void readInput(String[] args) {
	frac = Double.parseDouble(args[0]);
        numRect = Integer.parseInt(args[1]);
        stripWidth = Double.parseDouble(args[2]);
        aspectRatio = Integer.parseInt(args[3]);
        numDataSets = Integer.parseInt(args[4]);
	outPath = args[5] + SLASHOUT;
    }

    // Returns a random number between start inclusive and end exclusive
    private double getDoubleBetween(double start, double end) {
	return start + (rand.nextDouble() * (end - start));
    }

    private int numRect, w, aspectRatio, numDataSets;
    private double stripWidth, frac;
    private Random rand = new Random();
    private String outPath;

    private final String SLASHOUT = "/out";
    private final String COMMA = ",";
    private final String NEWLINE = "\n";
    private final String DOTCSV = ".csv";
}