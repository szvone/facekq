package cn.szvone.service.Impl;

import cn.szvone.dao.KqinfoDAO;
import cn.szvone.dao.KqlistDAO;
import cn.szvone.dao.StuclassDAO;
import cn.szvone.dao.StudentDAO;
import cn.szvone.entity.Kqinfo;
import cn.szvone.entity.Kqlist;
import cn.szvone.entity.Stuclass;
import cn.szvone.entity.Student;
import cn.szvone.service.WxService;
import cn.szvone.util.Face;
import cn.szvone.util.WxJsSDK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import java.util.Date;
import java.util.List;

@Service
public class WxServiceImpl implements WxService{
    @Autowired
    StudentDAO studentDAO;
    @Autowired
    StuclassDAO stuclassDAO;
    @Autowired
    KqlistDAO kqlistDAO;
    @Autowired
    KqinfoDAO kqinfoDAO;

    @Override
    public String shouquan(String code) {
        String openid = WxJsSDK.getOpenid(code);
        return openid;
    }

    @Override
    public Boolean editStudent(String name, String stuno, String classid, String openid) {
        Stuclass stuclass = stuclassDAO.queryById(classid);
        int i = studentDAO.edit(name,stuno,classid,stuclass.getName(),openid);
        if (i==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String checkface(String mid,String path) {
        //String imgurl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" +WxJsSDK.getNowtoken()+"&media_id="+mid;
        //String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
        //path += "/img/";
        String imgurl = "http://face.szvone.cn/facekq/img/"+WxJsSDK.downloadMedia(mid,path);
        //return imgurl;

        String token = Face.detect(imgurl);
        if (token==""){
            return "";
        }else {
            return token;
        }
    }

    @Override
    public String compare(String mid1, String mid2,String path,String openid) {
        String imgurl = "http://face.szvone.cn/facekq/img/"+WxJsSDK.downloadMedia(mid1,path);
        String imgur2 = "http://face.szvone.cn/facekq/img/"+WxJsSDK.downloadMedia(mid2,path);
        String token = Face.detect(imgurl);
        String xsdstr = Face.compared(token,imgur2);
        Double xsd = Double.valueOf(xsdstr);
        if (xsd>=80){
            studentDAO.setface(imgurl,token,openid);
            return "ok";
        }else{
            return xsdstr+"-"+imgurl+"-"+imgur2+"-"+token;
        }

    }

    @Override
    public List<Kqlist> getKqlist(String openid) {
        Student student = studentDAO.queryByOpenid(openid);
        List<Kqlist> kqlists = kqlistDAO.queryByClassid(student.getClassid());
        return kqlists;
    }

    @Override
    public String kq(String mid, String path, String openid, String id, String gps) {
        Student student = studentDAO.queryByOpenid(openid);
        Kqlist kqlist = kqlistDAO.queryById(Integer.valueOf(id));
        String imgurl = "http://face.szvone.cn/facekq/img/"+WxJsSDK.downloadMedia(mid,path);
        String xsdstr = Face.compared(student.getFacetoken(),imgurl);
        Double xsd = Double.valueOf(xsdstr);
        if (xsd>=80){

            Kqinfo kqinfo = new Kqinfo();
            kqinfo.setAddress(gps);
            kqinfo.setClassid(student.getClassid());
            kqinfo.setClassname(student.getClassname());
            kqinfo.setFaceimg(imgurl);
            kqinfo.setXsd(xsdstr);
            kqinfo.setStudentid(student.getId());
            kqinfo.setStudentname(student.getName());
            kqinfo.setTime(new Date());
            kqinfo.setKqid(kqlist.getId());
            kqinfoDAO.Kq(kqinfo);
            kqlistDAO.kq(Integer.valueOf(id),"["+String.valueOf(student.getId())+"]");
            return "ok";
        }else{
            return xsdstr;
        }
    }

}
