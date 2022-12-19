package com.geekbrains.lesson9;


//        1. Реализуйте возможность разметки класса с помощью набора ваших собственных аннотаций
//        (@Table(title), @Column). Напишите обработчик аннотаций, который позволит по размеченному
//        классу построить таблицу в базе данных.
//        2. * Второй обработчик аннотаций должен уметь добавлять объект размеченного класса в
//        полученную таблицу.
//        Замечание: Считаем что в проекте не связанных между собой сущностей, чтобы не
//        продумывать логику их взаимодействия.
public class MainApp {


    public static void main(String[] args) throws ClassNotFoundException {
        TableHandler.buildTable(Student.class);
        TableHandler.addObjectInTable(new Student(1, "Vano"));
    }
}