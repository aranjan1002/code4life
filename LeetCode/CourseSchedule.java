public class CourseSchedule {
    class CourseNode {
        int courseNum;
        List<CourseNode> preReqs = new ArrayList<CourseNode>();
        CourseNode(int n) {courseNum = n;}
        public String toString() {
            String result = Integer.toString(courseNum) + ": ";
            for (CourseNode pre : preReqs) {
                result += Integer.toString(pre.courseNum) + " ";
            }
            return result;
        }
    }
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        CourseNode[] courses = new CourseNode[numCourses];
        for (int i = 0; i <= numCourses - 1; i++) {
            courses[i] = new CourseNode(i);
        }
        for (int[] edge : prerequisites) {
            courses[edge[0]].preReqs.add(courses[edge[1]]);
            //System.out.println(courses[edge[0]]);
        }
        HashSet<CourseNode> visited = new HashSet<CourseNode>();
        for (CourseNode c : courses) {
            if (visited.contains(c) == false) {
                if (isCyclic(c, visited, new HashSet<CourseNode>()) == true) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isCyclic(CourseNode c, HashSet<CourseNode> visited, HashSet<CourseNode> visiting) {
        System.out.println(c.courseNum);
        visiting.add(c);
        for (CourseNode c_preReq : c.preReqs) {
            if (visiting.contains(c_preReq) == true) {
               // System.out.println("Found cycle");
                return true;
            }
            if (visited.contains(c_preReq) == false && isCyclic(c_preReq, visited, visiting) == true) {
                return true;
            }
        }
        visiting.remove(c);
        visited.add(c);
        return false;
    }
}