# Game of the Rope
Distributed implementations of the game of the rope:
- Multithreaded
- Message passing
- RMI

## How to run
### Message passing
Inside the folder there are a few shell scripts.
- To run locally, execute:
    - buildAndGenerateLocal.sh
    - runLocal.sh
- To run remotly, execute:
    - buildAndGenerateGlobal.sh
    - deployAndRun.sh

running remotly will not be successful has the remote machine has shutdown
### RMI
- To run remotly, execute:
    - buildAndGenerateGlobal.sh
    - deployAndRunFirstTime.sh

running will not be successful has the remote machine has shutdown
