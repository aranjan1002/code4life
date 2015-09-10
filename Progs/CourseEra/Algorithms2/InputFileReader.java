package pa;

import java.io.*;
import java.lang.*;
import java.util.ArrayList;

public class InputFileReader {
    // Reads two integers per line, assumes first line containt
    // total number of lines.
    public ArrayList<TwoIntegers> readTwoIntegersPerLine
	(String fileName)
	throws IOException {
	ArrayList<TwoIntegers> list = new ArrayList<TwoIntegers>();
	BufferedReader br = getBufferedReader(fileName);
	br.readLine(); // ignore the first line which contains the total
	// number of lines
	String line;
	while ((line = br.readLine()) != null) {
	    TwoIntegers temp = new TwoIntegers();
	    String[] split = line.split(" ");
	    temp.int1 = Integer.parseInt(split[0]);
	    temp.int2 = Integer.parseInt(split[1]);
	    list.add(temp);
	}
	br.close();
	return list;
    }

    public ArrayList<ThreeIntegers> readThreeIntegersPerLine
        (String fileName)
        throws IOException {
        ArrayList<ThreeIntegers> list = new ArrayList<ThreeIntegers>();
        BufferedReader br = getBufferedReader(fileName);
        br.readLine(); // ignore the first line which contains the total
        // number of lines
        String line;
        while ((line = br.readLine()) != null) {
            ThreeIntegers temp = new ThreeIntegers();
            String[] split = line.split(" ");
            temp.int1 = Integer.parseInt(split[0]);
            temp.int2 = Integer.parseInt(split[1]);
	    temp.int3 = Integer.parseInt(split[2]);
            list.add(temp);
        }
	br.close();
        return list;
    }

    public TwoIntegers readTwoIntegersFirstLine(String fileName) 
	throws IOException {
	BufferedReader br = getBufferedReader(fileName);
        String line = br.readLine();
	TwoIntegers temp = new TwoIntegers();
	String[] split = line.split(" ");
	temp.int1 = Integer.parseInt(split[0]);
	temp.int2 = Integer.parseInt(split[1]);
        br.close();
	return temp;
    }

    private BufferedReader getBufferedReader(String fileName)
        throws IOException {
        InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream(fileName));
        BufferedReader br = new BufferedReader(is);
        return br;
    }
    
}
