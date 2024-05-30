echo "Killing all processes."

# Function to kill process if running and check if it's killed
kill_if_running_and_check() {
    echo "Killing $1"
    # Check if there are any processes running on the port
    if sshpass -f password ssh sd103@$2 "fuser -n tcp $3"; then
        # Se algum processo estiver em execução, mata-o
        sshpass -f password ssh sd103@$2 "fuser -k -n tcp $3"
        sleep 2
        # Check if the process was killed
        if sshpass -f password ssh sd103@$2 "fuser -n tcp $3"; then
            echo "Failed to kill $1"
        else
            echo "$1 killed successfully"
        fi
    else
        echo "No process running on port $3 for $1"
    fi
}
kill_if_running_and_check "RMI registry" l040101-ws01.ua.pt 22121
kill_if_running_and_check "Registry" l040101-ws01.ua.pt 22122
#kill_if_running_and_check "General Repository" l040101-ws01.ua.pt 22121
#kill_if_running_and_check "Contestants Bench" l040101-ws10.ua.pt 22122
#kill_if_running_and_check "Playground" l040101-ws05.ua.pt 22125
#kill_if_running_and_check "Referee Site" l040101-ws06.ua.pt 22126

echo "Completed"
