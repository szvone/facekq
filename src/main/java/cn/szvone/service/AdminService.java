package cn.szvone.service;


import cn.szvone.entity.Kqinfo;
import cn.szvone.entity.Kqlist;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AdminService {
    List<Kqlist> getKqlist(int teacherid);
    int addKq(Kqlist kqlist);
    int delKq(int id);
    String login(String user, String pass, HttpServletRequest request);
    List<Kqinfo> getKqInfo(int kqid);
}
