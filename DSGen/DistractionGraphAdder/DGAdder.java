import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

public class DGAdder {
    public static void main (String args[]) {
        try {
            int numberOfTestCases = 25;
            for (int i = 1; i <= numberOfTestCases; i++) {
                // Define variables here
                // ====================================================================================
                int gc_size = 50;
                int gc_edges = 200;
                // Get these next 4 variables from the dataset metadata
                // https://docs.google.com/document/d/1Vj4fPJR8i3NQeLQib35eXwwD3gAx8J05vaweCDPyAjo/edit
                int as = 200;
                int sd = 50;
                int n1 = 100;
                int n2 = 70;
                String graph_filename = "set_c-testcase-" + i + ".txt";
                String distractionGraph_filename_A = "d" + i + "a.txt";
                String distractionGraph_filename_B = "d" + i + "b.txt";
                String output_filename = "set_d-testcase-" + i + ".txt";
                String sequenceSimOutput_filename = "similarity-" + output_filename;
                // ====================================================================================

                // Algorithm variables
                Scanner s = new Scanner(new FileReader(graph_filename));
                String edgeList_A = "";
                String edgeList_B = "";
                int[][] gc = new int[gc_size][gc_size];
                int[][] g1 = new int[n1][n1];
                int[][] g2 = new int[n2][n2];

                int a1, a2;
                String[] temp;

                // Get edge list A from original input and build G1 matrix
                // Also generate GC Matrix
                String[] input = s.nextLine().split("\\s+");
                int numberOfVertices_A = Integer.parseInt(input[0]);
                int numberOfEdges_A = Integer.parseInt(input[1]);
                for(int j = 0; j < numberOfEdges_A; j++)
                {
                        String orig = s.nextLine() + "\n";
                        edgeList_A += orig;
                        temp = orig.split("\\s+");
                        a1 = Integer.parseInt(temp[0].substring(1));
                        a2 = Integer.parseInt(temp[1].substring(1));
                        g1[a1-1][a2-1] = 1;
                        g1[a2-1][a1-1] = 1;

                        if (a1 <= gc_size && a2 <= gc_size) {
                            gc[a1-1][a2-1] = 1;
                            gc[a2-1][a1-1] = 1;
                        }
                }

                // Get edge list B from original input and build G2 matrix
                input = s.nextLine().split("\\s+");
                int numberOfVertices_B = Integer.parseInt(input[0]);
                int numberOfEdges_B = Integer.parseInt(input[1]);
                for(int j = 0; j < numberOfEdges_B; j++)
                {
                        String orig = s.nextLine() + "\n";
                        edgeList_A += orig;
                        temp = orig.split("\\s+");
                        a1 = Integer.parseInt(temp[0].substring(1));
                        a2 = Integer.parseInt(temp[1].substring(1));
                        g2[a1-1][a2-1] = 1;
                        g2[a2-1][a1-1] = 1;
                }

                // Get distraction graph metadata and edge list
                String edgeListFinal_A = addDistractionGraph (edgeList_A, distractionGraph_filename_A, "a", numberOfVertices_A, numberOfEdges_A);
                String edgeListFinal_B = addDistractionGraph (edgeList_B, distractionGraph_filename_B, "b", numberOfVertices_B, numberOfEdges_B);

                // Write Graph Output File
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                              new FileOutputStream(output_filename), "utf-8"))) {
                                  writer.write(edgeListFinal_A);
                                  writer.write(edgeListFinal_B);
                } catch (IOException exc) {
                    System.out.println ("File not found!");
                }

                // Write Sequence Similarity File
                String similarities = generateSequenceSimilarityFile (gc, g1, g2, as, sd, n1, n2);

                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                              new FileOutputStream(sequenceSimOutput_filename), "utf-8"))) {
                                  writer.write(similarities);
                } catch (IOException e) {
                    System.out.println ("File not found!");
                }

                System.out.println ("Generated Dataset #" + i);

            }

            System.out.println ("Done generating datasets!");

        } catch (IOException ex) {
            System.out.println ("File not found!");
        }
    }

    // Creates an edge list including the distraction graph
    public static String addDistractionGraph(String edgeList, String dgFileName, String prefix, int n, int e) {
        // Attach edge list to any random vertex
        // Add all vertices by g.length()
        String distractionGraphEdgeList = "";
        String[] edgeValues = null;
        int numberOfEdges = 0;
        int numberOfVertices = 0;
        try {
            Scanner sc = new Scanner(new FileReader(dgFileName));
            while (sc.hasNext()) {
                String[] temp = sc.nextLine().split("\\s+");
                int a = Integer.parseInt(temp[0].substring(1)) + n;
                int b = Integer.parseInt(temp[1].substring(1)) + n;
                int max = a > b ? a : b;
                numberOfVertices = numberOfVertices > max ? numberOfVertices : max;
                String edge = prefix + a + " " + prefix + b + "\n";
                distractionGraphEdgeList += edge;
                numberOfEdges++;
            }
        } catch (IOException ex) {
            System.out.println ("File not found!");
        }

        // Add Bridge to first vertex
        distractionGraphEdgeList += prefix + 1 + " " + prefix + String.valueOf(n + 1) + "\n";
        numberOfEdges++;

        // Create metadata;
        numberOfEdges += e;
        String metadata = numberOfVertices + " " + numberOfEdges + "\n";

        return metadata + edgeList + distractionGraphEdgeList;
    }

    public static String generateSequenceSimilarityFile(int[][] gc, int[][] g1, int[][] g2, int as, int sd, int n1, int n2) {
        String similarities = "";

        // Add similarity scores to the original graph
        for (int i = 1; i < gc.length + 1; i ++) {
            // Min + (int)(Math.random() * (Max - Min))
            int increment = (int)(Math.random() * sd);
            String temp = "a" + i + " " + "b" + i + " " + (as + increment) + "\n";
            similarities+=temp;
        }

        int max = n1 > n2 ? n1 : n2;
        int min = n1 + n2 - max;
        for (int i = 0; i < max ; i ++) {
            for (int j = 0; j < min; j++) {
                int edges1 = getNumberOfEdges(g1[i]);
                int edges2 = getNumberOfEdges(g2[j]);
                int sim = Math.abs(edges1 - edges2);
                if (sim <= 1 && edges1 != 0 && edges2 != 0 && sim != 0) { // Similar degrees within 2
                    double multiplier = edges1 > edges2 ? 1.0 * edges2 / edges1 : 1.0 * edges1 / edges2;
                    int sign = (int)(Math.random() * 2) - 1;
                    multiplier *= sd;
                    String temp = "a" + (i+1) + " " + "b" + (j+1) + " " + (as - multiplier) + "\n";
                    similarities+=temp;
                } else if (sim == 0){
                    String temp = "a" + (i+1) + " " + "b" + (j+1) + " " + (as - sd) + "\n";
                    similarities+=temp;
                }
            }
        }

        return similarities;
    }

    public static int getNumberOfEdges (int[] arr) {
        int count = 0;
        for (int i : arr) {
            if (i == 1)
                count ++;
        }
        return count;
    }

}
