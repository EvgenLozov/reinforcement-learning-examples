package com.example.tictactoe.qtable;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {

    List<Integer> positions;

    public Board() {
        this.positions = new ArrayList<>(Collections.nCopies(9, 0));;
    }

    private Board(List<Integer> positions){
        this.positions = positions;
    }

    Board copy(){
        return new Board(new ArrayList<>(positions));
    }

    List<Integer> availablePositions(){
        return IntStream.range(0, positions.size())
                .filter(i -> positions.get(i).equals(0))
                .boxed()
                .collect(Collectors.toList());
    }

    public void occupyPosition(int symbol, int position) {
        positions.set(position, symbol);
    }

    public String getHash() {
        return positions.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    public Optional<Integer> getWinner() {
        //check rows
        for (List<Integer> integers : ListUtils.partition(positions, 3)) {
            int sum = integers.stream().mapToInt(i -> i).sum();
            if (sum == 3){
                return Optional.of(1);
            }
            if (sum == -3){
                return Optional.of(-1);
            }
        }

        //check columns
        List<List<Integer>> columns = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Integer> column = new ArrayList<>();
            for (int j = i; j < 9; j = j+3) {
                column.add(positions.get(j));
            }
            columns.add(column);
        }

        for (List<Integer> column : columns) {
            int sum = column.stream().mapToInt(i -> i).sum();
            if (sum == 3){
                return Optional.of(1);
            }
            if (sum == -3){
                return Optional.of(-1);
            }
        }

        //check diagonals
        int diag1Sum = positions.get(0) + positions.get(4) + positions.get(8);
        int diag2Sum = positions.get(2) + positions.get(4) + positions.get(6);

        if (Math.max(Math.abs(diag1Sum), Math.abs(diag2Sum)) == 3){
            if (diag1Sum == 3 || diag2Sum == 3){
                return Optional.of(1);
            } else {
                return Optional.of(-1);
            }
        }

        if (availablePositions().isEmpty()){
            return Optional.of(0);
        }

        return Optional.empty();
    }

    public void print(){
        for (List<Integer> integers : ListUtils.partition(positions, 3)) {
            StringBuilder sb = new StringBuilder("| ")
                    .append(integers.get(0)).append(" | ")
                    .append(integers.get(1)).append(" | ")
                    .append(integers.get(2)).append(" | ");

            System.out.println(sb.toString().replace("-1", "Y").replace("1", "X"));
            System.out.println("---------------");
        }
    }
}
