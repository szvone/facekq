package cn.szvone.dao;

import cn.szvone.entity.Student;
import cn.szvone.util.WxJsSDK;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoader;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
//设置spring配置文件的位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class StudentDAOTest {
    @Autowired
    private StudentDAO studentDAO;

    @Test
    public void queryByOpenid() {
        Student student = studentDAO.queryByOpenid("1");
        System.out.println(student.toString());
    }

    @Test
    public void queryByClassid() {
        String mid = "iTgtxnWuhfkvvKIXLqZDhlRH9-DM6exctKvBDXq1yDhYZkLb12Df2KUBarzc8YNB";
        String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
        path += "/img/";
        WxJsSDK.downloadMedia(mid,path);
    }

    @Test
    public void register() {
    }
}