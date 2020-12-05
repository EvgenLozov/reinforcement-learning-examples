package com.example.tictactoe.qtable;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class QTableToNNTest extends TestCase {

    public void test() {
        String s = "11-1-1-100";

        List<Integer> integers = new ArrayList<>();

        for (int i = 0; i < s.length();) {
            int next = Character.getNumericValue(s.charAt(i));
            if (next == 1 || next == 0){
                i++;
            } else {
                i = i + 2;
            }
            integers.add(next);
        }

        System.out.println(integers);
    }
}