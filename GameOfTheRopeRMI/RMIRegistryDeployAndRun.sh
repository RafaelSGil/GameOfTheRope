echo "Transfering data to the RMIregistry node."
sshpass -f password ssh sd103@l040101-ws01.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd103@l040101-ws01.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password ssh sd103@l040101-ws01.ua.pt 'mkdir -p Public/classes/interfaces'
sshpass -f password ssh sd103@l040101-ws01.ua.pt 'rm -rf Public/classes/interfaces/*'
sshpass -f password scp dirRMIRegistry.zip sd103@l040101-ws01.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the RMIregistry node."
sshpass -f password ssh sd103@l040101-ws01.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirRMIRegistry.zip'
sshpass -f password ssh sd103@l040101-ws01.ua.pt 'cd test/GameOfTheRope/dirRMIRegistry ; cp interfaces/*.class /home/sd103/Public/classes/interfaces ; cp set_rmiregistry_d.sh /home/sd103'
echo "Executing program at the RMIregistry node."
sshpass -f password ssh sd103@l040101-ws01.ua.pt 'sh set_rmiregistry_d.sh sd103 22121'
