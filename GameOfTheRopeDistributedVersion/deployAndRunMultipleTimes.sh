
# Checks that the correct number of arguments has been passed in
if [ $# -ne 1 ]; then
    echo "Use: $0 <number of iterations>"
    exit 1
fi

# Function to execute commands and check for errors
run_with_error_check() {
    xterm  -T "General Repository"  -e "sh generalRepositoryDeployAndRun.sh" &
    sleep 1
    xterm  -T "Playground"  -e "sh playgroundDeployAndRun.sh" &
    sleep 1
    xterm  -T "Referee Site"  -e "sh refereeSiteDeployAndRun.sh" &
    sleep 1
    xterm  -T "Contestants Bench"  -e "sh contestantsBenchDeployAndRun.sh" &
    sleep 3
    xterm  -T "Contestants"  -e "sh contestantsDeployAndRun.sh" &
    sleep 2
    xterm  -T "Coach"  -e "sh coachDeployAndRun.sh" &
    sleep 2
    xterm  -T "Referee"  -e "sh refereeDeployAndRun.sh" &

    # Wait until all the processes have finished
    wait

    # Checks if any process has failed
    if [ $? -ne 0 ]; then
        echo "Error in iteration $1"
        exit 1
    fi
}

# Number of iterations supplied by the user
total_iterations=$1

# Runs the script the number of times provided by the user
i=1
while [ $i -le $total_iterations ]; do
    echo "Iteration Number: $i"
    run_with_error_check $i
    sleep 3
    sh killAll.sh
    i=$((i+1))
done

echo "All iterations have been successfully completed"
