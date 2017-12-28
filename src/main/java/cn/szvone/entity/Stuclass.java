package cn.szvone.entity;

public class Stuclass {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Stuclass(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Stuclass(){}

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Stuclass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
