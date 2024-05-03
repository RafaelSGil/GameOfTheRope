echo "Killing all processes."

# Function to kill process and check if it's killed
kill_and_check() {
    echo "Killing $1"
    sshpass -f password ssh sd103@$2 "fuser -k -n tcp $3"
    sleep 2
    if sshpass -f password ssh sd103@$2 "fuser -n tcp $3"; then
        echo "Failed to kill $1"
    else
        echo "$1 killed successfully"
    fi
}

kill_and_check "General Repository" l040101-ws01.ua.pt 22121
kill_and_check "Contestants Bench" l040101-ws02.ua.pt 22122
kill_and_check "Playground" l040101-ws05.ua.pt 22125
kill_and_check "Referee Site" l040101-ws01.ua.pt 22126

echo "Completed"