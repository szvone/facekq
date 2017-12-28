package cn.szvone.dao;

import cn.szvone.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentDAO {
    Student queryByOpenid(@Param("id") String id);

    List<Student> queryByClassid(int id);

    int register(Student student);

    int edit(@Param("name") String name, @Param("stuno") String stuno,@Param("classid") String classid, @Param("classname") String classname,@Param("openid") String openid);

    int setface(@Param("faceimg") String faceimg, @Param("facetoken") String facetoken, @Param("openid") String openid);
}
