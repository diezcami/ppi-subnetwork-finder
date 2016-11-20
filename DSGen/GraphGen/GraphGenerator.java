import java.util.Scanner;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;


public class GraphGenerator {
    public static void main (String args[]) {
        /*
        // Manual Generate
        Scanner sc = new Scanner (System.in);
        System.out.println ("Enter number of vertices (N): ");
        int n = Integer.parseInt(sc.nextLine());
        System.out.println ("Enter edge multiplier (E = N * Multiplier): ");
        int m = Integer.parseInt(sc.nextLine());
        System.out.println ("Enter file name (eg. output.txt): ");
        String fileName = sc.nextLine();
        generateGraph (n, m, fileName);
        System.out.println ("Generated a graph!");
        */


        // Auto Generate
        int num_testcases = 25;
        for (int i = 1; i <= num_testcases; i++) {
            String fileName = "set_c-" + i + ".txt";
            generateGraph (50, 4, fileName);
        }

    }

    public static void generateGraph (int n, int m, String fileName) {
        int[][] g = new int [n][n];
        int e = n * m;
        int a, b;

        while (e > 0) {
            // Min + (int)(Math.random() * (Max - Min))
            a = 0 + (int)(Math.random() * ((n-1) - 0)); // Vertex from GC, -1 bec. length isn't inclusive in matrix
            b = 0 + (int)(Math.random() * ((n-1) - 0)); // Vertex outside GC

            if (a > b) { // Swap
                 a = a + b;
                 b = a - b;
                 a = a - b;
            }

            if (g[a][b] == 0 && g[b][a] == 0 && a!=b) { // Dirty code/ clean up
                g[a][b] = 1;
                g[b][a] = 1;
                e--;
            }

            String edgeList = convertToEdgeList (g, "a");

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                          new FileOutputStream(fileName), "utf-8"))) {
                              writer.write(edgeList);
            } catch (IOException ex) {
                System.out.println ("File not found!");
            }
        }
    }

    // Converts the GC with added edges into an edge list
    // Takes DG metadata into account
    public static String convertToEdgeList (int[][] g, String prefix) {
        // Add all edges of the match list
        String edgeList = "";
        String metadata = "";
        int edgeCount = 0;
        for (int i = 1; i < g.length + 1; i++) {
            for (int j = i + 1; j < g.length + 1; j++) {
                if (g[i-1][j-1] == 1) {
                    String temp = prefix + String.valueOf(i) + " " + prefix + String.valueOf(j) + "\n";
                    edgeList = edgeList + temp;
                    edgeCount++;
                }
            }
        }

        metadata = g.length + " " + String.valueOf(edgeCount) + "\n";

        return metadata + edgeList;
    }
}
