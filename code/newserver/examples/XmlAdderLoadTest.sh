#!/bin/bash
#Args: [client_count] [host] [port]
exec java -Dxmladder.XmlAdderClient.bebug=false -Dxmladder.XmlAdderClient.brokenReq=false -cp ./dist/xmladder.jar:./../dist/commons-digester.jar:./../dist/commons-collections.jar:./../dist/commons-logging.jar:./../dist/commons-beanutils.jar xmladder.LoadTest "$@"