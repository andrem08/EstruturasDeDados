import java.util.*;

public class Graph {
    public Vertex[] vertices;
    public int size;

    //Um arco na forma de uma lista de adjascencia
    public static class Edges {
        int weight;
        int number;
        Edges next = null;
    }

    //Um vertice
    public static class Vertex {
        int path;
        //Somente para acompanhar o caminho
        int before;
        Edges edges;
    }

    public Graph(int vertices) {
        this.size = vertices;
        this.vertices = new Vertex[vertices];
        for (int i = 0; i < vertices; i++) this.vertices[i] = new Vertex();
        //André, voce poderia retirar, porém lembre de sempre resetar os grafos ultilizando aquele metodo
        }

    public void pull(int vertex1, int vertex2, int weight) {
        Edges next = this.vertices[vertex1].edges;

        Edges l1 = new Edges();
        l1.number = vertex2;
        l1.next = next;
        l1.weight = weight;
        this.vertices[vertex1].edges = l1;
    }

    // Omega(N)
    public void resetGraph(int path) {
        for (int i = 0; i < this.size; i++) {
            this.vertices[i].path = path;
            this.vertices[i].before = -1;
        }
    }
}

class GraphDijksra extends Graph{

    //PriorityQueue<Path> pq;
    private final int Infinity = Integer.MAX_VALUE/10;
    int c;
    private static class Vertex {
        int weight;
        int prev;
        int next;
    }
    
    private final int [] weights;
    private final Vertex [] vertex;
    private int delta;

    public void pull(int vertex1, int vertex2, int weight, int c, int []max){
        int newVertex;
        while (weight >= c){
            newVertex = max[0];
            max[0]++;
            pull(vertex1, newVertex, c - 1);
            weight -= c - 1;
            vertex1 = newVertex;
        }
        pull(vertex1, vertex2, weight);
    }

    public void pullPQ(int v, int w){
        //Caso precise dar update
        if (vertex[v].weight != Infinity){
            if (vertex[v].prev == -1){
                //Caso nao exista prev e next de v
                if (vertex[v].next == -1)
                    weights[vertex[v].weight % weights.length] = -1;
                //Caso só exista next de v
                else {
                    vertex[vertex[v].next].prev = -1;
                    vertex[v].next = -1;
                }
            }
            //Caso só exista prev de v
            else if(vertex[v].next == -1) {
                weights[vertex[v].weight % weights.length] = vertex[v].prev;
                vertex[v].prev = vertex[vertex[v].prev].next = -1;
            }
            //Caso exista prev e next de v
            else {
                vertex[vertex[v].next].prev = vertex[v].prev;
                vertex[vertex[v].prev].next = vertex[v].next;

                vertex[v].next = vertex[v].prev = -1;
            }
        }
        
        //Coleta o index do local de v em vertex
        int index = weights[w % weights.length];
        vertex[v].weight = w;
        if (weights[w % weights.length] != -1){
            //Incrementando na lista dublamente encadeada e setando os prev e next
            vertex[index].next = v;
            vertex[v].prev = index;
        }
        weights[w % weights.length] = v;
    }

    public int [] popPQ(){
        int indexShortest;
        for (indexShortest = 0; indexShortest < weights.length; indexShortest++)
            if (weights[(indexShortest + delta) < weights.length ? delta + indexShortest : delta + indexShortest - weights.length] != -1) break;
        if ( indexShortest == weights.length)    return new int[]{-1, 0};

        indexShortest = (indexShortest + delta) < weights.length ? delta + indexShortest : delta + indexShortest - weights.length;
        delta = indexShortest;

        int shortest = weights[indexShortest];

        weights[indexShortest] = vertex[shortest].prev;
        vertex[shortest].prev = -1;
        vertex[shortest].next = -1;
        //Retorna o indice do vertice e o seu peso
        return new int[]{shortest, vertex[shortest].weight};
    }
    public void reset(){
        resetGraph(Infinity);
        Arrays.fill(weights, -1);
        for (int i = 0; i < size; i++) {
            Vertex v = vertex[i];
            v.next = -1;
            v.prev = -1;
            v.weight = Infinity;
        }
        delta = 0;
    }

    public GraphDijksra(int vertices, int c) {
        super(vertices);
        this.c = c;
        Arrays.fill(weights = new int[c], -1);
        vertex = new Vertex[size];
        for (int i = 0; i < vertices; i++) {
            vertex[i] = new Vertex();
            vertex[i].next = -1;
            vertex[i].prev = -1;
            vertex[i].weight = Infinity;
        }
        delta = 0;
    }
}

class GraphDijkstraHeap extends Graph{
    private final int Infinity = Integer.MAX_VALUE/10;

    private static class Vertex {
        int weight;
        int index;
    }
    private final Vertex [] heap;
    private final int [] vertex;
    private int heapSize;

    //Construtor
    public GraphDijkstraHeap(int vertices){
        super(vertices);
        heap = new Vertex[vertices];
        vertex = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            heap[i] = new Vertex();
            heap[i].index = -1;
            heap[i].weight = Infinity;
        }
        Arrays.fill(vertex, -1);
        heapSize = 0;
    }

    //O(log(V))
    public void pullPQ(int v, int w){
        int index;
        //Caso o valor precise ser atualizado
        if(vertex[v] != -1){
            index = vertex[v];
            heap[index].weight = w;
        }
        //Caso contrario coloca o vertice no final do vetor
        //Problema em que, na hora que o programa inserem no heap no
        //exato local do heapSize, pode ser que ja tenha conteudo dentro dele
        //Uma forma de resolver é colocar na ultima posição do array
        else {
            index = size - 1;
            heap[index].weight = w;
            heap[index].index = v;
            vertex[v] = index;
            heapSize++;
        }
        //Verifica se o vertice tem pai menor do que ele
        //Caso não tenha, troca

        while (heap[(index - 1)/2].weight > heap[index].weight){
            Vertex aux = heap[(index - 1)/2];
            heap[(index - 1)/2] = heap[index];
            heap[index] = aux;
            if (heap[index].index != -1)
                vertex[heap[index].index] =  index;
            if (heap[(index - 1) / 2].index != -1)
                vertex[heap[(index - 1) / 2].index] = (index - 1)/2;
            index = (index - 1)/2;
        }
    }
    //O(log(V))
    private void heapify(int index){
        while (index*2 + 1 <= heapSize && index < size
            && (heap[index].weight > heap[index*2 + 1].weight
            || heap[index].weight > heap[index*2 + 2].weight )){
            //Identifica o pai que é menor que o filho
            //E trocam de valores
            if (index*2 + 2 == size || heap[index*2 + 1].weight < heap[index*2 + 2].weight){
                Vertex aux = heap[index*2 + 1];
                heap[index*2 + 1] = heap[index];
                heap[index] = aux;
                int aux2 = vertex[heap[index].index];
                vertex[heap[index].index] = vertex[heap[index*2 + 1].index];
                vertex[heap[index*2 + 1].index] = aux2;
                heapify(index*2 + 1);
            }
            else {
                Vertex aux = heap[index*2 + 2];
                heap[index*2 + 2] = heap[index];
                heap[index] = aux;
                int aux2 = vertex[heap[index].index];
                vertex[heap[index].index] = vertex[heap[index*2 + 2].index];
                vertex[heap[index*2 + 2].index] = aux2;
                heapify(index * 2 + 2);
            }
        }
    }
    public int [] popPQ(){
        if (heapSize == 0)  return new int[] {-1, -1};
        int [] remove = {heap[0].index, heap[0].weight};
        heap[0].weight = Infinity;
        heapify(0);
        heapSize--;
        return remove;
    }
}