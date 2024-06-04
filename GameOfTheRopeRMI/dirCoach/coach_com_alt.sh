CODEBASE="file:///home/"$1"/test/GameOfTheRope/dirCoach/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientGameOfTheRopeCoach localhost 22121