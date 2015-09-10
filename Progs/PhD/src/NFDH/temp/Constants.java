package edu.strippacking.NFDH;

import java.lang.*;
import java.util.*;

public class Constants {
    public static double W = 100;
    public static final String INPUTFILENAME = "Input200";

    // returns the optimum height of an input file if known
    // otherwise returns -1
    public static int getOptHeight(String input_filename) {
	if (input_filename.startsWith("nice") ||
	    input_filename.startsWith("path")) {
	    return 100;
	} else if (input_filename.startsWith("Hopper")) {
	    return 200;
	} else {
	    // lookup in the map
	    input_filename = input_filename.replace(".csv", "");
	    Integer opt_height = OPTHEIGHTMAP.get(input_filename);

	    if (opt_height != null) {
		return opt_height.intValue();
	    } else {
		return -1;
	    }
	}
    }

    private static final HashMap<String, Integer> OPTHEIGHTMAP =
	new HashMap<String, Integer>();
    
    static {
	OPTHEIGHTMAP.put(new String("HT01"), new Integer(20));
	OPTHEIGHTMAP.put(new String("HT02"), new Integer(20));
	OPTHEIGHTMAP.put(new String("HT03"), new Integer(20));
	OPTHEIGHTMAP.put(new String("HT04"), new Integer(15));
	OPTHEIGHTMAP.put(new String("HT05"), new Integer(15));
	OPTHEIGHTMAP.put(new String("HT06"), new Integer(15));
	OPTHEIGHTMAP.put(new String("HT07"), new Integer(30));
	OPTHEIGHTMAP.put(new String("HT08"), new Integer(30));
	OPTHEIGHTMAP.put(new String("HT09"), new Integer(30));
	OPTHEIGHTMAP.put(new String("c4-p1(Hopper)"), new Integer(60));
	OPTHEIGHTMAP.put(new String("c4-p2(Hopper)"), new Integer(60));
	OPTHEIGHTMAP.put(new String("c4-p3(Hopper)"), new Integer(60));
	OPTHEIGHTMAP.put(new String("c5-p1(Hopper)"), new Integer(90));
        OPTHEIGHTMAP.put(new String("c5-p2(Hopper)"), new Integer(90));
        OPTHEIGHTMAP.put(new String("c5-p3(Hopper)"), new Integer(90));
	OPTHEIGHTMAP.put(new String("c6-p1(Hopper)"), new Integer(120));
        OPTHEIGHTMAP.put(new String("c6-p2(Hopper)"), new Integer(120));
        OPTHEIGHTMAP.put(new String("c6-p3(Hopper)"), new Integer(120));
	OPTHEIGHTMAP.put(new String("c7-p1(Hopper)"), new Integer(160));
        OPTHEIGHTMAP.put(new String("c7-p2(Hopper)"), new Integer(160));
        OPTHEIGHTMAP.put(new String("c7-p3(Hopper)"), new Integer(160));
	OPTHEIGHTMAP.put(new String("N1Burke"), new Integer(40));
	OPTHEIGHTMAP.put(new String("N2Burke"), new Integer(50));
	OPTHEIGHTMAP.put(new String("N3Burke"), new Integer(50));
	OPTHEIGHTMAP.put(new String("N4Burke"), new Integer(80));
	OPTHEIGHTMAP.put(new String("N5Burke"), new Integer(100));
	OPTHEIGHTMAP.put(new String("N6Burke"), new Integer(100));
	OPTHEIGHTMAP.put(new String("N7Burke"), new Integer(100));
	OPTHEIGHTMAP.put(new String("N8Burke"), new Integer(80));
	OPTHEIGHTMAP.put(new String("N9Burke"), new Integer(150));
	OPTHEIGHTMAP.put(new String("N10Burke"), new Integer(150));
	OPTHEIGHTMAP.put(new String("N11Burke"), new Integer(150));
	OPTHEIGHTMAP.put(new String("N12Burke"), new Integer(300));
    }
}