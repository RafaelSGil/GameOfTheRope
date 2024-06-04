CODEBASE="file:///home/"$1"/test/GameOfTheRope/dirRefereeSite/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerGameOfTheRopeRefereeSite 22125 localhost 22121