echo "Transfering data to the Referee node."
sshpass -f password ssh sd103@l040101-ws09.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd103@l040101-ws09.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirReferee.zip sd103@l040101-ws09.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the Referee node."
sshpass -f password ssh sd103@l040101-ws09.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirReferee.zip'
echo "Executing program at the Referee node."
sshpass -f password ssh sd103@l040101-ws09.ua.pt 'cd test/GameOfTheRope/dirReferee ; sh referee_com_d.sh'