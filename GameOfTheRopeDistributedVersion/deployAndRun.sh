xterm  -T "General Repository" -hold -e "./generalRepositoryDeployAndRun.sh" &
sleep 1
xterm  -T "Contestants Bench" -hold -e "./contestantsBenchDeployAndRun.sh" &
sleep 1
xterm  -T "Playground" -hold -e "./playgroundDeployAndRun.sh" &
sleep 1
xterm  -T "Referee Site" -hold -e "./refereeSiteDeployAndRun.sh" &
sleep 1
xterm  -T "Referee" -hold -e "./refereeDeployAndRun.sh" &
xterm  -T "Coach" -hold -e "./coachDeployAndRun.sh" &
xterm  -T "Contestants" -hold -e "./contestantsDeployAndRun.sh" &