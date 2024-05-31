xterm  -T "RMI registry" -hold -e "sh RMIRegistryDeployAndRun.sh" &
sleep 4
xterm  -T "Registry" -hold -e "sh RegistryDeployAndRun.sh" &
sleep 10
xterm  -T "General Repository" -hold -e "sh generalRepositoryDeployAndRun.sh" &
sleep 6
xterm  -T "Playground" -hold -e "sh playgroundDeployAndRun.sh" &
sleep 4
xterm  -T "Referee Site" -hold -e "sh refereeSiteDeployAndRun.sh" &
sleep 4
xterm  -T "Contestants Bench" -hold -e "sh contestantsBenchDeployAndRun.sh" &
sleep 4
xterm  -T "Contestants" -hold -e "sh contestantsDeployAndRun.sh" &
sleep 4
xterm  -T "Coach" -hold -e "sh coachDeployAndRun.sh" &
sleep 4
xterm  -T "Referee" -hold -e "sh refereeDeployAndRun.sh" &
