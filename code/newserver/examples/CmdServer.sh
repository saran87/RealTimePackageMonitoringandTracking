#!/bin/bash
# Change shell_program to a valid executable
exec java -server -cp ./../dist/QuickServer.jar:./dist/cmdserver.jar cmdserver.CmdServer sh