#!/bin/bash
# Use any one of the command below to start the server.
#exec java -server -enableassertions -cp ./../dist/QuickServer.jar:./dist/echowebserver.jar echowebserver.EchoWebServer
exec java -server -ea -jar ./../dist/QuickServer.jar -load conf/EchoWebServer.xml