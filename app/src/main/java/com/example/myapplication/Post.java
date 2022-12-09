package com.example.myapplication;

public class Post {
   String name_owner,category,image,price,title,docId,code_owner,status;

   public Post(){

   }


    public Post(String name_owner, String category, String image, String price, String title, String docId, String code_owner, String status) {
        this.name_owner = name_owner;
        this.category = category;
        this.image = image;
        this.price = price;
        this.title = title;
        this.docId = docId;
        this.code_owner = code_owner;
        this.status = status;
    }





    public String getName_owner() {
        return name_owner;
    }

    public void setName_owner(String name_owner) {
        this.name_owner = name_owner;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
    public String getCode_owner() {
        return code_owner;
    }
    public void setCode_owner(String code_owner) {
        this.code_owner = code_owner;
    }


    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
