package com.example.tictactoe.qtable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HashToState implements Function<String, List<Integer>> {

    @Override
    public List<Integer> apply(String s) {
        List<Integer> state = new ArrayList<>();

        for (int i = 0; i < s.length();) {
            int next = Character.getNumericValue(s.charAt(i));
            if (next == 1 || next == 0){
                i++;
            } else {
                i = i + 2;
            }
            state.add(next);
        }

        return state;
    }
}
