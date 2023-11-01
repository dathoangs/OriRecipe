package com.example.orirecipe;

import java.util.*;

public class User {
    public String name, email, id;
    public ArrayList<String> favRecipe;
    public User() {
    }

    public User(String id, String name, String email, ArrayList<String> favRecipe) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.favRecipe = favRecipe;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getFavRecipe() {
        return favRecipe;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFavRecipe(ArrayList<String> favRecipe) {
        this.favRecipe = favRecipe;
    }
}