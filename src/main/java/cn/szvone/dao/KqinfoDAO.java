package cn.szvone.dao;

import cn.szvone.entity.Kqinfo;

import java.util.List;

public interface KqinfoDAO {
    int Kq(Kqinfo kqinfo);
    List<Kqinfo> queryByKqId(int id);
    List<Kqinfo> queryByStudentId(int id);
    List<Kqinfo> queryByClassId(int id);
}
