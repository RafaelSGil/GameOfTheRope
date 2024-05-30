CODEBASE="file:///home/"$1"/test/GameOfTheRope/dirPlayground/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerGameOfTheRopePlayground 22122 localhost 22121