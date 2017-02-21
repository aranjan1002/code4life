/**
 * Definition for an interval.
 * public class Interval {
 *     int start;
 *     int end;
 *     Interval() { start = 0; end = 0; }
 *     Interval(int s, int e) { start = s; end = e; }
 * }
 */
public class InsertInterval {
    public List<Interval> insert(List<Interval> intervals, Interval newInterval) {
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
            collapseIntervals(intervals);
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
                intl.start = newInterval.start;
            }
            if (intl.end < newInterval.end) {
                intl.end = newInterval.end;
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
    
    void collapseIntervals(List<Interval> intervals) {
        Interval curr_intl = intervals.get(0);
        
        for (int i = 1; i <= intervals.size() - 1; i++) {
            Interval intl = intervals.get(i);
            if (curr_intl.end >= intl.start) {
                curr_intl.end = intl.end;
                intervals.remove(i--);
            } else {
                curr_intl = intl;
            }
        }
    }
}