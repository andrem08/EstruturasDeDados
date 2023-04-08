//Nome: André Miyazawa
//Nusp: 11796187
import java.util.Random;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        testaAlgoritmos(10000);
        //allIndex(1000, 0.01, 10, true);
        //test(10, 10000, 1, 1000, true);
    }

    static void teste(){
        int[] start;
        int[] end;
        Scanner sc = new Scanner(System.in);
        String[] dim = sc.nextLine().split(" ");
        int[][] matrix = new int[Integer.parseInt(dim[1])][Integer.parseInt(dim[0])];
        for (int i = 0; i < Integer.parseInt(dim[1]); i++){
            char[] aux = sc.nextLine().toCharArray();
            for (int j = 0; j < aux.length; j++) {
                if (aux[j] == 'E') {
                    end = new int[]{i, j};
                    aux[j] = 0;
                }
                else if(aux[j] == 'H') {
                    
                    
                }
                else if(aux[j] == '.')
                matrix[i][j] = Character.getNumericValue(aux[j]);
            }
        }
    }

    static boolean testaAlgoritmos(int n) {
        MakeGraph make = new MakeGraph();
        ShortestPaths solve = new ShortestPaths();
        for (int i = 0; i < n; i++) {
            Graph g = make.genericGraph(10,0.3, 10);
            //Graph g = make.genericGraph(1000,0.01, 100);
            //Graph g = make.makeDAG(30, 0.1, 5);
            g = make.inputGraph(true);

            int start = 0;
            int end = g.size - 1;

            //make.printGraph(g, start, end);
            if (g.size == 0) continue;
            int path2 = solve.solveBellmanFord(g, start, end);
            GraphDijksra gd = make.toDijksra(g, 100);
            int path1 = solve.solveDijskra(gd, start, end);

            int path3 = solve.solveDAG(g, start, end);
            //int path4 = path3;
            //int path4 = solve.floydWarshall(g)[start][end];
            GraphDijkstraHeap gh = new GraphDijkstraHeap(g.size);
            gh.vertices = g.vertices;
            int path4 = solve.solveDijskraHeap(gh, start, end);
            path4 = path4 == Integer.MAX_VALUE/10 ? -1 : path4;
            if (path1 == path2 && path1 == path3 && path1 == path4)
                System.out.println(i + 1 + "^0 Correct!: " + path1 + ", " + path2 + ", " + path3 + " ," + path4);
            else if (path1 == path2 && path1 == path4)
                System.out.println(i + 1 + "^0 Cyclic Correct!: " + path1 + ", " + path2 + " ," + path4 + "\n\t\tAcyclic: " + path3);
            else {
                System.out.println(i + 1 + "^0 Incorrect: " + path1 + ", " + path2 + " ," + path3 + " ," + path4);
                make.printGraph(g, 0, g.size - 1);
                return false;
            }
            System.out.println(i);
        }
        return true;
    }

    static void test(int iterations, int n, double d, int k, boolean DAG){
        ShortestPaths solve = new ShortestPaths();
        MakeGraph make = new MakeGraph();
        Random random = new Random();
        Graph g;

        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {

            g = DAG ? make.genericGraph(n, d, k) : make.makeDAG(n, d, k);
            GraphDijksra gd = make.toDijksra(g, 1000);
            if (g.size == 0){
                i--;
                continue;
            }
            solve.dijkstra(gd,0, g.size - 1);
        }

        long end = System.nanoTime();
        System.out.println("\n#################");
        System.out.println("Dijskra\n");
        System.out.println("N: "+n);
        System.out.println("DAG: "+DAG);
        System.out.println("Time in sec: "+(double)((end - start)/1000000) );
        System.out.println("#################\n");

        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {

            g = DAG ? make.genericGraph(n, d, k) : make.makeDAG(n, d, k);
            if (g.size == 0){
                i--;
                continue;
            }
            solve.solveBellmanFord(g,0, g.size - 1);
        }
        end = System.nanoTime();
        System.out.println("\n#################");
        System.out.println("BellmanFord\n");
        System.out.println("N: "+n);
        System.out.println("DAG: "+DAG);
        System.out.println("Time in sec: "+(double)((end - start)/1000000) );
        System.out.println("#################\n");

        start = System.nanoTime();
        if (!DAG)   return;
        for (int i = 0; i < iterations; i++) {

            g = make.makeDAG(n, d, k);
            if (g.size == 0){
                i--;
                continue;
            }
            solve.solveDAG(g,0, g.size - 1);
        }
        end = System.nanoTime();
        System.out.println("\n#################");
        System.out.println("DAG solving\n");
        System.out.println("N: "+n);
        System.out.println("Time in sec: "+ (double)((end - start)/1000000) );
        System.out.println("#################\n");
    }

    static void allIndex(int n, double d, int k, boolean DAG){
        ShortestPaths solve = new ShortestPaths();
        MakeGraph make = new MakeGraph();
        Graph g;

        g = DAG ? make.makeDAG(n, d, k) : make.genericGraph(n, d, k);

        //make.printGraph(g, -1, -1);

        int [][] correct = solve.floydWarshall(g);

        long start = System.nanoTime();

        for (int i = 0; i < g.size; i++)
            for (int j = 0; j < g.size; j++) {
                int p = solve.solveBellmanFord(g, i, j);
                if (p != (correct[i][j] == Integer.MAX_VALUE/10 ? -1 : correct[i][j])) {
                    System.out.println("BellmanFord incorrect ["+i+", "+j+" ] "+p+", "+correct[i][j]);
                    //return;
                }
            }
        long end = System.nanoTime();
        System.out.println("\n#################");
        System.out.println("BellmanFord\n");
        System.out.println("N: "+n);
        System.out.println("DAG: "+DAG);
        System.out.println("Time in sec: "+(double)((end - start)/1000000)/1000 );
        System.out.println("#################\n");

        GraphDijksra gd = make.toDijksra(g, 1000);

        start = System.nanoTime();
        for (int i = 0; i < g.size; i++)
            for (int j = 0; j < g.size; j++) {
                int p = solve.solveDijskra(gd, i, j);
                if (p != (correct[i][j] == Integer.MAX_VALUE/10 ? -1 : correct[i][j])) {
                    System.out.println("Dijskra incorrect ["+i+", "+j+" ] "+p+", "+(correct[i][j] == Integer.MAX_VALUE ? -1 : correct[i][j]));
                    //return;
                }
            }

        end = System.nanoTime();
        System.out.println("\n#################");
        System.out.println("Dijskra\n");
        System.out.println("N: "+n);
        System.out.println("DAG: "+DAG);
        System.out.println("Time in sec: "+(double)((end - start)/1000000)/1000 );
        System.out.println("#################\n");

        GraphDijkstraHeap gh = new GraphDijkstraHeap(g.size);
        gh.vertices = g.vertices;

        start = System.nanoTime();
        for (int i = 0; i < g.size; i++)
            for (int j = 0; j < g.size; j++) {
                int p = solve.solveDijskraHeap(gh, i, j);
                if (p != (correct[i][j] == Integer.MAX_VALUE/10 ? -1 : correct[i][j])) {
                    System.out.println("Dijskra incorrect ["+i+", "+j+" ] "+p+", "+(correct[i][j] == Integer.MAX_VALUE ? -1 : correct[i][j]));
                    //return;
                }
            }

        end = System.nanoTime();
        System.out.println("\n#################");
        System.out.println("Dijskra\n");
        System.out.println("N: "+n);
        System.out.println("DAG: "+DAG);
        System.out.println("Time in sec: "+(double)((end - start)/1000000)/1000 );
        System.out.println("#################\n");

        if (!DAG)   return;

        start = System.nanoTime();
        for (int i = 0; i < g.size; i++)
            for (int j = 0; j < g.size; j++) {
                int p = solve.solveDAG(g, i, j);
                if (p != (correct[i][j] == Integer.MAX_VALUE/10 ? -1 : correct[i][j])) {
                    //System.out.println("Dag incorrect ["+i+", "+j+" ] "+p+", "+correct[i][j]);
                    //return;
                }
            }

        end = System.nanoTime();
        System.out.println("\n#################");
        System.out.println("DAG solving\n");
        System.out.println("N: "+n);
        System.out.println("Time in sec: "+ (double)((end - start)/1000000)/1000 );
        System.out.println("#################\n");
    }
}

