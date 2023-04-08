//André Miyazawa
//11796187
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int vertices;
        int edges;

        Scanner sc = new Scanner(System.in);

        //Inicializa |V| e |A|
        vertices = sc.nextInt();
        edges = sc.nextInt();

        Graph graph = new Graph();
        graph.floydWarshall(sc, vertices, edges);
    }
}
class Graph{
    private final int Infinity = 100000;
    int vertices;
    int edges;
    Planet[] planets;
    Order [] coldest;
    Order [] hottest;
    int[][] distance;

    private static class Planet implements Comparable<Planet> {
        //Os planetas vao ser ordenados pela temperatura portanto vamos guardar os índices
        //originais
        int index;
        int temperatures;
        //Construtor
        Planet(int index, int temperatures){
            this.index = index;
            this.temperatures = temperatures;
        }
        //Comparar planetas para ordenação
        public int compareTo(Planet p2){
            return this.temperatures - p2.temperatures;
        }
    }
    private static class Order implements Comparable <Order>{
        //Os pedidos vão ser separados e ordenados portanto vamos guardar
        // os índices originais
        int index;
        int start;
        int end;
        int k;  //As primeiras ord temperaturas
        int type;   //type == 0, primeiras mais frias, type == 1, primeiras mais quentes
        //Construtor
        Order(int index, int start, int end, int k, int type){
            this.index = index;
            this.start = start;
            this.end = end;
            this.k = k;
            this.type = type;
        }
        //Comparar planetas para ordenação
        @Override
        public int compareTo(Order o2) {
            return this.k - o2.k;
        }
    }

    private void init(Scanner sc) {
        //Grava as temperaturas de cada um dos planetas
        planets = new Planet[vertices];
        for (int i = 0; i < vertices; i++) planets[i] = new Planet(i, sc.nextInt());

        //Grava as distâncias dos planetas em uma matriz de adjacências
        distance = new int[vertices][vertices];
        for (int[] d : distance) Arrays.fill(d, Infinity);
        for (int i = 0; i < vertices; i++) distance[i][i] = 0;
        for (int i = 0; i < edges; i++) {
            int start = sc.nextInt() - 1, end = sc.nextInt() - 1, d = sc.nextInt();
            distance[start][end] = d;
            distance[end][start] = d;
        }
        //Grava os pedidos em ordem
        int nOrders = sc.nextInt();
        Order[] c = new Order[nOrders];
        Order[] h = new Order[nOrders];
        int indC = 0, indH = 0;
        //Separando os pedidos entre os 2 tipos
        for (int i = 0; i < nOrders; i++) {
            int start = sc.nextInt() - 1, end = sc.nextInt() - 1, k = sc.nextInt(), type = sc.nextInt();
            if (type == 0) {
                c[indC] = new Order(i, start, end, k, type);
                indC++;
            } else {
                h[indH] = new Order(i, start, end, k, type);
                indH++;
            }
        }
        coldest = new Order[indC];
        hottest = new Order[indH];
        System.arraycopy(c, 0, coldest, 0, indC);
        System.arraycopy(h, 0, hottest, 0, indH);
        //Primeiro ordenando os planetas em funcao da temperatura e testando o tipo = 0
        //TimSort realiza uma ordenação com O(n(log(n))
        Arrays.sort(planets);
        //Ordenando os pedidos pela restrição de planetas, da maior até a menor, de cada um dos pedidos
        Arrays.sort(hottest);
        Arrays.sort(coldest);

    }

    //Complexidade O(V^2)
    private void addPlanet(int [][] g, int index){
        //Complexidadde de O(n^2) para cada planeta adicionado
        for (int i = 0; i < vertices; i++) for (int j = 0; j < vertices; j++)
            if (g[i][j] > g[i][index] + g[index][j])
                g[i][j] = g[i][index] + g[index][j];
    }
    //Complexidade O(V)
    private void writeRow(int g[][], int index){
        for (int i = 0; i < vertices; i++)
            if (g[index][i] > distance[index][i])
                g[index][i] = distance[index][i];
    }
    //Complexidade O(V)
    private void refreshPlanet(int[][] g, int index1, int index2){
        for (int j = 0; j < vertices; j++)
            if (g[index2][j] > g[index2][index1] + g[index1][j])
                g[index2][j] = g[index2][index1] + g[index1][j];
    }
    //Complexidade O(V)
    private int numberColdPlanets(int end, int start){
        for (int i = 0; i < end; i++){
            if(start == vertices)   return start;
            int temp = planets[start].temperatures;
            while (start < vertices && temp == planets[start].temperatures)
                start++;
        }
        return start;
    }
    private int numberHotPlanets(int end, int start){
        //Adicionar as distancias dos planetas novos na matrix de distancias g
        for (int i = 0; i < end; i++){
            if(start == -1)   return start;
            int temp = planets[start].temperatures;
            while (start >= 0 && temp == planets[start].temperatures) start--;
        }
        return start;
    }
    //Complexidade O(V)
    private int shortPath(int [][] g, int start, int end){
        int menor = distance[start][end];
        for (int i = 0; i < vertices; i++)
            if (menor > distance[start][i] + g[i][end])
                menor = distance[start][i] + g[i][end];
        return menor == Infinity ? -1 : menor;
    }

    public void floydWarshall(Scanner sc, int vertices, int edges){
        //Inicializa os dados
        this.vertices = vertices;
        this.edges = edges;
        this.init(sc);

        int [] Results = new int[coldest.length + hottest.length];

        //Inicializando o grafo de distancias
        int [][] g = new int[vertices][vertices];
        for (int [] gRow: g) Arrays.fill(gRow, Infinity);
        for (int i = 0; i < vertices; i++) g[i][i] = 0;

        //Realizando primeiro para as temperaturas "mais frias" ( tipo = 0)
        int start = 0, oldOrd = 0;

        for (Order order : coldest) {
            //O índice do ultimo + 1 planeta à ir
            int end = numberColdPlanets(order.k - oldOrd, start);
            //Para todos os proximos planetas, escreve na matrix g as distancias da matrix distance
            for (int i = start; i < end; i++)   writeRow(g, planets[i].index);
            //Para todos os planetas anteriores, verifica os planetas posteriores, O(V^3)
            for (int i =  0; i < start; i++)  for (int j = start; j < end; j++)
                refreshPlanet(g, planets[i].index, planets[j].index);
            //Para todos os proximos planetas, verifica as minimas distancias com sua adjascência, O(V^3)
            for (int i = start; i < end; i++) addPlanet(g, planets[i].index);

            Results[order.index] = shortPath(g, order.start, order.end);
            start = end;
            oldOrd = order.k;
        }

        //Realizando as temperaturas "mais quentes" ( tipo = 1)
        for (int [] gRow: g) Arrays.fill(gRow, Infinity);
        for (int i = 0; i < vertices; i++) g[i][i] = 0;
        start = vertices - 1;
        oldOrd = 0;
        for (Order order : hottest) {
            //O índice do ultimo - 1 planeta à ir
            int end = numberHotPlanets(order.k - oldOrd, start);
            //Para todos os proximos planetas, escreve na matrix g as distancias da matrix distance
            for (int i = start; i > end; i--)   writeRow(g, planets[i].index);
            //Para todos os planetas anteriores, verifica os planetas posteriores, O(V^3)
            for (int i =  vertices - 1; i > start; i--)  for (int j = start; j > end; j--)
                refreshPlanet(g, planets[i].index, planets[j].index);
            //Para todos os proximos planetas, verifica as minimas ddistancias com sua adjascência, O(V^3)
            for (int i = start; i > end; i--)   addPlanet(g, planets[i].index);

            Results[order.index] = shortPath(g, order.start, order.end);
            start = end;
            oldOrd = order.k;
        }
        //Imprimindo os resultados
        for (int result : Results) System.out.println(result);
    }
}
