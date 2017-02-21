class MinStack {
    public void push(int x) {
        stack.add(idx, new Integer(x));
        if (x < min) {
            min = x;
        }
        idx++;
    }

    public void pop() {
        if (stack.get(idx - 1).intValue() == min) {
            updateMin();
        }
        idx--;
    }

    public int top() {
        return stack.get(idx - 1).intValue();
    }

    public int getMin() {
        return min;
    }
    
    private void updateMin() {
        min = Integer.MAX_VALUE;
        for (int i = 0; i <= idx - 2; i++) {
            int curr_val = stack.get(i).intValue();
            if (curr_val < min) {
                min = curr_val;
            }
        }
    }
    
    List<Integer> stack = new ArrayList<Integer>();
    int idx = 0;
    int min = Integer.MAX_VALUE;
}
