package cn.szvone.service.Impl;

import cn.szvone.dao.KqinfoDAO;
import cn.szvone.dao.KqlistDAO;
import cn.szvone.dao.TeacherDAO;
import cn.szvone.entity.Kqinfo;
import cn.szvone.entity.Kqlist;
import cn.szvone.entity.Student;
import cn.szvone.entity.Teacher;
import cn.szvone.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    KqlistDAO kqlistDAO;
    @Autowired
    TeacherDAO teacherDAO;
    @Autowired
    KqinfoDAO kqinfoDAO;
    @Override
    public List<Kqlist> getKqlist(int teacherid) {
        List<Kqlist> kqlists = kqlistDAO.queryByTeacherid(teacherid);
        return kqlists;
    }

    @Override
    public int addKq(Kqlist kqlist) {
        int i = kqlistDAO.addKq(kqlist);
        return i;
    }

    @Override
    public int delKq(int id) {
        int i = kqlistDAO.delKq(id);
        return i;
    }

    @Override
    public String login(String user, String pass, HttpServletRequest request) {
        Teacher teacher = teacherDAO.queryByUser(user);
        if (teacher==null)
            return "用户不存在";
        if (pass.equals(teacher.getPass()))
            return "密码错误";
        //request.getSession().setAttribute("id",teacher.getId());
        System.out.println("id1:"+teacher.getId());
        return String.valueOf(teacher.getId());
    }

    @Override
    public List<Kqinfo> getKqInfo(int kqid) {
        List<Kqinfo> kqinfos = kqinfoDAO.queryByKqId(kqid);
        return kqinfos;
    }
}
