package serverSide.utils;

import serverSide.main.SimulationParams;

import java.util.*;

/**
 * The Strategy class provides methods to determine the strategy for selecting players in the game.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class Strategy {
    /**
     * Strategy based on strength
     */
    public static final int STRENGTH = 0;

    /**
     * Strategy based on having a moderation of strength on the team
     */
    public static final int MODERATE = 1;

    private Strategy() {
    }

    /**
     * Determines and applies the specified strategy for player selection.
     *
     * @param strategy    The strategy to be applied.
     * @param team        The team for which the strategy is applied.
     * @param contStrength An array representing all contestants' strength.
     * @param contTeam      An array representing all contestants team.
     * @param playing     A list to store the IDs of contestants selected for playing.
     * @param benched     A list to store the IDs of contestants selected for being benched.
     */
    public static void useStrategy(int strategy, int team, int[] contStrength, int[] contTeam, List<Integer> playing, List<Integer> benched) {
        switch (strategy) {
            case 0:
                useStrength(team, contStrength, contTeam, playing, benched);
                break;
            case 1:
                useModerate(team, contStrength, contTeam, playing, benched);
                break;
        }
    }

    /**
     * Applies the strategy of selecting players based on their strength.
     *
     * @param team        The team for which the strategy is applied.
     * @param contStrength An array representing all contestants strength.
     * @param contTeam      An array representing all contestants team.
     * @param playing     A list to store the IDs of contestants selected for playing.
     * @param benched     A list to store the IDs of contestants selected for being benched.
     */
    private static void useStrength(int team, int[] contStrength, int[] contTeam, List<Integer> playing, List<Integer> benched) {
        List<Set<Integer>> aux = new ArrayList<>();

        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            if(contTeam[i] == team){
                Set<Integer> s1 = new HashSet<>();
                s1.add(i);
                s1.add(contStrength[i]);
                aux.add(s1);
            }
        }

        Comparator<Set<Integer>> comparator = (s1, s2) -> {
            int secondElement1 = getSecondElement(s1);
            int secondElement2 = getSecondElement(s2);
            return Integer.compare(secondElement2, secondElement1);
        };

        aux.sort(comparator);

        for (int i = 0; i < SimulationParams.NPLAYERSINCOMPETITION; i++) {
            Set<Integer> set = aux.get(i);
            int elem = getFirstElement(set);
            playing.add(elem);
        }
        for (int i = SimulationParams.NPLAYERSINCOMPETITION; i < SimulationParams.NPLAYERS; i++) {
            Set<Integer> set = aux.get(i);
            int elem = getFirstElement(set);
            benched.add(elem);
        }
    }

    /**
     * Applies the strategy of selecting players moderately based on their strength.
     *
     * @param team        The team for which the strategy is applied.
     * @param contStrength An array representing all contestants strength.
     * @param contTeam      An array representing all contestants team.
     * @param playing     A list to store the IDs of contestants selected for playing.
     * @param benched     A list to store the IDs of contestants selected for being benched.
     */
    private static void useModerate(int team, int[] contStrength, int[] contTeam, List<Integer> playing, List<Integer> benched) {
        List<Set<Integer>> aux = new ArrayList<>();

        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            if(contTeam[i] == team){
                Set<Integer> s1 = new HashSet<>();
                s1.add(i);
                s1.add(contStrength[i]);
                aux.add(s1);
            }
        }

        Comparator<Set<Integer>> comparator = (s1, s2) -> {
            int secondElement1 = getSecondElement(s1);
            int secondElement2 = getSecondElement(s2);
            return Integer.compare(secondElement2, secondElement1);
        };

        aux.sort(comparator);

        for (int i = 0; i < SimulationParams.NPLAYERSINCOMPETITION - 1; i++) {
            Set<Integer> set = aux.get(i);
            int elem = getFirstElement(set);
            playing.add(elem);
        }
        // Get last contestant
        Set<Integer> lastSet = aux.get(aux.size() - 1);
        int elem1 = getFirstElement(lastSet);
        playing.add(elem1);

        for (int i = SimulationParams.NPLAYERSINCOMPETITION - 1; i < SimulationParams.NPLAYERS - 1; i++) {
            Set<Integer> set = aux.get(i);
            int elem = getFirstElement(set);
            benched.add(elem);
        }
    }

    private static int getFirstElement(Set<Integer> set) {
        Iterator<Integer> iterator = set.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new IllegalArgumentException("Set is empty");
    }

    private static int getSecondElement(Set<Integer> set) {
        if (set.size() < 2) {
            throw new IllegalArgumentException("Set must contain at least two elements");
        }
        Iterator<Integer> iterator = set.iterator();
        iterator.next();
        return iterator.next();
    }
}