echo "Transfering data to the registry node."
sshpass -f password ssh sd103@l040101-ws01.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password scp dirRegistry.zip sd103@l040101-ws01.ua.pt:test/GameOfTheRope
echo "Decompressing data sent to the registry node."
sshpass -f password ssh sd103@l040101-ws01.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirRegistry.zip'
echo "Executing program at the registry node."RegistryDeployAndRun.sh
sshpass -f password ssh sd103@l040101-ws01.ua.pt 'cd test/GameOfTheRope/dirRegistry ; sh registry_com_d.sh sd103'
