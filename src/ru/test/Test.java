package ru.test;

import java.util.Calendar;

public class Test {
    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 180);
        System.out.println(c.getTime());
    }
}
