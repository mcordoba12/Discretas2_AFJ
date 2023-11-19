package com.example.cedintegradora.structure.graph;




import com.example.cedintegradora.structure.heap.Heap;
import com.example.cedintegradora.structure.heap.HeapNode;
import com.example.cedintegradora.structure.interfaces.ColorType;
import com.example.cedintegradora.structure.interfaces.IPriorityQueue;
import com.example.cedintegradora.structure.interfaces.Igraph;
import com.example.cedintegradora.structure.narytree.NaryTree;
import com.example.cedintegradora.structure.narytree.Node;

import java.util.*;

public class AdjencyListGraph <V extends Comparable<V> > implements Igraph<V> {

    private boolean isDirected;
    private ArrayList<Vertex<V>> vertexes;
    private ArrayList<Edge<V>> edges;

    private Hashtable<V, Hashtable<V,Integer>> weightedMatrix ;

    private boolean isWeighted;
    public AdjencyListGraph(boolean isDirected, boolean isWeighted) {
        this.vertexes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.isDirected = isDirected;
        if (isWeighted){
            this.isWeighted = true;
            this.weightedMatrix = new Hashtable<>();
        }
    }
    @Override
    public boolean insertVertex(V valueVertex) {
        getVertexes().add(new Vertex<>(valueVertex));
        if (isWeighted){
            getWeightedMatrix().put(valueVertex, new Hashtable<>()); // Add new vertex as a new row of weighted matrix.
            for (Vertex vertex: getVertexes()
            ) {
                getWeightedMatrix().get(valueVertex).put((V)vertex.getValue(),Integer.MAX_VALUE);
            }
            for (Hashtable<V,Integer> hashTable: getWeightedMatrix().values()
                 ) {
                hashTable.put(valueVertex, Integer.MAX_VALUE);
            }

            getWeightedMatrix().get(valueVertex).put(valueVertex,0);
        }
        return true;
    }


    public Map<?,?>[] dijkstra(V source){
        Vertex<V> vertexSource = searchVertex(source);

        Hashtable<V, Integer> distance=  new Hashtable<>();
        HashMap<V, V> prev =  new HashMap<>();
        IPriorityQueue priorityQueue = new Heap();

        for (Vertex<V> vertex: getVertexes()
             ) {
            distance.put(vertex.getValue(), Integer.MAX_VALUE);
            prev.put(vertex.getValue(), null);

            if (vertexSource.getValue().equals(vertex.getValue())) distance.put(vertexSource.getValue(), 0);

            priorityQueue.insert(distance.get(vertex.getValue()), vertex.getValue());
        }

            while(!priorityQueue.isEmpty()){
                Vertex<V> vertexToCheck =   searchVertex((V) priorityQueue.heapExtractMin());
                for (Vertex<V> adjacency: vertexToCheck.getAdjacency()
                     ) {
                    int temporal = distance.get(vertexToCheck.getValue()) +
                            getWeightedMatrix().get(vertexToCheck.getValue()).get(adjacency.getValue());

                    if (temporal <  distance.get(adjacency.getValue())){

                        distance.put(adjacency.getValue(), temporal);
                        prev.put(adjacency.getValue(),vertexToCheck.getValue());
                        priorityQueue.decreasePriority(adjacency.getValue(), temporal);
                }
            }
        }

            Map [] maps = {distance,prev};

            return maps;
    }




    public Integer searchWeightOfVertex(V from,  V to){
        Integer weight = -1;
        if (getWeightedMatrix().containsKey(from) && getWeightedMatrix().containsKey(to)) weight = getWeightedMatrix().get(from).get(to);

        return weight;
    }

    @Override
    public boolean insertEdge(V from, V to) {
        if (getVertexes().isEmpty()) return false;

        Vertex fromVertex = searchVertex(from); // Luego verficar si los valores no son nullos.
        Vertex toVertex = searchVertex(to);

        if (fromVertex == null || toVertex  == null) return false;
        fromVertex.getAdjacency().add(toVertex);
        if (!isDirected) toVertex.getAdjacency().add(fromVertex);

        return true;
    }

    public boolean insertWeightedEdge(V from, V to, Integer weight) {
        if (getVertexes().isEmpty()) return false;

        Vertex fromVertex = searchVertex(from); // Luego verficar si los valores no son nullos.
        Vertex toVertex = searchVertex(to);

        if (fromVertex == null || toVertex  == null) return false;
        edges.add(new Edge<>(fromVertex, toVertex, weight));
        fromVertex.getAdjacency().add(toVertex);
        getWeightedMatrix().get(from).put(to,weight);
        if (!isDirected) {
            toVertex.getAdjacency().add(fromVertex);
            getWeightedMatrix().get(to).put(from,weight);
        }

        return true;
    }

    @Override
    public boolean deleteVertex(V valueVertex) {
        for (int i = 0; i < getVertexes().size(); i++) {
            if (getVertexes().get(i).getValue().equals(valueVertex)){
                getVertexes().remove(i);
            }
        }

        for (Vertex<V> vertex : getVertexes()
        ) {
            for (int i = 0; i < vertex.getAdjacency().size(); i++) {
                if (vertex.getAdjacency().get(i).getValue().equals(valueVertex)){
                    vertex.getAdjacency().remove(i);
                }
            }
        }
        if (isWeighted){
            getWeightedMatrix().remove(valueVertex);
            for (Hashtable<V,Integer> hash: getWeightedMatrix().values()
            ) {
                hash.remove(valueVertex);
            }
        }
        return true;
    }

    @Override
    public boolean deleteEdge(V from, V to) {
        if (getVertexes().isEmpty()) return false;

        Vertex<V> fromVertex = searchVertex(from); // Luego verficar si los valores no son nullos.
        Vertex<V> toVertex = searchVertex(to);

        if (fromVertex == null || toVertex  == null) return false;

        fromVertex.getAdjacency().remove(toVertex);

        if (!isDirected) toVertex.getAdjacency().remove(fromVertex);

        if (isWeighted){
            getWeightedMatrix().get(from).put(to, Integer.MAX_VALUE);
            if (!isDirected) getWeightedMatrix().get(to).put(from,Integer.MAX_VALUE);
        }
        return true;
    }

    // BFS Methods

    @Override
    public NaryTree<V> bfs(V from) {

        Vertex fromVertex  = searchVertex(from);
        if (getVertexes().isEmpty()) return null;
        
        // Asignar valores por defecto
        for (Vertex vertex : getVertexes()
             ) {
            vertex.setColor(ColorType.WHITE);
            vertex.setFather(null);
            vertex.setDistance(Integer.MAX_VALUE);
        }
        fromVertex.setColor(ColorType.GRAY);
        fromVertex.setDistance(0);

        Queue<Vertex<V>> queue = new LinkedList();
        queue.add(fromVertex);

        NaryTree<V> naryTree = new NaryTree<>();
        naryTree.insertNode((V) fromVertex.getValue(), null);

        while (!queue.isEmpty()){

            Vertex<V> temporalFather = queue.poll();

            for (Vertex vertex : temporalFather.getAdjacency()
                 ) {
                if (vertex.getColor().equals(ColorType.WHITE)){

                    vertex.setColor(ColorType.GRAY);
                    vertex.setDistance( temporalFather.getDistance() +1);
                    vertex.setFather(temporalFather);
                    naryTree.insertNode((V) vertex.getValue(),(V) vertex.getFather().getValue() );
                    queue.add(vertex);
                }
            }
            temporalFather.setColor(ColorType.BLACK);
        }

        return naryTree;
    }


    public Stack<V> bfsForOneNode(V from, V to ){


        Vertex fromVertex  = searchVertex(from);
        Vertex toVertex  = searchVertex(to);


        if (getVertexes().isEmpty()) return null;

        // Asignar valores por defecto
        for (Vertex vertex : getVertexes()
        ) {
            vertex.setColor(ColorType.WHITE);
            vertex.setFather(null);
            vertex.setDistance(Integer.MAX_VALUE);
        }
        fromVertex.setColor(ColorType.GRAY);
        fromVertex.setDistance(0);

        Queue<Vertex<V>> queue = new LinkedList();
        queue.add(fromVertex);

        while (!queue.isEmpty() && toVertex.getColor().equals(ColorType.WHITE)){

            Vertex<V> temporalFather = queue.poll();

            for (Vertex vertex : temporalFather.getAdjacency()
            ) {
                if (vertex.getColor().equals(ColorType.WHITE) ){

                    vertex.setColor(ColorType.GRAY);
                    vertex.setDistance( temporalFather.getDistance() +1);
                    vertex.setFather(temporalFather);
                    queue.add(vertex);
                }
            }
            temporalFather.setColor(ColorType.BLACK);
        }

        Stack <V> path = new Stack<>();
        Vertex<V> tem  = toVertex;

        while(tem != null){
            path.add((V) tem.getValue());
            tem = tem.getFather();
        }
        return path;
    }

    //DFS Search

    @Override
    public ArrayList<NaryTree<V>> dfs() {
        for (Vertex<V> v: vertexes){
            v.setColor(ColorType.WHITE);
            v.setFather(null);
        }

        ArrayList<NaryTree<V>> forest = new ArrayList<>();

        for (Vertex<V> v: vertexes){
            if (v.getColor().equals(ColorType.WHITE)){
                NaryTree<V> tree = new NaryTree<>();
                dfsVisit(v, tree);
                forest.add(tree);
            }
        }

        return forest;
    }

    @Override
    public void dfsVisit(Vertex<V> from, NaryTree<V> tree) {
        from.setColor(ColorType.GRAY);
        V temp = null;
        if(from.getFather() != null) { temp = from.getFather().getValue(); }
        tree.insertNode(from.getValue(), temp);
        for (Vertex<V> v: from.getAdjacency()) {
            if (v.getColor().equals(ColorType.WHITE)){
                v.setFather(from);
                dfsVisit(v, tree);
            }
        }
        from.setColor(ColorType.BLACK);
    }


    public Stack<V> dfsForOneNode(V from, V to) {
        int size = dfs().size();
        if (dfs().size() >1) return null;
        NaryTree naryTree = dfs().get(0);
        Stack fromPath = new Stack<>();
        fromPath = pathToCeil(searchVertex(from), fromPath);

        Stack toPath = new Stack<>();
        toPath = pathToCeil(searchVertex(to), toPath);

        Queue result = new LinkedList();

        for (int i = 0; i <fromPath.size(); i++) {
            if (toPath.contains(fromPath.get(i))){
                if (fromPath.get(i).equals(from) || fromPath.get(i).equals(to)){
                    boolean isFound = false;
                    if (toPath.size() > fromPath.size()){
                        int j = toPath.size()-1;
                        while(!toPath.isEmpty()) {
                            if ((toPath.get(j).equals(from) || toPath.get(j).equals(to))) isFound =true;
                            if (isFound) result.add(toPath.pop());
                            else toPath.pop();
                            j--;
                        }
                    }else {
                        int j = fromPath.size()-1;
                        while(!fromPath.isEmpty()) {
                            if ((fromPath.get(j).equals(from) || fromPath.get(j).equals(to))) isFound =true;
                            if (isFound) result.add(fromPath.pop());
                            else fromPath.pop();
                            j--;
                        }
                    }
                } else {

                    V value = (V) fromPath.get(i);
                    Stack temporal = new Stack<>();

                    for (int j = 0; j < fromPath.size() ; j++) {
                        if (value.equals(fromPath.get(j))) break;
                        temporal.add(fromPath.get(j));
                    }

                    boolean isFound = false;
                    int j  = toPath.size()-1;
                    while (!toPath.isEmpty()){
                        if (toPath.get(j).equals(value)) isFound = true;
                        if (isFound) temporal.add(toPath.pop());
                        else toPath.pop();
                        j--;
                    }

                    while(!temporal.isEmpty()){
                        result.add(temporal.pop());
                    }
                }
                break;
            }
        }

        List list = (List) result;

        Stack stackResult = new Stack<>();

        if (list.get(0).equals(from)){

            for (int i = list.size()-1;i > -1; i--) {
                stackResult.add(list.get(i));
            }
        }else {
            for (int i = 0; i < result.size(); i++) {
                stackResult.add(list.get(i));
            }
        }



        return stackResult;

    }
    public Stack<V> pathToCeil (Vertex<V> current, Stack<V> stack){
        if (current == null) return  stack;
        stack.add(current.getValue());
        return pathToCeil(current.getFather(),stack);
    }

    public ArrayList<Edge<V>> kruskal() {
        ArrayList<Edge<V>> A = new ArrayList<>();
        UnionFind unionFind = new UnionFind(vertexes.size());
        Collections.sort(edges);
        for (Edge<V> edge : edges) {
            int u = vertexes.indexOf(edge.getFrom());
            int v = vertexes.indexOf(edge.getTo());
            if (unionFind.find(u) != unionFind.find(v)) {
                A.add(edge);
                unionFind.union(u, v);
            }
        }
        return A;
    }

    public NaryTree<Vertex<V>> prim(V sValue) {
        Vertex<V> s = searchVertex(sValue);
        NaryTree<Vertex<V>> naryTree = new NaryTree<>();
        naryTree.setRoot(new Node<>(s));
        Heap<Double, Vertex<V>> queue = new Heap<>();
        queue.insert(0.0, s);
        for(Vertex <V> vertex : vertexes){
            if(vertex != s){
                queue.insert(Double.MAX_VALUE, vertex);
                vertex.setColor(ColorType.WHITE);
            }
        }
        while (queue.getHeapSize() > 0){
            Vertex<V> u = queue.heapExtractMin();
            for(Vertex<V> adj : u.getAdjacency()){
                HeapNode<Double, Vertex<V>> heapNode = queue.searchByValue(adj);
                if(heapNode != null){
                    HeapNode<Double, Vertex<V>> v = heapNode;
                    if(v.getValue().getColor() == ColorType.WHITE && (getWeightedMatrix().get(u.getValue()).get(adj.getValue())) < v.getKey()){
                        v.setKey(Double.valueOf(getWeightedMatrix().get(u.getValue()).get(adj.getValue())));
                        //v.getValue().setWeight(Double.valueOf(getWeightedMatrix().get(u.getValue()).get(adj.getValue())));
                        queue.decreasePriority(v.getValue(),Double.valueOf(getWeightedMatrix().get(u.getValue()).get(adj.getValue())));
                        v.getValue().setFather(u);
                    }
                }
            }
            queue.buildHeap();
            u.setColor(ColorType.BLACK);
        }
        Queue<Vertex<V>> treeQueue = new LinkedList<>();
        treeQueue.add(s);
        while (!treeQueue.isEmpty()){
            Vertex<V> vertex = treeQueue.poll();
            for(Vertex<V> v : vertex.getAdjacency()){
                if(v.getFather() == vertex){
                    treeQueue.add(v);
                    naryTree.insertNode(v, vertex);
                }
            }
        }
        return naryTree;
    }
    
    public double[][] floydWarshall() {
        double[][] distances = new double[vertexes.size()][vertexes.size()];
        //Vertex<V>[][] previous = new Vertex[vertexList.size()][vertexList.size()];
        for (int i = 0; i < vertexes.size(); i++) {
            for (int j = 0; j < vertexes.size(); j++) {
                if ( i == j ) {
                    distances[i][j] = 0.0;
                } else if (vertexes.get(i).getAdjacency().contains(vertexes.get(j))) {
                    distances[i][j] = vertexes.get(i).getAdjacency().get((vertexes.get(i).getAdjacency().indexOf(vertexes.get(j)))).getDistance();

                } else {
                    distances[i][j] = Double.MAX_VALUE;

                }
                //previous[i][j] = vertexList.get(j);

            }
        }

        for (int k = 0; k < vertexes.size(); k++) {
            for (int i = 0; i < vertexes.size(); i++) {
                for (int j = 0; j < vertexes.size(); j++) {
                    if ( distances[i][j] > distances[i][k] + distances[k][j] ) {
                        distances[i][j] = distances[i][k] + distances[k][j];
                    }
                }
            }
        }
        return distances;
    }




    @Override
    public Vertex<V> searchVertex(V values) {
        if (getVertexes().isEmpty()) return null;
        else {
            for (Vertex<V> vertex: getVertexes()
                 ) {
                if (values.equals(vertex.getValue())) return vertex;
            }
        }
        return null;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public void setDirected(boolean directed) {
        isDirected = directed;
    }

    public ArrayList<Vertex<V>> getVertexes() {
        return vertexes;
    }

    public void setVertexes(ArrayList<Vertex<V>> vertexes) {
        this.vertexes = vertexes;
    }

    public boolean isWeighted() {
        return isWeighted;
    }

    public Hashtable<V, Hashtable<V, Integer>> getWeightedMatrix() {
        return weightedMatrix;
    }

    public void setWeightedMatrix(Hashtable<V, Hashtable<V, Integer>> weightedMatrix) {
        this.weightedMatrix = weightedMatrix;
    }

    public void setWeighted(boolean weighted) {
        isWeighted = weighted;
    }
}
