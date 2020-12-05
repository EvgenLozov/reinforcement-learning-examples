package com.example.tictactoe.qtable;

import com.example.Pair;
import com.google.common.collect.Lists;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QTableToNN {

    public static final int BATCH_SIZE = 32;
    public static final int EPOCHS = 50;

    public static void main(String[] args) throws IOException {
        String modelFile = "modelOne.bin";
        String qTableFile = "playerOne.ser";

        Map<String, Double> qTable = new QTableFileStorage().load(qTableFile);

        MultiLayerNetwork model = new MultiLayerNetwork(getNetworkConfig());
        model.init();

//        UIServer uiServer = UIServer.getInstance();
//        StatsStorage ganStatsStorage = new InMemoryStatsStorage();
//        uiServer.attach(ganStatsStorage);
//        model.setListeners(new StatsListener( ganStatsStorage, 20));

        IntStream.range(0, EPOCHS)
                .mapToObj(i -> new ArrayList<>(qTable.keySet()))
                .peek(Collections::shuffle)
                .flatMap(shuffled -> Lists.partition(shuffled, BATCH_SIZE).stream())
                .map(batchHashes ->
                        batchHashes.stream()
                                .map(s -> new Pair<>(new HashToState().apply(s), qTable.get(s)))
                                .collect(Collectors.toList()))
                .map(batchPairs ->
                        batchPairs.stream()
                            .map(p -> {
                                INDArray feature = new StateToFeature().apply(p.getFirst());
                                INDArray label = Nd4j.zeros(1,1).addi(p.getSecond());

                                return new Pair<>(feature, label);
                            })
                            .map(fl -> new DataSet(fl.getFirst(), fl.getSecond()))
                            .collect(Collectors.toList()))
                .map(DataSet::merge)
                .forEach(model::fit);


        qTable.entrySet().stream()
                .limit(10)
                .forEach(e -> {
                    List<Integer> state = new HashToState().apply(e.getKey());
                    INDArray feature = new StateToFeature().apply(state);
                    INDArray output = model.output(feature);
                    System.out.println("State: " + e.getKey());
                    System.out.println("QValue from qtable: " + e.getValue());
                    System.out.println("QValue from model: " + output.getDouble(0));
                    System.out.println("==============================================");

                });

//        ModelSerializer.writeModel(model, modelFile, true);

//        uiServer.stop();
    }

    private static MultiLayerConfiguration getNetworkConfig() {
        return new NeuralNetConfiguration.Builder()
                .activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .updater(new Sgd(0.01))
                .l2(0.0001)
                .list()
                .layer(new DenseLayer.Builder().nIn(18).nOut(9).build())
                .layer(new DenseLayer.Builder().nIn(9).nOut(3).build())
                .layer(new OutputLayer.Builder(
                        LossFunctions.LossFunction.SQUARED_LOSS).activation(Activation.IDENTITY)
                        .nIn(3).nOut(1).build())
                .backpropType(BackpropType.Standard)
                .build();
    }
}
