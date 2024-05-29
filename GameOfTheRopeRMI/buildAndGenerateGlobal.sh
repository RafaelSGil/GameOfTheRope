echo "Compiling source code."
javac -source 8 -target 8 -cp /home/$USER/Documents/GameOfTheRope/GameOfTheRopeDistributedVersion/genclass.jar */*.java */*/*.java */*/*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "RMI registry"
rm -rf dirRMIRegistry/interfaces
mkdir -p dirRMIRegistry/interfaces
cp interfaces/*.class dirRMIRegistry/interfaces
echo "Register Remote Objects"
rm -rf dirRegistry/serverSide dirRegistry/interfaces
mkdir -p dirRegistry/serverSide dirRegistry/serverSide/main dirRegistry/serverSide/objects dirRegistry/interfaces
cp serverSide/main/ServerRegisterRemoteObject.class dirRegistry/serverSide/main
cp serverSide/objects/RegisterRemoteObject.class dirRegistry/serverSide/objects
cp interfaces/Register.class dirRegistry/interfaces
echo "General Repository"
rm -rf dirGeneralRepos/serverSide dirGeneralRepos/clientSide dirGeneralRepos/interfaces dirGeneralRepos/commInfra
mkdir -p dirGeneralRepos/serverSide dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/objects dirGeneralRepos/interfaces dirGeneralRepos/clientSide dirGeneralRepos/clientSide/entities dirGeneralRepos/clientSide/entities/data
cp serverSide/main/ServerGameOfTheRopeGeneralRepository.class dirGeneralRepos/serverSide/main
cp serverSide/objects/*.class dirGeneralRepos/serverSide/objects
cp interfaces/Register.class interfaces/IGeneralRepository.class dirGeneralRepos/interfaces
cp clientSide/entities/*.class dirGeneralRepos/clientSide/entities
cp clientSide/entities/data/*.class dirGeneralRepos/clientSide/entities/data
echo "Contestants Bench"
rm -rf dirContestantsBench/serverSide dirGeneralRepos/clientSide dirContestantsBench/interfaces dirContestantsBench/commInfra
mkdir -p dirContestantsBench/serverSide dirContestantsBench/serverSide/main dirContestantsBench/serverSide/objects dirContestantsBench/interfaces dirContestantsBench/serverSide/utils dirContestantsBench/clientSide dirContestantsBench/clientSide/entities dirContestantsBench/commInfra
cp serverSide/main/ServerGameOfTheRopeContestantsBench.class dirContestantsBench/serverSide/main
cp serverSide/objects/*.class dirContestantsBench/serverSide/objects
cp interfaces/*.class dirContestantsBench/interfaces
cp serverSide/utils/*.class dirContestantsBench/serverSide/utils
cp clientSide/entities/ContestantStates.class clientSide/entities/CoachStates.class clientSide/entities/Coach.class clientSide/entities/Contestant.class clientSide/entities/Referee.class dirContestantsBench/clientSide/entities
cp commInfra/*.class dirContestantsBench/commInfra
echo "Playground"
rm -rf dirPlayground/serverSide dirPlayground/clientSide dirPlayground/interfaces dirPlayground/commInfra
mkdir -p dirPlayground/serverSide dirPlayground/serverSide/main dirPlayground/serverSide/objects dirPlayground/interfaces dirPlayground/clientSide dirPlayground/clientSide/entities dirPlayground/commInfra
cp serverSide/main/ServerGameOfTheRopePlayground.class dirPlayground/serverSide/main
cp serverSide/objects/*.class dirPlayground/serverSide/objects
cp interfaces/*.class dirPlayground/interfaces
cp clientSide/entities/ContestantStates.class clientSide/entities/RefereeStates.class clientSide/entities/CoachStates.class clientSide/entities/Coach.class clientSide/entities/Referee.class clientSide/entities/Contestant.class dirPlayground/clientSide/entities
cp commInfra/*.class dirPlayground/commInfra
echo "Referee Site"
rm -rf dirRefereeSite/serverSide dirRefereeSite/clientSide dirRefereeSite/interfaces dirRefereeSite/commInfra
mkdir -p dirRefereeSite/serverSide dirRefereeSite/serverSide/main dirRefereeSite/serverSide/objects dirRefereeSite/interfaces dirRefereeSite/clientSide dirRefereeSite/clientSide/entities dirRefereeSite/commInfra
cp serverSide/main/ServerGameOfTheRopeRefereeSite.class dirRefereeSite/serverSide/main
cp serverSide/objects/*.class dirRefereeSite/serverSide/objects
cp interfaces/*.class dirRefereeSite/interfaces
cp clientSide/entities/RefereeStates.class clientSide/entities/Referee.class dirRefereeSite/clientSide/entities
cp commInfra/*.class dirRefereeSite/commInfra
echo "Coach"
rm -rf dirCoach/interfaces dirCoach/clientSide dirCoach/commInfra
mkdir -p dirCoach/interfaces dirCoach/clientSide dirCoach/clientSide/main dirCoach/clientSide/entities dirCoach/commInfra
cp clientSide/main/ClientGameOfTheRopeCoach.class dirCoach/clientSide/main
cp clientSide/entities/Coach.class clientSide/entities/CoachStates.class dirCoach/clientSide/entities
cp interfaces/*.class dirContestant/interfaces
cp commInfra/*.class dirCoach/commInfra
echo "Contestant"
rm -rf dirContestant/interfaces dirContestant/clientSide dirContestant/commInfra
mkdir -p dirContestant/interfaces dirContestant/clientSide dirContestant/clientSide/main dirContestant/clientSide/entities dirContestant/commInfra
cp clientSide/main/ClientGameOfTheRopeContestant.class dirContestant/clientSide/main
cp clientSide/entities/Contestant.class clientSide/entities/ContestantStates.class dirContestant/clientSide/entities
cp interfaces/*.class dirContestant/interfaces
cp commInfra/*.class dirContestant/commInfra
echo "Referee"
rm -rf dirReferee/interfaces dirReferee/clientSide dirReferee/commInfra
mkdir -p dirReferee/interfaces dirReferee/clientSide dirReferee/clientSide/main dirReferee/clientSide/entities dirReferee/commInfra
cp clientSide/main/ClientGameOfTheRopeReferee.class dirReferee/clientSide/main
cp clientSide/entities/Referee.class clientSide/entities/RefereeStates.class dirReferee/clientSide/entities
cp interfaces/*.class dirReferee/interfaces
cp commInfra/*.class dirReferee/commInfra
echo "Compressing execution environments."
echo "General Repository"
rm -f dirGeneralRepos.zip
zip -rq dirGeneralRepos.zip dirGeneralRepos
echo "Contestants Bench"
rm -f dirContestantsBench.zip
zip -rq dirContestantsBench.zip dirContestantsBench
echo "Playground"
rm -f dirPlayground.zip
zip -rq dirPlayground.zip dirPlayground
echo "Referee Site"
rm -f dirRefereeSite.zip
zip -rq dirRefereeSite.zip dirRefereeSite
echo "Coach"
rm -f dirCoach.zip
zip -rq dirCoach.zip dirCoach
echo "Contestant"
rm -f dirContestant.zip
zip -rq dirContestant.zip dirContestant
echo "Referee"
rm -f dirReferee.zip
zip -rq dirReferee.zip dirReferee