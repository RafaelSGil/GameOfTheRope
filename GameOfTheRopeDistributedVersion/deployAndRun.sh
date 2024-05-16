xterm  -T "General Repository" -hold -e "sh generalRepositoryDeployAndRun.sh" &
sleep 1
xterm  -T "Playground" -hold -e "sh playgroundDeployAndRun.sh" &
sleep 1
xterm  -T "Referee Site" -hold -e "sh refereeSiteDeployAndRun.sh" &
sleep 1
xterm  -T "Contestants Bench" -hold -e "sh contestantsBenchDeployAndRun.sh" &
sleep 3
xterm  -T "Contestants" -hold -e "sh contestantsDeployAndRun.sh" &
sleep 2
xterm  -T "Coach" -hold -e "sh coachDeployAndRun.sh" &
sleep 2
xterm  -T "Referee" -hold -e "sh refereeDeployAndRun.sh" &
