import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SolutionChecker {
    public static void main (String args[]) {
        Scanner sc = new Scanner (System.in);
        System.out.println ("Enter file name: ");
        String filename = sc.nextLine();
        HashMap<Integer,Integer> answers = parseAnswers(filename);
        int[] solution = parseSolutionSet();

        System.out.println("===================== RESULTS FOR FILENAME:" + filename + " =====================");
        String[] metadata = filename.replace(".txt", "").split("-");
        int graph1Size = Integer.parseInt(metadata[1]) +  Integer.parseInt(metadata[0]);
        int graph2Size = Integer.parseInt(metadata[2]) +  Integer.parseInt(metadata[0]);

        System.out.println ("Number of nodes in Graph A: " + graph1Size);
        System.out.println ("Number of nodes in Graph B: " + graph2Size);
        System.out.println ("Number of expected matches: " + metadata[0]);
        System.out.println ();
        System.out.println ("Actual number of matches: " + answers.size());
        compare (answers, solution, graph2Size);

    }

    public static HashMap<Integer,Integer> parseAnswers (String filename) {
        HashMap<Integer, Integer> answers = new HashMap<Integer, Integer>();

        try {
            Scanner sc = new Scanner(new FileReader(filename));
            // Every index corresponds to the A vertex index -1. The stored integer corresponds to the matched B graph solution.
            String[] temp = null;
            int a, b;
            while (sc.hasNext()) {
                temp = sc.nextLine().split("\\s+");
                a = Integer.parseInt(temp[0].substring(1));
                b = Integer.parseInt(temp[1].substring(1));
                //solution[a-1] = b;
                answers.put(a,b);
            }
        } catch (IOException e) {
            System.out.println ("File not found!");
        }

        return answers;
    }

    public static int[] parseSolutionSet () {
        int[] solution = new int[3000];

        try {
            Scanner sc = new Scanner(new FileReader("spinal_results.txt"));
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

        return solution;
    }

    public static void compare (HashMap<Integer,Integer> answers, int[] solution, int max) {
        int correctMappings = 0;
        int sameMappings = 0;
        // Get number and percentage mapped
        for (int i = 0; i < max; i++) {
            if (answers.get(i) != null && answers.get(i) == solution[i-1]) {
                correctMappings++;
            }

            if (answers.get(i) != null && answers.get(i) == i) {
                sameMappings ++;
            }
        }
        System.out.println ("Number of correct matches: " + correctMappings + "/" + answers.size());
        System.out.println ("Percentage of correct matches: " + (correctMappings * 100 / answers.size()));
        System.out.println ("Number of n=n matches: " + sameMappings + "/" + answers.size());

    }

}
