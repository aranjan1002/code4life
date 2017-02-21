/**
 * Definition for an interval.
 * public class Interval {
 *     int start;
 *     int end;
 *     Interval() { start = 0; end = 0; }
 *     Interval(int s, int e) { start = s; end = e; }
 * }
 */
public class MergeIntervals {
    public List<Interval> merge(List<Interval> intervals) {
        Collections.sort(intervals, new Comparator<Interval>(){
            @Override
            public int compare(Interval obj0, Interval obj1) {
                return obj0.start - obj1.start;
            }
        });
        List<Interval> result = new ArrayList<Interval>();
        for (int i = 0; i <= intervals.size() - 1; i++) {
            Interval il = intervals.get(i);
            int start = il.start;
            int end = il.end;
            Interval il2 = il;
            int j = i;
            for (j = i + 1; j <= intervals.size() - 1; j++) {
                Interval il3 = intervals.get(j);
                if (il3.start <= end) {
                    if (end < il3.end) {
                        end = il3.end;
                    }
                } else {
                    break;
                }
            }
            System.out.println(j);
            i = j - 1;
            result.add(new Interval(start, end));
        } 
        return result;
    }
}