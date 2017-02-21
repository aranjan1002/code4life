public class CompareVersionNumbers {
    public int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        
        int i = 0;
        
        while (i <= v1.length - 1 && i <= v2.length - 1) {
            int n1 = new Integer(v1[i]).intValue();
            int n2 = new Integer(v2[i]).intValue();
            
            if (n1 < n2) {
                return -1;
            } else if (n1 > n2) {
                return 1;
            }
            i++;
        }
        while (i <= v1.length - 1) {
            if (new Integer(v1[i++]).intValue() > 0) {
                return 1;
            }
        }
        while (i <= v2.length - 1) {
            if (new Integer(v2[i++]).intValue() > 0) {
                return -1;
            }
        }
        return 0;
    }
}