package cn.szvone.entity;

import java.util.Date;

public class Kqinfo {
    private int id;
    private int kqid;
    private int classid;
    private String classname;
    private int studentid;
    private String studentname;
    private Date time;
    private String address;
    private String faceimg;
    private String xsd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKqid() {
        return kqid;
    }

    public void setKqid(int kqid) {
        this.kqid = kqid;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFaceimg() {
        return faceimg;
    }

    public void setFaceimg(String faceimg) {
        this.faceimg = faceimg;
    }

    public String getXsd() {
        return xsd;
    }

    public void setXsd(String xsd) {
        this.xsd = xsd;
    }

    @Override
    public String toString() {
        return "Kqinfo{" +
                "id=" + id +
                ", kqid=" + kqid +
                ", classid=" + classid +
                ", classname='" + classname + '\'' +
                ", studentid=" + studentid +
                ", studentname='" + studentname + '\'' +
                ", time=" + time +
                ", address='" + address + '\'' +
                ", faceimg='" + faceimg + '\'' +
                ", xsd='" + xsd + '\'' +
                '}';
    }

    public Kqinfo(int id, int kqid, int classid, String classname, int studentid, String studentname, Date time, String address, String faceimg, String xsd) {
        this.id = id;
        this.kqid = kqid;
        this.classid = classid;
        this.classname = classname;
        this.studentid = studentid;
        this.studentname = studentname;
        this.time = time;
        this.address = address;
        this.faceimg = faceimg;
        this.xsd = xsd;
    }
    public Kqinfo(){}
}
