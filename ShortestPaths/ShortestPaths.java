import java.util.Arrays;

public class ShortestPaths {
    private static final int Infinity = Integer.MAX_VALUE/10;

    //Chama os métodos respectivos para resolver as distancias
    public int solveDijskra(GraphDijksra graph, int start, int end) {
        dijkstra(graph, start, end);
        return graph.vertices[end].path == Infinity ? -1 : graph.vertices[end].path;
    }
    public int solveDijskraHeap(GraphDijkstraHeap graph, int start, int end) {
        dijkstraHeap(graph, start, end);
        return graph.vertices[end].path == Infinity ? -1 : graph.vertices[end].path;
    }
    public int solveBellmanFord(Graph Graph, int start, int end) {
        bellmanFord(Graph, start);
        return Graph.vertices[end].path == Infinity ? -1 : Graph.vertices[end].path;
    }
    public int solveDAG(Graph Graph, int start, int end) {
        DAG(Graph, start);
        return Graph.vertices[end].path == Infinity ? -1 : Graph.vertices[end].path;
    }
    public int solveFloydWarshall(Graph graph, int start, int end){
        int [][]d = floydWarshall(graph);
        return d[start][end] == Infinity ? -1 : d[start][end];
    }

    protected void dijkstra(GraphDijksra graph, int start, int end){

        //Reseta os arrays. O(V)
        graph.reset();

        Graph.Vertex vertex = graph.vertices[start];
        Graph.Edges edge = vertex.edges;

        //Inicializando o vertice de inicio
        vertex.path = 0;
        if(start == end || edge == null)    return;

        while(true) {
            //Em um vertice:
            //Verifica todas as arestas do vertice. O(A)
            //Adiciona ou atualiza a fila de prioridade com o novo caminho. O(1)
            for (;edge != null;edge = edge.next)
                if (vertex.path + edge.weight < graph.vertices[edge.number].path)
                    graph.pullPQ(edge.number, graph.vertices[edge.number].path = vertex.path + edge.weight);

            //Pega o menor caminho para seguir. O(n) -> Geralmente O(1) em grafos densos
            int [] index = graph.popPQ();
            //Caso encontre o vertice de fim, ou a fila esteja vazia
            if (index[0] == -1 || index[0] == end) return;

            //Atualiza os ponteiros e cai no mesmo algoritmo com um vertice diferente
            vertex = graph.vertices[index[0]];
            vertex.path = index[1];
            edge = vertex.edges;
        }
    }
    protected void dijkstraHeap(GraphDijkstraHeap graph, int start, int end){

        //Reseta os arrays. O(V)
        graph.resetGraph(Infinity);

        Graph.Vertex vertex = graph.vertices[start];
        Graph.Edges edge = vertex.edges;

        //Inicializando o vertice de inicio
        vertex.path = 0;
        if(start == end || edge == null)    return;

        while(true) {
            //Em um vertice:
            //Verifica todas as arestas do vertice. O(A)
            //Adiciona ou atualiza a fila de prioridade com o novo caminho. O(1)
            for (;edge != null;edge = edge.next)
                if (vertex.path + edge.weight < graph.vertices[edge.number].path)
                    graph.pullPQ(edge.number, graph.vertices[edge.number].path = vertex.path + edge.weight);

            //Pega o menor caminho para seguir. O(n) -> Geralmente O(1) em grafos densos
            int [] index = graph.popPQ();
            //Caso encontre o vertice de fim, ou a fila esteja vazia
            if (index[0] == -1 || index[0] == end) return;

            //Atualiza os ponteiros e cai no mesmo algoritmo com um vertice diferente
            vertex = graph.vertices[index[0]];
            vertex.path = index[1];
            edge = vertex.edges;
        }
    }

    protected void bellmanFord(Graph graph, int start){
        graph.resetGraph(Infinity);
        Graph.Edges edge;
        //Inicializando o vertice de inicio
        graph.vertices[start].path = 0;
        if(graph.vertices[start].edges == null) return;

        boolean change;
        for (int j = 0; j < graph.size; j++) {
            change = false;
            for (int i = 0; i < graph.size; i++) {
                if (graph.vertices[i].edges == null) continue;
                for (edge = graph.vertices[i].edges; true; edge = edge.next) {
                    //Verifica se o caminho deve ser modificado
                    if (graph.vertices[i].path + edge.weight < graph.vertices[edge.number].path) {
                        change = true;
                        graph.vertices[edge.number].path = graph.vertices[i].path + edge.weight;
                    }
                    //Caso o arco não exista
                    if (edge.next == null)break;
                }
            }
            if (!change) return;
        }
    }

    protected void DAG(Graph graph, int start) {
        graph.resetGraph(Infinity);
        Graph.Edges edge;

        //Inicializando o vertice de inicio
        graph.vertices[start].path = 0;
        if(graph.vertices[start].edges == null) return;

        for (int i = 0; i < graph.size; i++) {
            if (graph.vertices[i].edges == null) continue;

            for (edge = graph.vertices[i].edges; true; edge = edge.next) {
                if (graph.vertices[i].path + edge.weight < graph.vertices[edge.number].path)
                    graph.vertices[edge.number].path = graph.vertices[i].path + edge.weight;
                if (edge.next == null)break;
            }
        }
    }
    protected boolean topologicSort(Graph graph, int start){


        return true;
    }

    protected int [][] floydWarshall(Graph graph){
        graph.resetGraph(Infinity);
        int [][] distance = new int[graph.size][graph.size];
        Arrays.stream(distance).forEach(dis -> Arrays.fill(dis, Infinity));
        for (int i = 0; i < graph.size; i++) distance[i][i] = 0;

        int i, j, k;
        Graph.Edges edges;
        for (i = 0; i < graph.size; i++) {
            if(graph.vertices[i].edges == null)   continue;
            for (edges = graph.vertices[i].edges; true; edges = edges.next) {
                distance[i][edges.number] = edges.weight;
                if (edges.next == null) break;
            }
        }
        for (k = 0; k < graph.size; k++) for (i = 0; i < graph.size; i++) for (j = 0; j < graph.size; j++) {
            if (distance[i][j] > distance[i][k] + distance[k][j])
                distance[i][j] = distance[i][k] + distance[k][j];
        }
        return distance;
    }
}