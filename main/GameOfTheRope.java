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
import utils.InputProtection;

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

        Playground playground = new Playground(repository);                                       // reference to the playground
        RefereeSite refereeSite = new RefereeSite(repository);                                    // reference to the referee site
        ContestantsBench contestantsBench = new ContestantsBench(contestants, repository);          // reference to the contestants bench

        // referee, coach and contestants initialization
        Referee referee = new Referee("referee", refereeSite, playground);   // referee thread
        for (int i = 0; i < SimulationParams.NTEAMS; ++i){
            coaches[i] = new Coach((i % 2 == 0 ? 1 : 2), contestantsBench, playground, refereeSite);
        }
        for (int i = 0; i < SimulationParams.NCONTESTANTS; ++i){
            contestants[i] = new Contestant((i+1), (i % 2 == 0 ? 1 : 2), Contestant.GenerateRandomStrength(), contestantsBench, playground, refereeSite);
        }

        // start of the simulation
        referee.start();
        for (int i = 0; i < SimulationParams.NTEAMS; ++i){
            coaches[i].start();
        }
        for (int i = 0; i < SimulationParams.NCONTESTANTS; ++i){
            contestants[i].start();
        }

        // wait for the end of the simulation
        GenericIO.writelnString();
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            try{
                contestants[i].join();
            }catch (InterruptedException e){}

            GenericIO.writelnString("The contestant " + (i+1) + " has terminated");
        }
        GenericIO.writelnString();
        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            try{
                coaches[i].join();
            }catch (InterruptedException e){}

            GenericIO.writelnString("The coach " + (i+1) + " has terminated");
        }
        GenericIO.writelnString();
        try{
            referee.join();
        }catch (InterruptedException e){}
        GenericIO.writelnString("The referee has terminated");
        GenericIO.writelnString("The program will terminate...");
    }
}
