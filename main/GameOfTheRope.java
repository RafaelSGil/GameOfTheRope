package main;

import entities.Coach;
import entities.Contestant;
import entities.Referee;
import genclass.FileOp;
import genclass.GenericIO;
import sharedregions.ContestantsBench;
import sharedregions.GeneralRepository;
import sharedregions.Playground;
import sharedregions.RefereeSite;
import utils.Strategy;

/**
 * This class represents the main program for the Game of the Rope simulation.
 * It's responsible for the creation and initialization of the entities threads and the shared regions,fot th
 * for the simulation execution and waiting for all threads to finish execution.
 *
 *  @author [Miguel Cabral]
 *  @author [Rafael Gil]
 */
public class GameOfTheRope {
    public static void main(String[] args) {
        Contestant[] contestants = new Contestant[SimulationParams.NCONTESTANTS];       // array of contestants threads
        Coach[] coaches = new Coach[SimulationParams.NTEAMS];                           // array of coach threads
        String fileName;                                                                // logging file name
        char opt;                                                                       // selected option
        boolean success;                                                                // end of operation flag

        // problem initialization

        System.out.println("\tGame of the Rope");
        do
        { GenericIO.writeString ("Logging file name? ");
            fileName = GenericIO.readlnString ();
            if (FileOp.exists (".", fileName))
            { do
            { GenericIO.writeString ("There is already a file with this name. Delete it (y - yes; n - no)? ");
                opt = GenericIO.readlnChar ();
            } while ((opt != 'y') && (opt != 'n'));
                success = opt == 'y';
            }
            else success = true;
        } while (!success);

        GeneralRepository repository = new GeneralRepository(fileName);
        RefereeSite refereeSite = new RefereeSite(repository);                                    // reference to the referee site
        Playground playground = new Playground(repository);                                       // reference to the playground
        ContestantsBench contestantsBench = new ContestantsBench(repository);          // reference to the contestants bench

        // referee, coach and contestants initialization
        Referee referee = new Referee("referee", refereeSite, playground, contestantsBench);   // referee thread
        for (int i = 0; i < SimulationParams.NTEAMS; ++i){
            coaches[i] = new Coach("Coa" + (i+1), (i % 2 == 0 ? 0 : 1), (i % 2 == 0 ? Strategy.STRENGTH : Strategy.MODERATE),contestantsBench, playground, refereeSite);
        }
        for (int i = 0; i < SimulationParams.NCONTESTANTS; ++i){
            contestants[i] = new Contestant("Cont_" + (i+1), i, (i % 2 == 0 ? 0 : 1), Contestant.GenerateRandomStrength(), contestantsBench, playground, refereeSite);
        }

        // start of the simulation
        for (int i = 0; i < SimulationParams.NCONTESTANTS; ++i){
            contestants[i].start();
        }
        for (int i = 0; i < SimulationParams.NTEAMS; ++i){
            coaches[i].start();
        }
        referee.start();

        // wait for the end of the simulation
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            try{
                contestants[i].join();
            }catch (InterruptedException e){}

            GenericIO.writelnString("The contestant " + (i) + " has terminated");
        }
        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            try{
                coaches[i].join();
            }catch (InterruptedException e){}

            GenericIO.writelnString("The coach " + (i+1) + " has terminated");
        }
        try{
            referee.join();
        }catch (InterruptedException e){}
        GenericIO.writelnString("The referee has terminated");
        GenericIO.writelnString("The program will terminate...");
    }
}
