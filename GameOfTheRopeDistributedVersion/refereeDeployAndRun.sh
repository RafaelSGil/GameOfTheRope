echo "Transfering data to the Referee node."
sshpass -f password ssh sd103@l040101-ws09.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd103@l040101-ws09.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirReferee.zip sd103@l040101-ws09.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the Referee node."
sshpass -f password ssh sd103@l040101-ws09.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirReferee.zip'
echo "Executing program at the Referee node."
sshpass -f password ssh sd103@l040101-ws09.ua.pt 'cd test/GameOfTheRope/dirReferee ; java clientSide.main.ClientGameOfTheRopeReferee l040101-ws01.ua.pt 22121 l040101-ws10.ua.pt 22122 l040101-ws06.ua.pt 22126 l040101-ws05.ua.pt 22125 log'