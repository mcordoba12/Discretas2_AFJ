package com.example.cedintegradora.structure.narytree;

import java.util.ArrayList;

public class NaryTree<V>{

    private Node<V> root;

    public NaryTree() {
    }

    public NaryTree(Node<V> root) {
        this.root = root;
    }

    /**
     * Deletes a node with the specified child element and its associated father element.
     *
     * @param child  the element of the node to be deleted.
     * @param father the element of the father node.
     */
    public void deleteNode(V child, V father){
        if(child.equals(root.getElement())){
            if (!root.isLeaf()){
                root = null;
            }else System.out.println("No se puede eliminar la raíz");
        }else {
            Node<V> fatherNode = root.searchElement(father);
            if (fatherNode == null) System.out.println("No se encontró");
            else fatherNode.deleteChild( child);
        }
    }

    /**
     * Calculates the weight (total number of nodes) of the tree.
     *
     * @return the weight of the tree.
     */
    public int weight (){
        if (root == null) return 0;
        else return root.weight();
    }

    /**
     * Calculates the height of the tree.
     *
     * @return the height of the tree.
     */
    public int treeHegiht (){
        if (root == null) return 0;
        else return root.treeHeight();
    }

    /**
     * Performs a pre-order traversal of the tree and returns a list of nodes in the traversal order.
     *
     * @return a list of nodes in pre-order.
     */
    public ArrayList<Node> preOrder(){
        if (root == null) return null;
        return root.preOrder(new ArrayList<Node>());
    }

    /**
     * Performs a post-order traversal of the tree and returns a list of nodes in the traversal order.
     *
     * @return a list of nodes in post-order.
     */
    public ArrayList<Node> postOrder(){
        if (root == null) return null;
        return root.postOrder(new ArrayList<Node>());
    }

    /**
     * Inserts a new node with the specified element under the node with the given father element.
     *
     * @param toInsert the element to insert.
     * @param father   the element of the father node.
     */
    public void insertNode (V toInsert, V father){
        if (root == null) {
            root = new Node<>(toInsert);
        }else{
            Node<V> nodeFather = root.searchElement(father);
            if (nodeFather != null)  nodeFather.insertNode(toInsert);
            else System.out.println("No se pudo insertar, padre no existe");
        }
    }

    /**
     * Searches for a node with the specified element in the tree.
     *
     * @param element the element to search for.
     * @return the node with the specified element, or null if not found.
     */
    public Node<V> searhcElement (V element){
        if (root == null)return null;
        else{
            return this.searhcElement(element);
        }
    }

    /**
     * Gets the root node of the tree.
     *
     * @return the root node.
     */
    public Node<V> getRoot() {
        return root;
    }

    /**
     * Sets the root node of the tree.
     *
     * @param root the new root node.
     */
    public void setRoot(Node<V> root) {
        this.root = root;
    }
}
