package com.example.tictactoe.qtable;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.File;
import java.io.IOException;

public class TicTacToePlayWithNNBot {
    public static void main(String[] args) throws IOException {

        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(new File("modelXretrain.bin"));

        NNPlayer bot = NNPlayer.XneuralNetPlayer(model);
        Player human = HumanPlayer.OhumanPlayer();

        TicTacToeGame game = new TicTacToeGame(bot, human);
        game.play();

    }
}
