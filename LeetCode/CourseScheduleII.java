public class CourseScheduleII {
    class CourseNode {
        int courseNum;
        boolean curr_stack = false;
        List<CourseNode> preReqs = new ArrayList<CourseNode>();
    }
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<CourseNode> nodesList = new ArrayList<CourseNode>();
        int[] result = new int[0];
        for (int i = 0; i <= numCourses - 1; i++) {
            CourseNode node = new CourseNode();
            node.courseNum = i;
            nodesList.add(node);
        }

        for (int i = 0; i <= prerequisites.length - 1; i++) {
            CourseNode node = nodesList.get(prerequisites[i][0]);
            node.preReqs.add(nodesList.get(prerequisites[i][1]));
        }

        // for (int i = 0; i <= numCourses - 1; i++) {
        //     if (isCycle(nodesList.get(i), new HashSet<CourseNode>()) == true) {
        //         return result;
        //     }
        // }

        Stack<Integer> s = new Stack<Integer>();
        HashSet<CourseNode> h = new HashSet<CourseNode>();

        for (int i = 0; i <= numCourses - 1; i++) {
            if (h.contains(nodesList.get(i)) == false) {
                boolean isCyclic = topologicalSort(h, s, nodesList.get(i));
                if (isCyclic == true) {
                    return new int[0];
                }
            }
        }

        result = new int[numCourses];
        int idx = numCourses - 1;
        while (s.isEmpty() == false) {
            result[idx--] = s.pop();
        }

        return result;
    }
    
    boolean topologicalSort(HashSet<CourseNode> h, Stack<Integer> s, CourseNode n) {
        if (h.contains(n) == true) {
            return false;
        }
        if (n.curr_stack == true) {
            return true;
        }
        n.curr_stack = true;
        boolean isCyclic = false;
        for (int i = 0; i <= n.preReqs.size() - 1; i++) {
            CourseNode p = n.preReqs.get(i);
            if(topologicalSort(h, s, p) == true) {
                return true;
            }
        }
        n.curr_stack = false;
        s.add(n.courseNum);
        h.add(n);
        return isCyclic;
    }

    // boolean isCycle(CourseNode n, HashSet<CourseNode> visited) {
    //     for (int i = 0; i <= n.preReqs.size() - 1; i++) {
    //         CourseNode p = n.preReqs.get(i);
    //         if (visited.contains(p) == true) {
    //             return true;
    //         }
    //         visited.add(p);
    //         if (isCycle(p, visited) == true) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }
}
