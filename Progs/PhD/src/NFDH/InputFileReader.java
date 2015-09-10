package edu.strippacking.NFDH;

import java.io.*;
import java.util.ArrayList;

class InputFileReader {
    public static ArrayList<Rectangle> readFile(String fileName)
	throws IOException {
        ArrayList<Rectangle> rectList = new ArrayList<Rectangle>();
        BufferedReader br = getBufferedReader(fileName);
        rectList.add(readFirstLine(br));
	String line;
        while ((line = br.readLine()) != null) {
            Rectangle rect = readRectangle(line, false);
	    if (rect != null) {
		rectList.add(rect);
	    }
	}
	return rectList;
    }    

    public static ArrayList<Rectangle> readFile
	(String fileName, double expectedFractionOfPreemption) 
    throws IOException {
	ArrayList<Rectangle> rectList = new ArrayList<Rectangle>();
	BufferedReader br = getBufferedReader(fileName);
	rectList.add(readFirstLine(br));
	String line;
	while ((line = br.readLine()) != null) {
	    Rectangle rect = readRectangle(line, false);
	    if (rect != null) {
		boolean isPreemptive = 
			decidePreemptive(expectedFractionOfPreemption);
		rect.setPreemptive(isPreemptive);
		rectList.add(rect);
	    }
	}	
	return rectList;
    }

    private static Rectangle readFirstLine(BufferedReader br) 
	throws IOException {
	String line = br.readLine();
	if (line.contains("eight")) {
	    // contains column names like width or height
	    line = br.readLine();
	}
	Rectangle rect = readRectangle(line, true);
	if (rect == null) {
	    throw new IOException("Empty file");
	}
	return rect;
    }

    private static BufferedReader getBufferedReader(String fileName) 
	throws IOException {
	InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream(fileName));
        BufferedReader br = new BufferedReader(is);
	return br;
    }

    private static Rectangle readRectangle(String line, 
					   boolean firstLine) {
	line = line.trim().replaceAll(" +", ",");
	//System.out.println(line);                                     
	if (line.startsWith(",")) {
	    return null;
	}
	String[] split = line.split(",");
	double h = Double.parseDouble(split[1]);
	double w = Double.parseDouble(split[2]);
	Rectangle rect = new Rectangle(w, h);
	if (firstLine == true) {
	    Constants.W = Double.parseDouble(split[3]);
	}
	return rect;
    }

    private static boolean decidePreemptive(double threshold) {
        double rand = Math.random();
        if (rand <= threshold) {
            return true;
        }
        return false;
    }
}
