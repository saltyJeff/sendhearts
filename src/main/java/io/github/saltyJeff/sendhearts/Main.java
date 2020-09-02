package io.github.saltyJeff.sendhearts;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        final int GOAL_HEARTS = 1_000_000;
        KeyboardState head = new KeyboardState();
        KbdStateDataStructure ds = new KbdStateDataStructure();

        while(head.hearts < GOAL_HEARTS) {
            head.addSuccessorStates(ds);
            head = ds.pop();
        }
        // print out all steps in backtrace
        Stack<KeyboardState> stack = new Stack<>();
        int maxCost = head.cost;
        while(head != null) {
            stack.add(head);
            head = head.originator;
        }
        while(!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
        System.out.println("COST NEEDED TO ENTER 1 MILLION HEARTS: "+maxCost);
    }
}
