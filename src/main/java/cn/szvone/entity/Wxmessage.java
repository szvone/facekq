package cn.szvone.entity;

import java.util.Date;

public class Wxmessage {

    private String from;
    private String to;
    private String time;
    private String msgType;
    private String content;
    private String msgId;
    private String even;
    @Override
    public String toString() {
        return "Wxmessage{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", time='" + time + '\'' +
                ", msgType='" + msgType + '\'' +
                ", content='" + content + '\'' +
                ", msgId='" + msgId + '\'' +
                ", even="+even+
                '}';
    }

    public String getEven() {
        return even;
    }

    public void setEven(String even) {
        this.even = even;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Wxmessage(String str){
        from = getMiddleStr(str,"<FromUserName><![CDATA[","]]></FromUserName>");
        to = getMiddleStr(str,"<ToUserName>","</ToUserName>");
        time = getMiddleStr(str,"<CreateTime>","</CreateTime>");
        msgType = getMiddleStr(str,"<MsgType>","</MsgType>");
        content = getMiddleStr(str,"<Content><![CDATA[","]]></Content>");
        msgId = getMiddleStr(str,"<MsgId>","</MsgId>");
        even = getMiddleStr(str,"<Event>","</Event>");
    }

    public String getResStr(String str){
        String resstr = "<xml>\n" +
                "\n" +
                "<ToUserName>"+from+"</ToUserName>\n" +
                "\n" +
                "<FromUserName>"+to+"</FromUserName>\n" +
                "\n" +
                "<CreateTime>"+new Date().getTime()+"</CreateTime>\n" +
                "\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "\n" +
                "<Content><![CDATA["+str+"]]></Content>\n" +
                "\n" +
                "</xml>";
        return resstr;
    }


    //取出中间文本
    public static String getMiddleStr(String str,String startStr,String endStr){
        String result;
        int start = str.indexOf(startStr);              // 从a开始
        if (start==-1){
            return "";
        }
        int end = str.substring(start+startStr.length()).indexOf(endStr);                   // 截止至b
        //System.out.println("START:"+start+" end:"+(start+end)+" length:"+str.length());
        if (end==-1) {
            result = str.substring(start);      // 截取结果
        }else{
            result = str.substring(start+startStr.length(), start+startStr.length() + end);      // 截取结果
        }
        return result;
    }


}
