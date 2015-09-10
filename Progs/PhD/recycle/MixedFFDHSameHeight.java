package edu.strippacking.FFDH;

class MixedFFDHSameHeight {
    public int applyMixedFFDHAndGetHeight(int[] widths, 
					  boolean[] isPreemptive,
					  int maxHeight) {
	int[] lvl_occ = new int[maxHeight];
	int highest_lvl = -1;
	for (int i = 0; i < widths.length; i++) {
	    int w = widths[i];
	    int j = 0;
	    int most_vac_lvl = -1;
	    int max_vac = 0;
	    for (j = 0; j <= highest_lvl; j++) {
		// iterate from first level onwards
		// to find the first fit
		int curr_lvl_vac = 10 - lvl_occ[j];
		if (curr_lvl_vac >= w) {
		    // if fits well, done
		    lvl_occ[j] += w;
		    break;
		}
		else if (isPreemptive[i]) {
		    // doesnt' fit and is preemptive
		    // in this case keep iterating
		    // and update the best fit that may
		    // be used later
		    if (curr_lvl_vac > max_vac) {
			max_vac = curr_lvl_vac;
			most_vac_lvl = j;
		    }
		}
	    }
	    if (j > highest_lvl) {
		// if this is not reached, job is 
		// already placed
		highest_lvl++;
		if (isPreemptive[i]) {
		    // fit in the most vacant level
		    // and rest in a new level
		    if (most_vac_lvl >= 0) {
			lvl_occ[most_vac_lvl] = 10;
			w -= max_vac;
		    }
		}
		lvl_occ[highest_lvl] = w;
	    }
	    // print(lvl_occ);
	}
	print(lvl_occ);
	return highest_lvl + 1;
    }

    private void print(int[] lvlOcc) {
	for (int i = 0; i < lvlOcc.length; i++) {
	    System.out.println(i + ": " + lvlOcc[i]);
	    if (lvlOcc[i] == 0) {
		break;
	    }
	}
	System.out.println();
    }
}

	