package com.example.samridhamla06.faptitude_mongo;

/**
 * Created by samridhamla06 on 03/12/15.
 */
public class Question {
    private int qid;
    private String desc;
    private int image_id;
    private String caption;
    private String insert_user;

    public Question(int qid, String desc, String caption) {
        this.qid = qid;
        this.desc = desc;
        this.caption = caption;
    }

    public Question(int qid, String desc, int image_id, String caption, String insert_user) {
        this.qid = qid;
        this.desc = desc;
        this.image_id = image_id;
        this.caption = caption;
        this.insert_user = insert_user;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return "Question{" +
                "qid=" + qid +
                ", desc='" + desc + '\'' +
                ", image_id=" + image_id +
                ", caption='" + caption + '\'' +
                ", insert_user='" + insert_user + '\'' +
                '}';
    }

    public String getInsert_user() {
        return insert_user;
    }

    public void setInsert_user(String insert_user) {
        this.insert_user = insert_user;
    }
}
