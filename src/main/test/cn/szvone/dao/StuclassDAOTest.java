package cn.szvone.dao;

import cn.szvone.entity.Stuclass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
//设置spring配置文件的位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class StuclassDAOTest {
    @Autowired
    StuclassDAO stuclassDAO;

    @Test
    public void queryAll() {
        List<Stuclass> stuclasses =stuclassDAO.queryAll();
        for (Stuclass stuclass:stuclasses){
            System.out.println(stuclass.toString());
        }
    }

    @Test
    public void queryById() {
        Stuclass stuclass = stuclassDAO.queryById("1");
        System.out.println(stuclass.toString());
    }
}