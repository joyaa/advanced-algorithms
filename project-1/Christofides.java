import java.util.*;

public class Christofides{

    public int[] solve(ArrayList<Vertex> points){
    		int N = points.size();
				int[][] distanceMatrix = calculateDistanceMatrix(points);

				int mst[]= MST(distanceMatrix , N);

				int match[][] = greedyMatch(mst,distanceMatrix, N);
				
		}


	//Distance between all points, graph in a matrix representation.
  private int[][] calculateDistanceMatrix(LinkedList<Vertex> points) {
    int N = points.size();
    int[][] dM = new int[N][N];
 		int dist;

    for(int i = 0; i < N; ++i) {
      for(int j = i+1; j < N; ++j) { //POSSIBLE SPEEDUP CHECK! 
        
      	if (i == j) {
      		dM[i][j] = 0;
      		dM[j][i] = 0;
      		break;
      	}
				double x = Math.pow(points.get(i).x - points.get(j).x, 2);	
				double y = Math.pow(points.get(i).y - points.get(j).y, 2);
				dist = (int) Math.round(Math.sqrt(x+y));
    		
    		dM[i][j] = dist;
      	dM[j][i] = dist;
      }
    }
  }

	  // find MST using Prim's Algorithm
  	// https://en.wikipedia.org/wiki/Prim%27s_algorithm
  	// http://cs.fit.edu/~ryan/java/programs/graph/Prim-java.html
    public int[] MST(int[][] dM,int N){

			boolean inMST[] = new boolean[N]; // has been visited
			double cost[] = new double[N]; //avstånd från nod i och nod parent[i].
			int pred[] = new int[N]; //predecessor node in tree

			for(int i=0;i<N;i++){
					cost[i] = 2*(10^6) + 1; //cost between each node and cheapest connection
			}

			//unvisited nodes, starting with all
			Vector unvisited = new Vector();
			for(int i = 0; i < N; i++)
				unvisited.add(i);

			int currentNode = 0; // starting node
			cost[0]=0; //

			double tempCost;
			Integer newNode;

			while (!unvisited.isEmpty()) {
					
					inMST[currentNode] = true; //added in tree
					unvisited.removeElement(currentNode);

					for(int v = 0; v < N; v++){
							if( !inMST[v] && (dM[currentNode][v] < cost[v]) ) {
									//update lowest cost neigbour
									cost[v] = dM[currentNode][v];
									pred[v] = currentNode; //add current node to MST path
							}
					}

					// find min cost vertex
					double minCost = 2*(10^6)+2;
					for(int i = 0; i < unvisited.size(); i++){
							
							newNode = (Integer) unvisited.elementAt(i); ////prioqueue istället???
							
							tempCost = cost[newNode.intValue()];

							if(tempCost < minCost){
									currentNode = newNode.intValue();
									minCost = tempCost;
							}
					}
			}
			
			return pred;
    }


    // http://www.cs.cornell.edu/Courses/cs6820/2014fa/matchingNotes.pdf
    // http://people.cs.uchicago.edu/~laci/HANDOUTS/greedymatching.pdf
    public int[][] greedyMatch(int[] mst, int[][] dM, int N){

    	int[] countDegrees = new int[N];

    	//count degrees of all nodes in MST
    	for (int i = 0; i < N; ++i) {
    			countDegrees[i]++;
    			if (!(mst[mst[i]] == i))
    				countDegrees[mst[i]]++;    			

    	}

    	int oddsN = 0;
    	//save odd degree nodes from MST
    	ArrayList<Integer> oddNodes = new ArrayList();
    	for (int i = 0; i < N; i++) {
    		if (!(countDegrees[i] % 2 == 0)) {
    			oddNodes.add(i);	//add the node with odd degree
    			oddsN++;
    		}
    	}
    	//now G' is the graph of all odd nodes with all edges between them 

    	Edge edges[][] = new Edge[oddsN][oddsN];

			//greedy method

			//sort all edges between the odd edges
			for(int from : oddNodes) {
					for(int to : oddNodes) {
							if(from != to)
									edges[from][to] = new Edge(from, to, dM[from][to]);
							else
									edges[from][to] = new Edge(from, to, 2*10^6);
					}
					Arrays.sort(edges[from]); 
			}

			boolean matched[] = new boolean[N];
			int match[][] = new int[(oddsN/2)][2];

			int k = 0;
			for(int i = 0; i < oddsN; i++) {
					for(int j = 0; j < oddsN; j++) {
							int from = edges[i][j].from;
							int to = edges[i][j].to;
							if( matched[from] || matched[to] ) //cant use edge as in matching
									continue;
							else {
									// add both nodes to matched as edge between them
									matched[from] = true;
									matched[to] = true;
									match[k][0] = from;
									match[k][1] = to;
									k++;
							}
					}
			}

			return match;
	}

  public class Edge {
  int from;
  int to;
  double cost;

    public Edge(int from, int to, double cost) {
				this.from = from;
				this.to = to;
				this.cost = cost;
    }
	}

}
