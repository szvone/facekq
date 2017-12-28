package cn.szvone.dto;

public class TableRes<T> {
    private int code;
    private String msg;
    private int count;
    private T data;

    public TableRes(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public TableRes(int code, int count, T data) {
        this.code = code;
        this.data = data;
        this.count = count;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
