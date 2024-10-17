package org.example.studentsresponsestimesstatisticalsystem.member;

import lombok.Data;

/**
 * @author 张文博
 * @version 1.0.0
 * @title Student
 * @description 学生类
 * @creat 2024/10/12 上午11:25
 **/
@Data
public class Student {
    private String name;
    private String studentId ;
    private String grade;
    private int time = 0;

    public Student(String name, String studentId, String grade, int time) {
        this.name = name;
        this.studentId = studentId;
        this.grade = grade;
        this.time = time;
    }

    public Student(String name, String studentId, String grade) {
        this.name = name;
        this.studentId = studentId;
        this.grade = grade;
    }

    public void addTime(int time){
        this.time+=time;
    }
}
