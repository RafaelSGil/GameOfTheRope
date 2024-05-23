echo "Transfering data to the Contestants node."
sshpass -f password ssh sd103@l040101-ws08.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd103@l040101-ws08.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirContestant.zip sd103@l040101-ws08.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the Contestants node."
sshpass -f password ssh sd103@l040101-ws08.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirContestant.zip'
echo "Executing program at the Contestants node."
sshpass -f password ssh sd103@l040101-ws08.ua.pt 'cd test/GameOfTheRope/dirContestant ; java clientSide.main.ClientGameOfTheRopeContestant l040101-ws01.ua.pt 22121 l040101-ws10.ua.pt 22122 l040101-ws06.ua.pt 22126 l040101-ws05.ua.pt 22125'