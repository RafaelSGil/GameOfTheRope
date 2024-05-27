CODEBASE="file:///home/"$1"/test/GameOfTheRope/dirGeneralRepos/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerGameOfTheRopeGeneralRepository 22122 localhost 22121