
# Checks that the correct number of arguments has been passed in
if [ $# -ne 1 ]; then
    echo "Use: $0 <number of iterations>"
    exit 1
fi

# Function to execute commands and check for errors
run_with_error_check() {
    sh deployAndRunOtherTimes.sh &

    # Wait until all the processes have finished


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
    sleep 60
    i=$((i+1))
done

echo "All iterations have been successfully completed"
