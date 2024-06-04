echo "Transfering data to the Contestants node."
sshpass -f password ssh sd103@l040101-ws10.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd103@l040101-ws10.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirContestant.zip sd103@l040101-ws10.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the Contestants node."
sshpass -f password ssh sd103@l040101-ws10.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirContestant.zip'
echo "Executing program at the Contestants node."
sshpass -f password ssh sd103@l040101-ws10.ua.pt 'cd test/GameOfTheRope/dirContestant ; sh contestant_com_d.sh'