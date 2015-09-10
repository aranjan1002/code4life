public class MajorityVote {
    public int getMajority(int[] arr) {
        int cnt = 1;
        int curr_candidate = arr[0];
        
        for (int i = 1; i <= arr.length - 1; i++) {
            if (arr[i] == curr_candidate) {
                cnt++;
            } else {
                cnt--;
            }
            if (cnt == 0) {
                curr_candidate = arr[i];
                cnt = 1;
            }
        }
        
        if (cnt >= 1) {
            return curr_candidate;
        }
        
        return -1;
    }

    public static void main(String[] args) {
	int[] arr = {1, 1, 3, 5, 5};

	System.out.println(new MajorityVote().getMajority(arr));
    }
}