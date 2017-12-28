package cn.szvone.dao;

import cn.szvone.entity.Teacher;

public interface TeacherDAO {
    Teacher queryByUser(String user);
    int addTeacher(Teacher teacher);
}
