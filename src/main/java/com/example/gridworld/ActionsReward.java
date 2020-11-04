package com.example.gridworld;

import java.util.Map;

class ActionsReward {

    Map<Action, Double> actionsRewards;

    ActionsReward(Map<Action, Double> actionsRewards) {
        this.actionsRewards = actionsRewards;
    }

    void put(Action action, Double reward){
        actionsRewards.put(action, reward);
    }

    double getReward(Action action){
        return actionsRewards.get(action);
    }

    Action actionWithMaxReward(){
        return actionsRewards.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{ ");
        for (Map.Entry<Action, Double> actionDoubleEntry : actionsRewards.entrySet()) {
            sb.append(actionDoubleEntry.getKey()).append(": ").append(actionDoubleEntry.getValue()).append("; ");
        }
        sb.append("}");
        return sb.toString();
    }
}
