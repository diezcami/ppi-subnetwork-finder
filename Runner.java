//For edge lists
//No correspondence matrix

import java.util.*;
import java.io.*;

public class Runner
{
	public static void main(String[] args)
	{
		long startTime = System.currentTimeMillis();
		Scanner s = new Scanner(System.in);
		String[] temp = null;
		int a1, a2, b1, b2;
		// Input for Graph 1
		//System.out.println ("Creating Matrix 1");
		String[] input = s.nextLine().split("\\s+");
		int numberOfNodes = Integer.parseInt(input[0]);
		int numberOfEdges = Integer.parseInt(input[1]);
		int[][] graph1 = new int[numberOfNodes][numberOfNodes];
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
		numberOfNodes = Integer.parseInt(input[0]);
		numberOfEdges = Integer.parseInt(input[1]);
		int[][] graph2 = new int[numberOfNodes][numberOfNodes];
		for(int i = 0; i < numberOfEdges; i++)
		{
			temp = s.nextLine().split("\\s+");
			b1 = Integer.parseInt(temp[0].substring(1));
			b2 = Integer.parseInt(temp[1].substring(1));
			graph2[b1-1][b2-1] = 1;
			graph2[b2-1][b1-1] = 1;
		}

		//int[][] correspondenceGraph = findCorrespondenceMatrix (graph2, graph1); //CHANGE HERE. Switched graph1 and graph2
		cliqueFind(graph2, graph1, graph2.length, graph1.length); //CHANGE HERE

		long stopTime = System.currentTimeMillis();
		System.out.println(stopTime - startTime);
	}

	public static int getAdjacency (int a1, int a2, int b1, int b2, int[][]graph1, int[][]graph2) {
			//return (((graph1[a1][a2] == 1) && (graph2[b1][b2] == 1)) || ((graph1[a1][a2] == 0) && (graph2[b1][b2] == 0)) && a1 != a2 && b1 != b2) ? 1 : 0;

			if((graph1[a1][a2] == 1) && (graph2[b1][b2] == 1))
			{
				return 2;
			}
			else if ((graph1[a1][a2] == 0) && (graph2[b1][b2] == 0))
			{
				if(a1 != a2 && b1 != b2) return 1;
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
	public static String decoder(int a, int l1, int l2)
	{
		int graph1 = a/l2;
		int graph2 = a - (l2 * graph1);
		return ("a" + (graph1+1) + " b" + (graph2+1) + " (" + (a+1) + ")");
	}
	public static void cliqueFind(int[][]g1, int[][]g2, int length1, int length2) //CHANGE HERE
	{
		File file = new File("sampleinput-answers.txt");
		PrintWriter p = null;
		try
		{
			p = new PrintWriter("sampleinput-answers.txt");
		}
		catch(FileNotFoundException fe){};
		int corrLength = g1.length*g2.length;
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
				adj = getAdjacency(i % g1.length, j % g1.length, (int)Math.floor(1.0 * i / corrLength * g2.length), (int)Math.floor(1.0 * j / corrLength * g2.length), g1, g2);
				if(adj != 0) num+= adj;
			}
			edges[i] = num;
			maxi.add(new Node(i, 0, edges[i]));
		}

		ArrayList<Integer> cliq = new ArrayList<Integer>();
		String stack = "";

		for(int i = 0; i < Math.min(10, corrLength); i++)
		{
			ArrayList<Integer> temp = new ArrayList<Integer>();
			//int top = 0;
			//int most = 0;
			int top = maxi.poll().index;
			//decoder(top, length1, length2);
			/*System.out.println("--START--");
			for(int r = 0; r < a[top].length; r++)
			{
				if(a[top][r] != 0) decoder(r, length1, length2); //System.out.print(r + " ");
			}
			System.out.println("--END--");*/

			boolean checker = true;

			Node no = null;

			int counter = 0;
			int num = 0;
			Node test = null;

			PriorityQueue<Node> pq = new PriorityQueue<Node>();

			temp.add(top);
			Node t = new Node(top, 0, 0);
			//t.writeStack("STARTING VERTEX: " + (top+1) + " " + edges[top] +"\n");
			int adj1 = 0;
			int adj2 = 0;
			int adj3 = 0;
			for(int j = 0; j < corrLength; j++)
			{
				adj1 = getAdjacency(top % g1.length, j % g1.length, (int)Math.floor(1.0 * top / corrLength * g2.length), (int)Math.floor(1.0 * j / corrLength * g2.length), g1, g2);
				if(adj1 != 0)
				{
					for(int k = 0; k < corrLength; k++)
					{
						adj2 = getAdjacency(j % g1.length, k % g1.length, (int)Math.floor(1.0 * j / corrLength * g2.length), (int)Math.floor(1.0 * k / corrLength * g2.length), g1, g2);
						if(adj2 != 0)
						{
							num += adj2;
							adj3 = getAdjacency(k % g1.length, top % g1.length, (int)Math.floor(1.0 * k / corrLength * g2.length), (int)Math.floor(1.0 * top / corrLength * g2.length), g1, g2);
							if(adj3 != 0) counter+= adj3;
						}
					}
					test = new Node(j, counter, num);
					pq.add(test);
					//System.out.println(test.stacktrace);
					//if(test.stacktrace.length() > 2) t.writeStack(test.stacktrace + "\n");
					counter = 0;
					num = 0;
				}
			}
			while(pq.peek() != null)
			{
				no = pq.poll();
				for(int k = 0; k < temp.size(); k++)
				{
					adj = getAdjacency(no.index % g1.length, temp.get(k) % g1.length, (int)Math.floor(1.0 * no.index / corrLength * g2.length), (int)Math.floor(1.0 * temp.get(k) / corrLength * g2.length), g1, g2);
					if(adj == 0)
					{
						checker = false;
						break;
					}
				}
				if(checker) temp.add(no.index);
				checker = true;
			}
			if(temp.size() >= cliq.size())
			{
				cliq = temp;
				stack = t.stacktrace;
			}
		}

		//System.out.println("length1: " +length1);
		//System.out.println("length2: " +length2);
		p.println("Clique: ");
		for(int i = 0; i < cliq.size(); i++)
		{
			//System.out.println(cliq.get(i));
			p.println(decoder(cliq.get(i), length2, length1)); //CHANGE HERE
			//System.out.print((cliq.get(i)) + " ");
		}
		//System.out.println("");
		//System.out.println(stack);
		p.close();
	}
	static class Node implements Comparable<Node>
	{
	int index;
	int neighbs;
	int edges;
	String stacktrace;
	public Node(int a, int b, int c)
	{
		index = a;
		neighbs = b;
		edges = c;
		stacktrace = "";
	}
	public void writeStack(String s)
	{
		stacktrace += s;
	}
	public int compareTo(Node other)
	{
    	int min = 0;
    	/*if(Integer.compare(this.neighbs, other.neighbs) == 0) return Integer.compare(this.edges, other.edges);
    	else return Integer.compare(this.neighbs, other.neighbs);*/
    	if((this.edges+this.neighbs)*-1 == (other.edges+other.neighbs)*-1)
    	{
    		min = Math.min(this.index+1, other.index+1);
    		//writeStack("TIE: " + (this.index+1) + " " + (other.index+1) + " CHOSEN FIRST: " + min);
    		//System.out.println(stacktrace);
    	}
    	return Integer.compare((this.edges+this.neighbs)*-1, (other.edges+other.neighbs)*-1);
    	//IF EQUAL ADD NODES TO STACK TRACE


	}
	}
}
