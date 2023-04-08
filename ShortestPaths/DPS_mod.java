public class DPS_mod {

    public static class Bridges{
        public static class GraphBridges extends Graph{
            int cnt;
            int bcnt;
            int[] parnt;
            int[] pre;
            int[] low;

            public GraphBridges(int vertices) {
                super(vertices);
                pre = new int[vertices];
                low = new int[vertices];
                parnt = new int[vertices];
                cnt = bcnt = 0;
            }
        }
        public void allArtic(Graph graph) {
            GraphBridges gb = new GraphBridges(graph.size);
            System.arraycopy(graph.vertices, 0, gb.vertices, 0, graph.size);

            for (int i = 0; i < graph.size; i++)
                gb.pre[i] = -1;

            for (int i = 0; i < graph.size; i++) {
                if (gb.pre[i] == -1) {
                    gb.parnt[i] = i;
                    artculacao(gb, i);
                }
            }
            System.out.print("Pre: ");
            for (int i = 0; i < gb.size; i++) System.out.print(gb.pre[i] + " ");
            System.out.print("\nLow: ");
            for (int i = 0; i < gb.size; i++) System.out.print(gb.low[i] + " ");
        }
        public void allBridgges(Graph graph) {
            GraphBridges gb = new GraphBridges(graph.size);
            System.arraycopy(graph.vertices, 0, gb.vertices, 0, graph.size);

            for (int i = 0; i < graph.size; i++)
                gb.pre[i] = -1;

            for (int i = 0; i < graph.size; i++) {
                if (gb.pre[i] == -1) {
                    gb.parnt[i] = i;
                    bridgeR(gb, i);
                }
            }
            System.out.print("Pre: ");
            for (int i = 0; i < gb.size; i++) System.out.print(gb.pre[i] + " ");
            System.out.print("\nLow: ");
            for (int i = 0; i < gb.size; i++) System.out.print(gb.low[i] + " ");
        }

        protected void bridgeR(GraphBridges gb, int v){
            int w;
            gb.low[v] = gb.pre[v] = gb.cnt++;
            if (gb.vertices[v].edges == null)   return;
            for (Graph.Edges p = gb.vertices[v].edges; p != null ; p = p.next) {
                if (gb.pre[w = p.number] == -1) {
                    gb.parnt[w] = v;
                    bridgeR(gb, w);
                    if (gb.low[v] > gb.low[w])  gb.low[v] = gb.low[w];

                    if (gb.low[w] == gb.pre[w]) {
                        gb.bcnt++;
                        System.out.println("Ponte entre "+v+" e "+w);
                    }
                }
                else if (w != gb.parnt[v] && gb.low[v] > gb.pre[w])
                    gb.low[v] = gb.pre[w];
            }

        }
        protected void artculacao(GraphBridges gb, int v){
            int w;
            gb.low[v] = gb.pre[v] = gb.cnt++;
            if (gb.vertices[v].edges == null)   return;
            for (Graph.Edges p = gb.vertices[v].edges; p != null ; p = p.next) {
                if (gb.pre[w = p.number] == -1) {
                    gb.parnt[w] = v;
                    artculacao(gb, w);
                    if (gb.low[v] > gb.low[w])  gb.low[v] = gb.low[w];

                    if(gb.low[w] >= gb.pre[v]){
                        gb.bcnt++;
                        System.out.println("Articulacao "+v);
                    }
                }
                else if (w != gb.parnt[v] && gb.low[v] > gb.pre[w])
                    gb.low[v] = gb.pre[w];
            }
        }
    }
}
