package com.example.myapplication;

public class Comment {
    String content,idPost,owner_comment,docId;

    public Comment(){

    }

    public Comment(String content, String idPost, String owner_comment, String docId) {
        this.content = content;
        this.idPost = idPost;
        this.owner_comment = owner_comment;
        this.docId = docId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getOwner_comment() {
        return owner_comment;
    }

    public void setOwner_comment(String owner_comment) {
        this.owner_comment = owner_comment;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
