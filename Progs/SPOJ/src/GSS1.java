import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Anshu on 2/19/2017.
 */
public class GSS1 {
    List<GSS1Node> list = new ArrayList<GSS1Node>();
    public static void main(String[] args) {
        new GSS1().solve();
    }

    public void solve() {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] nums = new int[N];
        for (int i = 0; i < N; i++) nums[i] = sc.nextInt();

        GSS1Node root = buildSegTreeAndGetRoot(nums, 0, N - 1);

        int M = sc.nextInt();
        while (M-- > 0) {
            int result = 0, curr_result = 0;
            int x = sc.nextInt();
            int y = sc.nextInt();

            --x; --y;
            recurse(root, x, y);

            GSS1Node first = list.get(0);
            GSS1Node temp = new GSS1Node(first.max,
                    first.max_prefix,
                    first.max_suffix,
                    first.sum,
                    0,
                    0);
            for (int i = 1; i < list.size(); i++) {
                GSS1Node curr_node = list.get(i);
                temp.max = Math.max(temp.max, Math.max(curr_node.max, temp.max_suffix + curr_node.max_prefix));
                temp.max_prefix = Math.max(temp.max_prefix, temp.sum + curr_node.max_prefix);
                temp.max_suffix = Math.max(temp.max_suffix, curr_node.sum + temp.max_suffix);
                temp.sum += curr_node.sum;
            }

            System.out.println(temp.max);
            list.clear();
        }
    }

    void recurse(GSS1Node node, int low, int high) {
        System.out.println(node.startIdx + " " + node.endIdx);
        if (node.startIdx >= low && node.endIdx <= high) {
            System.out.println(node.startIdx + " " + node.endIdx);
            list.add(node);
        } else {
            if (node.left != null && node.left.endIdx >= low)
                recurse((GSS1Node) node.left, low, high);
            if (node.right != null && node.right.startIdx <= high)
                recurse((GSS1Node) node.right, low, high);
        }
    }

    GSS1Node buildSegTreeAndGetRoot(int[] nums, int low, int high) {
        if (low < high - 1) {
            int mid = (low + high) / 2;
            GSS1Node left = buildSegTreeAndGetRoot(nums, low, mid);
            GSS1Node right = buildSegTreeAndGetRoot(nums, mid + 1, high);
            GSS1Node node = new GSS1Node(Math.max(left.max, Math.max(right.max, left.max_suffix + right.max_prefix)),
                                Math.max(left.max_prefix, left.sum + right.max_prefix),
                                Math.max(right.max_suffix, right.sum + left.max_suffix),
                                left.sum + right.sum,
                                low,
                                high);
            node.left = left;
            node.right = right;
            return node;
        } else {
            return new GSS1Node(nums[low], nums[low], nums[low], nums[low], low, low);
        }
    }

    class Node {
        int startIdx;
        int endIdx;

        Node left, right;

        Node(int s, int e) { startIdx = s; endIdx = e; }
    }

    class GSS1Node extends Node {
        int max, max_prefix, max_suffix, sum;

        GSS1Node(int max, int max_prefix, int max_suffix, int sum, int s, int e) {
            super(s, e);
            this.max = max;
            this.max_prefix = max_prefix;
            this.max_suffix = max_suffix;
            this.sum = sum;
        }
    }
}
