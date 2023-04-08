import java.util.*;

public class MakeGraph{
    public void printGraph(Graph graph, int start, int end){
        System.out.println("Start: "+start);
        System.out.println("End: "+end);
        int acyclic = 0;
        for (int i = 0; i < graph.size; i++) {
            Graph.Edges edges;
            edges = graph.vertices[i].edges;
            while (true){
                if(edges == null) break;
                if(edges.number <= i)   acyclic = 1;
                if(edges.next == null)  break;
                edges = edges.next;
            }
            if (acyclic == 1){
                System.out.println("Cyclic");
                break;
            }
        }
        if(acyclic == 0)
            System.out.println("Acyclic");

        for (int i = 0; i < graph.size; i++) {
            System.out.print("Edges of vertex "+i+" is: [");
            Graph.Edges edges;
            edges = graph.vertices[i].edges;
            while (true){
                if(edges == null) break;
                System.out.print(" ["+edges.number+", "+edges.weight+"] ");
                if(edges.next == null)  break;
                edges = edges.next;
            }
            System.out.println("]");
        }

        for (int i = 0; i < graph.size; i++) {
            for (Graph.Edges edges = graph.vertices[i].edges; edges != null ; edges = edges.next) {
                System.out.println(i+" "+edges.number+" "+edges.weight );
                if (edges.next == null)break;
            }

        }

        System.out.println();
//        for (int i = 0; i < graph.size; i++)
//            System.out.println("Paths "+i+" "+(graph.vertices[i].path == 1000000 ? -1 : graph.vertices[i].path));

    }
    //Inicializa um grafo pelo input
    public Graph inputGraph(boolean weight){
        Scanner sc = new Scanner(System.in);
        int vertices = sc.nextInt();
        int edges = sc.nextInt();
        Graph graph = new Graph(vertices);
        if (weight)
        for (int i = 0; i < edges; i++) graph.pull(sc.nextInt(), sc.nextInt(), sc.nextInt());
        else
            for (int i = 0; i < edges; i++) graph.pull(sc.nextInt(), sc.nextInt(), 1);
        return graph;
    }

    public Graph inputDigraph(boolean weight){
        Scanner sc = new Scanner(System.in);
        int vertices = sc.nextInt();
        int edges = sc.nextInt();
        Graph graph = new Graph(vertices);

        if (weight) {
            for (int i = 0; i < edges ; i++) {
                int v1 = sc.nextInt();
                int v2 = sc.nextInt();
                int p = sc.nextInt();
                graph.pull(v1, v2, p);
                graph.pull(v2, v1, p);
            }
        }
        else{
            for (int i = 0; i < edges; i++) {
                int v1 = sc.nextInt();
                int v2 = sc.nextInt();
                graph.pull(v1, v2, 0);
                graph.pull(v2, v1, 0);
            }
        }
        return graph;
    }
    //Inicializa um grafo genérico
    public Graph genericGraph(int N, double density, int k) {
        int vertices = (int) Math.round((density - 1 + Math.sqrt(Math.pow(1 - density, 2) + 4 * density * N)) /( 2 * density));

        if( vertices == 0)  return null;
        if (vertices > N) vertices = N;

        Random rdm = new Random();
        Graph graph = new Graph(vertices);

        for (int i = 0; i < vertices; i++)
            for (int j = 0; j < vertices; j++)
                if (rdm.nextDouble() < density && i != j)
                    graph.pull(i, j, rdm.nextInt(k + 1));
        return graph;
    }
    //Inicia um DAG
    public Graph makeDAG(int N, double density, int k) {
        int vertices = (int) Math.round((density/2 - 1 + Math.sqrt(Math.pow(1 - density/2, 2) + 2 * density * N)) /(density));

        if( vertices == 0)  return null;
        if (vertices > N)   vertices = N;

        Random rdm = new Random();
        Graph graph = new Graph(vertices);

        for (int i = 0; i < vertices; i++)
            for (int j = i + 1; j < vertices; j++)
                if (rdm.nextDouble() < density)
                    graph.pull(i, j, rdm.nextInt(k + 1));
        return graph;
    }

    public GraphDijksra toDijksra(Graph graph, int c){

        int nVertices = graph.size;
        int []max = {graph.size};
        for (int i = 0; i < graph.size; i++)
            for (Graph.Edges edge = graph.vertices[i].edges; edge != null; edge = edge.next)
                if (edge.weight >= c)
                    nVertices += edge.weight / (c - 1);

        GraphDijksra graphDijskra = new GraphDijksra(nVertices, c);
        for (int i = 0; i < graph.size; i++)
            for (Graph.Edges edge = graph.vertices[i].edges; edge != null; edge = edge.next)
                graphDijskra.pull(i, edge.number, edge.weight, c, max);
        return graphDijskra;
    }
}