CODEBASE="file:///home/"$1"/test/GameOfTheRope/dirContestantsBench/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerGameOfTheRopeContestantsBench 22123 localhost 22121