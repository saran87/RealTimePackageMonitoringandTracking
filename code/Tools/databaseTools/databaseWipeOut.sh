#! /bin/bash
# databaseWipeOut.sh

dashes="-------------------------------";

echo ${dashes};
echo
echo "Wipeout Mongodb database"
echo
echo ${dashes};

echo ${dashes};
echo
echo "All collections from the specified database will be wiped out/removed"
echo
echo ${dashes};
sleep 2s
echo ${dashes};
echo "WARNING"
echo "You are about to delete all the data from the specified database"
echo "ALL DATA WILL BE REMOVED AND CANNOT BE RECOVERED"
sleep 2s
echo
read -p "Continue with wipe out/removal process (y/n) ?" -n 1 isCont
isCont=${isCont,,} #tolowercase
echo
if [[ $isCont == y ]]; then
    echo "Continuing with wipe out/removal process"
    sleep 2s
    echo
else
    echo "Exiting wipe out/removal process"
    sleep 2s
    echo
    echo "Exiting program"
    exit 1
fi
echo
echo ${dashes};


dbName="realTimeDataDb"

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

sleep 2s

echo "Starting wipe out/removal process for ${realTimeDataDb} database"
echo
sleep 3s

echo "Deleting Configurations collection"
mongo ${dbName} --eval "db.configurations.remove()"
sleep 3s
echo "Deleted Configurations collection"
echo

echo "Deleting Humidity collection"
mongo ${dbName} --eval "db.humidity.remove()"
sleep 3s
echo "Deleted Humidity collection"
echo

echo "Deleting Shock collection"
mongo ${dbName} --eval "db.shock.remove()"
sleep 3s
echo "Deleted Shock collection"
echo

echo "Deleting Temperature collection"
mongo ${dbName} --eval "db.temperature.remove()"
sleep 3s
echo "Deleted Temperature collection"
echo

echo "Deleting Vibration collection"
mongo ${dbName} --eval "db.vibration.remove()"
sleep 3s
echo "Deleted Vibration collection"
echo
echo ${dashes}
sleep 2s

echo
echo "${dbName} emptied completely"
echo 
echo "Exiting program"


