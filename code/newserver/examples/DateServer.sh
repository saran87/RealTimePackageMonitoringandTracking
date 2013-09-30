#!/bin/bash
# Exchages object over socket
exec java -server -cp ./../dist/QuickServer.jar:./dist/dateserver.jar dateserver.DateServer