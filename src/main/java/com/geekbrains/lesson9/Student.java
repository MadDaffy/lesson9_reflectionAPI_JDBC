package com.geekbrains.lesson9;

@TableAnnotation(title = "student")
public class Student {

    @ColumnAnnotation
    int id;

    @ColumnAnnotation
    String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }


}
