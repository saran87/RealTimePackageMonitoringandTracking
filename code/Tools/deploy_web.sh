sudo  cp -R ../WebApplication/server/ deploy/

cd deploy

git init 

git add .

git add -u 

curDate=date

git commit -m "deployed on `$curDate`"

git branch -m deploy

git remote add origin https://github.com/saran87/RealTimePackageMonitoringandTracking.git

git push origin deploy --force

cd ../

rm -rdf deploy
