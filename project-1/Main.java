import java.lang.Math.*;
import java.util.LinkedList;
import java.lang.Integer;

public class Main{
    private Vertex[] points;
    private int N;

    public static void main(String args[]){
        Main obj = new Main();
        obj.run();
    }    
    public void run() {
        Kattio io = new Kattio(System.in, System.out);
        readTSPInstance(io);

        Vertex[] tour = points;

        //if(N < 40) {
         //   tour = initialTour();
        //} else {
        //    for(int i = 0;i < N;++i) 
        //        tour[i] = i;
        //}

        tour = improveTour(tour);
        for(int i = 0; i < N;++i) {
            io.println(tour[i].id);
        }
        io.println(totalDistance(tour));
        io.close();
    }	

    private void readTSPInstance(Kattio io){
        N = io.getInt();
        int i = 0;
        points = new Vertex[N];
        while (io.hasMoreTokens()) {
            Vertex v = new Vertex(i, io.getDouble(), io.getDouble());
            points[i] = v; 
            ++i; 
        }
    }

    // Naive algorithm
//    private int[] initialTour(){
//        int tour[] = new int[N];
//        boolean used[] = new boolean[N];
//        tour[0] = 0;
//        used[0] = true;
//        for(int i = 1;i < N; ++i) {
//            int best = -1;
//            for(int j = 0;j < N; ++j) {
//                if(used[j] != true) {
//                    if(best == -1)
//                        best = j;
//                    //else if(distanceMatrix[tour[i-1]][j] < distanceMatrix[tour[i-1]][best])
//                    else if(eucDistance(tour[i-1],j) < eucDistance(tour[i-1],best))
//                        best = j;
//                }
//            } 
//            tour[i] = best;
//            used[best] = true;
//        }
//
//        return tour;
//    }

    private int totalDistance(Vertex[] tour) {
        int length = 0;
        for (int i = 1; i < N; ++i) {
            length += eucDistance(tour[i-1], tour[i]);
        }
        length += eucDistance(tour[N-1],tour[0]);
        return length;
    }
    
    //2-opt Array
    private Vertex[] improveTour(Vertex[] tour){
        int best_distance = totalDistance(tour);
        int visited = 0, current = 0;
        while(visited < N) {
            Vertex currentVertex = tour[current];
            if(currentVertex.isActive()) {
                // use findMove 
                double gain = twoOptMove(current, currentVertex, tour);

                // move was found
                if(gain < 0) {
                    current = wrap(current-1, N);
                    visited = 0;
                    best_distance += gain;
                    continue;
                }
                currentVertex.setActive(false);
            }

            // no move wa found or the city was "inactive"
            // then we should investigate for next city
            current = wrap(current+1, N);
            visited++;	 
        }
        return tour;
    }

    // A more sophisticated approach to calculating distance
    private double calculateMove(Vertex x, Vertex y, Vertex c1, Vertex c2) {
        double x_y = eucDistanceCompare(x, y), c1_c2 = eucDistanceCompare(c1, c2);
        double x_c1 =  eucDistanceCompare(x, c1), y_c2 = eucDistanceCompare(y, c2);

        // triangle of equality => at least 1 edge is shorter
        // if both edges is longer, no move is to be made
        if(x_y < x_c1 && c1_c2 < y_c2)
            return 1;		

        // TODO: Implement eucDistance that does not make the square root calculation, 
        return (Math.sqrt(x_c1) + Math.sqrt(y_c2)) - (Math.sqrt(x_y) + Math.sqrt(c1_c2));
    }
    
    private double twoOptMove(int current, Vertex currentVertex, Vertex[] tour) {
        int prev = wrap(current-1, N);
        int next = wrap(current+1, N);
        Vertex prevVertex = tour[prev];
        Vertex nextVertex = tour[next];    
        
        
        for(int i = wrap(current+2, N), j = wrap(current+3, N); j != current;i = j, j = wrap(j+1, N)) {
            Vertex c1 = tour[i];
            Vertex c2 = tour[j];
            
            double delta_c1 = calculateMove(prevVertex, currentVertex, c1, c2);
            if(delta_c1 < 0) {
                activate(prevVertex, nextVertex, c1, c2);
                twoOptSwap(tour, Math.min(prev, i)+1, Math.max(prev, i));
                return delta_c1;
            } 
            double delta_c2 = calculateMove(currentVertex, nextVertex, c1, c2);
            if(delta_c1 < 0) {
                activate(prevVertex, nextVertex, c1, c2);
                twoOptSwap(tour, Math.min(current, i)+1, Math.max(current, i));
                return delta_c2;
            } 
        }
    return 0;
    }

    private void twoOptSwap(Vertex[] tour, int from, int to) {
        for(int i = from, j = to;i < j; ++i, --j) { 
            Vertex temp = tour[i];
            tour[i] = tour[j];
            tour[j] = temp;
        }
    }
    
    private void activate(Vertex x, Vertex y, Vertex c1, Vertex c2) {
        x.setActive(true); 
        y.setActive(true);
        c1.setActive(true);
        c2.setActive(true); 
    }
    
    private int wrap(int i, int max) {
        return (max+i) % max;
    }

    // TODO: Make new eucDistance with no sqrt if necessary
    private int eucDistance(Vertex i, Vertex j) {
        double x = Math.pow(i.x - j.x, 2);	
        double y = Math.pow(i.y - j.y, 2);
        return (int)Math.round(Math.sqrt(x+y));
    }
    private int eucDistanceCompare(Vertex i, Vertex j) {
        double x = i.x - j.x;
        double y = i.y - j.y;
        return (int) Math.round((x*x)+(y*y));
    }

    private class Vertex{
        int id;
        double x;
        double y;
        boolean active = true;
        private Vertex(int id, double x, double y){
            this.id = id;
            this.x = x;
            this.y = y;
        }
        private boolean isActive() {
            return active;
        }
        private void setActive(boolean bool) {
            active = bool;
        }
        private int getID() {
            return id;
        }
 
    }
}
