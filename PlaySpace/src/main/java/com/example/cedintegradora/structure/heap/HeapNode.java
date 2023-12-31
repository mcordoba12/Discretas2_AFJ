package com.example.cedintegradora.structure.heap;

public class HeapNode <K extends Comparable,V> {

    private K key;

    private V value;


    //Constructor
    public HeapNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    //Getter and Setter

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
