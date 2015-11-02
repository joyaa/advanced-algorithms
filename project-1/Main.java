import java.lang.Math.*;
import java.util.LinkedList;
public class Main{
  private LinkedList<Vertex> points;
  private int N;
  public static void main(String args[]){
      Main obj = new Main();
      obj.run();
  }    
  public void run() {
    Kattio io = new Kattio(System.in, System.out);
    readTSPInstance(io);
    int tour[] = initialTour();
		for(int i = 0; i < N;++i) {
			System.out.println(tour[i]);
		}
    io.close();
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
					else if(eucDistance(tour[i-1],j) < eucDistance(tour[i-1], best))
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
  private void improveTour(){

  }		
  private class Vertex{
    double x;
    double y;
    private Vertex(double x, double y){
      this.x = x;
      this.y = y;
    }
  }
	private double eucDistance(int i, int j) {
		double x = Math.pow(points.get(i).x - points.get(j).x, 2);	
		double y = Math.pow(points.get(i).y - points.get(j).y, 2);
		return Math.sqrt(x+y);
	}
}
