#TODO does not work
javadoc -cp genclass.jar:entities/data ./**/*.java -d ../javadoc
cd ../javadoc
brave-browser index.html
echo "DOES NOT WORK!"