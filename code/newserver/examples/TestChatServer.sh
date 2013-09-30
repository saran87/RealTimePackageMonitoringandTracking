#!/bin/bash
#args = port ip maxClient clientidstart
exec java -cp ./../dist/QuickServer.jar:./dist/chatserver.jar chatserver.TestChatServer 7412 127.0.0.1 "$@"