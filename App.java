package Solitaire1d;

import java.util.*;

public class App {

    private static ArrayList<TreeSet<StringBuilder>> wins;

    public static void main(String[] args) {
        int target = 0;
        try {
            target = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.err.println("Usage java Solitaire1d.App <target>");
        }
        wins = new ArrayList<TreeSet<StringBuilder>>();
        calcWins(target);
        System.out.println("The number of solvable states with " + target + " pegs is : "
                + wins.get(target - 1).size());
        for (StringBuilder t : wins.get(target - 1)) {
            System.out.println(t);
        }
    }

    public static void calcWins(int target) {
        TreeSet<StringBuilder> pegwins = new TreeSet<StringBuilder>();
        pegwins.add(new StringBuilder("X")); // X is peg O is blank
        wins.add(pegwins); // add a single peg for the possible wins at 1 peg.
        for (int i = 1; i < target; i++) {
            TreeSet<StringBuilder> thislayer = new TreeSet<StringBuilder>();
            for (StringBuilder b : wins.get(i - 1)) {
                for (int j = 0; j < b.length(); j++) {
                    thislayer.add(unhopLeft(b, j));
                    thislayer.add(unhopRight(b, j));
                }
            }
            thislayer = removeDupes(thislayer, i + 1);
            wins.add(i, thislayer);
        }
    }

    public static StringBuilder unhopLeft(StringBuilder curr, int index) {
        StringBuilder result = new StringBuilder(curr);
        if (index == 0) {
            result = new StringBuilder("XXO").append(result.substring(1));
        } else {
            // this is actually fine and can never generate an out of bounds exception.
            if (result.charAt(index) == 'X' && result.charAt(index - 1) == 'O' && result.charAt(index - 2) == 'O') {
                result = new StringBuilder(result.substring(0, index - 2));
                result.append("XXO");
                result.append(curr.substring(index + 1, curr.length()));
                // System.out.println("Left internal : " + curr + " gives " + result + " using
                // index " + index);
            } else {
                result = new StringBuilder(curr);
            }
        }
        result = cleanUpBlanks(result);

        // System.out.println("Unhop left input : " + curr + " UnHop left result : " +
        // result);
        return result;
    }

    public static StringBuilder unhopRight(StringBuilder curr, int index) {
        StringBuilder result = new StringBuilder(curr);
        if (index == result.length() - 1) {
            result = new StringBuilder(result.substring(0, result.length() - 1)).append("OXX");
        } else {
            if (result.charAt(index) == 'X' && result.charAt(index + 1) == 'O' && result.charAt(index + 2) == 'O') {
                result = new StringBuilder(result.substring(0, index));
                result.append("OXX");
                result.append(curr.substring(index + 3, curr.length()));
                // System.out.println("Right internal : " + curr + " gives " + result + " using
                // index " + index);
            } else {
                result = new StringBuilder(curr);
            }
        }
        result = cleanUpBlanks(result);
        // System.out.println("Unhop right input : " + curr + " UnHop right result : " +
        // result);
        return result;
    }

    public static StringBuilder cleanUpBlanks(StringBuilder input) {
        int start = 0;
        int end = input.length() - 1;
        StringBuilder result;
        if (input.length() == 0) {
            return new StringBuilder();
        }
        while (input.charAt(start) == 'O') {
            start++;
        }
        while (input.charAt(end) == 'O') {
            end--;
        }
        result = new StringBuilder(input.substring(start, end + 1));
        // System.out.println("Cleanup " + input + " using indices " + start + "," + end
        // + " result : " + result);
        return result;
    }

    public static int countpegs(StringBuilder input) {
        int result = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == 'X') {
                result++;
            }
        }
        // System.out.println(input + " has " + result + " pegs");
        return result;
    }

    public static TreeSet<StringBuilder> removeDupes(TreeSet<StringBuilder> input, int pegs) {
        TreeSet<StringBuilder> result = new TreeSet<StringBuilder>();
        for (StringBuilder b : input) {
            StringBuilder reverse = new StringBuilder(b);
            reverse.reverse();
            if (b.length() > 0 && !result.contains(reverse) && countpegs(b) == pegs) {
                result.add(b);
            }
        }
        return result;
    }
}
