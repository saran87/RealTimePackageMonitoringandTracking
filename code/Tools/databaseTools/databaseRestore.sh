#! /bin/bash
# databaseRestore.sh

dashes="-------------------------------";

echo ${dashes};
echo
echo "Restore Mongodb database"
echo
echo ${dashes};

echo ${dashes};
echo
echo "All collections will be imported from backup folder"
echo "All collections will be of JSON type and imported using mongoimport command"
echo
echo ${dashes};
sleep 2s
echo ${dashes};
echo "WARNING"
echo "Please make sure that you have emptied/removed all the collections from the database"
echo "Failing to do so will append the records from backup collections into existing records"
sleep 2s
echo
read -p "Continue with Restore process (y/n) ?" -n 1 isCont
isCont=${isCont,,} #tolowercase
echo
if [[ $isCont == y ]]; then
	echo "Continuing with restore process"
	sleep 2s
	echo
else
	echo "Exiting restore process"
	echo "Use databaseWipeOut.sh to empty/remove the collections"
	sleep 2s
	echo
	echo "Exiting program"
	exit 1
fi
echo ${dashes};

dbName="realTimeDataDb";
echo

echo "Default database set to ${dbName}.";

read -p "Continue (y/n) " -n 1 opt

opt=${opt,,} #tolowercase

if [[ $opt == y ]]; then
    echo    
    echo "Using default database ${dbName}";
    sleep 1s
    echo
else
    echo
    read -e -p "Enter name of database to back up: " dbName
    sleep 1s
    echo "Database changed to ${dbName}"
    echo ${dashes}
    echo
fi


latestFolder=$(ls ~/backups/ | tail -1)

echo "Latest backup is ${latestFolder}"

read -p "Continue from latest backup i.e. ${latestFolder} (y/n) " -n 1 opt2
opt2=${opt2,,} #tolowercase

if [[ $opt2 == y ]]; then
	echo
	echo "Restoring from latest folder i.e. ${latestFolder}";
	sleep 1s
else
	echo
	echo ${dashes}
	echo "List of backup folders"
	ls -t1 ~/backups/
	echo ${dashes}
	echo
	read -e -p "Enter the correct folder to be restored from:  " newFolder
	if [[ -d ~/backups/"${newFolder}" ]]; then
		latestFolder=$newFolder
		echo
		sleep 1s
		echo "${newFolder} exists"
		echo "Restoring from ${newFolder}";
		echo
	else
		echo	
		sleep 1s
		echo "ERROR: Invalid Folder name";
		echo "FOLDER DOES NOT EXIST!"
		sleep 1s
		echo
		echo "Restore process will be continued from latest backup i.e. ${latestFolder}"
	fi
fi

fileArray=(`ls -1 ~/backups/"${latestFolder}"/*.json | sort`)
sleep 1s
echo ${dashes}
echo "${#fileArray[@]} files in the folder ${latestFolder}";
echo
#echo ${fileArray[@]}
echo "The files are:  (sorted alphabetically)";
#order: configurations - humidity - shock - temperature - vibration
ls -1 ~/backups/"${latestFolder}"/*.json | sort
echo
echo ${dashes}

sleep 2s

echo "Starting restoration process into ${dbName}";
sleep 2s
echo

echo "Restoring Configurations collection"
echo "Filename: ${fileArray[0]}";
mongoimport --db ${dbName} --collection configurations --file ${fileArray[0]}
echo "Successfully imported Configurations collection from ${fileArray[0]}"
sleep 3s
echo

echo "Restoring Humidity collection"
echo "Filename: ${fileArray[1]}";
mongoimport --db ${dbName} --collection humidity --file ${fileArray[1]}
echo "Successfully imported Humidity collection from ${fileArray[1]}"
sleep 3s
echo 

echo "Restoring Shock collection"
echo "Filename: ${fileArray[2]}";
mongoimport --db ${dbName} --collection shock --file ${fileArray[2]}
echo "Successfully imported Shock collection from ${fileArray[2]}"
sleep 3s
echo

echo "Restoring Temperature collection"
echo "Filename: ${fileArray[3]}";
mongoimport --db ${dbName} --collection temperature --file ${fileArray[3]}
echo "Successfully imported Temperature collection from ${fileArray[3]}"
sleep 3s
echo

echo "Restoring Vibration collection"
echo "Filename: ${fileArray[4]}";
mongoimport --db ${dbName} --collection vibration --file ${fileArray[4]}
echo "Successfully imported Vibration collection from ${fileArray[4]}"
sleep 3s
echo

echo "Restoration complete";
echo "Program exiting"; 
