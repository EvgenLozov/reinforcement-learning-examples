package com.example.gridworld;

public class GridWorldGameMain {
    public static void main(String[] args) {
        Agent agent = new Agent();
        agent.showStateValues();
        agent.play(100);
        agent.showStateValues();
    }
}
