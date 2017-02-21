public class BestTimetoBuyandSellStock {
    public int maxProfit(int[] prices) {
        int max_profit = 0;
        
        for (int i = 0; i < prices.length; i++) {
            int curr_max = findMaxProfit(prices, i);
            if (curr_max > max_profit) {
                max_profit = curr_max;
            }
        }
        return max_profit;
    }
    
    public int findMaxProfit(int[] prices, int idx) {
        int buy_price = prices[idx];
        int curr_max = 0;
        int max = 0;
        
        for (int i = idx + 1; i < prices.length; i++) {
            curr_max = prices[i] - buy_price;
            if (curr_max > max) {
                max = curr_max;
            }
        }
        return max;
    }
}