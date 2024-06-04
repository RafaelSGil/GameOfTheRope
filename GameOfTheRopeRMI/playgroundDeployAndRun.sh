echo "Transfering data to the Playground node."
sshpass -f password ssh sd103@l040101-ws07.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd103@l040101-ws07.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirPlayground.zip sd103@l040101-ws07.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the Playground node."
sshpass -f password ssh sd103@l040101-ws07.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirPlayground.zip'
echo "Executing program at the Playground node."
sshpass -f password ssh sd103@l040101-ws07.ua.pt 'cd test/GameOfTheRope/dirPlayground ; sh playground_com_d.sh'