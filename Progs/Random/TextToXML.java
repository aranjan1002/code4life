import java.io.*;

class TextToXML {
    public static void main(String[] args) 
    throws Exception {
	StringBuilder sb = new StringBuilder();
	InputStreamReader is;
	BufferedReader bs;
	TextToXML t = new TextToXML();
	sb.append("<HurricaneTrajectories>\n");
        File dir = new File(args[0]);
	String[] files = dir.list();
	for(int i=0; i<files.length; i++) {
	    is = new InputStreamReader(new FileInputStream(new File(files[i])));
	    bs = new BufferedReader(is);
	    sb.append(t.getXML(bs, files[i]));
	}
	sb.append("</HurricaneTrajectories>\n");
	System.out.println(sb);
    }

    public StringBuilder getXML(BufferedReader bs, String fileName) 
    throws Exception {
	String str;
	StringBuilder sb = new StringBuilder();
	sb.append("<Hurricane id=\"" + fileName + "\">\n");
	sb.append("<Trajectory>\n");
	while((str = bs.readLine()) != null) {
	    str = str.replaceAll(" ", "");
	    sb.append("<Point ");
	    String[] tokens = str.split(",");
	    for(int i=0; i<tokens.length; i++) {
		if(tokens[i].length() != 0)
		    sb.append(arr[i] + "\"" + tokens[i] + "\" ");
	    }
	    sb.append("/>\n");
	}
	sb.append("</Trajectory>\n");
	sb.append("</Hurricane>\n");
	return sb;
    }

    String[] arr = {"basin=", "cy=", "warningdate=", "", "tech=", "tau=", "lat=", "long=", "vmax=", "mslp=", "ty=", "rad=", "windcode=", "rad1=", "rad2=", "rad3=", "rad4=", "radp=", "rrp=", "mrd=", "gusts=", "eye=", "subregion=", "maxseas=", "", "dir=", "speed=", "stormname=", "depth="};
}

    