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
            int numberOfTestCases = 1;
            for (int i = 1; i <= numberOfTestCases; i++) {
                // Define variables here
                // ====================================================================================
                /*String graph_filename = "set_a-" + i + ".txt";
                String distractionGraph_filename_A = "distraction-" + i + "-A.txt";
                String distractionGraph_filename_B = "distraction-" + i + "-B.txt";
                String output_filename = "set_b-testcase-" + i + ".txt";*/

                String graph_filename = "test.txt";
                String distractionGraph_filename_A = "dg1.txt";
                String distractionGraph_filename_B = "dg2.txt";
                String output_filename = "output.txt";
                // ====================================================================================

                // Algorithm variables
                Scanner s = new Scanner(new FileReader(graph_filename));
                String edgeList_A = "";
                String edgeList_B = "";

                // Get edge list A from original input
                String[] input = s.nextLine().split("\\s+");
                int numberOfVertices_A = Integer.parseInt(input[0]);
                int numberOfEdges_A = Integer.parseInt(input[1]);
                for(int j = 0; j < numberOfEdges_A; j++)
                {
                        edgeList_A += s.nextLine();
                        edgeList_A += "\n";
                }

                // Get edge list B from original input
                input = s.nextLine().split("\\s+");
                int numberOfVertices_B = Integer.parseInt(input[0]);
                int numberOfEdges_B = Integer.parseInt(input[1]);
                for(int j = 0; j < numberOfEdges_B; j++)
                {
                        edgeList_B += s.nextLine();
                        edgeList_B += "\n";
                }

                // Get distraction graph metadata and edge list
                String edgeListFinal_A = addDistractionGraph (edgeList_A, distractionGraph_filename_A, "a", numberOfVertices_A, numberOfEdges_A);
                String edgeListFinal_B = addDistractionGraph (edgeList_B, distractionGraph_filename_B, "b", numberOfVertices_B, numberOfEdges_B);

                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                              new FileOutputStream(output_filename), "utf-8"))) {
                                  writer.write(edgeListFinal_A);
                                  writer.write(edgeListFinal_B);
                } catch (IOException exc) {
                    System.out.println ("File not found!");
                }


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
}
