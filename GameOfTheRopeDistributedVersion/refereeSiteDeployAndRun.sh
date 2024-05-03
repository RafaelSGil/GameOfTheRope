echo "Transfering data to the Referee Site node."
sshpass -f password ssh sd103@l040101-ws06.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd103@l040101-ws06.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirContestantsBench.zip sd103@l040101-ws06.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the Referee Site node."
sshpass -f password ssh sd103@l040101-ws06.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirContestantsBench.zip'
echo "Executing program at the Referee Site node."
sshpass -f password ssh sd103@l040101-ws06.ua.pt 'cd test/GameOfTheRope/dirContestantsBench ; java serverSide.main.ServerGameOfTheRopeRefereeSite 22125 l040101-ws01.ua.pt 22121'