public class RestoreIPAddresses {
    public List<String> restoreIpAddresses(String s) {
        List<String> new_list = new ArrayList<String>();
        new_list.add(new String());
        restoreIpAddresses(s, 1, new_list);
        return result;
    }
    
     public void restoreIpAddresses(String s, int n, List<String> list) {
        // System.out.println(list);
        if (n == 4) {
            if (s.length() > 3) {
                return;
            }
            int a = new Integer(s).intValue();
            if (a <= 255 && checkPossibility(s) == true) {
                addToResult(list, s);
            }
        } else {
            int l = s.length();
            if (l <= 1) {
                return;
            }
            List<String> new_list = addToNewList(list, s.substring(0, 1));
            restoreIpAddresses(s.substring(1, l), n + 1, new_list);
            if (l >= 3) {
                String possible_value = s.substring(0, 2);
                if (checkPossibility(possible_value) == true) {
                    List<String> new_list2 = addToNewList(list, possible_value);
                    restoreIpAddresses(s.substring(2, l), n + 1, new_list2);
                }
            }
            if (l >= 4) {
                String possible_value = s.substring(0, 3);
                if (new Integer(possible_value).intValue() <= 255 && checkPossibility(possible_value) == true) {
                    List<String> new_list3 = addToNewList(list, possible_value);
                    restoreIpAddresses(s.substring(3, l), n + 1, new_list3);
                }
            }
        }
    }

     public List<String> addToNewList(List<String> list, String s) {
        List<String> new_list = new ArrayList<String>();
        // System.out.println(new_list.size());
        for (int i = 0; i <= list.size() - 1; i++) {
            String current_string = new String(list.get(i));
            if (current_string.length() > 0) {
                new_list.add(current_string + "." + s);
            } else {
                new_list.add(current_string + s);
            }
        }

        return new_list;
    }

    public void addToResult(List<String> list, String s) {
        for (int i = 0; i <= list.size() - 1; i++) {
            result.add(list.get(i) + "." + s);
        }
    }
    
    public boolean checkPossibility(String possible_value) {
        if (possible_value.length() > 1 && possible_value.charAt(0) == '0') {
            return false;
        }
        return true;
    }
    
    List<String> result = new ArrayList<String>();
}