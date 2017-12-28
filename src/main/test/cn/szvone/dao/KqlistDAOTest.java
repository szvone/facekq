package cn.szvone.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
//设置spring配置文件的位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class KqlistDAOTest {
    @Autowired
    KqlistDAO kqlistDAO;

    @Test
    public void kq() {
        kqlistDAO.kq(1,"2,");
    }
}