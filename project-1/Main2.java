import java.lang.Math.*;
import java.util.LinkedList;
import java.lang.Integer;

public class Main2{
  private LinkedList<Vertex> points;
  private int N;
  private int distanceMatrix[][];


  public static void main(String args[]){
      Main2 obj = new Main2();
      obj.run();
  }    
  public void run() {
    Kattio io = new Kattio(System.in, System.out);
    readTSPInstance(io);

    //calculateDistanceMatrix();
    int tour[] = new int[N];
    
    if(N < 40) {
      tour = initialTour();
    } else {
      for(int i = 0;i < N;++i) 
        tour[i] = i;
    }

    SegmentTree tourST = createST(tour);

    if (N < 150)
      tour = improveTour(tour);
		for(int i = 0; i < N;++i) {
			io.println(tour[i]);
		}
    io.close();
  }	
	
  private SegmentTree createST(int[] tour) {
    return new SegmentTree(tour, N);
  }

  private void readTSPInstance(Kattio io){
    N = io.getInt();
    int i = 0;
    points = new LinkedList<Vertex>();
    while (io.hasMoreTokens()) {
      Vertex v = new Vertex(io.getDouble(), io.getDouble());
      points.add(v); 
      ++i; 
    }
  }

  // Naive algorithm
  private int[] initialTour(){
    int tour[] = new int[N];
   	boolean used[] = new boolean[N];
		tour[0] = 0;
		used[0] = true;
		//double distance[][] = new double[n][n];
    for(int i = 1;i < N; ++i) {
			int best = -1;
      for(int j = 0;j < N; ++j) {
				if(used[j] != true) {
					if(best == -1)
						best = j;
					//else if(distanceMatrix[tour[i-1]][j] < distanceMatrix[tour[i-1]][best])
					else if(eucDistance(tour[i-1],j) < eucDistance(tour[i-1],best))
            best = j;
				}
			} 
			tour[i] = best;
			used[best] = true;
    }

		return tour;
		/*	
    V = points.clone();
    LinkedList<Vertex> U = new LinkedList<Vertex();  
    u = V.pollFirst();
    while(u != null) {
      U.add(u);
    }*/
  }

  // private void calculateDistanceMatrix() {
  //   distanceMatrix = new int[N][N];
 
  //   for(int i = 0;i < N; ++i) 
  //     for(int j = 0;j < N; ++j) 
  //       distanceMatrix[i][j] = eucDistance(i, j);
  // }

  private int totalDistance(int[] tour) {
    int length = 0;
    for (int i = 1; i < N; ++i) {
      length += eucDistance(tour[i-1], tour[i]);
    }
    length += eucDistance(tour[N-1],0);
    return length;
  }

  // 2-opt ST
  private SegmentTree improveTour(SegmentTree tour){
    int [] best_tour = new int[N];
    int best_distance;
    int new_distance;
    int[] new_tour = new int[N];
    boolean improved = true, restart = false;
    while(improved) {
      best_distance = totalDistance(tour);
      restart = false;
      for(int i = 0; i < N-1;++i) {
        for(int j = i+1;j < N;++j) {
          new_tour = twoOptSwap(tour, i, j);
          new_distance = totalDistance(new_tour);
          if(new_distance < best_distance) {
            restart = true;
            System.arraycopy(new_tour, 0, best_tour, 0, N);
            break;
          } 
        }
        if(restart) 
          break;
      }
      if(!restart) {
        improved = false;
      }
    }
    return best_tour;
  }


  // 2-opt Array
  // private int[] improveTour(int[] tour){
  //   int [] best_tour = new int[N];
  //   int best_distance;
  //   int new_distance;
  //   int[] new_tour = new int[N];
  //   boolean improved = true, restart = false;
  //   while(improved) {
  //     best_distance = totalDistance(tour);
  //     restart = false;
  //     for(int i = 0; i < N-1;++i) {
  //       for(int j = i+1;j < N;++j) {
  //         new_tour = twoOptSwap(tour, i, j);
  //         new_distance = totalDistance(new_tour);
  //         if(new_distance < best_distance) {
  //           restart = true;
  //           System.arraycopy(new_tour, 0, best_tour, 0, N);
  //           break;
  //         } 
  //       }
  //       if(restart) 
  //         break;
  //     }
  //     if(!restart) {
  //       improved = false;
  //     }
  //   }
  //   return best_tour;
  // }


  private int[] twoOptSwap(int[] tour, int i, int j) {
    int temp = tour[i];
    tour[i] = tour[j];
    tour[j] = temp;
    return tour;
  }

	private int eucDistance(int i, int j) {
		double x = Math.pow(points.get(i).x - points.get(j).x, 2);	
		double y = Math.pow(points.get(i).y - points.get(j).y, 2);
		return (int)Math.round(Math.sqrt(x+y));
	}

    private class Vertex{
    double x;
    double y;
    private Vertex(double x, double y){
      this.x = x;
      this.y = y;
    }
  }
}
