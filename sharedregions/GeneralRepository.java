package sharedregions;

import entities.Coach;
import entities.Contestant;
import entities.Referee;

public class GeneralRepository {
    private Referee referee;
    private Coach[] coaches;
    private Contestant[] contestants;
    private int Game;
    private int Trial;
    private int ropePosition;

    private String fileName;

    public GeneralRepository(String fileName){
        this.fileName = fileName;
    }

}
