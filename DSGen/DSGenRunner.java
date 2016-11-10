import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

public class DSGenRunner {
    public static void main (String args[]) {
        try {
            Scanner sc = new Scanner (System.in);
            //System.out.println ("Input G_c file name: ");
            //String gc_filename = sc.nextLine();
            String gc_filename = "gc-ribbon.txt";

            // Parsing G_c into a matrix
            Scanner s = new Scanner(new FileReader(gc_filename));
            String[] temp = null;
            int a1, a2;
            String[] input = s.nextLine().split("\\s+");
            int numberOfVertices = Integer.parseInt(input[0]);
            int numberOfEdges = Integer.parseInt(input[1]);
            int[][] gc = new int[numberOfVertices][numberOfVertices];
            for(int i = 0; i < numberOfEdges; i++)
            {
                temp = s.nextLine().split("\\s+");
                a1 = Integer.parseInt(temp[0].substring(1));
                a2 = Integer.parseInt(temp[1].substring(1));
                gc[a1-1][a2-1] = 1;
                gc[a2-1][a1-1] = 1;
            }

            // Accept n1, n2, e1, e2
            /*
            int n1, n2, e1, e2, d1, d2, as, sd;
            System.out.println ("Input n1, n2, e1, e2, d1, d2, avg. similarity, standard dev: ");
            temp = sc.nextLine().split("\\s+");
            n1 = Integer.parseInt(temp[0]);
            n2 = Integer.parseInt(temp[1]);
            e1 = Integer.parseInt(temp[2]);
            e2 = Integer.parseInt(temp[3]);
            d1 = Integer.parseInt(temp[2]);
            d2 = Integer.parseInt(temp[3]);
            as = Integer.parseInt(temp[4]);
            sd = Integer.parseInt(temp[5]);
            System.out.println ("Input dg1 (Enter n for none): ");
            String dg1 = sc.nextLine();
            System.out.println ("Input dg2 (Enter n for none): ");
            String dg2 = sc.nextLine();
            System.out.println ("Input file name: ");
            String fileName = sc.nextLine();

            DatasetGenerator dg = new DatasetGenerator (gc, n1, n2, e1, e2, d1, d2, as, sd, dg1, dg2, fileName);
            */
            DatasetGenerator dg = new DatasetGenerator (gc, 40, 20, 30, 20, 60, 30, 0, 0, "n", "dg-octopus.txt", "output.txt");
            dg.generate(); // Ouput G1 and G2
        } catch (IOException e) {
            System.out.println ("File not found!");
        }
    }
}
