package cn.szvone.entity;

public class Student {
    private int id;
    private String openid;
    private String name;
    private String stuno;
    private int classid;
    private String facetoken;
    private String faceimg;
    private String classname;

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public Student(int id, String openid, String name, String stuno, int classid, String facetoken, String faceimg, String classname) {
        this.id = id;
        this.openid = openid;
        this.name = name;
        this.stuno = stuno;
        this.classid = classid;
        this.facetoken = facetoken;
        this.faceimg = faceimg;
        this.classname = classname;
    }

    public Student(){}

    public Student(String openid) {
        this.openid = openid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStuno() {
        return stuno;
    }

    public void setStuno(String stuno) {
        this.stuno = stuno;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getFacetoken() {
        return facetoken;
    }

    public void setFacetoken(String facetoken) {
        this.facetoken = facetoken;
    }

    public String getFaceimg() {
        return faceimg;
    }

    public void setFaceimg(String faceimg) {
        this.faceimg = faceimg;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", name='" + name + '\'' +
                ", stuno='" + stuno + '\'' +
                ", classid=" + classid +
                ", facetoken='" + facetoken + '\'' +
                ", faceimg='" + faceimg + '\'' +
                '}';
    }
}
