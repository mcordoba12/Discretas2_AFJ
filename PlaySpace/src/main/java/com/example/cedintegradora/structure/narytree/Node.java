package com.example.cedintegradora.structure.narytree;

import java.util.ArrayList;

public class Node<V> {

    private V element;
    private ArrayList<Node> children;

    public Node(V element) {
        this.element = element;
        children = new ArrayList<>();
    }

    /**
     * Deletes the specified child element from the list of children.
     *
     * @param child The child element to be deleted.
     */
    public void deleteChild(V child) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getElement().equals(child)) {
                // Check if the child is a leaf before removal.
                if (children.get(i).isLeaf()) {
                    children.remove(i);
                } else {
                    System.out.println("Cannot delete non-leaf child.");
                }
            }
        }
    }

    /**
     * Calculates the height of the tree rooted at this node.
     *
     * @return The height of the tree. For a leaf node, the height is 1.
     */
    public int treeHeight() {
        if (isLeaf()) {
            return 1;
        } else {
            int maxHeight = 0;
            for (int i = 0; i < children.size(); i++) {
                int tempHeight = children.get(i).treeHeight();
                if (tempHeight > maxHeight) {
                    maxHeight = tempHeight;
                }
            }
            return maxHeight + 1;
        }
    }

    /**
     * Calculates the weight of the tree rooted at this node.
     *
     * @return The weight of the tree. For a leaf node, the weight is 1.
     */
    public int weight() {
        if (isLeaf()) {
            return 1;
        } else {
            int accumulator = 1;
            for (int i = 0; i < children.size(); i++) {
                accumulator += children.get(i).weight();
            }
            return accumulator;
        }
    }

    /**
     * Performs a pre-order traversal of the tree rooted at this node.
     *
     * @param nodeList An ArrayList to store the nodes visited during the traversal.
     * @return The ArrayList containing nodes in the order visited (pre-order).
     */
    public ArrayList<Node> preOrder(ArrayList<Node> nodeList) {
        if (isLeaf()) {
            nodeList.add(this);
            return nodeList;
        }

        nodeList.add(this);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).preOrder(nodeList);
        }
        return nodeList;
    }

    /**
     * Performs a post-order traversal of the tree rooted at this node.
     *
     * @param nodeList An ArrayList to store the nodes visited during the traversal.
     * @return The ArrayList containing nodes in the order visited (post-order).
     */
    public ArrayList<Node> postOrder(ArrayList<Node> nodeList) {
        if (isLeaf()) {
            nodeList.add(this);
            return nodeList;
        }

        for (int i = 0; i < children.size(); i++) {
            children.get(i).postOrder(nodeList);
        }
        nodeList.add(this);
        return nodeList;
    }

    /**
     * Searches for a specific element in the tree rooted at this node using a depth-first search.
     *
     * @param element The element to search for.
     * @return The node containing the specified element, or null if the element is not found.
     */
    public Node<V> searchElement(V element) {
        // Check if the current node contains the element
        if (element.equals(this.getElement())) {
            return this;
        }

        // If the node is a leaf, the element is not found
        if (this.isLeaf()) {
            return null;
        }

        // Recursively search in the children nodes
        for (int i = 0; i < children.size(); i++) {
            Node<V> searchElement = getChildren().get(i).searchElement(element);
            if (searchElement != null) {
                return searchElement;
            }
        }

        // Element not found in the subtree rooted at this node
        return null;
    }

    public void insertNode(V toInsert){
        this.children.add(new Node(toInsert));
    }

    /**
     * Checks if the current node is a leaf node (has no children).
     *
     * @return true if the node is a leaf, false otherwise.
     */
    public boolean isLeaf() {
        // Check if the node has no children
        return children.size() == 0;
    }

    public V getElement() {
        return element;
    }

    public void setElement(V element) {
        this.element = element;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }
}
