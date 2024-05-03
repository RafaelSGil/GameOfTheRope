cd /home/$USER/Documents/testGameOfTheRopeDistributedVersion/dirGeneralRepos
gnome-terminal --title="GeneralRepository" -- java -cp .:/home/$USER/2semestre/SD/GameOfTheRope/GameOfTheRopeDistributedVersion/genclass.jar serverSide.main.ServerGameOfTheRopeGeneralRepository 22161

cd /home/$USER/Documents/testGameOfTheRopeDistributedVersion/dirContestantsBench
gnome-terminal --title="ContestantsBench" -- java -cp .:/home/$USER/2semestre/SD/GameOfTheRope/GameOfTheRopeDistributedVersion/genclass.jar serverSide.main.ServerGameOfTheRopeContestantsBench 22162 127.0.0.1 22161

cd /home/$USER/Documents/testGameOfTheRopeDistributedVersion/dirPlayground
gnome-terminal --title="Playground" -- java -cp .:/home/$USER/2semestre/SD/GameOfTheRope/GameOfTheRopeDistributedVersion/genclass.jar serverSide.main.ServerGameOfTheRopePlayground 22163 127.0.0.1 22161

cd /home/$USER/Documents/testGameOfTheRopeDistributedVersion/dirRefereeSite
gnome-terminal --title="RefereeSite" -- java -cp .:/home/$USER/2semestre/SD/GameOfTheRope/GameOfTheRopeDistributedVersion/genclass.jar:clientSide/entities/*.class serverSide.main.ServerGameOfTheRopeRefereeSite 22164 127.0.0.1 22161
sleep 1
cd /home/$USER/Documents/testGameOfTheRopeDistributedVersion/dirContestant
gnome-terminal --title="Contestant" -- java -cp .:/home/$USER/2semestre/SD/GameOfTheRope/GameOfTheRopeDistributedVersion/genclass.jar clientSide.main.ClientGameOfTheRopeContestant 127.0.0.1 22161 127.0.0.1 22162 127.0.0.1 22164 127.0.0.1 22163
sleep 1
cd /home/$USER/Documents/testGameOfTheRopeDistributedVersion/dirCoach
gnome-terminal --title="Coach" -- java -cp .:/home/$USER/2semestre/SD/GameOfTheRope/GameOfTheRopeDistributedVersion/genclass.jar clientSide.main.ClientGameOfTheRopeCoach 127.0.0.1 22161 127.0.0.1 22162 127.0.0.1 22164 127.0.0.1 22163

cd /home/$USER/Documents/testGameOfTheRopeDistributedVersion/dirReferee
gnome-terminal --title="Referee" -- java -cp .:/home/$USER/2semestre/SD/GameOfTheRope/GameOfTheRopeDistributedVersion/genclass.jar clientSide.main.ClientGameOfTheRopeReferee 127.0.0.1 22161 127.0.0.1 22162 127.0.0.1 22164 127.0.0.1 22163 logger