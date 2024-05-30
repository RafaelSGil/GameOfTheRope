echo "Transfering data to the general repository node."
sshpass -f password ssh sd103@l040101-ws05.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd103@l040101-ws05.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirGeneralRepos.zip sd103@l040101-ws05.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd103@l040101-ws05.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirGeneralRepos.zip'
echo "Executing program at the server general repository."
sshpass -f password ssh sd103@l040101-ws05.ua.pt 'cd test/GameOfTheRope/dirGeneralRepos ; sh generalrepos_com_d.sh'
echo "Server shutdown."
sshpass -f password ssh sd103@l040101-ws05.ua.pt 'cd test/GameOfTheRope/dirGeneralRepos ; less log'
#sshpass -f password scp sd103@l040101-ws05.ua.pt:test/GameOfTheRope/dirGeneralRepos/log .
#intellij-idea-ultimate log
