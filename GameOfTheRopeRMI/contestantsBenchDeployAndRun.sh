echo "Transfering data to the Contestants Bench node."
sshpass -f password ssh sd103@l040101-ws06.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd103@l040101-ws06.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirContestantsBench.zip sd103@l040101-ws06.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the Contestants Bench node."
sshpass -f password ssh sd103@l040101-ws06.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirContestantsBench.zip'
echo "Executing program at the Contestants Bench node."
sshpass -f password ssh sd103@l040101-ws06.ua.pt 'cd test/GameOfTheRope/dirContestantsBench ; sh contestantsbench_com_d.sh'