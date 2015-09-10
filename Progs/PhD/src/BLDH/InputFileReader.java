package edu.strippacking;

import java.io.*;
import java.util.ArrayList;

public class InputFileReader {
    public static ArrayList<Job> readFile(String fileName)
	throws IOException {
        ArrayList<Job> rectList = new ArrayList<Job>();
        BufferedReader br = getBufferedReader(fileName);
        rectList.add(readFirstLine(br));
	String line;
        while ((line = br.readLine()) != null) {
            Job rect = readJob(line, false);
	    if (rect != null) {
		rectList.add(rect);
	    }
	}
	return rectList;
    }    

    public static ArrayList<Job> readFile
	(String fileName, double expectedFractionOfPreemption) 
    throws IOException {
	ArrayList<Job> rectList = new ArrayList<Job>();
	BufferedReader br = getBufferedReader(fileName);
	rectList.add(readFirstLine(br));
	String line;
	while ((line = br.readLine()) != null) {
	    Job rect = readJob(line, false);
	    if (rect != null) {
		boolean isPreemptive = 
			decidePreemptive(expectedFractionOfPreemption);
		rect.setPreemptive(isPreemptive);
		rectList.add(rect);
	    }
	}	
	return rectList;
    }

    private static Job readFirstLine(BufferedReader br) 
	throws IOException {
	String line = br.readLine();
	if (line.contains("eight")) {
	    // contains column names like width or height
	    line = br.readLine();
	}
	Job rect = readJob(line, true);
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

    private static Job readJob(String line, 
			       boolean firstLine) {
	line = line.trim().replaceAll(" +", ",");
	//System.out.println(line);                                     
	if (line.startsWith(",")) {
	    return null;
	}
	String[] split = line.split(",");
	double h = Double.parseDouble(split[1]);
	double w = Double.parseDouble(split[2]);
	Job rect = new Job(w, h);
	if (firstLine == true) {
	    Constants.D = Double.parseDouble(split[3]);
	    System.out.println("D=" + Constants.getD());
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
