public class PascalSolution#39;sTriangle {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        
        List<Integer> row1 = new ArrayList<Integer>();
        row1.add(new Integer(1));
        List<Integer> row2 = new ArrayList<Integer>();
        row2.add(new Integer(1));
        row2.add(new Integer(1));
        List<Integer> previous_row = row2;
        
        if (numRows >= 1) {
            result.add(row1);
        }
        if (numRows >= 2) {
            result.add(row2);
        }
        
        for (int i = 3; i <= numRows; i++) {
            List<Integer> new_row = new ArrayList<Integer>();
            new_row.add(new Integer(1));
            for (int j = 0; j <= previous_row.size() - 2; j++) {
                int a = previous_row.get(j).intValue();
                int b = previous_row.get(j + 1).intValue();
                new_row.add(new Integer(a + b));
            }
            new_row.add(new Integer(1));
            previous_row = new_row;
            result.add(new_row);
        }
        
        return result;
    }
}