class ReverseStr {
    public static void main(String[] args) {
	String str = "abcde\0";
        
        if (str == null) {
            return;
        } else if (str.length() == 1) {
            return;
        } else {
            StringBuilder str_builder = new StringBuilder();
            for (int i = str.length() - 2; i >= 0; i--) {
                str_builder.append(str.charAt(i));
            }
            
            str_builder.append('\0');
	    System.out.println(str_builder.toString());
        }
    }
}