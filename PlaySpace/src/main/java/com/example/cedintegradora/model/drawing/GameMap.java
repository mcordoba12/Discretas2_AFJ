package com.example.cedintegradora.model.drawing;


import com.example.cedintegradora.model.entities.objects.functional.PressurePlate;
import com.example.cedintegradora.structure.graph.AdjacencyMatrix;
import com.example.cedintegradora.structure.graph.AdjencyList;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class GameMap {

    private ArrayList<ArrayList<MapNode>> mapGuide;
    private AdjencyList<Coordinate> graph;
    private AdjacencyMatrix<Coordinate> matrixGraph;

    private double width;
    private double height;

    private double nodeSize;

    private double chunkSize;


    public GameMap(double width, double height, double nodeSize, double chunkSize) {
        this.width = width;
        this.height = height;
        this.nodeSize = nodeSize;
        this.mapGuide = new ArrayList<>();
        this.graph = new AdjencyList<>(false, false);
        this.chunkSize = chunkSize;
        this.matrixGraph = new AdjacencyMatrix<>(false, false);
    }

    /**
     * Generates and adds pressure plates to the existing list of pressure plates.
     *
     * @param pressurePlates The existing list of pressure plates.
     * @param totalPlates    The total number of pressure plates to be generated and added.
     * @return The updated list of pressure plates, including the newly generated plates.
     */
    public CopyOnWriteArrayList<PressurePlate> creatingPressurePlates(CopyOnWriteArrayList<PressurePlate> pressurePlates, int totalPlates) {
        Random random = new Random();
        int platesCreated = 0;  // Contador para rastrear la cantidad de placas creadas

        while (platesCreated < totalPlates) {
            for (int i = 2; i < height / nodeSize && platesCreated < totalPlates; i += chunkSize + 4) {
                double yRange = i + chunkSize;
                for (int j = 4; j < width / nodeSize && platesCreated < totalPlates; j += chunkSize + 3) {
                    int rowNodeSelection = 0;
                    int columnNodeSelection = 0;
                    while (true) {
                        rowNodeSelection = random.nextInt((int) yRange - i) + i;
                        columnNodeSelection = random.nextInt((int) chunkSize) + j;
                        if (!((rowNodeSelection > 2 && rowNodeSelection < 6) && (columnNodeSelection > 5 && columnNodeSelection < 9)))
                            break;
                    }
                    if (getMapGuide().get(rowNodeSelection).get(columnNodeSelection).isNavigable()) {
                        pressurePlates.add(new PressurePlate(
                                getMapGuide().get(rowNodeSelection).get(columnNodeSelection).getPosition().getX(),
                                getMapGuide().get(rowNodeSelection).get(columnNodeSelection).getPosition().getY()
                        ));
                        platesCreated++;
                    }
                }
            }
        }

        return pressurePlates;
    }

    /**
     * Initializes the map by populating it with nodes and their corresponding coordinates.
     * Each node represents a position on the map grid.
     */
    public void initialFillingOfMapWithNodesAndCoordinates() {
        double yPosition = -40;

        for (int i = 0; i < height / nodeSize; i++) {

            yPosition += nodeSize;
            double xPosition = -40;
            mapGuide.add(new ArrayList<MapNode>());

            for (int j = 0; j < width / nodeSize; j++) {
                xPosition += nodeSize;

                MapNode node = new MapNode(xPosition, yPosition, true);
                getMapGuide().get(i).add(node);
            }
        }
    }

    /**
     * Associates a given set of coordinates (x, y) with the closest navigable MapNode on the map.
     *
     * @param x The x-coordinate to associate.
     * @param y The y-coordinate to associate.
     * @return The closest navigable MapNode to the specified coordinates.
     */
    public MapNode associateMapNode(double x, double y) {
        MapNode temporal = new MapNode();
        double distance = Double.MAX_VALUE;

        for (int i = 0; i < height / nodeSize; i++) {
            for (int j = 0; j < width / nodeSize; j++) {
                MapNode actual = getMapGuide().get(i).get(j);
                if (!actual.navigable) continue;
                double pitagoras =
                        Math.sqrt(Math.pow(actual.getPosition().getX() - x, 2) +
                                Math.pow(actual.getPosition().getY() - y, 2));
                if (pitagoras <= distance) {
                    temporal = actual;
                    distance = pitagoras;
                }
            }
        }
        return temporal;
    }

    /**
     * Computes the shortest path from a source coordinate to a destination coordinate
     * using Breadth-First Search (BFS) on a graph represented by an adjacency list.
     *
     * @param from The starting coordinate of the path.
     * @param to   The destination coordinate of the path.
     * @return A stack of coordinates representing the shortest path from 'from' to 'to'.
     */
    public Stack<Coordinate> shortestPathUsingListAdjacencyBFS(Coordinate from, Coordinate to) {

        return graph.bfsForOneNode(from, to);

    }

    /**
     * Computes the shortest path from a source coordinate to a destination coordinate
     * using Depth-First Search (DFS) on a graph represented by an adjacency list.
     *
     * @param from The starting coordinate of the path.
     * @param to   The destination coordinate of the path.
     * @return A stack of coordinates representing the shortest path from 'from' to 'to'.
     */
    public Stack<Coordinate> shortestPathUsingListAdjacencyDFS(Coordinate from, Coordinate to) {

        return graph.dfsForOneNode(from, to);
    }

    /**
     * Computes the shortest path from a source coordinate to a destination coordinate
     * using Breadth-First Search (BFS) on a graph represented by an adjacency matrix.
     *
     * @param from The starting coordinate of the path.
     * @param to   The destination coordinate of the path.
     * @return A stack of coordinates representing the shortest path from 'from' to 'to'.
     */
    public Stack<Coordinate> shortestPathUsingMatrixAdjacencyBFS(Coordinate from, Coordinate to) {

        return matrixGraph.bfsSingleNode(from, to);
    }

    /**
     * Computes the shortest path from a source coordinate to a destination coordinate
     * using Depth-First Search (DFS) on a graph represented by an adjacency matrix.
     *
     * @param from The starting coordinate of the path.
     * @param to   The destination coordinate of the path.
     * @return A stack of coordinates representing the shortest path from 'from' to 'to'.
     */
    public Stack<Coordinate> shortestPathUsingMatrixAdjacencyDFS(Coordinate from, Coordinate to) {

        return matrixGraph.dfsForOneNode(from, to);
    }

    /**
     * Creates non-navigable obstacles on the map to introduce barriers.
     * The obstacles are generated within chunks, and their positions are marked as non-navigable.
     * Additionally, neighboring nodes may also be marked as non-navigable to form larger obstacles.
     */
    public void creatingNotNavigableObstacles() {

        Random random = new Random();
        for (int i = 0; i < height / nodeSize; i += chunkSize) {
            double yRange = i + chunkSize;
            for (int j = 0; j < width / nodeSize; j += chunkSize) {

                if ((i > 2 && i < 6) && (j < 9 && j > 5)) continue;

                int rowNodeSelection = random.nextInt(i, (int) yRange);
                int columnNodeSelection = random.nextInt(j, j + (int) chunkSize);
                getMapGuide().get(rowNodeSelection).get(columnNodeSelection).setNavigable(false);

                int blocks = 1;
                while (blocks <= chunkSize - 2) {

                    switch (random.nextInt(0, 5)) {
                        case 0: // up
                            if (rowNodeSelection - 1 >= i) {
                                getMapGuide().get(rowNodeSelection - 1).get(columnNodeSelection).setNavigable(false);
                                blocks++;
                            }
                            break;
                        case 1: // down
                            if (rowNodeSelection + 1 < yRange) {
                                getMapGuide().get(rowNodeSelection + 1).get(columnNodeSelection).setNavigable(false);
                                blocks++;
                            }
                            break;
                        case 2: // left
                            if (columnNodeSelection - 1 >= j) {
                                getMapGuide().get(rowNodeSelection).get(columnNodeSelection - 1).setNavigable(false);
                                blocks++;
                            }
                            break;
                        case 3: // Right
                            if (columnNodeSelection + 1 < j + chunkSize) {
                                getMapGuide().get(rowNodeSelection).get(columnNodeSelection + 1).setNavigable(false);
                                blocks++;
                            }
                            break;

                    }

                }
            }
        }
    }

    /**
     * Establishes a graph representation of the map for finding minimum paths.
     * The graph is constructed based on navigable nodes in the map, where each node corresponds to a Coordinate.
     * Edges are added between adjacent navigable nodes to represent valid paths.
     */
    public void establishGraphMapRepresentationForMinimumPaths() {

        // Iterate over map rows
        Set<Coordinate> coordinateSet = new HashSet<>();
        // Iterate over map columns
        for (int i = 0; i < height / nodeSize; i++) {
            for (int j = 0; j < width / nodeSize; j++) {

                Coordinate actualNode = new Coordinate();
                actualNode.setX(getMapGuide().get(i).get(j).getPosition().getX());
                actualNode.setY(getMapGuide().get(i).get(j).getPosition().getY());

                // Check if the current node is navigable and hasn't been added to the graph
                if (getMapGuide().get(i).get(j).isNavigable() && !coordinateSet.contains(actualNode)) {
                    graph.insertVertex(actualNode);
                    coordinateSet.add(actualNode);
                }

                // Check if the current node and its right neighbor are both navigable
                if (getMapGuide().get(i).get(j).isNavigable() && j != width / nodeSize - 1) {

                    if (getMapGuide().get(i).get(j + 1).isNavigable()) {

                        Coordinate coordinate = new Coordinate();
                        coordinate.setX(getMapGuide().get(i).get(j + 1).getPosition().getX());
                        coordinate.setY(getMapGuide().get(i).get(j + 1).getPosition().getY());
                        graph.insertVertex(coordinate);
                        coordinateSet.add(coordinate);
                        graph.insertEdge(actualNode, coordinate);

                    }
                }

                if (i == 0) continue;

                if (getMapGuide().get(i).get(j).isNavigable() && getMapGuide().get(i - 1).get(j).isNavigable()) {

                    Coordinate coordinate = new Coordinate();
                    coordinate.setX(getMapGuide().get(i - 1).get(j).getPosition().getX());
                    coordinate.setY(getMapGuide().get(i - 1).get(j).getPosition().getY());

                    if (!coordinateSet.contains(coordinate)) {
                        graph.insertVertex(coordinate);
                        coordinateSet.add(coordinate);
                    }
                    graph.insertEdge(actualNode, coordinate);

                }
            }
        }
    }


    /**
     * Establishes a matrix graph representation of the map for finding minimum paths.
     * Nodes in the matrix graph correspond to navigable coordinates on the map.
     * Edges are added between adjacent navigable nodes to represent valid paths.
     */
    public void establishMatrixGraphMapRepresentationForMinimumPaths() {
        Set<Coordinate> coordinateSet = new HashSet<>();

        // Iterate over map rows and columns to create nodes and edges in the matrix graph
        for (int i = 0; i < height / nodeSize; i++) {
            for (int j = 0; j < width / nodeSize; j++) {
                Coordinate actualNode = new Coordinate();
                actualNode.setX(getMapGuide().get(i).get(j).getPosition().getX());
                actualNode.setY(getMapGuide().get(i).get(j).getPosition().getY());

                // Check if the current node is navigable and hasn't been added to the matrix graph
                if (getMapGuide().get(i).get(j).isNavigable() && !coordinateSet.contains(actualNode)) {
                    matrixGraph.insertVertex(actualNode);
                    coordinateSet.add(actualNode);
                }

                // Check if the current node and its right neighbor are both navigable
                if (getMapGuide().get(i).get(j).isNavigable() && j != width / nodeSize - 1) {
                    if (getMapGuide().get(i).get(j + 1).isNavigable()) {
                        Coordinate coordinate = new Coordinate();
                        coordinate.setX(getMapGuide().get(i).get(j + 1).getPosition().getX());
                        coordinate.setY(getMapGuide().get(i).get(j + 1).getPosition().getY());

                        // Add the right neighbor to the matrix graph and create an edge
                        if (!coordinateSet.contains(coordinate)) {
                            matrixGraph.insertVertex(coordinate);
                            coordinateSet.add(coordinate);
                        }
                        matrixGraph.insertEdge(actualNode, coordinate);
                    }
                }

                // Skip the first row to avoid out-of-bounds index
                if (i == 0) continue;

                // Check if the current node and its upper neighbor are both navigable
                if (getMapGuide().get(i).get(j).isNavigable() && getMapGuide().get(i - 1).get(j).isNavigable()) {
                    Coordinate coordinate = new Coordinate();
                    coordinate.setX(getMapGuide().get(i - 1).get(j).getPosition().getX());
                    coordinate.setY(getMapGuide().get(i - 1).get(j).getPosition().getY());

                    // Add the upper neighbor to the matrix graph and create an edge
                    if (!coordinateSet.contains(coordinate)) {
                        matrixGraph.insertVertex(coordinate);
                        coordinateSet.add(coordinate);
                    }
                    matrixGraph.insertEdge(actualNode, coordinate);
                }
            }
        }
    }


    /**
     * Checks for collision between a given box and the navigable nodes on the map.
     *
     * @param box The box to check for collision.
     * @return True if there is a collision, indicating that the box overlaps with non-navigable nodes on the map; otherwise, false.
     */
    public boolean mapCollision(Box box) {
        Coordinate coordinateUp = new Coordinate(box.getxMin(), box.getyMin());
        Coordinate coordinateDown = new Coordinate(box.getxMax(), box.getyMax());
        List<Coordinate> coordinates = new ArrayList<>();

        // Populate a list of coordinates representing the corners of the given box
        coordinates.add(coordinateDown);
        coordinates.add(coordinateUp);
        coordinates.add(new Coordinate(box.getxMax(), box.getyMin()));
        coordinates.add(new Coordinate(box.getxMin(), box.getyMax()));

        boolean isCollision = false;

        // Check for collision with navigable nodes for each corner coordinate
        for (Coordinate coordinate : coordinates) {
            int yPosition = (int) Math.floor(coordinate.getY() / getNodeSize());
            int xPosition = (int) Math.floor(coordinate.getX() / getNodeSize());

            // Check if the node at the given position is non-navigable
            isCollision = !getMapGuide().get(yPosition).get(xPosition).isNavigable() || isCollision;

            // If collision is detected, return true immediately
            if (isCollision) return true;
        }

        // Return false if no collision is detected
        return false;
    }

    /**
     * Checks if a given box exceeds the map limits, taking into account a buffer zone.
     *
     * @param box The box to check against the map limits.
     * @return True if the box exceeds the map limits, considering a buffer zone; otherwise, false.
     */
    public boolean mapLimit(Box box) {
        // Define buffer zones for the map limits
        int bufferZone = 3;

        // Check if any corner of the box exceeds the specified map limits
        if (box.getxMin() < bufferZone || box.getyMin() < bufferZone ||
                box.getxMax() > getWidth() - bufferZone || box.getyMax() > getHeight() - bufferZone) {
            return true;
        }

        // Return false if the box is within the map limits
        return false;
    }

    /**
     * Gets the 2D ArrayList representing the map guide.
     *
     * @return The 2D ArrayList containing MapNode objects that represent the guide of the map.
     */
    public ArrayList<ArrayList<MapNode>> getMapGuide() {
        return mapGuide;
    }

    /**
     * Gets the width of the map.
     *
     * @return The width of the map as a double value.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the width of the map.
     *
     * @param width The new width value to be set for the map.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Gets the height of the map.
     *
     * @return The height of the map as a double value.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height of the map.
     *
     * @param height The new height value to be set for the map.
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Sets the map guide, a 2D ArrayList of MapNode objects representing the structure of the map.
     *
     * @param mapGuide The new map guide to be set for the map.
     */
    public void setMapGuide(ArrayList<ArrayList<MapNode>> mapGuide) {
        this.mapGuide = mapGuide;
    }

    /**
     * Gets the adjacency list graph associated with the map.
     *
     * @return The AdjacencyListGraph containing coordinates that represents the graph of the map.
     */
    public AdjencyList<Coordinate> getGraph(){
        return graph;}


    /**
     * Sets the adjacency list graph for the map.
     *
     * @param graph The AdjacencyListGraph containing coordinates to be set for the map.
     */
    public void setGraph(AdjencyList<Coordinate> graph) {
        this.graph = graph;
    }

    /**
     * Gets the size of the nodes on the map.
     *
     * @return The size of each node on the map as a double value.
     */
    public double getNodeSize() {
        return nodeSize;
    }

    /**
     * Sets the size of the nodes on the map.
     *
     * @param nodeSize The new size value to be set for each node on the map.
     */
    public void setNodeSize(double nodeSize) {
        this.nodeSize = nodeSize;
    }

    /**
     * Gets the size of the chunks used in map generation and obstacle creation.
     *
     * @return The size of each chunk as a double value.
     */
    public double getChunkSize() {
        return chunkSize;
    }

    /**
     * Sets the size of the chunks used in map generation and obstacle creation.
     *
     * @param chunkSize The new size value to be set for each chunk on the map.
     */
    public void setChunkSize(double chunkSize) {
        this.chunkSize = chunkSize;
    }


    /**
     * Gets the matrix graph associated with the map.
     *
     * @return The AdjacencyMatrixGraph containing coordinates that represents the matrix graph of the map.
     */
    public AdjacencyMatrix<Coordinate> getMatrixGraph() {
        return matrixGraph;
    }

    /**
     * Sets the matrix graph for the map.
     *
     * @param matrixGraph The AdjacencyMatrixGraph containing coordinates to be set for the map.
     */
    public void setMatrixGraph(AdjacencyMatrix<Coordinate> matrixGraph) {
        this.matrixGraph = matrixGraph;
    }
}
