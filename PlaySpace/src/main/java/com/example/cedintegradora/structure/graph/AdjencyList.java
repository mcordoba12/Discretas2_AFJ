package com.example.cedintegradora.structure.graph;




import com.example.cedintegradora.structure.heap.Heap;
import com.example.cedintegradora.structure.heap.HeapNode;
import com.example.cedintegradora.structure.interfaces.ColorType;
import com.example.cedintegradora.structure.interfaces.IPriorityQueue;
import com.example.cedintegradora.structure.interfaces.Igraph;
import com.example.cedintegradora.structure.narytree.NaryTree;
import com.example.cedintegradora.structure.narytree.Node;

import java.util.*;

public class AdjencyList<V extends Comparable<V> > implements Igraph<V> {

    private boolean isDirected;
    private ArrayList<Vertex<V>> vertexes;
    private ArrayList<Edge<V>> edges;

    private Hashtable<V, Hashtable<V,Integer>> weightedMatrix ;

    private boolean isWeighted;

    /**
     * Constructs an adjacency list for a graph.
     *
     * @param isDirected True if the graph is directed, false otherwise.
     * @param isWeighted True if the graph is weighted, false otherwise.
     */
    public AdjencyList(boolean isDirected, boolean isWeighted) {
        this.vertexes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.isDirected = isDirected;
        if (isWeighted){
            this.isWeighted = true;
            this.weightedMatrix = new Hashtable<>();
        }
    }
    /**
     * Inserts a new vertex with the given value into the graph.
     *
     * @param valueVertex The value of the vertex to be inserted.
     * @return True if the insertion is successful, false otherwise.
     */
    @Override
    public boolean insertVertex(V valueVertex) {
        // Add a new vertex to the list of vertices
        getVertexes().add(new Vertex<>(valueVertex));

        // If the graph is weighted, update the weighted matrix
        if (isWeighted) {
            // Add a new row for the new vertex in the weighted matrix
            getWeightedMatrix().put(valueVertex, new Hashtable<>());

            // Initialize weights to maximum for all existing vertices
            for (Vertex vertex : getVertexes()) {
                getWeightedMatrix().get(valueVertex).put((V) vertex.getValue(), Integer.MAX_VALUE);
            }

            // Initialize weights to maximum for the new vertex with all existing vertices
            for (Hashtable<V, Integer> hashTable : getWeightedMatrix().values()) {
                hashTable.put(valueVertex, Integer.MAX_VALUE);
            }

            // Set the weight to 0 for the new vertex with itself
            getWeightedMatrix().get(valueVertex).put(valueVertex, 0);
        }

        // Return true to indicate successful insertion
        return true;
    }



    /**
     * Applies Dijkstra's algorithm to find the shortest paths from the given source vertex to all other vertices in the graph.
     *
     * @param source The source vertex for which the shortest paths are calculated.
     * @return An array containing two maps:
     *         - Map 1: Represents the distance from the source to each vertex.
     *         - Map 2: Represents the previous vertex in the shortest path from the source to each vertex.
     */
    public Map<?, ?>[] dijkstra(V source) {
        // Get the source vertex from the graph
        Vertex<V> vertexSource = searchVertex(source);

        // Initialize distance and previous maps
        Hashtable<V, Integer> distance = new Hashtable<>();
        HashMap<V, V> prev = new HashMap<>();

        // Initialize priority queue using a heap
        IPriorityQueue priorityQueue = new Heap();

        // Initialize distance and previous maps for all vertices
        for (Vertex<V> vertex : getVertexes()) {
            distance.put(vertex.getValue(), Integer.MAX_VALUE);
            prev.put(vertex.getValue(), null);

            // Set distance to 0 for the source vertex
            if (vertexSource.getValue().equals(vertex.getValue())) {
                distance.put(vertexSource.getValue(), 0);
            }

            // Insert vertices into the priority queue with their distances
            priorityQueue.insert(distance.get(vertex.getValue()), vertex.getValue());
        }

        // Perform Dijkstra's algorithm
        while (!priorityQueue.isEmpty()) {
            Vertex<V> vertexToCheck = searchVertex((V) priorityQueue.heapExtractMin());

            // Update distances and previous vertices for adjacent vertices
            for (Vertex<V> adjacency : vertexToCheck.getAdjacency()) {
                int temporal = distance.get(vertexToCheck.getValue()) +
                        getWeightedMatrix().get(vertexToCheck.getValue()).get(adjacency.getValue());

                // If a shorter path is found, update distance and previous
                if (temporal < distance.get(adjacency.getValue())) {
                    distance.put(adjacency.getValue(), temporal);
                    prev.put(adjacency.getValue(), vertexToCheck.getValue());
                    priorityQueue.decreasePriority(adjacency.getValue(), temporal);
                }
            }
        }

        // Return an array containing distance and previous maps
        Map[] maps = {distance, prev};
        return maps;
    }

    /**
     * Searches for the weight of the edge between two vertices in the weighted graph.
     *
     * @param from The source vertex.
     * @param to   The destination vertex.
     * @return The weight of the edge from the source vertex to the destination vertex.
     *         If either the source or destination vertex is not found, or if there is no edge between them, -1 is returned.
     */
    public Integer searchWeightOfVertex(V from, V to) {
        Integer weight = -1;

        // Check if the weighted matrix contains both source and destination vertices
        if (getWeightedMatrix().containsKey(from) && getWeightedMatrix().containsKey(to)) {
            weight = getWeightedMatrix().get(from).get(to);
        }

        return weight;
    }


    /**
     * Inserts an edge between two vertices in the graph.
     *
     * @param from The value of the source vertex.
     * @param to   The value of the destination vertex.
     * @return {@code true} if the edge is successfully inserted, {@code false} otherwise.
     *         Returns {@code false} if the graph is empty or if either the source or destination vertex is not found.
     */
    @Override
    public boolean insertEdge(V from, V to) {
        // Check if the graph is empty
        if (getVertexes().isEmpty()) {
            return false;
        }

        // Search for the vertices with the given values
        Vertex fromVertex = searchVertex(from);
        Vertex toVertex = searchVertex(to);

        // Check if either the source or destination vertex is not found
        if (fromVertex == null || toVertex == null) {
            return false;
        }

        // Add the destination vertex to the adjacency list of the source vertex
        fromVertex.getAdjacency().add(toVertex);

        // If the graph is undirected, add the source vertex to the adjacency list of the destination vertex
        if (!isDirected) {
            toVertex.getAdjacency().add(fromVertex);
        }

        return true;
    }

    /**
     * Inserts a weighted edge between two vertices in the graph.
     *
     * @param from   The value of the source vertex.
     * @param to     The value of the destination vertex.
     * @param weight The weight of the edge.
     * @return {@code true} if the weighted edge is successfully inserted, {@code false} otherwise.
     *         Returns {@code false} if the graph is empty or if either the source or destination vertex is not found.
     */
    public boolean insertWeightedEdge(V from, V to, Integer weight) {
        // Check if the graph is empty
        if (getVertexes().isEmpty()) {
            return false;
        }

        // Search for the vertices with the given values
        Vertex fromVertex = searchVertex(from);
        Vertex toVertex = searchVertex(to);

        // Check if either the source or destination vertex is not found
        if (fromVertex == null || toVertex == null) {
            return false;
        }

        // Create a new weighted edge and add it to the edges list
        edges.add(new Edge<>(fromVertex, toVertex, weight));

        // Add the destination vertex to the adjacency list of the source vertex
        fromVertex.getAdjacency().add(toVertex);

        // Update the weighted matrix with the weight of the edge
        getWeightedMatrix().get(from).put(to, weight);

        // If the graph is undirected, update the weighted matrix for the reverse edge
        if (!isDirected) {
            toVertex.getAdjacency().add(fromVertex);
            getWeightedMatrix().get(to).put(from, weight);
        }

        return true;
    }


    /**
     * Deletes a vertex with the specified value from the graph.
     *
     * @param valueVertex The value of the vertex to be deleted.
     * @return {@code true} if the vertex is successfully deleted, {@code false} otherwise.
     */
    @Override
    public boolean deleteVertex(V valueVertex) {
        // Remove the vertex from the list of vertices
        for (int i = 0; i < getVertexes().size(); i++) {
            if (getVertexes().get(i).getValue().equals(valueVertex)) {
                getVertexes().remove(i);
            }
        }

        // Remove references to the deleted vertex from the adjacency lists of other vertices
        for (Vertex<V> vertex : getVertexes()) {
            for (int i = 0; i < vertex.getAdjacency().size(); i++) {
                if (vertex.getAdjacency().get(i).getValue().equals(valueVertex)) {
                    vertex.getAdjacency().remove(i);
                }
            }
        }

        // If the graph is weighted, remove the corresponding row and column from the weighted matrix
        if (isWeighted) {
            getWeightedMatrix().remove(valueVertex);
            for (Hashtable<V, Integer> hash : getWeightedMatrix().values()) {
                hash.remove(valueVertex);
            }
        }

        return true;
    }


    /**
     * Deletes an edge between the specified vertices from the graph.
     *
     * @param from The value of the vertex where the edge starts.
     * @param to   The value of the vertex where the edge ends.
     * @return {@code true} if the edge is successfully deleted, {@code false} otherwise.
     */
    @Override
    public boolean deleteEdge(V from, V to) {
        // Check if the graph is empty
        if (getVertexes().isEmpty()) {
            return false;
        }

        // Find the vertices corresponding to the given values
        Vertex<V> fromVertex = searchVertex(from);
        Vertex<V> toVertex = searchVertex(to);

        // Check if the vertices are found
        if (fromVertex == null || toVertex == null) {
            return false;
        }

        // Remove the reference to the 'toVertex' from the adjacency list of 'fromVertex'
        fromVertex.getAdjacency().remove(toVertex);

        // If the graph is undirected, remove the reference to 'fromVertex' from the adjacency list of 'toVertex'
        if (!isDirected) {
            toVertex.getAdjacency().remove(fromVertex);
        }

        // If the graph is weighted, update the weight in the weighted matrix
        if (isWeighted) {
            getWeightedMatrix().get(from).put(to, Integer.MAX_VALUE);

            // If the graph is undirected, update the weight in the opposite direction as well
            if (!isDirected) {
                getWeightedMatrix().get(to).put(from, Integer.MAX_VALUE);
            }
        }

        return true;
    }


    // BFS Methods

    /**
     * Performs a Breadth-First Search (BFS) starting from the specified vertex.
     *
     * @param from The value of the vertex to start the BFS from.
     * @return A new NaryTree containing the vertices visited during BFS.
     */
    @Override
    public NaryTree<V> bfs(V from) {
        // Find the vertex corresponding to the given value
        Vertex fromVertex = searchVertex(from);

        // Check if the graph is empty or the vertex is not found
        if (getVertexes().isEmpty() || fromVertex == null) {
            return null;
        }

        // Initialize default values for all vertices
        for (Vertex vertex : getVertexes()) {
            vertex.setColor(ColorType.WHITE);
            vertex.setFather(null);
            vertex.setDistance(Integer.MAX_VALUE);
        }

        // Set initial values for the starting vertex
        fromVertex.setColor(ColorType.GRAY);
        fromVertex.setDistance(0);

        // Initialize a queue for BFS
        Queue<Vertex<V>> queue = new LinkedList<>();
        queue.add(fromVertex);

        // Create a new NaryTree to store the BFS result
        NaryTree<V> naryTree = new NaryTree<>();
        naryTree.insertNode((V) fromVertex.getValue(), null);

        // Perform BFS
        while (!queue.isEmpty()) {
            Vertex<V> temporalFather = queue.poll();

            // Iterate through adjacent vertices
            for (Vertex vertex : temporalFather.getAdjacency()) {
                if (vertex.getColor().equals(ColorType.WHITE)) {
                    vertex.setColor(ColorType.GRAY);
                    vertex.setDistance(temporalFather.getDistance() + 1);
                    vertex.setFather(temporalFather);
                    naryTree.insertNode((V) vertex.getValue(), (V) vertex.getFather().getValue());
                    queue.add(vertex);
                }
            }

            temporalFather.setColor(ColorType.BLACK);
        }

        return naryTree;
    }



    /**
     * Performs a Breadth-First Search (BFS) from the specified start vertex to find a path to the target vertex.
     *
     * @param from The value of the start vertex.
     * @param to   The value of the target vertex.
     * @return A Stack containing the path from the start to the target vertex. Returns null if the graph is empty or vertices are not found.
     */
    public Stack<V> bfsForOneNode(V from, V to) {
        // Find the vertices corresponding to the given values
        Vertex fromVertex = searchVertex(from);
        Vertex toVertex = searchVertex(to);

        // Check if the graph is empty or vertices are not found
        if (getVertexes().isEmpty() || fromVertex == null || toVertex == null) {
            return null;
        }

        // Initialize default values for all vertices
        for (Vertex vertex : getVertexes()) {
            vertex.setColor(ColorType.WHITE);
            vertex.setFather(null);
            vertex.setDistance(Integer.MAX_VALUE);
        }

        // Set initial values for the start vertex
        fromVertex.setColor(ColorType.GRAY);
        fromVertex.setDistance(0);

        // Initialize a queue for BFS
        Queue<Vertex<V>> queue = new LinkedList<>();
        queue.add(fromVertex);

        // Perform BFS until the target vertex is found or the queue is empty
        while (!queue.isEmpty() && toVertex.getColor().equals(ColorType.WHITE)) {
            Vertex<V> temporalFather = queue.poll();

            // Iterate through adjacent vertices
            for (Vertex vertex : temporalFather.getAdjacency()) {
                if (vertex.getColor().equals(ColorType.WHITE)) {
                    vertex.setColor(ColorType.GRAY);
                    vertex.setDistance(temporalFather.getDistance() + 1);
                    vertex.setFather(temporalFather);
                    queue.add(vertex);
                }
            }

            temporalFather.setColor(ColorType.BLACK);
        }

        // Reconstruct the path using a stack
        Stack<V> path = new Stack<>();
        Vertex<V> temp = toVertex;

        while (temp != null) {
            path.add((V) temp.getValue());
            temp = temp.getFather();
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

    /**
     * Performs a Depth-First Search (DFS) visit starting from the specified vertex and adds the visited vertices to a tree.
     *
     * @param from The starting vertex for DFS.
     * @param tree The NaryTree to which the visited vertices will be added.
     */
    @Override
    public void dfsVisit(Vertex<V> from, NaryTree<V> tree) {
        // Mark the current vertex as visited
        from.setColor(ColorType.GRAY);

        // Determine the value of the parent vertex
        V parentValue = null;
        if (from.getFather() != null) {
            parentValue = from.getFather().getValue();
        }

        // Insert the current vertex into the tree
        tree.insertNode(from.getValue(), parentValue);

        // Recursively visit adjacent vertices that are not visited
        for (Vertex<V> v : from.getAdjacency()) {
            if (v.getColor().equals(ColorType.WHITE)) {
                v.setFather(from);
                dfsVisit(v, tree);
            }
        }

        // Mark the current vertex as fully explored
        from.setColor(ColorType.BLACK);
    }



    /**
     * Performs a Depth-First Search (DFS) and finds the path from the 'from' vertex to the 'to' vertex.
     *
     * @param from The starting vertex for DFS.
     * @param to   The destination vertex for the path.
     * @return A Stack containing the vertices along the path from 'from' to 'to', or null if there is no valid path.
     */
    public Stack<V> dfsForOneNode(V from, V to) {
        // Check if there is a single DFS tree
        int size = dfs().size();
        if (size > 1) {
            return null;
        }

        // Retrieve the single DFS tree
        NaryTree naryTree = dfs().get(0);

        // Obtain the paths from 'from' and 'to' vertices to their common ancestor
        Stack fromPath = pathToCeil(searchVertex(from), new Stack<>());
        Stack toPath = pathToCeil(searchVertex(to), new Stack<>());

        // Initialize a queue to store the resulting path
        Queue result = new LinkedList();

        // Iterate through the vertices in the 'fromPath'
        for (int i = 0; i < fromPath.size(); i++) {
            // Check if the 'toPath' contains the current vertex
            if (toPath.contains(fromPath.get(i))) {
                // Check if the current vertex is 'from' or 'to'
                if (fromPath.get(i).equals(from) || fromPath.get(i).equals(to)) {
                    boolean isFound = false;

                    // Determine the index to start extracting the path based on the longer path
                    int j = toPath.size() - 1;
                    while (!toPath.isEmpty()) {
                        if ((toPath.get(j).equals(from) || toPath.get(j).equals(to))) {
                            isFound = true;
                        }
                        if (isFound) {
                            result.add(toPath.pop());
                        } else {
                            toPath.pop();
                        }
                        j--;
                    }
                } else {
                    // Extract the path from the 'from' vertex to the common ancestor
                    V value = (V) fromPath.get(i);
                    Stack temporal = new Stack<>();

                    for (int j = 0; j < fromPath.size(); j++) {
                        if (value.equals(fromPath.get(j))) {
                            break;
                        }
                        temporal.add(fromPath.get(j));
                    }

                    boolean isFound = false;
                    int j = toPath.size() - 1;

                    // Extract the path from the 'to' vertex to the common ancestor
                    while (!toPath.isEmpty()) {
                        if (toPath.get(j).equals(value)) {
                            isFound = true;
                        }
                        if (isFound) {
                            temporal.add(toPath.pop());
                        } else {
                            toPath.pop();
                        }
                        j--;
                    }

                    // Transfer the temporal path to the result queue
                    while (!temporal.isEmpty()) {
                        result.add(temporal.pop());
                    }
                }
                break;
            }
        }

        // Convert the result queue to a list
        List list = (List) result;

        // Initialize a stack to store the final result
        Stack stackResult = new Stack<>();

        // Determine the order of vertices in the final result based on whether 'from' is the starting vertex
        if (list.get(0).equals(from)) {
            for (int i = list.size() - 1; i > -1; i--) {
                stackResult.add(list.get(i));
            }
        } else {
            for (int i = 0; i < result.size(); i++) {
                stackResult.add(list.get(i));
            }
        }

        return stackResult;
    }

    /**
     * Recursively constructs a path from the given vertex to its ancestor and stores it in a Stack.
     *
     * @param current The current vertex.
     * @param stack   The Stack to store the path.
     * @return A Stack containing the path from the given vertex to its ancestor.
     */
    public Stack<V> pathToCeil(Vertex<V> current, Stack<V> stack) {
        // Base case: if the current vertex is null, return the current state of the stack
        if (current == null) {
            return stack;
        }

        // Add the value of the current vertex to the stack
        stack.add(current.getValue());

        // Recursively call the method for the parent of the current vertex
        return pathToCeil(current.getFather(), stack);
    }


    /**
     * Applies Kruskal's algorithm to find the minimum spanning tree of the graph.
     *
     * @return An ArrayList of edges representing the minimum spanning tree.
     */
    public ArrayList<Edge<V>> kruskal() {
        // Initialize an ArrayList to store the edges of the minimum spanning tree
        ArrayList<Edge<V>> A = new ArrayList<>();

        // Initialize a UnionFind data structure with the number of vertices
        UnionFind unionFind = new UnionFind(vertexes.size());

        // Sort the edges in non-decreasing order based on their weights
        Collections.sort(edges);

        // Iterate through the sorted edges
        for (Edge<V> edge : edges) {
            // Find the indices of the vertices connected by the current edge
            int u = vertexes.indexOf(edge.getFrom());
            int v = vertexes.indexOf(edge.getTo());

            // Check if adding the current edge creates a cycle in the minimum spanning tree
            if (unionFind.find(u) != unionFind.find(v)) {
                // Add the current edge to the minimum spanning tree
                A.add(edge);
                // Union the sets of vertices connected by the current edge
                unionFind.union(u, v);
            }
        }

        // Return the minimum spanning tree
        return A;
    }


    /**
     * Applies Prim's algorithm to find the minimum spanning tree of the graph starting from a specified vertex.
     *
     * @param sValue The value of the starting vertex.
     * @return A NaryTree representing the minimum spanning tree.
     */
    public NaryTree<Vertex<V>> prim(V sValue) {
        // Find the starting vertex
        Vertex<V> s = searchVertex(sValue);

        // Initialize a NaryTree to store the minimum spanning tree
        NaryTree<Vertex<V>> naryTree = new NaryTree<>();
        naryTree.setRoot(new Node<>(s));

        // Initialize a priority queue to keep track of the vertices and their distances
        Heap<Double, Vertex<V>> queue = new Heap<>();
        queue.insert(0.0, s);

        // Initialize the queue with maximum distances for all other vertices
        for (Vertex<V> vertex : vertexes) {
            if (vertex != s) {
                queue.insert(Double.MAX_VALUE, vertex);
                vertex.setColor(ColorType.WHITE);
            }
        }

        // Main loop of Prim's algorithm
        while (queue.getHeapSize() > 0) {
            // Extract the vertex with the minimum distance from the queue
            Vertex<V> u = queue.heapExtractMin();

            // Iterate through the adjacency of the current vertex
            for (Vertex<V> adj : u.getAdjacency()) {
                // Search for the heap node corresponding to the adjacent vertex
                HeapNode<Double, Vertex<V>> heapNode = queue.searchByValue(adj);

                // If the heap node is found and the adjacent vertex is white (not yet included in the tree)
                if (heapNode != null && heapNode.getValue().getColor() == ColorType.WHITE &&
                        getWeightedMatrix().get(u.getValue()).get(adj.getValue()) < heapNode.getKey()) {
                    // Update the distance and priority of the adjacent vertex in the queue
                    heapNode.setKey(Double.valueOf(getWeightedMatrix().get(u.getValue()).get(adj.getValue())));
                    queue.decreasePriority(heapNode.getValue(), Double.valueOf(getWeightedMatrix().get(u.getValue()).get(adj.getValue())));
                    heapNode.getValue().setFather(u);
                }
            }

            // Rebuild the heap to maintain the priority queue property
            queue.buildHeap();

            // Mark the current vertex as black (included in the minimum spanning tree)
            u.setColor(ColorType.BLACK);
        }

        // Traverse the minimum spanning tree and build the NaryTree
        Queue<Vertex<V>> treeQueue = new LinkedList<>();
        treeQueue.add(s);
        while (!treeQueue.isEmpty()) {
            Vertex<V> vertex = treeQueue.poll();
            for (Vertex<V> v : vertex.getAdjacency()) {
                if (v.getFather() == vertex) {
                    treeQueue.add(v);
                    naryTree.insertNode(v, vertex);
                }
            }
        }

        // Return the minimum spanning tree
        return naryTree;
    }


    /**
     * Applies the Floyd-Warshall algorithm to find the all-pairs shortest paths in the graph.
     *
     * @return A 2D array representing the shortest distances between all pairs of vertices.
     */
    public double[][] floydWarshall() {
        // Initialize a 2D array to store distances between vertices
        double[][] distances = new double[vertexes.size()][vertexes.size()];

        // Populate the initial distances array with direct edge distances or maximum value for non-adjacent vertices
        for (int i = 0; i < vertexes.size(); i++) {
            for (int j = 0; j < vertexes.size(); j++) {
                if (i == j) {
                    distances[i][j] = 0.0;
                } else if (vertexes.get(i).getAdjacency().contains(vertexes.get(j))) {
                    distances[i][j] = vertexes.get(i).getAdjacency().get(vertexes.get(i).getAdjacency().indexOf(vertexes.get(j))).getDistance();
                } else {
                    distances[i][j] = Double.MAX_VALUE;
                }
            }
        }

        // Apply the Floyd-Warshall algorithm to update distances
        for (int k = 0; k < vertexes.size(); k++) {
            for (int i = 0; i < vertexes.size(); i++) {
                for (int j = 0; j < vertexes.size(); j++) {
                    if (distances[i][j] > distances[i][k] + distances[k][j]) {
                        distances[i][j] = distances[i][k] + distances[k][j];
                    }
                }
            }
        }

        // Return the 2D array of distances
        return distances;
    }

    /**
     * Searches for a vertex with the given value in the graph.
     *
     * @param value The value to search for in the vertices.
     * @return The vertex with the specified value, or null if not found.
     */
    @Override
    public Vertex<V> searchVertex(V value) {
        // Check if the list of vertices is empty
        if (getVertexes().isEmpty()) {
            return null;
        } else {
            // Iterate through the vertices to find the one with the specified value
            for (Vertex<V> vertex : getVertexes()) {
                if (value.equals(vertex.getValue())) {
                    return vertex;
                }
            }
        }
        // Return null if the vertex with the specified value is not found
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
