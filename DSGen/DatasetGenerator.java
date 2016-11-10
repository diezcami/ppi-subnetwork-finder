import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

public class DatasetGenerator {
        int[][] g1, g2, gc;
        int e1, e2, n1, n2, d1, d2, as, sd;
        int f1, f2;
        String fileName, dg1, dg2;

    public DatasetGenerator (int[][] gc, int n1, int n2, int e1, int e2, int d1, int d2, int as, int sd, String dg1, String dg2, String fileName) {
        this.n1 = n1; // Keep track for adding edges
        this.n2 = n2;
        this.e1 = e1;
        this.e2 = e2;
        this.d1 = d1;
        this.d2 = d2;
        this.as = as;
        this.sd = sd;
        this.fileName = fileName;
        this.dg1 = dg1;
        this.dg2 = dg2;

        // Create G1 and G2 based on GC
        this.gc = gc;
        this.g1 = new int[n1][n1]; // N1: Inclusive of vertices in subgraph
        this.g2 = new int[n2][n2];


        for(int i = 0; i < gc.length; i++) {
            for (int j = i + 1; j < gc.length; j++) {
                this.g1[i][j] = gc[i][j];
                this.g2[i][j] = gc[i][j];
            }
        }

    }

    public void generate () {

        g1 = addEdges(g1, e1, d1);
        g2 = addEdges(g2, e2, d2);
        System.out.println ("Added edges (e1, e2, d1, d2)");

        String edgeListA = convertToEdgeList (g1, "a", dg1);
        String edgeListB = convertToEdgeList (g2, "b", dg2);

        if (!dg1.equals("n"))
            edgeListA = addDistractionGraph (edgeListA, dg1, n1, "a");
        if (!dg2.equals("n"))
            edgeListB = addDistractionGraph (edgeListB, dg2, n2, "b");
        System.out.println ("Added distraction graphs (if provided)");

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                      new FileOutputStream(fileName), "utf-8"))) {
                          writer.write(edgeListA);
                          writer.write(edgeListB);
        } catch (IOException e) {
            System.out.println ("File not found!");
        }
        System.out.println ("Output file generated!");
        //generateSequenceSimilarityFile();
    }

    // Bulky scary part of the algorithm
    // Adds e crossing edges and d inter edges to the initial GC
    public int[][] addEdges(int[][] g, int e, int d) {
        int n = gc.length;
        int a, b;

        // Add e crossing edges: Connected to GC
        while (e > 0) {
            // Min + (int)(Math.random() * (Max - Min))
            a = 0 + (int)(Math.random() * ((n-1) - 0)); // Vertex from GC, -1 bec. length isn't inclusive in matrix
            b = n + (int)(Math.random() * ((g.length - 1) - n)); // Vertex outside GC
            if (g[a][b] == 0) {
                g[a][b] = 1;
                e--;
            }
        }

        // Add d inter edges: Additional edges
        while (d > 0) {
            // Min + (int)(Math.random() * ((Max - Min) + 1))
            a = n + (int)(Math.random() * (g.length - n)); // Vertex outside GC
            b = n + (int)(Math.random() * (g.length - n)); // Vertex outside GC

            if (g[a][b] == 0) {
                g[a][b] = 1;
                d--;
            }
        }

        return g;
    }

    // Converts the GC with added edges into an edge list
    // Takes DG metadata into account
    public String convertToEdgeList (int[][] g, String prefix, String dg) {
        // Add all edges of the match list
        String edgeList = "";
        String metadata = "";
        int edgeCount = 0;
        for (int i = 1; i < g.length; i++) {
            for (int j = i + 1; j < g.length; j++) {
                if (g[i-1][j-1] == 1) {
                    String temp = prefix + String.valueOf(i) + " " + prefix + String.valueOf(j) + "\n";
                    edgeList = edgeList + temp;
                    edgeCount++;
                }
            }
        }

        if (dg.equals("n")) {
            metadata = g.length + " " + String.valueOf(edgeCount) + "\n";
        } else {
            String temp = "";
            try {
                Scanner sc = new Scanner(new FileReader(dg));
                temp = sc.nextLine(); // Metadata
            } catch (IOException e) {
                System.out.println ("File not found!");
            }
            String[] dgMetadata = temp.split("\\s+");
            int a = Integer.parseInt(dgMetadata[0]); // Number of vertices
            int b = Integer.parseInt(dgMetadata[1]); // Number of edges
            // edgeCount + b + 1 ; Edges + DG + Bridge to DG
            metadata = String.valueOf(g.length + a) + " " + String.valueOf(edgeCount + b + 1) + "\n";
        }


        return metadata + edgeList;
    }

    // Adds distraction graphs (Accepted in edge list form) to the graphs
    public String addDistractionGraph(String edgeList, String dgFileName, int n, String prefix) {
        // Attach edge list to any random vertex
        // Add all vertices by g.length()
        String dg = "";
        String[] temp = null;
        int a, b;
        try {
            Scanner sc = new Scanner(new FileReader(dgFileName));
            sc.nextLine(); // Metadata
            while (sc.hasNext()) {
                temp = sc.nextLine().split("\\s+");
                a = Integer.parseInt(temp[0].substring(1)) + n;
                b = Integer.parseInt(temp[1].substring(1)) + n;
                String edge = prefix + a + " " + prefix + b + "\n";
                dg += edge;
            }
        } catch (IOException e) {
            System.out.println ("File not found!");
        }
        // Add Bridge to first vertex
        dg += prefix + 1 + " " + prefix + String.valueOf(n + 1) + "\n";
        return edgeList + dg;
    }

/*
    public void generateSequenceSimilarityFile() {

    } */
}
