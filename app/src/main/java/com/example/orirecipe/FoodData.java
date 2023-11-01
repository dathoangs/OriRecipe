package com.example.orirecipe;

public class FoodData {

    private String itemName, itemDesc, itemImage, itemId, userID, userName;
    int likeNumber;

    public FoodData(String itemId, String itemName, String itemDesc,
                    String itemImage, String userID, String userName, int likeNumber) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemImage = itemImage;
        this.userID = userID;
        this.userName = userName;
        this.likeNumber = likeNumber;
    }

    public FoodData (){}

    public String getItemName() {
        return itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getItemId() {
        return itemId;
    }

    public int getLikeNumber() {
        return likeNumber;
    }
}