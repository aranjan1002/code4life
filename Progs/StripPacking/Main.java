public class Main {
    public static void main(String[] args) {
	new Main.start();
    }

    publc void start() {
	InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream("Input"));
        BufferedReader br = new BufferedReader(is);
        OutputStreamWriter os =
            new OutputStreamWriter(
                                   new FileOutputStream("Output"));
        BufferedWriter bw = new BufferedWriter(os);
        String line;
	
        int test = 1;
        line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] inp = line.split(" ");
	    for (int i = 1; i < inp.length; i = i + 2) {
		int length = Integer.parseInt(inp[i]);
		int breadth = Integer.parseInt(inp[i + 1]);
		inputRectangles.add(new Rectangle(length, breadth));
	    }
	}
	br.close();
	for (int i = 0; i < inputRectangles.length(); i++) {
	    permute(allPermutationsOfRectangles, inputRectangles[i]);
	}
	
    }

    public void permute(List<List<Rectangle>> list, Rectangle toInsert) {
	for (List<Rectangle> rect : list) {
	    list.remove(rect);
	    insertAtEachPosition(rect, toInsert);
	    list.add(rect);
	}
    }

    public void insertAtEachPosition(List<Rectangle> rect, Rectangle toInsert) {
	for (int i = 0; i < rect.size(); i++) {
	    rec.add(i, toInsert);
	}
	rec.add(rec.size(), toInsert);
    }

    List<Rectangle> inputRectangles = new ArrayList<Rectangle>;
    List<List<Rectangle>> allPermutationsOfRectangles = new ArrayList<ArrayList<Rectangle>>;
}