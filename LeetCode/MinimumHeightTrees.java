public class MinimumHeightTrees {
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (n == 1) {
            List<Integer> result = new ArrayList<Integer>();
            result.add(0);
            return result;
        }
        List<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        
        for (int i = 0; i <= n - 1; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i <= edges.length - 1; i++) {
            //System.out.println(edges[i][0] + " " + edges[i][1]);
            adj[edges[i][0]].add(edges[i][1]);
            adj[edges[i][1]].add(edges[i][0]);
        }
        
        List<Integer> leaves = new ArrayList<Integer>();
        for (int i = 0; i <= adj.length - 1; i++) {
            if (adj[i].size() == 1) {
                leaves.add(i);
            }
        }
        while (n > 2) {
            n = n - leaves.size();
            List<Integer> new_leaves = new ArrayList<Integer>();
            for (int i = 0; i <= leaves.size() - 1; i++) {
                int p = adj[leaves.get(i)].get(0);
                adj[p].remove(leaves.get(i));
                if (adj[p].size() == 1) {
                    new_leaves.add(p);
                }
            }
            leaves = new_leaves;
        }
        return leaves;
    }
}