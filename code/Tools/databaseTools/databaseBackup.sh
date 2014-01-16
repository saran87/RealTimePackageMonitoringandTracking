#! /bin/bash
# databaseBackup.sh
# author Amarnath

dashes="-------------------------------";

echo ${dashes};
echo
echo "Backup of Mongodb database"
echo
echo ${dashes};

echo ${dashes};
echo
echo "All collections will be exported to backup folder"
echo "All collections will be of JSON type and exported using mongoexport command"
echo
echo ${dashes};

dbName="realTimeDataDb";
echo

echo "Default database set to ${dbName}.";

read -p "Continue (y/n) " -n 1 opt

opt=${opt,,} #tolowercase

if [[ $opt == y ]]; then
	echo	
    echo "Using default database ${dbName}";
else
	echo
	read -e -p "Enter name of database to back up: " -i "Enter a valid Database name" dbName
	sleep 1s	 	
	echo "Database changed to ${dbName}"
fi

echo ${dashes};

now=$(date +"%b-%d-%Y-%H-%M-%S");

folderName="${dbName}-backup-${now}";

echo "Creating folder ${folderName}";

mkdir ~/backups/${folderName};

echo "Folder created ${folderName}";
echo ${dashes}
sleep 3s
echo "Starting Backup process. Exporting Collections";

echo 

echo "Exporting Configurations collection";
configurationsFile="configurations-${now}.json"
mongoexport --db ${dbName} --collection configurations --out ~/backups/${folderName}/${configurationsFile}
sleep 2s
echo "exported ${configurationsFile}";
echo ${dashes}
echo

sleep 2s

echo "Exporting Temperature collection";
temperatureFile="temperature-${now}.json"
mongoexport --db ${dbName} --collection temperature --out ~/backups/${folderName}/${temperatureFile}
sleep 3s
echo "exported Temperature collection to ${temperatureFile}";
echo ${dashes}
echo

sleep 2s

echo "Exporting Humidity collection";
humidityFile="humidity-${now}.json"
mongoexport --db ${dbName} --collection humidity --out ~/backups/${folderName}/${humidityFile}
sleep 3s
echo "exported Humidity collection to ${humidityFile}";
echo ${dashes}
echo

sleep 2s

echo "Exporting Vibration collection";
vibrationFile="vibration-${now}.json"
mongoexport --db ${dbName} --collection vibration --out ~/backups/${folderName}/${vibrationFile}
sleep 3s
echo "exported Vibration collection to ${vibrationFile}";
echo ${dashes}
echo

sleep 2s

echo "Exporting Shock collection";
shockFile="shock-${now}.json"
mongoexport --db ${dbName} --collection shock --out ~/backups/${folderName}/${shockFile}
sleep 3s
echo "exported Shock collection to ${shockFile}";

echo 

echo "All collections exported successfully"
echo "List of files exported: "
echo ${dashes}
ls -1  ~/backups/${folderName}/
echo ${dashes}
sleep 3s
echo
echo ${dashes}
echo "Use databaseRestore.sh to import/restore collections"
echo ${dashes}

sleep 3s
echo 
echo "Program exiting"
