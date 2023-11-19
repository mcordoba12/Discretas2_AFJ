package com.example.cedintegradora.structure.graph;

import com.example.cedintegradora.structure.heap.Heap;
import com.example.cedintegradora.structure.heap.HeapNode;
import com.example.cedintegradora.structure.interfaces.ColorType;
import com.example.cedintegradora.structure.interfaces.Igraph;
import com.example.cedintegradora.structure.narytree.NaryTree;
import com.example.cedintegradora.structure.narytree.Node;

import java.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;





public class AdjacencyMatrixGraph <V extends Comparable<V>> implements Igraph<V> {

    ArrayList<Vertex<V>> vertexes;
    ArrayList<Edge<V>> edges;
    ArrayList<ArrayList<Double>> adjacencyMatrix;

    boolean isDirected;
    boolean isWeighted;

    public AdjacencyMatrixGraph(boolean isDirected, boolean isWeighted) {
        vertexes = new ArrayList<>();
        edges = new ArrayList<>();
        adjacencyMatrix = new ArrayList<>();
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;
    }



    public Stack<V> dfsForOneNode(V from, V to) {
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
    private Stack<V> pathToCeil (Vertex<V> current, Stack<V> stack){
        if (current == null) return  stack;
        stack.add(current.getValue());
        return pathToCeil(current.getFather(),stack);
    }

    @Override
    public boolean insertVertex(V valueVertex) {
        Vertex<V> vertex = new Vertex<>(valueVertex);
        if(!vertexes.contains(vertex)){
            vertexes.add(vertex);
            int vertexPos = vertexes.indexOf(vertex);
            adjacencyMatrix.add(new ArrayList<Double>());
            if(vertexPos == 0){
                adjacencyMatrix.get(0).add(Double.MAX_VALUE);
                return true;
            }
            for(int i = 0; i < vertexPos; i++){
                adjacencyMatrix.get(i).add(Double.MAX_VALUE);
                adjacencyMatrix.get(vertexPos).add(Double.MAX_VALUE);
            }
            adjacencyMatrix.get(vertexPos).add(Double.MAX_VALUE);
            return true;
        }
        return false;
    }

    @Override
    public boolean insertEdge(V fromValue, V toValue) {
        Vertex<V> from = searchVertex(fromValue);
        Vertex<V> to = searchVertex(toValue);
        if(!vertexes.contains(from) || !vertexes.contains(to)) return false;
        int v1Pos = vertexes.indexOf(from);
        int v2Pos = vertexes.indexOf(to);
        if (!isWeighted) {
            if(isDirected){
                if(adjacencyMatrix.get(v1Pos).get(v2Pos) < Double.MAX_VALUE) return false;
                adjacencyMatrix.get(v1Pos).set(v2Pos, 1.0);
            } else {
                if(adjacencyMatrix.get(v1Pos).get(v2Pos) < Double.MAX_VALUE || adjacencyMatrix.get(v2Pos).get(v1Pos) < Double.MAX_VALUE) return false;
                adjacencyMatrix.get(v1Pos).set(v2Pos, 1.0);
                adjacencyMatrix.get(v2Pos).set(v1Pos, 1.0);
            }
        }
        return true;
    }

    public boolean insertEdge(V fromValue, V toValue, Double weight) {
        Vertex<V> from = searchVertex(fromValue);
        Vertex<V> to = searchVertex(toValue);
        if(!vertexes.contains(from) || !vertexes.contains(to)) return false;
        edges.add(new Edge<>(from, to, weight));
        int v1Pos = vertexes.indexOf(from);
        int v2Pos = vertexes.indexOf(to);
        if (isWeighted) {
            if(isDirected){
                if(adjacencyMatrix.get(v1Pos).get(v2Pos) < Double.MAX_VALUE) return false;
                adjacencyMatrix.get(v1Pos).set(v2Pos, weight);
            } else {
                if(adjacencyMatrix.get(v1Pos).get(v2Pos) < Double.MAX_VALUE || adjacencyMatrix.get(v2Pos).get(v1Pos) < Double.MAX_VALUE) return false;
                adjacencyMatrix.get(v1Pos).set(v2Pos, weight);
                adjacencyMatrix.get(v2Pos).set(v1Pos, weight);
            }
        }
        return true;
    }


    @Override
    public boolean deleteVertex(V valueVertex) {
        Vertex<V> vertex = searchVertex(valueVertex);
        if(!vertexes.contains(vertex)) return false;
        int vertexPos = vertexes.indexOf(vertex);
        vertexes.remove(vertexPos);
        adjacencyMatrix.remove(vertexPos);
        for (ArrayList<Double> doubles : adjacencyMatrix) {
            doubles.remove(vertexPos);
        }
        return true;
    }

    @Override
    public boolean deleteEdge(V from, V to) {
        Vertex<V> v1 = searchVertex(from);
        Vertex<V> v2 = searchVertex(to);
        if(!vertexes.contains(v1) || !vertexes.contains(v2)) return false;
        int v1Pos = vertexes.indexOf(v1);
        int v2Pos = vertexes.indexOf(v2);
        if(isDirected){
            adjacencyMatrix.get(v1Pos).set(v2Pos, Double.MAX_VALUE);
            return true;
        } else {
            adjacencyMatrix.get(v1Pos).set(v2Pos, Double.MAX_VALUE);
            adjacencyMatrix.get(v2Pos).set(v1Pos, Double.MAX_VALUE);
            return true;
        }
    }

    @Override
    public NaryTree<V> bfs(V from) {
        NaryTree<V> naryTree = new NaryTree<>();
        naryTree.setRoot(new Node<V>(from));
        Queue<Vertex<V>> queue = new LinkedList<>();

        for (Vertex<V> u :vertexes) {
            u.setColor(ColorType.WHITE);
            u.setDistance(Integer.MAX_VALUE);
            u.setFather(null);
        }
        Vertex<V> s = searchVertex(from);

        s.setColor(ColorType.GRAY);
        s.setDistance(0);
        s.setFather(null);
        queue.add(s);
        while(!queue.isEmpty()){
            Vertex<V> u = queue.poll();
            int uPos = vertexes.indexOf(u);
            for(int i = 0; i < adjacencyMatrix.get(uPos).size(); i++){
                if(adjacencyMatrix.get(uPos).get(i) < Double.MAX_VALUE){
                    if(vertexes.get(i).getColor() == ColorType.WHITE){
                        vertexes.get(i).setColor(ColorType.GRAY);
                        vertexes.get(i).setDistance(u.getDistance()+1);
                        vertexes.get(i).setFather(u);
                        naryTree.insertNode(vertexes.get(i).getValue(), vertexes.get(i).getFather().getValue());
                        queue.add(vertexes.get(i));
                    }
                }
            }
            u.setColor(ColorType.BLACK);
        }
        return naryTree;
    }

    @Override
    public ArrayList<NaryTree<V>> dfs() {
        ArrayList<NaryTree<V>> naryTrees = new ArrayList<>();
        for (Vertex<V> u: vertexes) {
            u.setColor(ColorType.WHITE);
            u.setFather(null);
        }

        for (Vertex<V> u: vertexes) {
            if (u.getColor() == ColorType.WHITE){
                NaryTree<V> naryTree = new NaryTree<>();
                naryTree.setRoot(new Node<V>(u.getValue()));
                dfsVisit(u, naryTree);
                naryTrees.add(naryTree);
            }
        }

        return naryTrees;
    }

    @Override
    public void dfsVisit(Vertex<V> from, NaryTree<V> tree) {
        from.setColor(ColorType.GRAY);

        int uPos = vertexes.indexOf(from);
        for(int i = 0; i < adjacencyMatrix.get(uPos).size(); i++) {
            if(adjacencyMatrix.get(uPos).get(i) < Double.MAX_VALUE) {
                if(vertexes.get(i).getColor() == ColorType.WHITE){
                    vertexes.get(i).setFather(from);
                    tree.insertNode(vertexes.get(i).getValue(), from.getValue());
                    dfsVisit(vertexes.get(i),tree);
                }
            }
        }
        from.setColor(ColorType.BLACK);
    }

    @Override
    public Vertex<V> searchVertex(V values) {
        if (getVertexes().isEmpty()) return null;
        else {
            for (Vertex<V> vertex: getVertexes()) {
                if (vertex.getValue().equals(values)) return vertex;
            }
        }
        return null;
    }

    public Stack<V> bfsSingleNode(V from , V to) {
        if (getVertexes().isEmpty()) return null;
        Vertex<V> fromVertex  = searchVertex(from);
        Vertex<V> toVertex  = searchVertex(to);

        for (Vertex<V> u :vertexes) {
            u.setColor(ColorType.WHITE);
            u.setDistance(Integer.MAX_VALUE);
            u.setFather(null);
        }

        fromVertex.setColor(ColorType.GRAY);
        fromVertex.setDistance(0);

        Queue<Vertex<V>> queue = new LinkedList<>();
        queue.add(fromVertex);

        while(!queue.isEmpty()){
            Vertex<V> u = queue.poll();
            int uPos = vertexes.indexOf(u);
            for(int i = 0; i < adjacencyMatrix.get(uPos).size(); i++){
                if(adjacencyMatrix.get(uPos).get(i) < Double.MAX_VALUE){
                    if(vertexes.get(i).getColor() == ColorType.WHITE){
                        vertexes.get(i).setColor(ColorType.GRAY);
                        vertexes.get(i).setDistance(u.getDistance()+1);
                        vertexes.get(i).setFather(u);
                        queue.add(vertexes.get(i));
                    }
                }
            }
            u.setColor(ColorType.BLACK);
        }

        Stack path = new Stack();
        Vertex<V> current = toVertex;
        while(current != null){
            path.add(current.getValue());
            current = current.getFather();
        }


        return path;

    }




    public void dfsVisitSingleNode(Vertex<V> from, NaryTree<V> tree) {
        from.setColor(ColorType.GRAY);

        int uPos = vertexes.indexOf(from);
        for(int i = 0; i < adjacencyMatrix.get(uPos).size(); i++) {
            if(adjacencyMatrix.get(uPos).get(i) < Double.MAX_VALUE) {
                if(vertexes.get(i).getColor() == ColorType.WHITE){
                    vertexes.get(i).setFather(from);
                    tree.insertNode(vertexes.get(i).getValue(), from.getValue());
                    dfsVisitSingleNode(vertexes.get(i),tree);
                }
            }
        }
        from.setColor(ColorType.BLACK);
    }

    public double[][] floydWarshall() {
        double[][] distances = new double[vertexes.size()][vertexes.size()];
        //Vertex<V>[][] previous = new Vertex[vertexList.size()][vertexList.size()];
        for(int i = 0; i < vertexes.size(); i++){
            for(int j = 0; j < vertexes.size(); j++){
                if(i == j){
                    distances[i][j] = 0;
                } else {
                    distances[i][j] = adjacencyMatrix.get(i).get(j);
                }

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
            int uPos = vertexes.indexOf(u);
            for(int i = 0; i < vertexes.size(); i++){
                if(i != uPos){
                    if(adjacencyMatrix.get(uPos).get(i) < Double.MAX_VALUE){
                        HeapNode<Double, Vertex<V>> heapNode = queue.searchByValue(vertexes.get(i));
                        if(heapNode != null){
                            double weight = adjacencyMatrix.get(uPos).get(i);
                            HeapNode<Double, Vertex<V>> v = heapNode;
                            if(v.getValue().getColor() == ColorType.WHITE && weight < v.getKey()){
                                v.setKey(weight);
                                //v.getValue().setWeight(weight);
                                queue.decreasePriority(v.getValue(),weight);
                                v.getValue().setFather(u);
                            }
                        }
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
            int vertexPos = vertexes.indexOf(vertex);
            for(int i = 0; i < vertexes.size(); i++){
                if(i != vertexPos){
                    double weight = adjacencyMatrix.get(vertexPos).get(i);
                    if(weight < Double.MAX_VALUE){
                        Vertex<V> v = vertexes.get(i);
                        if(v.getFather() == vertex){
                            treeQueue.add(v);
                            naryTree.insertNode(v, vertex);
                        }
                    }
                }
            }
        }
        return naryTree;

    }

    public NaryTree<Vertex<V>> prim(){
        Vertex<V> s = vertexes.get(0);
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
            int uPos = vertexes.indexOf(u);
            for(int i = 0; i < vertexes.size(); i++){
                if(i != uPos){
                    if(adjacencyMatrix.get(uPos).get(i) < Double.MAX_VALUE){
                        HeapNode<Double, Vertex<V>> heapNode = queue.searchByValue(vertexes.get(i));
                        if(heapNode != null){
                            double weight = adjacencyMatrix.get(uPos).get(i);
                            HeapNode<Double, Vertex<V>> v = heapNode;
                            if(v.getValue().getColor() == ColorType.WHITE && weight < v.getKey()){
                                v.setKey(weight);
                                //v.getValue().setWeight(weight);
                                queue.decreasePriority(v.getValue(),weight);
                                v.getValue().setFather(u);
                            }
                        }
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
            int vertexPos = vertexes.indexOf(vertex);
            for(int i = 0; i < vertexes.size(); i++){
                if(i != vertexPos){
                    double weight = adjacencyMatrix.get(vertexPos).get(i);
                    if(weight < Double.MAX_VALUE){
                        Vertex<V> v = vertexes.get(i);
                        if(v.getFather() == vertex){
                            treeQueue.add(v);
                            naryTree.insertNode(v, vertex);
                        }
                    }
                }
            }
        }
        return naryTree;



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




    public ArrayList<ArrayList<?>> dijkstra (Vertex<V> source){
        ArrayList<Vertex<V>> previous = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();

        Heap<Double, Vertex<V>> queue = new Heap<>();
        for (int i = 0; i < vertexes.size(); i++) {
            if (vertexes.get(i) != source){
                distances.add(Double.MAX_VALUE);
            }else{
                distances.add(0.0);
            }
            previous.add(null);
            queue.insert(distances.get(i),vertexes.get(i));
        }
        queue.buildHeap();

        while (queue.getHeapSize()>0){
            Vertex<V> u = queue.heapExtractMin();
            int uPos = vertexes.indexOf(u);
            for(int i = 0; i < vertexes.size(); i++){
                if(adjacencyMatrix.get(uPos).get(i) < Double.MAX_VALUE){
                    double alt = distances.get(uPos) + adjacencyMatrix.get(uPos).get(i);
                    if(alt < distances.get(i)){
                        distances.set(i, alt);
                        previous.set(i, u);
                        queue.decreasePriority((queue.searchByValue(vertexes.get(i))).getValue(), alt);
                    }
                    queue.buildHeap();
                }

            }
        }
        ArrayList<ArrayList<?>> temp = new ArrayList<>();
        temp.add(previous);
        temp.add(distances);
        return temp;
    }









    public ArrayList<Vertex<V>> getVertexes() {
        return vertexes;
    }



    public void setVertexes(ArrayList<Vertex<V>> vertexes) {
        this.vertexes = vertexes;
    }



    public ArrayList<ArrayList<Double>> getAdjacencyMatrix() {
        return adjacencyMatrix;
    }



    public void setAdjacencyMatrix(ArrayList<ArrayList<Double>> adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }



    public boolean isDirected() {
        return isDirected;
    }



    public void setDirected(boolean isDirected) {
        this.isDirected = isDirected;
    }



    public boolean isWeighted() {
        return isWeighted;
    }



    public void setWeighted(boolean isWeighted) {
        this.isWeighted = isWeighted;
    }

}
