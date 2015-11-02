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
    initialTour();
    io.close();
  }	
	
  private void readTSPInstance(Kattio io){
    N = io.getInt();
    int i = 0;
    points = new LinkedList<Vertex>();
    while (io.hasMoreTokens()) {
      Vertex v = new Vertex(io.getDouble(), io.getDouble());
      points[i] = v; 
      ++i; 
    }
    System.out.println(points[0].x);
    System.out.println(points[0].y);
  }
  private void initialTour(){
    int tour[] = new tour[N];
    
    for(i = 0;i < N; ++i) {
      for(i = 0
    }
    V = points.clone();
    LinkedList<Vertex> U = new LinkedList<Vertex();  
    u = V.pollFirst();
    while(u != null) {
      U.add(u);
      
    }
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
}
