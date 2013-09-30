#!/bin/bash
exec java -server -Xbatch -Dxmladder.AddNumberReq.performanceTest=false -Dorg.quickserver.util.logging.SimpleJDKLoggingHook.Level=INFO -cp ./../dist/QuickServer.jar:./dist/xmladder.jar xmladder.XmlAdder
