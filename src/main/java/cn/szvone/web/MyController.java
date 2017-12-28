package cn.szvone.web;

import cn.szvone.dao.KqlistDAO;
import cn.szvone.dao.StuclassDAO;
import cn.szvone.dao.StudentDAO;
import cn.szvone.dto.TableRes;
import cn.szvone.entity.*;
import cn.szvone.service.AdminService;
import cn.szvone.service.WxService;
import cn.szvone.util.WxJsSDK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api")
public class MyController {
    @Autowired
    private WxService wxService;
    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private StuclassDAO stuclassDAO;
    @Autowired
    private KqlistDAO kqlistDAO;
    @Autowired
    private AdminService adminService;
    @RequestMapping(value = "/wxToken")
    public void wxToken(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        boolean isGet = request.getMethod().toLowerCase().equals("get");
        PrintWriter print = response.getWriter();

        if (isGet) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            System.out.println("signature = "+signature +" timestamp="+timestamp+" nonce="+nonce+" echostr="+echostr);

            print.write(echostr);
            print.flush();

        }else{
            BufferedReader br = request.getReader();
            String str, wholeStr = "";
            while((str = br.readLine()) != null){
                wholeStr += str;
            }
            System.out.println(wholeStr);
            Wxmessage wxmessage = new Wxmessage(wholeStr);

            System.out.println(wxmessage.toString());

            String appid = "wx69f746dd9e0b7d40";
            String reurl = "http://face.szvone.cn/facekq/api/shouquan?url=edit.html";
            String shouquan = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri=" + reurl + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";


            if (wxmessage.getEven().indexOf("[subscribe]")!=-1){
                //String res = wxService.newGz(wxmessage.getFrom());
                print.write(wxmessage.getResStr("欢迎关注，请点击<a href='"+shouquan+"'>此处登记您的信息</a>！"));
                return;
            }


            //WxServiceImpl wxService = new WxServiceImpl();

            //String res = wxService.getRes(wxmessage.getContent(),wxmessage.getFrom());
            print.write(wxmessage.getResStr(wxmessage.getContent()));

            System.out.println(wxmessage.getContent());
        }

    }

    @RequestMapping(value = "/shouquan")
    public String shouquan(HttpServletRequest request,HttpServletResponse response) throws IOException {

        String openid = wxService.shouquan(request.getParameter("code"));
        String url = request.getParameter("url");
        if (openid!=null){
            Student student = studentDAO.queryByOpenid(openid);
            if (student==null){
                student = new Student(openid);
                studentDAO.register(student);
            }
            Cookie cookie = new Cookie("userid",String.valueOf(student.getId()));
            cookie.setPath("/");
            response.addCookie(cookie);
            request.getSession().setAttribute("user",student);
            request.getSession().setAttribute("openid",openid);
        }
        //response.getWriter().print(openid);
        if (url==null){
            return "redirect:../user.html";
        }else{
            return "redirect:../"+url;
        }

    }

    @RequestMapping(value = "/getWxToken")
    public void getWxToken(HttpServletResponse response,String url) throws IOException {
        String json = WxJsSDK.getParam(url);
        response.getWriter().print(json);
    }

    @RequestMapping(value = "/getUserInfo")
    @ResponseBody
    public Student getUserInfo(HttpServletResponse response,HttpServletRequest request) {
        Student student = (Student) request.getSession().getAttribute("user");
        Student student1 = studentDAO.queryByOpenid(student.getOpenid());
        if(student1.getFaceimg()==null){
            student1.setFaceimg("https://tvax4.sinaimg.cn/default/images/default_avatar_male_180.gif");
        }
        return student1;
    }

    @RequestMapping(value = "/getAllClass")
    @ResponseBody
    public List<Stuclass> getAllClass(HttpServletResponse response,HttpServletRequest request) {
        List<Stuclass> stuclasses = stuclassDAO.queryAll();
        return stuclasses;
    }

    @RequestMapping(value = "/updataface")
    @ResponseBody
    public void updataface(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws IOException {
        //List<Stuclass> stuclasses = stuclassDAO.queryAll();
        //response.getWriter().print("1");

        String mid1 = request.getParameter("mid1");
        String path = session.getServletContext().getRealPath("/")+"/img/";
        String mid2 = request.getParameter("mid2");

        String is = wxService.compare(mid1,mid2,path, (String) session.getAttribute("openid"));
        if (is.equals("ok"))
            response.getWriter().print("1");
        else
            response.getWriter().print(is);

    }

    @RequestMapping(value = "/checkface")
    public void checkface(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws IOException {
        //List<Stuclass> stuclasses = stuclassDAO.queryAll();
        String mid = request.getParameter("mid");
        String path = session.getServletContext().getRealPath("/");
        path += "/img/";

        String is = wxService.checkface(mid,path);
        if (is!="")
            response.getWriter().print(is);
        else
            response.getWriter().print("error");

    }



    @RequestMapping(value = "/setInfo")
    public void setInfo(HttpServletResponse response,HttpServletRequest request) throws IOException {
        String name = request.getParameter("name");
        String stuno = request.getParameter("stuno");
        String classid = request.getParameter("classid");
        if (name == null || stuno == null || classid==null){
            return;
        }
        Boolean b = wxService.editStudent(name,stuno,classid, (String) request.getSession().getAttribute("openid"));
        if (b){
            response.getWriter().print("1");
        }
    }

    @RequestMapping(value = "/getKqlist")
    @ResponseBody
    public List<Kqlist> getKqlist(HttpServletResponse response, HttpServletRequest request) {
        String openid = (String) request.getSession().getAttribute("openid");
        if (openid==null)
            return null;
        List<Kqlist> kqlists = wxService.getKqlist(openid);
        return kqlists;
    }

    @RequestMapping(value = "/kq")
    public void kq(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws IOException {

        String mid = request.getParameter("mid");
        String path = session.getServletContext().getRealPath("/")+"/img/";
        String gps = request.getParameter("gps");
        String id = request.getParameter("id");

        String is = wxService.kq(mid,path, (String) session.getAttribute("openid"),id,gps);
        if (is.equals("ok"))
            response.getWriter().print("1");
        else
            response.getWriter().print(is);

    }


    @RequestMapping(value = "/getKqlist1")
    @ResponseBody
    public TableRes<List<Kqlist>> getKqlist1(HttpServletResponse response, HttpServletRequest request) {
        String teacherid = (String) request.getSession().getAttribute("id");
        System.out.println(teacherid);
        if (teacherid==null)
            return null;
        List<Kqlist> kqlists = adminService.getKqlist(Integer.valueOf(teacherid));
        if (kqlists!=null)
            return new TableRes<List<Kqlist>>(0,1,kqlists);
        return null;
    }

    @RequestMapping(value = "/getKqinfo")
    @ResponseBody
    public TableRes<List<Kqinfo>> getKqinfo(HttpServletResponse response, HttpServletRequest request) {
        String kqid = request.getParameter("id");
        System.out.println(kqid);
        if (kqid==null)
            return null;
        List<Kqinfo> kqinfos = adminService.getKqInfo(Integer.valueOf(kqid));
        if (kqinfos!=null)
            return new TableRes<List<Kqinfo>>(0,1,kqinfos);
        return null;
    }

    @RequestMapping(value = "/login")
    public void login(HttpServletResponse response, HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("utf-8");
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
        String res = adminService.login(user,pass,request);
        response.setCharacterEncoding("utf-8");
        if (!res.equals("用户不存在") && !res.equals("密码错误")){
            request.getSession().setAttribute("name",user);
            request.getSession().setAttribute("id",res);
            response.getWriter().print("<h1>登录成功,正在跳转中...</h1><script>setTimeout(function () {\n" +
                    "                        window.location.href=\"../kqlist.html\";\n" +
                    "                    },3000);</script>");
            return;
        }
        response.getWriter().print("<h1>"+res+",正在跳转中...</h1><script>setTimeout(function () {\n" +
                "                        window.location.href=\"../index.html\";\n" +
                "                    },3000);</script>");
        //return "";
    }

    @RequestMapping(value = "/addKq")
    @ResponseBody
    public void addKq(HttpServletResponse response, HttpServletRequest request) throws IOException {

        String name = request.getParameter("name");
        String classid = request.getParameter("classid");
        if (request.getSession().getAttribute("id")==null || name==null || classid==null)
            return;
        Kqlist kqlist = new Kqlist();
        Stuclass stuclass = stuclassDAO.queryById(classid);

        kqlist.setClassid(Integer.valueOf(classid));
        kqlist.setCname(stuclass.getName());
        kqlist.setClassid(stuclass.getId());
        kqlist.setName(name);
        kqlist.setStarttime(new Date());
        kqlist.setSum(0);
        kqlist.setTeacherid(Integer.valueOf((String) request.getSession().getAttribute("id")));
        kqlist.setTeachername((String) request.getSession().getAttribute("name"));
        kqlist.setStus("");

        int res = adminService.addKq(kqlist);
        if (res==1){
            response.getWriter().print("1");
        }

    }
    @RequestMapping(value = "/delKq")
    public void delKq(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String id = request.getParameter("id");
        int res = adminService.delKq(Integer.valueOf(id));
        response.getWriter().print(res);
    }
}
