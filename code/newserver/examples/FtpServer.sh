#!/bin/bash
# exec java -server -cp ./../dist/QuickServer.jar:./dist/ftpserver.jar ftpserver.FtpServer "$@"
exec java -server -jar ./../dist/QuickServer.jar -load conf/FtpServer.xml