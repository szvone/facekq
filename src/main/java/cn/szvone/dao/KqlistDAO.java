package cn.szvone.dao;

import cn.szvone.entity.Kqinfo;
import cn.szvone.entity.Kqlist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KqlistDAO {
    int addKq(Kqlist kqlist);
    int delKq(int id);

    int kq(@Param("id") int id, @Param("stuid") String stuid);

    List<Kqlist> queryByTeacherid(int id);
    List<Kqlist> queryByClassid(int id);

    Kqlist queryById(int id);
}
