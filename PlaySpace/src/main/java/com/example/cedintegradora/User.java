package com.example.cedintegradora;

public class User {
    static private User user;

    static public User getInstance(){
        if(user==null) user = new User();
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    private String name;
    private int lives;

    private User(){

    }
}
