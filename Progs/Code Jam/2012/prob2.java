import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class prob2 {

    private String pathFile = "";

    // lecture pour le fichier de recherche
    public List<String> lectureFile(String nFichier) {
	List<String> res = new ArrayList<String>();
	String nomFichier = pathFile + nFichier;
	try {
	    BufferedReader in = new BufferedReader(new FileReader(nomFichier));
	    // String line = in.readLine();
	    String line;
	    while ((line = in.readLine()) != null) {
		res.add(line);
	    }
	    in.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return res;
    }

    // écrit le résultat des trie
    public void ecrireFile(List<String> content, String nFichier) {
	String nomFichier = pathFile + nFichier;
	try {
	    PrintWriter out = new PrintWriter(new FileWriter(nomFichier));
	    Iterator<String> it = content.iterator();
	    int compteur = 1;
	    while (it.hasNext()) {
		String str = it.next();
		out.println("Case #" + compteur + ": " + str);
		System.out.println("Case #" + compteur + ": " + str);
		compteur++;
	    }
	    out.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private static void printStatus(int n, int m, Double[][] best) {
	for (int i = 0; i < n; i++) {
	    for (int j = 0; j < m; j++) {
		System.out.print(best[i][j] + " ");
	    }
	    System.out.println();
	}
    }
    private static void faireLaTraversee(int i, int j, int H, int N, int M, int[][] ceilings, int[][] floors,
					 Double[][] best, boolean constantWater) {

	int[] nextI = { i + 1, i, i - 1, i };
	int[] nextJ = { j, j - 1, j, j + 1 };
	//System.out.println("Moved To: " + i + " " + j);
	//printStatus(N, M, best);
	for (int a = 0; a < 4; a++) {
	    int ii = nextI[a];
	    int jj = nextJ[a];
	    if (ii < 0 || ii >= N || jj < 0 || jj >= M) {
		continue;
	    }

	    Double t = best[i][j];
	    if (t == null) {
	    }
	    int hnow = (int) Math.round(H - (10 * t));

	    int c = ceilings[i][j];
	    int cc = ceilings[ii][jj];
	    int f = floors[i][j];
	    int ff = floors[ii][jj];
	    int h = Math.max(f, hnow);
	    int hh = Math.max(ff, hnow);

	    if (cc - ff < 50 || cc - f < 50 || c - ff < 50) {
		continue;
	    }

	    if (constantWater) {
		if (cc - hh < 50) {
		    continue;
		}
		double timeNext = 0.0;
		if (best[ii][jj] == null || timeNext < best[ii][jj]) {
		    best[ii][jj] = timeNext;
		    faireLaTraversee(ii, jj, H, N, M, ceilings, floors, best, true);
		}
	    } else {
		Double twait = t;
		if (cc - hh < 50) {
		    twait = t + ((hh - cc + 50) / 10.0);
		}

		int hwait = (int) Math.round(h - 10 * (twait - t));

		int timeneeded = ((hwait) - f >= 20) ? 1 : 10;

		double timeNext = twait + timeneeded;
		if (best[ii][jj] == null || timeNext < best[ii][jj]) {
		    best[ii][jj] = timeNext;
		    faireLaTraversee(ii, jj, H, N, M, ceilings, floors, best, false);
		}
	    }
	}
    }

    private static String solve(Iterator<String> it) {
	String[] liste = it.next().split(" ");
	int H = Integer.parseInt(liste[0]);
	int N = Integer.parseInt(liste[1]);
	int M = Integer.parseInt(liste[2]);

	int[][] plafonds = new int[N][M];
	int[][] sols = new int[N][M];
	Double[][] meilleur = new Double[N][M];

	for (int i = 0; i < N; i++) {
	    String line = it.next();
	    String[] tokens = line.split(" ");
	    for (int j = 0; j < M; j++) {
		plafonds[i][j] = Integer.parseInt(tokens[j]);
	    }
	}
	for (int i = 0; i < N; i++) {
	    String line = it.next();
	    String[] tokens = line.split(" ");
	    for (int j = 0; j < M; j++) {
		sols[i][j] = Integer.parseInt(tokens[j]);
	    }
	}

	meilleur[0][0] = 0.0;

	faireLaTraversee(0, 0, H, N, M, plafonds, sols, meilleur, true);
	if (meilleur[N - 1][M - 1] != null) {
	    return meilleur[N - 1][M - 1].toString();
	}

	for (int i = 0; i < N; i++) {
	    for (int j = 0; j < M; j++) {
		if (meilleur[i][j] != null) {
		    faireLaTraversee(i, j, H, N, M, plafonds, sols, meilleur, false);
		}
	    }
	}

	if (meilleur[N - 1][M - 1] == null) {
	}
	String answer = meilleur[N - 1][M - 1].toString();
	return answer;
    }

    public static void main(String args[]) {
	prob2 pm = new prob2();
	String inputname = "Input";
	List<String> inputList = pm.lectureFile(inputname);
	Iterator<String> it = inputList.iterator();
	List<String> resList = new ArrayList<String>();

	int T = Integer.parseInt(it.next());
	for (int t = 0; t < T; t++) {
	    System.out.println("Case #" + (t + 1) + ":");
	    resList.add(solve(it));
	}

	pm.ecrireFile(resList, inputname + ".answer.out");
    }

}
