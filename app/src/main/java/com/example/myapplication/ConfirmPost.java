package com.example.myapplication;

public class ConfirmPost {
    String docId,idPost,ownerPost,studentConfirm;

    public ConfirmPost(){}

    public ConfirmPost(String docId, String idPost, String ownerPost, String studentConfirm) {
        this.docId = docId;
        this.idPost = idPost;
        this.ownerPost = ownerPost;
        this.studentConfirm = studentConfirm;

    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getOwnerPost() {
        return ownerPost;
    }

    public void setOwnerPost(String ownerPost) {
        this.ownerPost = ownerPost;
    }

    public String getStudentConfirm() {
        return studentConfirm;
    }

    public void setStudentConfirm(String studentConfirm) {
        this.studentConfirm = studentConfirm;
    }


}
