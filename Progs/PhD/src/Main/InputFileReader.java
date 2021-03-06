package edu.strippacking;

import java.io.*;
import java.util.ArrayList;

class InputFileReader {
    public static ArrayList<Job> readFile(String fileName)
	throws IOException {
        ArrayList<Job> jobs = new ArrayList<Job>();
        BufferedReader br = getBufferedReader(fileName);
        jobs.add(readFirstLine(br));
	String line;
        while ((line = br.readLine()) != null) {
            Job j = readJob(line, false);
	    if (j != null) {
		jobs.add(j);
	    }
	}
	return jobs;
    }    

    public static ArrayList<Job> readFile
	(String fileName, double expectedFractionOfPreemption) 
    throws IOException {
	ArrayList<Job> jobs = new ArrayList<Job>();
	BufferedReader br = getBufferedReader(fileName);
	jobs.add(readFirstLine(br, expectedFractionOfPreemption));
	String line;
	while ((line = br.readLine()) != null) {
	    Job j = readJob(line, false);
	    if (j != null) {
		boolean isPreemptive = 
			decidePreemptive(expectedFractionOfPreemption);
		j.isPreemptable = isPreemptive;
		jobs.add(j);
	    }
	}	
	return jobs;
    }

    private static Job readFirstLine(BufferedReader br, 
				     double expectedFractionOfPreemption) 
	throws IOException {
	String line = br.readLine();
        if (line.contains("eight")) {
            // contains column names like width or height
            line = br.readLine();
        }
        Job j = readJob(line, true);
	if (j != null) {
                boolean isPreemptive =
		    decidePreemptive(expectedFractionOfPreemption);
                j.isPreemptable = isPreemptive;
	}
	else {
	    throw new IOException("Empty file");
	}
	return j;
    }

    private static Job readFirstLine(BufferedReader br) 
	throws IOException {
	return readFirstLine(br, 0);
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
	Job j = new Job(h, w);
	if (split.length == 5) {
	    j.isPreemptable = Boolean.parseBoolean(split[4]);
	}
	if (firstLine == true) {
	    Constants.W = Double.parseDouble(split[3]);
	}
	return j;
    }

    private static boolean decidePreemptive(double threshold) {
        double rand = Math.random();
        if (rand <= threshold) {
            return true;
        }
        return false;
    }

    private boolean tempBool = false;
}
