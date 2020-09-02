package io.github.saltyJeff.sendhearts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class KbdStateDataStructure {
    // a 2-layer mapping of hearts => bufferedHearts => cost
    HashMap<Integer, HashMap<Integer, Integer>> visited = new HashMap<>();
    // UCS requires that you use a priority queue that sorts by lowest cost
    PriorityQueue<KeyboardState> frontier = new PriorityQueue<>();
    void add(KeyboardState state) {
        // we should only consider a keyboard state if for a given "heart" and "bufferHeart",
        // its cost is lower than the current greatest cost
        HashMap<Integer, Integer> bufferToCost = visited.get(state.hearts);
        if(bufferToCost == null) {
            bufferToCost = new HashMap<>();
            bufferToCost.put(state.bufferHearts, state.cost);
            visited.put(state.hearts, bufferToCost);
            frontier.add(state);
        }
        int minCost = bufferToCost.getOrDefault(state.bufferHearts, 0);
        if(minCost == 0 || state.cost < minCost) {
            bufferToCost.put(state.bufferHearts, state.cost);
            frontier.add(state);
        }
    }
    KeyboardState pop() {
        return frontier.poll();
    }
}
