import java.lang.Math.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.LinkedList;
import java.lang.Integer;

public class Main{
    private Vertex[] points;
    private int N;
    private int[] dcache;

    public static void main(String args[]){
        Main obj = new Main();
        obj.run();
    }    
    public void run() {
        Kattio io = new Kattio(System.in, System.out);
        readTSPInstance(io);

        int[] tour = new int[N];
        
        dcache = new int[N*N];
        for (int i = 0; i < N; i++) {
            tour[i] = i;
            for (int j = i; j < N; j++) {
                dcache[i*N+j] = eucDistanceCompare(i, j);
                dcache[j*N+i] = dcache[i*N+j];
            }
        }
        
        tour = initialTour();

        improveTourv2(tour);

        for(int i = 0; i < N;++i) {
            io.println(tour[i]);
        }
        io.close();
    }   
    
    private int distance(int i, int j) {
        return dcache[i*N+j];
    }

    private void readTSPInstance(Kattio io){
        N = io.getInt();
        int i = 0;
        points = new Vertex[N];
        while (io.hasMoreTokens()) {
            Vertex v = new Vertex(io.getDouble(), io.getDouble());
            points[i] = v; 
            ++i; 
        }
    }

    private int[] initialTour(){
        int tour[] = new int[N];
        boolean used[] = new boolean[N];
        tour[0] = 0;
        used[0] = true;
        for(int i = 1;i < N; ++i) {
            int best = -1;
            for(int j = 0;j < N; ++j) {
                if(used[j] != true) {
                    if(best == -1)
                        best = j;
                    else if(distance(tour[i-1], j) < distance(tour[i-1],best))
                        best = j;
                }
            } 
            tour[i] = best;
            used[best] = true;
        }
        return tour;
    }

    private void improveTourv2(int[] tour) {
        int mini = -1;
        int minj = -1;
        int minchange; 
        int look;
        do {
            minchange = 0;
            for(int i = 0; i < N-2;++i) {
                for(int j = i+2; j < N;++j) {
                    if(j+1 == N)
                        look = 0; 
                    else
                        look = j+1;
                    int change = distance(tour[i], tour[j]) + distance(tour[i+1], tour[look])
                                 - distance(tour[i], tour[i+1]) - distance(tour[j], tour[look]); 
                    
                    if(minchange > change) {
                        minchange = change;
                        mini = i; minj = j;
                    }
                }
            }
            if(minchange == 0) {
                break;
            }
            tour = twoOptSwap(tour, mini, minj);
        } while ( minchange < 0);
    }
    
    private int[] twoOptSwap(int[] tour, int from, int to) {
        if(from == to)
            return tour;
        for(int i = from + 1, j = to;i < j; ++i, --j) { 
            int temp = tour[i];
            tour[i] = tour[j];
            tour[j] = temp;
        }
        return tour;
    }
    
    private int wrap(int i, int max) {
        return (max+i) % max;
    }

    private int eucDistanceCompare(int i, int j) {
        double x = points[i].x - points[j].x;
        double y = points[i].y - points[j].y;
        return (int) Math.round(Math.sqrt((x*x)+(y*y)));
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
