public class BestTimetoBuyandSellStockwithCooldown {
    public int maxProfit(int[] prices) {
        if (prices.length <= 1) return 0;
        int[] sell = new int[prices.length];
        int[] buy = new int[prices.length];
        sell[0] = 0;
        buy[0] = -(1) * prices[0];
        for (int i = 1; i <= prices.length - 1; i++) {
            int max_sell = 0;
            if (i != 1) {
                max_sell = sell[i - 2];
            }
            buy[i] = Math.max(buy[i - 1], max_sell - prices[i]);
            sell[i] = Math.max(buy[i - 1] + prices[i], sell[i - 1]);
        }
        return Math.max(buy[prices.length - 1], sell[prices.length - 1]);
    }
}