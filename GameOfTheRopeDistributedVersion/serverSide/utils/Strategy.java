package serverSide.utils;

import serverSide.entities.ContestantBenchProxy;
import serverSide.main.SimulationParams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
     * @param contestants An array of Contestant objects representing all contestants.
     * @param playing     A list to store the IDs of contestants selected for playing.
     * @param benched     A list to store the IDs of contestants selected for being benched.
     */
    public static void useStrategy(int strategy, int team, ContestantBenchProxy[] contestants, List<Integer> playing, List<Integer> benched) {
        switch (strategy) {
            case 0:
                useStrength(team, contestants, playing, benched);
                break;
            case 1:
                useModerate(team, contestants, playing, benched);
                break;
        }
    }

    /**
     * Applies the strategy of selecting players based on their strength.
     *
     * @param team        The team for which the strategy is applied.
     * @param contestants An array of Contestant objects representing all contestants.
     * @param playing     A list to store the IDs of contestants selected for playing.
     * @param benched     A list to store the IDs of contestants selected for being benched.
     */
    private static void useStrength(int team, ContestantBenchProxy[] contestants, List<Integer> playing, List<Integer> benched) {
        ArrayList<ContestantBenchProxy> aux = new ArrayList<>();

        for (ContestantBenchProxy c : contestants) {
            if (c.getContestantTeam() == team) {
                aux.add(c);
            }
        }

        aux.sort(Comparator.comparingInt(ContestantBenchProxy::getContestantStrength).reversed());

        for (int i = 0; i < SimulationParams.NPLAYERSINCOMPETITION; i++) {
            playing.add(aux.get(i).getContestantId());
        }
        for (int i = SimulationParams.NPLAYERSINCOMPETITION; i < SimulationParams.NPLAYERS; i++) {
            benched.add(aux.get(i).getContestantId());
        }
    }

    /**
     * Applies the strategy of selecting players moderately based on their strength.
     *
     * @param team        The team for which the strategy is applied.
     * @param contestants An array of Contestant objects representing all contestants.
     * @param playing     A list to store the IDs of contestants selected for playing.
     * @param benched     A list to store the IDs of contestants selected for being benched.
     */
    private static void useModerate(int team, ContestantBenchProxy[] contestants, List<Integer> playing, List<Integer> benched) {
        ArrayList<ContestantBenchProxy> aux = new ArrayList<>();

        for (ContestantBenchProxy c : contestants) {
            if (c.getContestantTeam() == team) {
                aux.add(c);
            }
        }

        aux.sort(Comparator.comparingInt(ContestantBenchProxy::getContestantStrength).reversed());

        for (int i = 0; i < SimulationParams.NPLAYERSINCOMPETITION - 1; i++) {
            playing.add(aux.get(i).getContestantId());
        }
        // Get last contestant
        playing.add(aux.get(aux.size() - 1).getContestantId());
        for (int i = SimulationParams.NPLAYERSINCOMPETITION - 1; i < SimulationParams.NPLAYERS - 1; i++) {
            benched.add(aux.get(i).getContestantId());
        }
    }
}
