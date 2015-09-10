/**
 * Definition for an interval.
 * public class Interval {
 *     int start;
 *     int end;
 *     Interval() { start = 0; end = 0; }
 *     Interval(int s, int e) { start = s; end = e; }
 * }
 */
import java.util.*;

public class Solution {
    public static void main(String[] args) {
	int a = 1;

	new Solution().solve(a);
    }

    public void solve(int a) {
	List<Interval> intervals = new ArrayList<Interval>();
        Interval intl1 = new Interval(1, 5);
        Interval intl2 = new Interval(2, 7);

        intervals.add(intl1);
	insert(intervals, intl2);
	System.out.println(intervals);
    }

    public List<Interval> insert(List<Interval> intervals, 
				 Interval newInterval) {
        int x = findPos(intervals, newInterval.start);
        int y = findPos(intervals, newInterval.end);
        System.out.println(x + " " + y);
        insertIntervalBetween(x, y, newInterval, intervals);
        return intervals;
    }
    
    int findPos(List<Interval> intervals, int val) {
        int pos = 0;
        for (int i = 0; i <= intervals.size() - 1; i++) {
            Interval curr_interval = intervals.get(i);
            if (val < curr_interval.start) {
                break;
            } else if (val >= curr_interval.start && 
		       val <= curr_interval.end) {
                pos++;
                break;
            } else {
                pos = pos + 2;
            }
        }
        return pos;
    }
    
    void insertIntervalBetween(int pos1, 
			       int pos2, 
			       Interval newInterval, 
			       List<Interval> intervals) {
        insert_end(pos2, newInterval, intervals);
        insert_start(pos1, newInterval, intervals);
	System.out.println(intervals);
        collapse_indices(intervals);
    }
    
    void insert_end(int pos2, 
		    Interval newInterval, 
		    List<Interval> intervals) {
        if (pos2 % 2 == 1) {
            return;
        }
        
        int idx = pos2 / 2;
        intervals.add(idx, newInterval);
    }
    
    void insert_start(int pos1, 
		      Interval newInterval, 
		      List<Interval> intervals) {
        if (pos1 % 2 == 1) {
            return;
        }
        
        int idx = pos1 / 2;
        intervals.add(idx, newInterval);
    }
    
    void collapse_indices(List<Interval> intervals) {
        for (int i = 0; i <= intervals.size() - 2; i++) {
            Interval curr_intl = intervals.get(i);
            Interval next_intl = intervals.get(i + 1);
            while (next_intl.start <= curr_intl.end) {
                intervals.remove(i + 1);
                if (i + 1 <= intervals.size() - 1) {
                    next_intl = intervals.get(i + 1);
                } else {
                    break;
                }
            }
        }
    }

    public List<Interval> insert2(List<Interval> intervals, 
				  Interval newInterval) {
        if (intervals.size() == 0) {
            intervals.add(newInterval);
            return intervals;
        }
        
        boolean changeMade = false;
        for (int i = 0; i <= intervals.size() - 1; i++) {
            Interval intl = intervals.get(i);
            if (changeMade == false) {
                changeMade = changeIntervalIfNeeded(intl, newInterval);
            } else {
                changeIntervalIfNeeded(intl, newInterval);
            }
        }
        
        if (changeMade == true) {
            removeDuplicates(intervals);
        } else {
            insertInterval(intervals, newInterval);
        }
        
        return intervals;
    }
    
    boolean changeIntervalIfNeeded(Interval intl, Interval newInterval) {
        if (intl.start > newInterval.end) {
            return false;
        } else if (intl.end < newInterval.start) {
            return false;
        } else {
            if (intl.start > newInterval.start) {
                newInterval.start = intl.start;
            }
            if (intl.end < newInterval.end) {
                newInterval.end = intl.end;
            }
            return true;
        }
    }
    
    void removeDuplicates(List<Interval> intervals) {
        Interval curr_intl = intervals.get(0);
        
        for (int i = 1; i <= intervals.size() - 1; i++) {
            if (isEqual(curr_intl, intervals.get(i)) == true) {
                intervals.remove(i);
            } else {
                curr_intl = intervals.get(i);
            }
        }
    }
    
    boolean isEqual(Interval a, Interval b) {
        if (a.start == b.start && a.end == b.end) {
            return true;
        }
        return false;
    }
    
    void insertInterval(List<Interval> intervals, Interval newInterval) {
        for (int i = 0; i <= intervals.size() - 1; i++) {
            Interval intl = intervals.get(i);
            if (intl.start > newInterval.end) {
                intervals.add(i, newInterval);
                return;
            }
        }
        
        intervals.add(newInterval);
    }

    class Interval extends Object {
	int start;
	int end;
	Interval() { start = 0; end = 0; }
	Interval(int s, int e) { start = s; end = e; }
	
	// @Override
	public String toString() {
	    String result = new String();
	    result = result + start + " " + end + " ";
	    return result; 
	}
    }
}