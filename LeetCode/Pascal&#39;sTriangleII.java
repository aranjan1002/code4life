public class PascalSolution#39;sTriangleII {
    public List<Integer> getRow(int rowIndex) {
        List<Integer> row1 = new ArrayList<Integer>();
        row1.add(new Integer(1));
        List<Integer> row2 = new ArrayList<Integer>();
        row2.add(new Integer(1));
        row2.add(new Integer(1));
        List<Integer> previous_row = row2;
        List<Integer> new_row = row1;
        List<Integer> temp_row;
        
        if (rowIndex == 0) {
            return row1;
        }
        if (rowIndex == 1) {
            return (row2);
        }
        
        for (int i = 2; i <= rowIndex; i++) {
            new_row.clear();
            new_row.add(new Integer(1));
            for (int j = 0; j <= previous_row.size() - 2; j++) {
                int a = previous_row.get(j).intValue();
                int b = previous_row.get(j + 1).intValue();
                new_row.add(new Integer(a + b));
            }
            new_row.add(new Integer(1));
            temp_row = previous_row;
            previous_row = new_row;
            new_row = temp_row;
        }
        
        return previous_row;
    }
}