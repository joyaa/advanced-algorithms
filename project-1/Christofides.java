import java.util.*;

public class Christofides{

	public Vertex[] solve(Vertex[] points){
		int N = points.length;

		// int[][] distanceMatrix = calculateDistanceMatrix(points, N);
		// System.err.println("Calced distance matrix!" + distanceMatrix.length);

		// int mst[]= mst(distanceMatrix , N);
		// System.err.println("Calced mst!" + mst.length);

		// int match[][] = matching(mst,distanceMatrix, N);
		// System.err.println("Calced macthing!" + match.length);


		// ArrayList<Integer> nodes[] = multiGraph(mst, match);
		// System.err.println("Calced multigraph!" + nodes.length);
		// //System.out.println("Multigraph: "+ nodes.length);


		// int tspRoute[] = eulerCycle(nodes);
		// System.err.println("Calced tspRoute!"+ tspRoute.length);
		ArrayList<Integer>[] nodes = getMultiGraph(points, N);

		int tspRoute[] = eulerCycle(nodes);

		Vertex[] route = new Vertex[N];

		for (int i = 0; i < tspRoute.length; i++) {
			route[i] = points[tspRoute[i]];
			//System.out.println(tspRoute[i]);
		}
		return route;
	}

	public ArrayList<Integer>[] getMultiGraph(Vertex[] p, int N){
		int[][] distanceMatrix = calculateDistanceMatrix(p, N);
		int mst[]= mst(distanceMatrix , N);
		int match[][] = matching(mst,distanceMatrix, N);
		return multiGraph(mst, match);
	}

	public class Edge implements Comparable{
	int from;
	int to;
	double cost;

	    public Edge(int from, int to, double cost) {
			this.from = from;
			this.to = to;
			this.cost = cost;
	    }

	    public int compareTo(Object o) {
	    	Edge e = (Edge) o; //Correct????
	    	if(this.cost == e.cost)
	    		return 0;
	    	else if (this.cost > e.cost) 
	    		return 1;
	    	else 
	    		return -1;
	    }

	}

	//Distance between all points, graph in a matrix representation.
	private int[][] calculateDistanceMatrix(Vertex[] points, int N) {
		int[][] dM = new int[N][N];
				int dist;

		for(int i = 0; i < N; ++i) {
		  for(int j = i+1; j < N; ++j) { //POSSIBLE SPEEDUP CHECK j = i+1! 
		    
				//double x = Math.pow(points.get(i).x - points.get(j).x, 2);	
				//double y = Math.pow(points.get(i).y - points.get(j).y, 2);
				double x = Math.pow(points[i].x - points[j].x, 2);
				double y = Math.pow(points[i].y - points[j].y, 2);

				dist = (int) Math.round(Math.sqrt(x+y));
			
				dM[i][j] = dist;
	  		dM[j][i] = dist;
		  	
		  }
		}
		return dM;
	}


	  // find MST using Prim's Algorithm, Prims as have a lot of edges (but fibonacci heap?)
		// https://en.wikipedia.org/wiki/Prim%27s_algorithm
		// http://cs.fit.edu/~ryan/java/programs/graph/Prim-java.html
	public int[] mst(int[][] dM,int N){

			boolean inMST[] = new boolean[N]; // has been visited
			double cost[] = new double[N]; //distance between two adjacent nodes
			int pred[] = new int[N]; //predecessor node in tree

			for(int i=0;i<N;i++){
				cost[i] = 2*(10^6); //cost between each node and cheapest connection
			}

			//unvisited nodes, starting with all
			ArrayList<Integer> unvisited = new ArrayList();
			for(int i = 0; i < N; i++)
				unvisited.add(new Integer(i));

			int currentNode = 0; // starting node
			cost[0] = 0; //

			double tempCost;
			//Integer newNode;
			double minCost;

			while (!unvisited.isEmpty()) {
				
				inMST[currentNode] = true; //added in tree
				unvisited.remove(new Integer(currentNode)); //remove element specified by object

				//everyone has everyone as neighbour, therefore just go through all edges everytime
				for(int v = 0; v < N; v++){
					if( !inMST[v] && (dM[currentNode][v] < cost[v]) ) {
						//update lowest cost neigbour
						cost[v] = dM[currentNode][v];
						pred[v] = currentNode; //add current node to MST path

					}
				}

				// find min cost vertex
				minCost = 2*(10^6)+1;
				for(Integer newNode : unvisited){
					//newNode = (Integer) unvisited.elementAt(i); ////prioqueue istället???
					
					tempCost = cost[newNode];

					if(tempCost < minCost){
						currentNode = newNode;
						minCost = tempCost;
					}
				}
			}
			// System.out.println("Unvisited: " + unvisited.size());
			// int k = 0;
			// for (int i = 0; i < inMST.length; i++) {
			// 	if (inMST[i]) { k++;}
			// }
			//System.out.println("Nodes in MST: " + k);
			
			//System.out.println(Arrays.toString(pred));
			return pred;
	}


	public ArrayList<Integer> countOddNodes (int[] tree, int N) {
		int[] countDegrees = new int[N];

		//count degrees of all nodes in MST
		for (int i = 1; i < N; ++i) {
			countDegrees[i]++;
			if (tree[tree[i]] != i)
				countDegrees[tree[i]]++;    			
		}

		int oddsN = 0;
		//save odd degree nodes from MST
		ArrayList<Integer> oddNodes = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			//int c = countDegrees[i];
			//if (((c/2)*2) == c) {
			if (countDegrees[i] % 2 != 0) {
				oddNodes.add(i);	//add the node with odd degree
				oddsN++;
			}
		}
		return oddNodes;
	}
 
	// http://www.cs.cornell.edu/Courses/cs6820/2014fa/matchingNotes.pdf
	// http://people.cs.uchicago.edu/~laci/HANDOUTS/greedymatching.pdf
	public int[][] matching(int[] mst, int[][] dM, int N){

		ArrayList<Integer> oddNodes = countOddNodes(mst, N);
		int oddsN = oddNodes.size();
		//now G' is the graph of all odd nodes with all edges between them 
		//System.out.println("Odd nodes: " + oddsN);
		Edge edges[][] = new Edge[oddsN][oddsN];

		//greedy method to find min matching
		//System.out.println("oddsN: " + oddsN);
		//sort all edges between the odd edges
		for(int i = 0; i < oddsN; i++) {
			for(int j = 0; j < oddsN; j++) {
				int from = oddNodes.get(i);
				int to = oddNodes.get(j);
				if(from != to) {
					edges[i][j] = new Edge(from, to, dM[from][to]);
				}
				else {
					edges[i][j] = new Edge(from, to, 2*10^6);

				}
			}
			Arrays.sort(edges[i]); //sort in ascending order (shortest edge first)
		}

		boolean matched[] = new boolean[N];
		int match[][] = new int[(oddsN/2)][2]; //nr of odd numbered nodes divisble by 2 ???  

		//choose shortest edge from every edge, or following shortest if earlier used.
		int k = 0;
		int from, to;
		for(int i = 0; i < oddsN; i++) {
			for(int j = 0; j < oddsN; j++) {
				from = edges[i][j].from;
				to = edges[i][j].to;
				if (from == to) 
					continue;
				else if( matched[from] || matched[to] ) //cant use edge as already in matching
					continue;
				else {
					// add both nodes to matched as edge between them
					match[k][0] = from;
					match[k][1] = to;
					matched[from] = true;
					matched[to] = true;
					k++;
				}
			}
		}

		return match;
	}

	private ArrayList<Integer>[] multiGraph(int[] mst, int[][] match) {
		int mstN = mst.length; //all nodes
		//neighbourlist matrix
		ArrayList<Integer> nodes[] = new ArrayList[mstN];
		//create the graph with length of number of nodes in MST < matching
		for(int i = 0; i < mstN; i++){
			nodes[i] = new ArrayList();
		}
		
		//add nodes and neighbours from MST lägg till, both ways.
		for(int i = 1; i < mstN; i++){ // 1 ?????
			if (i != mst[i] ) { 
				nodes[i].add(mst[i]);	
				nodes[mst[i]].add(i);
			}
		}
		
		//add nodes from matching lägg till, both ways.
		for(int i = 0; i < match.length; i++){
			if (i != match[i][1]) {
				nodes[match[i][0]].add(match[i][1]);
				nodes[match[i][1]].add(match[i][0]);
			}
		}

		// for(int i = 0; i < mstN; i++){
		// 	System.out.println(nodes[i].toString());
		// }

		return nodes;
	}

	// possible as the graph now only has even-degree vertices
	private int[] eulerCycle(ArrayList<Integer> graph[]) {
			
		// 		for (ArrayList<Integer> list : graph ){
		// 	System.out.println("\n");
		// 	for (int i = 0; i < list.size(); i++) {
		// 		System.out.print(list.get(i));
		// 	}
		// }

		ArrayList<Integer> path = new ArrayList<Integer>();

		int N = graph.length;
		//Hierholzers Algorithm
		//goo.gl/GxdGY2

		//neighbour matrix
		// int[] neigbourCount = new int[N];
		// boolean[][] nm = new boolean[N][N];
		// for (int i = 0; i<N; i++) {
		// 	for (int node : graph[i]) {
		// 		if (nm[i][node] == false ){
		// 			neigbourCount[i]++;
		// 			neigbourCount[node]++;
		// 		}	
		// 		nm[i][node] = true;
		// 		nm[node][i] = true;

		// 	}
		// } 

		// //Find starting cycle
		// int root = 0;
		// path.add(root);

		// int next = graph[root].get(0);

		// int prev = root;

		// do {
		// 	if(next != root)
		// 		path.add(next);
			
		// 	nm[root][next] = false;
		// 	nm[next][root] = false;
		// 	neigbourCount[root]--;
		// 	neigbourCount[next]--;

		// 	prev = next;
		// 	next = graph[next].get(0);

		// } while (prev != root);


		//Find starting cycle
		int root = 0; //starting vertex, could be randomized
		path.add(root);

		int next = graph[root].get(0);
		int prev = root;
		ListIterator<Integer> li;

		do {
			if(next != root)
				path.add(next);
			
			graph[prev].remove(0); //remove edge from current to next
			
			li = graph[next].listIterator();
			while (li.hasNext()) { //remove edge from previous to current
				if (li.next() == prev) {
					li.remove();
					break;
				}
			}
			prev = next;
			next = graph[next].get(0);
		} while (prev != root);
		

		// for (ArrayList<Integer> list : graph ){
		// 	System.out.println("\n");
		// 	for (int i = 0; i < list.size(); i++) {
		// 		System.out.print(list.get(i));
		// 	}
		// }
		// System.out.println("Path:\n");
		// for (int i = 0; i < path.size(); i++) {
		// 		System.out.print(path.get(i));
		// }
		
		//for every nodein path, if it has unused edges,
		//create cycle from those and add to path

		int p = 0;
		int tempRoot;
		int fuck = 0;
		int fuck1 = 0;
		int fuck2 = 0;
		while(p < path.size()) {
				tempRoot = path.get(p);
				fuck++;
				//System.out.println("cycle: " + p);
				if(graph[tempRoot].size() > 0) {
						//pathnode has unused edges
						ArrayList<Integer> tempPath = new ArrayList();

						tempPath.add(tempRoot);

						int tempPrev = tempRoot;
						int tempNext = graph[tempRoot].get(0);

						//find new cycle to add to path									
						do {
							if(tempNext != tempRoot)
								tempPath.add(tempNext);

							graph[tempPrev].remove(0);

							li = graph[tempPrev].listIterator();
							fuck1++;
							while (li.hasNext()) {
								fuck2++;
								if (li.next() == tempPrev) {
									li.remove();
									break;
								}
							}
							tempPrev = tempNext;
							if(tempNext != tempRoot)
								tempNext = graph[tempPrev].get(0);
						} while (tempPrev != tempRoot) ;

						if(0 < tempPath.size()) {
								//insert the new cycle to the path at where the node had the unused edges
							int i = 0;
							for(int node : path) {
								if(node == tempRoot) {
									tempPath.remove(0);
									path.addAll(i, tempPath);
									break;
								}
								i++;
							}
							//System.out.println(path.toString());
						}
						//p = 0;
				} else 
					p++;
			}
			//System.out.println("Loops: "+ fuck + " " + fuck1 + " " + fuck2);
			//System.out.println("Path: "+ path.toString());
			//System.out.println("Path length: "+ path.size());
			//find shortcuts that skips vertices appearing more than once
			int[] tspRoute=new int[N];
			boolean included[]=new boolean[N];
			p=0;
			for(int i=0;i<path.size();i++){
					int node = path.get(i);
					if(!included[node]) {
							tspRoute[p] = node;								
							included[node] = true;
							p++;
					}
			}
			if(p != graph.length) 
				//System.out.println("Constructed TSP route too short");
			
			for (int i = 0; i < N; i++) {
			//	System.out.println("Route: "+ tspRoute[i]);
			}

			return tspRoute;				
	}

}