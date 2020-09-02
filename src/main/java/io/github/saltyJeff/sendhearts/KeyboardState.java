package io.github.saltyJeff.sendhearts;

import java.util.*;

public class KeyboardState implements Comparable<KeyboardState> {
    int hearts; // the number of hearts on screen
    int bufferHearts; // the number of hearts in the paste buffer
    KeyboardState originator; // the parent that this state descends from
    ACTION action; // the action that created this state
    int cost; // the total cost of all states and actions up to and including this state
    // this function just assigns costs to actions
    static int costOfAction(ACTION action) {
        switch(action) {
            case TYPE_HEART:
                return 15;
            case CTRL_C:
                return 250;
            case CTRL_V:
                return 125;
        }
        return 0;
    }
    // zero-arg constructor that makes a bunch of zeros
    public KeyboardState() {
        cost = hearts = bufferHearts = 0;
        originator = null;
        action = ACTION.IDLE;
    }
    // constructor that takes in an originator, and an action to apply to create this new state
    public KeyboardState(KeyboardState originator, ACTION action) {
        this.originator = originator;
        this.cost = this.originator.cost + costOfAction(action);
        this.action = action;
        switch(action) {
            case TYPE_HEART:
                hearts = originator.hearts + 1;
                break;
            case CTRL_C:
                hearts = originator.hearts;
                bufferHearts = hearts;
                break;
            case CTRL_V:
                hearts = originator.hearts + originator.bufferHearts;
                bufferHearts = originator.bufferHearts;
                break;
        }
    }
    void addSuccessorStates(KbdStateDataStructure ds) {
        // add successors of this state to the data structure

        // we need to avoid stupid decisions so we try to filter out some sub-optimal actions
        // the data structure will take care of filtering out sub-optimal states
        ds.add(new KeyboardState(this, ACTION.TYPE_HEART));
        if(hearts > bufferHearts) { // we should only do a CTRL+C if the hearts on screen is greater than the hearts in the buffer
            ds.add(new KeyboardState(this, ACTION.CTRL_C));
        }
        if(bufferHearts > 0) {  // we should only do a CTRL+V if the paste buffer is non-empty
            ds.add(new KeyboardState(this, ACTION.CTRL_V));
        }
    }
    @Override
    public int hashCode() {
        return cost * 51 + hearts * 31 + bufferHearts;
    }
    @Override
    public String toString() {
        return String.format("%s (cost: %d) TOTAL: %d\t contained: %d, buffered: %d", action.toString(), costOfAction(action), cost, hearts, bufferHearts);
    }
    @Override
    public boolean equals(Object o) {
        KeyboardState obj = (KeyboardState) o;
        return this.hearts == obj.hearts && this.bufferHearts == obj.bufferHearts;
    }
    @Override
    // in our priority queue, we need to sort by cost
    public int compareTo(KeyboardState o) {
        return this.cost - o.cost;
    }
    enum ACTION {
        IDLE,
        TYPE_HEART,
        CTRL_C,
        CTRL_V
    }
}
