import java.lang.Math.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.LinkedList;
import java.lang.Integer;
import java.util.*;

public class Main{
    private Vertex[] points;
    private int N, max; // start0, start1;
    private int[] dcache;

    public static void main(String args[]){
        Main obj = new Main();
        obj.run();
    }    
    public void run() {
        Kattio io = new Kattio(System.in, System.out);
        readTSPInstance(io);

        int[] tour = new int[N];
        
        max = 0; 
        int min = 2*10^6;
        int distance;
        dcache = new int[N*N];
        for (int i = 0; i < N; i++) {
            tour[i] = i;
            for (int j = i+1; j < N; j++) {
                distance = eucDistanceCompare(i, j);
                dcache[i*N+j] = distance;
                dcache[j*N+i] = distance;
                if (distance > max)
                    max = distance;
                // if (distance < min){
                //     min = distance;
                //     start0 = i;
                //     start1 = j;
                // }
            }
        }
        
        //tour = initialTour();
        tour = greedy();
        //Christofides ch = new Christofides();
        //tour = ch.christofides(dcache);

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

    //http://lcm.csa.iisc.ernet.in/dsa/node186.html
    private int[] greedy() {
        int tour[] = new int[N];
        int degree[] = new int[N];

        // starting with random edge
        //Random r = new Random();
        //int start = r.nextInt(N);
        
        // starting with shortest edge
        // tour[0] = start0;
        // degree[start0]++;
        // tour[1] = start1;
        // degree[start1] = 2;
        // int i = start1;

        // starting with 0
        tour[0] = 0;
        degree[0]++;

        int i = 0;
        int shortest, dist;
        int iTemp = 0;
        int length = 0; //nr of edges, 1 when starting with shortest
        do {
            shortest = max+1;
            for (int j = 0; j < N; ++j) {
                dist = distance(i,j);
                if ((i != j) && distance(i,j) < shortest) {
                    if (degree[j] < 2) {
                        if ((degree[j] == 0 && length < N-1) || (degree[j] == 1 && length == N-1)) {
                            iTemp = j;
                            shortest = dist;
                        }
                    }
                } 
            }
            ++length;
            if (length == N) {
               break;
            }
            tour[length] = iTemp; 
            degree[iTemp] = 2;
            i = iTemp;
            
        } while (length < N);
        return tour;
    }

    private int[] initialTour(){
        int tour[] = new int[N];
        boolean used[] = new boolean[N];
        tour[0] = 0;
        used[0] = true;
        for(int i = 1; i < N; ++i) {
            int best = -1;
            for(int j = 0; j < N; ++j) {
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
