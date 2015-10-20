import java.util.Comparator;

class EdgeComparator implements Comparator {
    public int compare(Object e1, Object e2) {
	return ((Edge)e1).weight - ((Edge) e2).weight;
    }
}
