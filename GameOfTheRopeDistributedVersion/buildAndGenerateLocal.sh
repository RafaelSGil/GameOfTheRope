echo "Compiling source code."
javac -source 8 -target 8 -cp /home/$USER/Documents/GameOfTheRope/GameOfTheRopeDistributedVersion/genclass.jar */*.java */*/*.java */*/*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "General Repository"
rm -rf dirGeneralRepos
mkdir -p dirGeneralRepos dirGeneralRepos/serverSide dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/entities dirGeneralRepos/serverSide/sharedRegions dirGeneralRepos/clientSide dirGeneralRepos/clientSide/entities dirGeneralRepos/clientSide/entities/data dirGeneralRepos/commInfra
cp serverSide/main/ServerGameOfTheRopeGeneralRepository.class dirGeneralRepos/serverSide/main
cp serverSide/entities/*.class dirGeneralRepos/serverSide/entities
cp serverSide/sharedRegions/*.class dirGeneralRepos/serverSide/sharedRegions
cp clientSide/entities/*.class dirGeneralRepos/clientSide/entities
cp clientSide/entities/data/*.class dirGeneralRepos/clientSide/entities/data
cp commInfra/*.class dirGeneralRepos/commInfra
echo "Contestants Bench"
rm -rf dirContestantsBench
mkdir -p dirContestantsBench dirContestantsBench/serverSide dirContestantsBench/serverSide/main dirContestantsBench/serverSide/entities dirContestantsBench/serverSide/sharedRegions dirContestantsBench/serverSide/utils dirContestantsBench/clientSide dirContestantsBench/clientSide/entities dirContestantsBench/clientSide/stubs dirContestantsBench/commInfra
cp serverSide/main/ServerGameOfTheRopeContestantsBench.class dirContestantsBench/serverSide/main
cp serverSide/entities/*.class dirContestantsBench/serverSide/entities
cp serverSide/sharedRegions/*.class dirContestantsBench/serverSide/sharedRegions
cp serverSide/utils/*.class dirContestantsBench/serverSide/utils
cp clientSide/stubs/*.class dirContestantsBench/clientSide/stubs
cp clientSide/entities/ContestantStates.class clientSide/entities/CoachStates.class clientSide/entities/CoachCloning.class clientSide/entities/ContestantCloning.class clientSide/entities/RefereeCloning.class dirContestantsBench/clientSide/entities
cp commInfra/*.class dirContestantsBench/commInfra
echo "Playground"
rm -rf dirPlayground
mkdir -p dirPlayground dirPlayground/serverSide dirPlayground/serverSide/main dirPlayground/serverSide/entities dirPlayground/serverSide/sharedRegions dirPlayground/clientSide dirPlayground/clientSide/entities dirPlayground/clientSide/stubs dirPlayground/commInfra
cp serverSide/main/ServerGameOfTheRopePlayground.class dirPlayground/serverSide/main
cp serverSide/entities/*.class dirPlayground/serverSide/entities
cp serverSide/sharedRegions/*.class dirPlayground/serverSide/sharedRegions
cp clientSide/stubs/*.class dirPlayground/clientSide/stubs
cp clientSide/entities/ContestantStates.class clientSide/entities/RefereeStates.class clientSide/entities/CoachStates.class clientSide/entities/CoachCloning.class clientSide/entities/RefereeCloning.class clientSide/entities/ContestantCloning.class dirPlayground/clientSide/entities
cp commInfra/*.class dirPlayground/commInfra
echo "Referee Site"
rm -rf dirRefereeSite
mkdir -p dirRefereeSite dirRefereeSite/serverSide dirRefereeSite/serverSide/main dirRefereeSite/serverSide/entities dirRefereeSite/serverSide/sharedRegions dirRefereeSite/clientSide dirRefereeSite/clientSide/entities dirRefereeSite/clientSide/stubs dirRefereeSite/commInfra
cp serverSide/main/ServerGameOfTheRopeRefereeSite.class dirRefereeSite/serverSide/main
cp serverSide/entities/*.class dirRefereeSite/serverSide/entities
cp serverSide/sharedRegions/*.class dirRefereeSite/serverSide/sharedRegions
cp clientSide/stubs/*.class dirRefereeSite/clientSide/stubs
cp clientSide/entities/RefereeStates.class clientSide/entities/RefereeCloning.class dirRefereeSite/clientSide/entities
cp commInfra/*.class dirRefereeSite/commInfra
echo "Coach"
rm -rf dirCoach
mkdir -p dirCoach dirCoach/serverSide dirCoach/serverSide/main dirCoach/clientSide dirCoach/clientSide/main dirCoach/clientSide/entities dirCoach/clientSide/stubs dirCoach/commInfra
cp clientSide/main/ClientGameOfTheRopeCoach.class dirCoach/clientSide/main
cp clientSide/entities/Coach.class clientSide/entities/CoachStates.class dirCoach/clientSide/entities
cp clientSide/stubs/*.class dirCoach/clientSide/stubs
cp commInfra/*.class dirCoach/commInfra
echo "Contestant"
rm -rf dirContestant
mkdir -p dirContestant dirContestant/serverSide dirContestant/serverSide/main dirContestant/clientSide dirContestant/clientSide/main dirContestant/clientSide/entities dirContestant/clientSide/stubs dirContestant/commInfra
cp clientSide/main/ClientGameOfTheRopeContestant.class dirContestant/clientSide/main
cp clientSide/entities/Contestant.class clientSide/entities/ContestantStates.class dirContestant/clientSide/entities
cp clientSide/stubs/*.class dirContestant/clientSide/stubs
cp commInfra/*.class dirContestant/commInfra
echo "Referee"
rm -rf dirReferee
mkdir -p dirReferee dirReferee/serverSide dirReferee/serverSide/main dirReferee/clientSide dirReferee/clientSide/main dirReferee/clientSide/entities dirReferee/clientSide/stubs dirReferee/commInfra
cp clientSide/main/ClientGameOfTheRopeReferee.class dirReferee/clientSide/main
cp clientSide/entities/Referee.class clientSide/entities/RefereeStates.class dirReferee/clientSide/entities
cp clientSide/stubs/*.class dirReferee/clientSide/stubs
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
echo "Deploying and decompressing execution environments."
mkdir -p /home/$USER/Documents/testGameOfTheRopeDistributedVersion
rm -rf /home/$USER/Documents/testGameOfTheRopeDistributedVersion/*
cp dirGeneralRepos.zip dirContestantsBench.zip dirPlayground.zip dirRefereeSite.zip dirCoach.zip dirContestant.zip dirReferee.zip /home/$USER/Documents/testGameOfTheRopeDistributedVersion
cd /home/$USER/Documents/testGameOfTheRopeDistributedVersion
unzip -q dirGeneralRepos.zip
unzip -q dirContestantsBench.zip
unzip -q dirPlayground.zip
unzip -q dirRefereeSite.zip
unzip -q dirCoach.zip
unzip -q dirContestant.zip
unzip -q dirReferee.zip
echo "Execution environments deployed."

