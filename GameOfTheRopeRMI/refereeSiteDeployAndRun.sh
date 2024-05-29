echo "Transfering data to the Referee Site node."
sshpass -f password ssh sd103@l040101-ws08.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd103@l040101-ws08.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirRefereeSite.zip sd103@l040101-ws08.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the Referee Site node."
sshpass -f password ssh sd103@l040101-ws08.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirRefereeSite.zip'
echo "Executing program at the Referee Site node."
sshpass -f password ssh sd103@l040101-ws08.ua.pt 'cd test/GameOfTheRope/dirRefereeSite ; sh refereesite_com_d.sh'