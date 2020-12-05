package com.example.tictactoe.qtable;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;
import java.util.function.Function;

public class StateToFeature implements Function<List<Integer>, INDArray> {

    @Override
    public INDArray apply(List<Integer> state) {
        double[] featureDoubles = new double[18];
        for (int i = 0; i < 9; i++) {
            if (state.get(i) == 1) {
                featureDoubles[i] = 1;
            } else if (state.get(i) == -1) {
                featureDoubles[i + 9] = 1;
            }
        }

        return Nd4j.create(new double[][]{featureDoubles});
    }
}
