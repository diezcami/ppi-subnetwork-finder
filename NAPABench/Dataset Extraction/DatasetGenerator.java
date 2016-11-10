import java.util.Queue;
import java.util.HashMap;
import java.util.Set;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.Collections;

public class DatasetGenerator {
        int[][] graph1;
        int[][] graph2;
        int[] solution;

    public DatasetGenerator (int[][] graph1, int[][]graph2) {
        this.graph1 = graph1;
        this.graph2 = graph2;
		System.out.println ("Successfully created matrices");
        parseSolutionSet();
        System.out.println ("Parsed solution set into an array!");

    }

    public void parseSolutionSet () {
        try {
            Scanner sc = new Scanner(new FileReader("spinal_results.txt"));
            solution = new int[3000];
            // Every index corresponds to the A vertex index -1. The stored integer corresponds to the matched B graph solution.
            String[] temp = null;
            int a, b;
            while (sc.hasNext()) {
                temp = sc.nextLine().split("\\s+");
                a = Integer.parseInt(temp[0].substring(1));
                b = Integer.parseInt(temp[1].substring(1));
                solution[a-1] = b;
            }
        } catch (IOException e) {
            System.out.println ("File not found!");
        }
    }

    public void generate (int n, int r) { // n: number of matched nodes, r: restriction
        // Build dataset of N connected and matched nodes per graph (matchList)
        System.out.println ("Building Matched Integer List for Graph A");
        Set<Integer> matchListA = buildMatchList(n, r);
        System.out.println ("Building Matched Integer List for Graph B");
        Set<Integer> matchListB = buildCorrespondingMatchList(matchListA);

        // For every node, add all first-degree neighbours (completeList)
        System.out.println ("Building Complete Integer List for Graph A");
        Set<Integer> neighbourListA = buildNeighbourList (matchListA, graph1, r);
        System.out.println ("Building Complete Integer List for Graph B");
        Set<Integer> neighbourListB = buildNeighbourList (matchListB, graph2, r);

        // For every node in completeList+matchList, add edges they're connected to
        System.out.println ("Building Edge List for Graph A");
        String edgeListA = getEdges (matchListA, neighbourListA, graph1, "a", r);
        System.out.println ("Building Edge List for Graph B");
        String edgeListB = getEdges (matchListB, neighbourListB, graph2, "b", r);


        // Put final output in a .txt file
        // Naming Convention: Number of matched nodes - Additional neighbours in A - Additional neighbours in B .txt
        String fileName = String.valueOf(matchListA.size()) + "-" + String.valueOf(neighbourListA.size()) + "-" + String.valueOf(neighbourListB.size()) + ".txt";
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                      new FileOutputStream(fileName), "utf-8"))) {
                          writer.write(edgeListA);
                          writer.write(edgeListB);
        } catch (IOException e) {
            System.out.println ("File not found!");
        }

        System.out.println ("Successfully generated input file!");

    }

    public Set<Integer> buildMatchList (int size, int r) {
        // Build dataset of N connected and matched nodes per graph (returns matchList)
        Queue<Integer> candidateList = new LinkedList<Integer>();
        // Get the starting node
        int start = new Random().nextInt(r);
        getStartingNode:
        for (int i = 1; i < graph1.length; i++) {
            for (int j = i + 1; j < graph1.length; j++) {
                if (graph1[i-1][j-1] == 1) {
                    candidateList.add(i);
                    break getStartingNode;
                }
            }
        }
        System.out.println ("Retrieved starting node: " + start);

        Set<Integer> matchList = new HashSet<Integer>();
        while (matchList.size() < size) {
            Integer node = candidateList.poll();
            if (node <= r && solution[node-1] <= r) {
                matchList.add(node);
                // Add neighbours
                for (int i = node + 1; i < graph1.length; i++) {
                    if (graph1[node-1][i-1] == 1 && i <= r) {
                        candidateList.add(i);
                    }
                }
            }
        }

        for (int i : matchList) {
            System.out.println ("Added to Matchlist: A" + i);
        }
        return matchList;

    }

    public Set<Integer> buildCorrespondingMatchList (Set<Integer> matchListA) {
        // Create corresponding matchlist for B, given matchListA
        Set<Integer> matchListB = new HashSet<Integer>();
        for (int i : matchListA) {
            matchListB.add(solution[i-1]);
            System.out.println ("Added to list of matched vertices: B" + (i));
        }

        return matchListB;

    }

    public Set<Integer> buildNeighbourList (Set<Integer> matchList, int[][] vertices, int r) {
        // For every node, add all first-degree neighbours (returns neighbourList)
        Set<Integer> neighbourList = new HashSet<Integer>();
        for (int i : matchList) {
            for (int j = i + 1; j < vertices.length; j++) {
                if (vertices[i-1][j-1] == 1 && !matchList.contains(j) && j <= r) {
                    neighbourList.add(j);
                }
            }
        }
        return neighbourList;
    }

    public String getEdges (Set<Integer> matchList, Set<Integer> neighbourList, int[][] vertices, String prefix, int r) {
        // Add all edges of the match list
        String edgeList = "";
        String metadata = "";
        int edgeCount = 0;
        for (int i : matchList) {
            for (int j = i + 1; j < vertices.length; j++) {
                if (vertices[i-1][j-1] == 1 && j <= r) {
                    String temp = prefix + String.valueOf(i) + " " + prefix + String.valueOf(j) + "\n";
                    edgeList = edgeList + temp;
                    edgeCount++;
                }
            }
        }

        // Add certain edges of the neighbourList (Filter out vertices not in the complete list)
        for (int i : neighbourList) {
            for (int j = i + 1; j < vertices.length; j++) {
                if (vertices[i-1][j-1] == 1 && neighbourList.contains(j) && j <= r) {
                    String temp = prefix + String.valueOf(i) + " " + prefix + String.valueOf(j) + "\n";
                    edgeList = edgeList + temp;
                    edgeCount++;
                }
            }
        }
        int totalVertexCount = matchList.size() + neighbourList.size();
        // metadata = String.valueOf(totalVertexCount) + " " + String.valueOf(edgeCount) + "\n";
        int maxValue = Collections.max(matchList) > Collections.max(neighbourList) ? Collections.max(matchList) : Collections.max(neighbourList);
        metadata = maxValue + " " + String.valueOf(edgeCount) + "\n";


        return metadata + edgeList;
    }

}
