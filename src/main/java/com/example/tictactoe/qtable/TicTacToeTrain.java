package com.example.tictactoe.qtable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class TicTacToeTrain {

    public static void main(String[] args) throws IOException {

        double explorationRate = 0.3;
        double learningRate = 0.1;
        double decayGamma = 0.9;

        BotPlayer playerOne = new BotPlayer("p1", 1, explorationRate, learningRate, decayGamma);
        BotPlayer playerTwo = new BotPlayer("p2", -1, explorationRate, learningRate, decayGamma);

        TicTacToeGame game = new TicTacToeGame(playerOne, playerTwo);

        //training
        for (int i = 0; i < 30000; i++) {
            System.out.println("Round : " + i);
            game.play();
        }

        saveToFile(playerOne.getqValues(), "playerOne.ser");
        saveToFile(playerTwo.getqValues(), "playerTwo.ser");

    }

    private static void saveToFile(Map<String, Double> qValues, String fileName) throws IOException {
        FileOutputStream fos =
                new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(qValues);
        oos.close();
        fos.close();

        System.out.println("Serialized HashMap data is saved in " + fileName);
    }
}
