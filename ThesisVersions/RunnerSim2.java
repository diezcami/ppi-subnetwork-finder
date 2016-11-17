//For edge lists
//No correspondence matrix
//Fixed variable names
//Heuristics: Number of weighted edges, connections in neighborhood, similarity
//Checks seq sim first. Edges = tiebreaker

import java.util.*;
import java.io.*;

public class RunnerSim2
{
	public static void main(String[] args)
	{
		try
		{
		long startTime = System.currentTimeMillis();
		Scanner s = new Scanner(System.in);
		System.out.println("Type in file name of input:");
		String filename = s.nextLine();
		Scanner s2 = new Scanner(new FileReader(filename));
		String[] temp = null;
		int graph1row, graph1col, graph2row, graph2col;
		//System.out.println ("Creating Matrix 1");
		String[] input = s2.nextLine().split("\\s+");
		int numberOfNodes = Integer.parseInt(input[0]);
		int numberOfEdges = Integer.parseInt(input[1]);
		int[][] graph1 = new int[numberOfNodes][numberOfNodes];
		for(int i = 0; i < numberOfEdges; i++)
		{
			temp = s2.nextLine().split("\\s+");
			graph1row = Integer.parseInt(temp[0].substring(1));
			graph1col = Integer.parseInt(temp[1].substring(1));
			graph1[graph1row-1][graph1col-1] = 1;
			graph1[graph1col-1][graph1row-1] = 1;
		}
		//System.out.println ("Creating Matrix 2");
		input = s2.nextLine().split("\\s+");
		numberOfNodes = Integer.parseInt(input[0]);
		numberOfEdges = Integer.parseInt(input[1]);
		int[][] graph2 = new int[numberOfNodes][numberOfNodes];
		for(int i = 0; i < numberOfEdges; i++)
		{
			temp = s2.nextLine().split("\\s+");
			graph2row = Integer.parseInt(temp[0].substring(1));
			graph2col = Integer.parseInt(temp[1].substring(1));
			graph2[graph2row-1][graph2col-1] = 1;
			graph2[graph2col-1][graph2row-1] = 1;
		}
		//SEQUENCE SIMILARITY INPUT
		System.out.println("Type in name of sequence similarity scores file:");
		String filename2 = s.nextLine();
		s2 = new Scanner(new FileReader(filename2));
		//int numberOfSims = Integer.parseInt(s.nextLine());
		int g1Dex = 0; 
		int g2Dex = 0;
		int corrDex = 0;
		int[]seqSim = new int[graph1.length*graph2.length];
		System.out.println(seqSim.length);
		Arrays.fill(seqSim, 0);
		
		while(s2.hasNextLine())
		{
			
			temp = s2.nextLine().split(" ");
			g1Dex = Integer.parseInt(temp[0].substring(1)) ;
			g2Dex = Integer.parseInt(temp[1].substring(1)) ;
			corrDex = (graph2.length*g1Dex) + g2Dex - graph2.length - 1;
			System.out.println("corr: " + corrDex);
			seqSim[corrDex] = Integer.parseInt(temp[2]);
		}

		cliqueFind(graph2, graph1, graph2.length, graph1.length, seqSim); //CHANGE HERE

		long stopTime = System.currentTimeMillis();
		System.out.println(stopTime - startTime);
		}
		catch (IOException e) {
            System.out.println ("File not found!");
        }
	}

	public static int getAdjacency (int graph1row, int graph1col, int graph2row, int graph2col, int[][]graph1, int[][]graph2) {

			if((graph1[graph1row][graph1col] == 1) && (graph2[graph2row][graph2col] == 1))
			{
				return 2;
			}
			else if ((graph1[graph1row][graph1col] == 0) && (graph2[graph2row][graph2col] == 0))
			{
				if(graph1row != graph1col && graph2row != graph2col) return 1;
				return 0;
			}
			else return 0;
	}

	public static int[][] findCorrespondenceMatrix (int[][] graph1, int[][] graph2)
	{
		int[][] correspondenceGraph = new int[graph1.length * graph2.length][graph1.length * graph2.length];
		for (int i = 0; i < correspondenceGraph.length; i++) { // Row
			for (int j = 0; j < correspondenceGraph.length; j++) { // Column
				double b1 = Math.floor(1.0 * i / correspondenceGraph.length * graph2.length);
				double b2 = Math.floor(1.0 * j / correspondenceGraph.length * graph2.length);
				if (getAdjacency (i % graph1.length, j % graph1.length, (int) b1, (int) b2, graph1, graph2 ) != 0) {
					correspondenceGraph[i][j] = getAdjacency (i % graph1.length, j % graph1.length, (int) b1, (int) b2, graph1, graph2 );
				}
			}
		}
		return correspondenceGraph;
	}
	public static String decoder(int aIndex, int length1, int length2)
	{
		int graph1 = aIndex/length2;
		int graph2 = aIndex - (length2 * graph1);
		return ("a" + (graph1+1) + " b" + (graph2+1) + " (" + (aIndex+1) + ")");
	}
	public static void cliqueFind(int[][]graph1, int[][]graph2, int length1, int length2, int[]seqSim)
	{
		File file = new File("sampleinput-answers-6.txt");
		PrintWriter printer = null;
		try
		{
			printer = new PrintWriter("sampleinput-answers-6.txt");
		}
		catch(FileNotFoundException fe){};
		int corrLength = graph1.length*graph2.length;
		int[] edges = new int[corrLength];
		PriorityQueue<Node> maxi = new PriorityQueue<Node>();
		int adj = 0;
		for(int i = 0; i < corrLength; i++)
		{
			if(i == corrLength/4) System.out.println("25%");
			if(i == corrLength/2) System.out.println("50%");
			int num = 0;
			for(int j = 0; j < corrLength; j++)
			{
				adj = getAdjacency(i % graph1.length, j % graph1.length, (int)Math.floor(1.0 * i / corrLength * graph2.length), (int)Math.floor(1.0 * j / corrLength * graph2.length), graph1, graph2);
				if(adj != 0) num+= adj;
			}
			edges[i] = num;
			maxi.add(new Node(i, 0, edges[i], seqSim[i]));
		}

		ArrayList<Integer> cliq = new ArrayList<Integer>();
		int bestSim = 0;
		String stack = "";
		int currTry = 0;

		for(int i = 0; i < Math.min(10, corrLength); i++)
		{
			ArrayList<Integer> temp = new ArrayList<Integer>();
			int currSim = 0;
			int top = maxi.poll().index;

			boolean checker = true;

			Node no = null;

			int counter = 0;
			int num = 0;
			Node test = null;

			PriorityQueue<Node> pq = new PriorityQueue<Node>();

			temp.add(top);
			currSim += seqSim[top];
			Node t = new Node(top, 0, 0, seqSim[top]);
			int adj1 = 0;
			int adj2 = 0;
			int adj3 = 0;
			for(int j = 0; j < corrLength; j++)
			{
				adj1 = getAdjacency(top % graph1.length, j % graph1.length, (int)Math.floor(1.0 * top / corrLength * graph2.length), (int)Math.floor(1.0 * j / corrLength * graph2.length), graph1, graph2);
				if(adj1 != 0)
				{
					for(int k = 0; k < corrLength; k++)
					{
						adj2 = getAdjacency(j % graph1.length, k % graph1.length, (int)Math.floor(1.0 * j / corrLength * graph2.length), (int)Math.floor(1.0 * k / corrLength * graph2.length), graph1, graph2);
						if(adj2 != 0)
						{
							num += adj2;
							adj3 = getAdjacency(k % graph1.length, top % graph1.length, (int)Math.floor(1.0 * k / corrLength * graph2.length), (int)Math.floor(1.0 * top / corrLength * graph2.length), graph1, graph2);
							if(adj3 != 0) counter+= adj3;
						}
					}
					test = new Node(j, counter, num, seqSim[j]);
					pq.add(test);
					//currSim += seqSim[j];
					counter = 0;
					num = 0;
				}
			}
			while(pq.peek() != null)
			{
				no = pq.poll();
				for(int k = 0; k < temp.size(); k++)
				{
					adj = getAdjacency(no.index % graph1.length, temp.get(k) % graph1.length, (int)Math.floor(1.0 * no.index / corrLength * graph2.length), (int)Math.floor(1.0 * temp.get(k) / corrLength * graph2.length), graph1, graph2);
					if(adj == 0)
					{
						checker = false;
						break;
					}
				}
				if(checker) 
				{
					temp.add(no.index);
					currSim += no.similarity;
				}
				checker = true;
			}

			//System.out.println("curr sim: " + currSim);
			if(currSim > bestSim)
			//if(temp.size() >= cliq.size())
			//if(temp.size() + currSim >= cliq.size() + bestSim)
			{
				bestSim = currSim;
				cliq = temp;
				stack = t.stacktrace;
				currTry = i;
			}
			else if(currSim == bestSim)
			{
				if(temp.size() >= cliq.size())
				{
					bestSim = currSim;
					cliq = temp;
					stack = t.stacktrace;
					currTry = i;
				}
			}
			currSim = 0;
		}

		printer.println("Clique: ");
		System.out.println("Current try: " + currTry);
		for(int i = 0; i < cliq.size(); i++)
		{
			printer.println(decoder(cliq.get(i), length2, length1)); //CHANGE HERE
			System.out.println(seqSim[cliq.get(i)]);
			//System.out.println("best sim: " + bestSim);
		}

		
		printer.close();
	}
	static class Node implements Comparable<Node>
	{
	int index;
	int neighbs;
	int edges;
	int similarity;
	String stacktrace;
	public Node(int a, int b, int c, int d)
	{
		index = a;
		neighbs = b;
		edges = c;
		similarity = d;
		stacktrace = "";
	}
	public void writeStack(String s)
	{
		stacktrace += s;
	}
	public int compareTo(Node other)
	{
    	int min = 0;
    	/*if((this.edges+this.neighbs+this.similarity)*-1 == (other.edges+other.neighbs+other.similarity)*-1)
    	{
    		min = Math.min(this.index+1, other.index+1);
    	}
    	return Integer.compare((this.edges+this.neighbs+this.similarity)*-1, (other.edges+other.neighbs+other.similarity)*-1);*/
    	/*if((this.similarity)*-1 == (other.similarity)*-1)
    	{
    		return Integer.compare((this.similarity)*-1, (other.similarity)*-1);
    	}
    	else return Integer.compare((this.edges+this.neighbs)*-1, (other.edges+other.neighbs)*-1);*/
    	if((this.similarity)*-1 == (other.similarity)*-1)
    	{
    		return Integer.compare((this.edges+this.neighbs)*-1, (other.edges+other.neighbs)*-1);
    	}
    	else return Integer.compare((this.similarity)*-1, (other.similarity)*-1);

	}
	}
}
