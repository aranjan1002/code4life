package edu.strippacking.DataSetGenerator;

import java.lang.*;
import java.io.*;
import java.util.ArrayList;

/* This class generates datasets for testing algorithms designed for 
   instances where power demands are a mix of preemptable and 
   non preemptable ones. More precisely, it generates a collection 
   of power demands based on a sample usage pattern of a household or 
   factory etc. This usage pattern is given an input file as explained below.

   The input file contains six columns in csv format: 
   Appliances, Height, Width, StripWidth, Total, Preemptable.
   Appliances: Appliance name like oven, fan etc..
   Height: The power requirement of the appliance
   Width: The duration for which the appliace should run, these can be a range 
    like 20-40. The data generated would have the duration for this appliance 
    between 20 and 40 units of time.
   StripWidth: The total duration. Only the first row is read.
   Total: Number of instances of appliances. This can be a range too. 
   Preemptable: Indicates whether the demand is preemptable or not.

   The output file(s) contain five columns:
   Reference: The serial number of the row
   Height: Power requirement of the demand
   Width: Duration of the demand
   Stripwidth: The total duration
   Preemptable: Whether the demand is preemptable or not
   
   Parameters to this class:
   InputFileName: the input file name
   Number of Outputs: the number of instances of datasets to be generated.
   OutputFileName: the starting letters of the output file name(s). "_i" is 
    added to the name where i indicates the number of dataset (e.g. _5).
*/

public class MixedDataSetGen {
    public static void main(String[] args) 
    throws IOException {
	new MixedDataSetGen().start(args);
    }

    private void start(String[] args) 
    throws IOException {
	readArgs(args);
	readInputFile();
	generateOutputFiles();
    }

    private void readArgs(String[] args) {
	if (args.length != 4) {
            throw new IllegalArgumentException(MSG);
        }
	fileName = args[0];
	total = Integer.parseInt(args[1]);
	outputFileName = args[2];
	instances = Integer.parseInt(args[3]);
    }
    
    private void readInputFile() 
    throws IOException {
	BufferedReader br = getBufferedReader();
	br.readLine(); // skip first line with column names
	// this line contains stripwidth
	applianceList.add(readFirstLine(br.readLine()));
	while ((line = br.readLine()) != null) {
	    Appliance app = new Appliance(line);
	    applianceList.add(app);
	}
    }

    private BufferedReader getBufferedReader() 
	throws IOException {
	InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream(fileName));
        BufferedReader br = new BufferedReader(is);
        return br;
    }
    
    private Appliance readFirstLine(String line) {
	String[] split = line.split(",");
	totalDuration = split[3];
	return new Appliance(line);
    }

    private void generateOutputFiles() 
    throws IOException {
        for (int i = 0; i < total; i++) {
	    BufferedWriter bw = getWriter(outputFileName + "_" + i);
	    bw.write(head);
	    bw.newLine();
	    for (int j = 0; j < instances; j++) {
		writeData(bw);
	    }
	    bw.close();
	    wroteFirstLine = false;
	    reference = 1;
        }
    }

    private BufferedWriter getWriter(String fileName)
        throws IOException {
        OutputStreamWriter ow =
            new OutputStreamWriter(
                                   new FileOutputStream(fileName));
        return new BufferedWriter(ow);
    }

    private void writeData(BufferedWriter bw) 
    throws IOException {
	String[] sample;
	for (Appliance app : applianceList) {
	    if (wroteFirstLine == false) {
		sample = app.getSample(totalDuration, reference);
		wroteFirstLine = true;
	    } else {
		sample = app.getSample(reference);
	    }
	    reference += sample.length;
	    writeSample(bw, sample);
	}
    }

    private void writeSample(BufferedWriter bw, String[] sample) 
    throws IOException {
	for (String s : sample) {
	    bw.write(s);
	    bw.newLine();
	}
    }

    private String fileName, outputFileName, line, totalDuration;
    private int total, instances, reference = 1;
    private ArrayList<Appliance> applianceList = new ArrayList<Appliance>();
    private boolean wroteFirstLine = false;

    private static final String MSG = "Usage: \n" +
	"java edu.strippacking.DataSetGenerator.MixedDataSetGen " +
	"<InputFileName> <Number of Output Files> <OutputFileName>" +
	"<Number of instances in each output file>";
     private static final String head = "Reference,height,width,stripwidth," +
	 "preemptable";
}