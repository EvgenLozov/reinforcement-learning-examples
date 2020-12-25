package com.example.tictactoe.qtable;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.File;
import java.io.IOException;

public class TicTacToeNetworkTrain {

    public static void main(String[] args) throws IOException {
        int rounds = 1000;

        MultiLayerNetwork modelX = ModelSerializer.restoreMultiLayerNetwork(new File("modelX.bin"));
        MultiLayerNetwork modelO = ModelSerializer.restoreMultiLayerNetwork(new File("modelO.bin"));

        NNPlayer playerX = NNPlayer.XneuralNetPlayer(modelX);
        NNPlayer playerO = NNPlayer.OneuralNetPlayer(modelO);

        TicTacToeGame game = new TicTacToeGame(playerX, playerO);

        for (int i = 0; i < rounds; i++) {
            System.out.println("Round : " + i);
            game.play();
        }

        ModelSerializer.writeModel(playerX.getNetwork(), "modelXretrain.bin", true);
        ModelSerializer.writeModel(playerO.getNetwork(), "modelOretrain.bin", true);

    }
}
