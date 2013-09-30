#!/bin/bash
# exec java -server -enableassertions -cp ./../dist/QuickServer.jar:./dist/echoserver.jar echoserver.EchoServer
# In production you may disable assertions using -disableassertions or -da 
exec java -server -ea -jar ./../dist/QuickServer.jar -load conf/EchoServer.xml