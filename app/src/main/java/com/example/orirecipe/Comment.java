package com.example.orirecipe;

import java.util.ArrayList;

public class Comment {
    ArrayList <String> comment;
    public Comment (ArrayList<String> comment){
        this.comment = comment;
    }

    public Comment (){}

    public void setComment(ArrayList<String> comment) {
        this.comment = comment;
    }

    public ArrayList<String> getComment() {
        return comment;
    }
}
