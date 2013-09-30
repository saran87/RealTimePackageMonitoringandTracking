@rem Use any one of the command below to start the server.
@rem java -server -enableassertions -cp %classpath%;.\..\dist\QuickServer.jar;.\dist\echowebserver.jar echowebserver.EchoWebServer
@java -server -ea -jar .\..\dist\QuickServer.jar -load .\conf\EchoWebServer.xml