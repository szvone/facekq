package cn.szvone.entity;

import java.util.Date;

public class Kqlist {
    private int id;
    private String name;
    private int classid;
    private String cname;
    private Date starttime;
    private int sum;
    private int teacherid;
    private String teachername;
    private String stus;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getStus() {
        return stus;
    }

    public void setStus(String stus) {
        this.stus = stus;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public Kqlist(){}

    public Kqlist(int id, String name, int classid, Date starttime, int sum, int teacherid, String teachername) {
        this.id = id;
        this.name = name;
        this.classid = classid;
        this.starttime = starttime;
        this.sum = sum;
        this.teacherid = teacherid;
        this.teachername = teachername;
    }

    @Override
    public String toString() {

        return "Kqlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classid=" + classid +
                ", starttime=" + starttime +
                ", sum=" + sum +
                ", teacherid=" + teacherid +
                '}';
    }

    public int getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(int teacherid) {
        this.teacherid = teacherid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
