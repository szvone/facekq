package cn.szvone.dao;

import cn.szvone.entity.Stuclass;

import java.util.List;

public interface StuclassDAO {
    List<Stuclass> queryAll();

    Stuclass queryById(String id);
}
