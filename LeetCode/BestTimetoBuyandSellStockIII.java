public class BestTimetoBuyandSellStockIII {
    public int maxProfit(int[] prices) {
        int max_profit1 = 0, max_profit2 = 0;
        if (prices.length == 0) {
            return 0;
        }
        int[] max_profit_inc = new int[prices.length];
        int[] max_profit_dec = new int[prices.length];
        int curr_min = prices[0];
        
        for (int i = 1; i <= prices.length - 1; i++) {
            if (prices[i] < curr_min) {
                curr_min = prices[i];
                max_profit_inc[i] = max_profit_inc[i - 1];
            } else {
                int diff = prices[i] - curr_min;
                if (diff > max_profit1) {
                    max_profit1 = diff;
                    max_profit_inc[i] = diff;
                } else {
                    max_profit_inc[i] = max_profit_inc[i - 1];
                }
            }
        }
        
        int curr_max = prices[prices.length - 1];
        for (int i = prices.length - 2; i > 0; i--) {
            if (prices[i] > curr_max) {
                curr_max = prices[i];
                max_profit_dec[i] = max_profit_dec[i + 1];
            } else {
                int diff = curr_max - prices[i];
                if (diff > max_profit2) {
                    max_profit2 = diff;
                    max_profit_dec[i] = diff;
                } else {
                    max_profit_dec[i] = max_profit_dec[i + 1];
                }
            }
        }
        
        int curr_result = max_profit1;
        for (int i = 1; i <= prices.length - 2; i++) {
            int curr_sum = max_profit_inc[i] + max_profit_dec[i + 1];
            if (curr_sum > curr_result) {
                curr_result = curr_sum; 
            }
        }
        
        return curr_result;
    }
}