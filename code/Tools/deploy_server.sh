cd ../Server

mvn clean dependency:copy-dependencies package

cd target 

echo "java -cp Server-1.0-SNAPSHOT.jar:dependency/netty-all-4.0.13.Final.jar:dependency/mongo-java-driver-2.11.3.jar:dependency/protobuf-java-2.5.0.jar:dependency/NetworkMessage-1.0.jar rtpmt.server.App" > run

git init

git add .

git add -u

git commit -m "deploying server"

git branch -m deploy_server

git remote add origin https://github.com/saran87/RealTimePackageMonitoringandTracking.git

git push origin deploy_server --force
