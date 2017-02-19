import java.math.BigInteger;
import java.util.Scanner;

/**
 * Created by Anshu on 2/18/2017.
 */
public class PALIN {
    public static void main(String[] args) {
        new PALIN().solve();
    }

    public void solve() {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            String s = sc.next();
            if (s.equals("9")) {
                System.out.println("11");
                continue;
            }
            int len = s.length();
            String s1 = firstHalf(s);
            String s2 = secondHalf(s);
            if (isGreater(s1, s2)) {
                System.out.println(join(s1, len));
            } else {
                //System.out.println("here");
                System.out.println(join(increment(s1), len));
            }
        }
    }

    String firstHalf(String s) {
        int len = s.length();
        return s.substring(0, (len + 1) / 2);
    }

    String secondHalf(String s) {
        int len = s.length();
        return s.substring((len + 1) / 2);
    }

    boolean isGreater(String s1, String s2) {
        //System.out.println(s1 + " " + s2);
        int idx1 = s1.length() - 1;
        if (s1.length() != s2.length()) {
            idx1 = s1.length() - 2;
        }
        int idx2 = 0;
        while (idx1 >= 0 && idx2 <= s2.length() - 1) {
            if (s1.charAt(idx1) > s2.charAt(idx2)) return true;
            if (s1.charAt(idx1) < s2.charAt(idx2)) return false;
            idx1--;
            idx2++;
        }
        return false;
    }

    String increment(String s) {
        BigInteger bi = new BigInteger(s);
        bi = bi.add(new BigInteger("1"));
        return bi.toString();
    }

    String join(String s1, int len) {
        String rev = new StringBuilder(s1).reverse().toString();
        int s = 0;
        if (len < s1.length() * 2) s = 1;
        return s1 + rev.substring(s);
    }
}
