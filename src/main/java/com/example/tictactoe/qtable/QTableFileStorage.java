package com.example.tictactoe.qtable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class QTableFileStorage {

    Map<String, Double> load(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (HashMap) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    void save(Map<String, Double> qTable, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(qTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Serialized HashMap data is saved in " + fileName);
    }
}
