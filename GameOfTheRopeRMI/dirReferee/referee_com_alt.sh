CODEBASE="file:///home/"$1"/test/GameOfTheRope/dirReferee/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientGameOfTheRopeReferee localhost 22121 log