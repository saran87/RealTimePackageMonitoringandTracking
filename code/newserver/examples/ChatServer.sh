#!/bin/bash
#exec java -cp ./../dist/QuickServer.jar:dist/chatserver.jar chatserver.ChatServer
exec java -server -ea -jar ./../dist/QuickServer.jar -load conf/ChatServer.xml