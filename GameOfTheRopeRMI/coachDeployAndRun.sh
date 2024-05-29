echo "Transfering data to the Coach node."
sshpass -f password ssh sd103@l040101-ws10.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd103@l040101-ws10.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirCoach.zip sd103@l040101-ws10.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the Coach node."
sshpass -f password ssh sd103@l040101-ws10.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirCoach.zip'
echo "Executing program at the Coach node."
sshpass -f password ssh sd103@l040101-ws10.ua.pt 'cd test/GameOfTheRope/dirCoach ; sh coach_com_d.sh'