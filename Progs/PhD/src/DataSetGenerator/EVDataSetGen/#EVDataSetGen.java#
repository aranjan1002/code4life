package edu.strippacking.DataGen;

import java.lang.Integer;
import java.lang.IllegalArgumentException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class EVDataSetGen {
    public static void main(String[] args) 
	throws IOException, IllegalArgumentException {
	new EVDataSetGen().generateData(args);
    }

    private void generateData(String[] args) 
	throws IOException, IllegalArgumentException {
	readArgs(args);
	int num_of_PHEV_cars = (totalNoOfCars * numOfPHEVIn100Cars) / 100;
	boolean first_line = true;
	writer = getWriter();
	for (int i = 1; i <= num_of_PHEV_cars; i++) {
	    int duration = rand.nextInt(MAX_CHARGE_TIME_PHEV) + 1;
	    writer.write(String.format(OUT_STR_FORMAT,
				       i,
				       PHEV_POWER,
				       duration));
	    if (first_line) {
		// write total time as well
		writer.write(Integer.toString(totalTime)); 
		first_line = false;
	    } 
	    writer.newLine();
	}
	for (int i = num_of_PHEV_cars + 1; i <= totalNoOfCars; i++) {
	    int duration = rand.nextInt(MAX_CHARGE_TIME_EV) + 1;
	    writer.write(String.format(OUT_STR_FORMAT,
					 i,
					 EV_POWER,
					 duration));
	    if (first_line) {
                // write total time as well
                writer.write(Integer.toString(totalTime));
                first_line = false;
            }
	    writer.newLine();
	}
	writer.close();
    }

    private void readArgs(String[] args) 
	throws IllegalArgumentException {
	if (args.length != 4) {
	    throw new IllegalArgumentException(USAGE_ERR_MSG);
	}
	totalNoOfCars = Integer.parseInt(args[0]);
	numOfPHEVIn100Cars = Integer.parseInt(args[1]);
	totalTime = Integer.parseInt(args[2]);
	outputFileName = args[3];
    }

    // assumes that outputFileName is initiallized with right name
    private BufferedWriter getWriter() 
	throws IOException {
	OutputStreamWriter ow =
	    new OutputStreamWriter(
				   new FileOutputStream(outputFileName));
	return new BufferedWriter(ow);
    }

    private int totalNoOfCars = 0;
    private int totalTime = 0;
    private int numOfPHEVIn100Cars = 0;
    private String outputFileName = new String();
    private Random rand = new Random();
    private BufferedWriter writer;

    private static final int PHEV_POWER = 22;
    private static final int EV_POWER = 38;
    private static final int MAX_CHARGE_TIME_PHEV = 33;
    private static final int MAX_CHARGE_TIME_EV = 61;
    private static final String OUT_STR_FORMAT = "%d,%d,%d,";
    private static final String USAGE_ERR_MSG = "Usage: \n" +
	"java EVDataSetGen <TotalNoOfCars> <Number of PHEV in 100 cars> " +
	"<total time> <output file name>";
}
