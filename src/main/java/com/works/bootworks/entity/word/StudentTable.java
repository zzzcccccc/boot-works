package com.works.bootworks.entity.word;

import java.util.List;

public class StudentTable {
    private String title;
    private List<Student> studentList;

    private List<Student> studentList1;

    public List<Student> getStudentList1() {
        return studentList1;
    }

    public void setStudentList1(List<Student> studentList1) {
        this.studentList1 = studentList1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
}

