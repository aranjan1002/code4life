package edu.strippacking.NFDH;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;


public class PreemptiveNFDHDW extends PreemptiveNFDH {

    @Override
    public double getHeightAfterPacking(ArrayList<Rectangle>
					rectList) {
	Collections.sort(rectList,
                         new Comparator() {
                             public int compare(Object o1, Object o2) {
                                 Rectangle r1 = (Rectangle) o1;
                                 Rectangle r2 = (Rectangle) o2;
                                 if (r1.width < r2.width) {
                                     return 1;
                                 } else if (r1.width == r2.width) {
                                     return 0;
                                 } else {
                                     return -1;
                                 }
                             }
                         }
                         );
	return super.getHeightAfterPacking(rectList);
    }
}