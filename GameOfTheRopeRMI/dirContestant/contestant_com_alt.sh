CODEBASE="file:///home/"$1"/test/GameOfTheRope/dirContestant/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientGameOfTheRopeContestant localhost 22128