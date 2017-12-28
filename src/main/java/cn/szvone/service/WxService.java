package cn.szvone.service;


import cn.szvone.entity.Kqlist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WxService {
    String shouquan(String code);

    Boolean editStudent(String name,String stuno,String classid,String openid);

    String checkface(String mid,String path);

    String compare(String mid1,String mid2,String path,String openid);

    List<Kqlist> getKqlist(String openid);

    String kq(String mid,String path,String openid,String id, String gps);
}
