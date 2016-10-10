import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

public class Generate {
    public static void main (String args[]) {
        try {
            Scanner s = new Scanner(new FileReader("input.txt"));
            long startTime = System.currentTimeMillis();
            String[] temp = null;
            int a1, a2, b1, b2;
            // Input for Graph 1
            //System.out.println ("Creating Matrix 1");
            String[] input = s.nextLine().split("\\s+");
            int numberOfIntegers = Integer.parseInt(input[0]);
            int numberOfEdges = Integer.parseInt(input[1]);
            int[][] graph1 = new int[numberOfIntegers][numberOfIntegers];
            for(int i = 0; i < numberOfEdges; i++)
            {
                temp = s.nextLine().split("\\s+");
                a1 = Integer.parseInt(temp[0].substring(1));
                a2 = Integer.parseInt(temp[1].substring(1));
                graph1[a1-1][a2-1] = 1;
                graph1[a2-1][a1-1] = 1;
            }
            //System.out.println ("Creating Matrix 2");
            input = s.nextLine().split("\\s+");
            numberOfIntegers = Integer.parseInt(input[0]);
            numberOfEdges = Integer.parseInt(input[1]);
            int [][]graph2 = new int[numberOfIntegers][numberOfIntegers];
            for(int i = 0; i < numberOfEdges; i++)
            {
                temp = s.nextLine().split("\\s+");
                b1 = Integer.parseInt(temp[0].substring(1));
                b2 = Integer.parseInt(temp[1].substring(1));
                graph2[b1-1][b2-1] = 1;
                graph2[b2-1][b1-1] = 1;
            }

            DatasetGenerator dg = new DatasetGenerator (graph1, graph2);

            s = new Scanner (System.in);
            System.out.println ("Enter number of nodes to be matched: ");
            // Generates output file of size n
            int a = s.nextInt();
            System.out.println ("Enter size restriction: ");
            // Generates output file of size n
            int b = s.nextInt();
            dg.generate(a, b);

        } catch (IOException e) {
            System.out.println ("File not found!");
        }

    }
}
