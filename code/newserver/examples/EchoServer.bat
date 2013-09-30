@rem java -server -enableassertions -cp %classpath%;.\..\dist\QuickServer.jar;.\dist\echoserver.jar echoserver.EchoServer
@rem In production you may disable assertions using -disableassertions or -da 
@java -server -ea -jar .\..\dist\QuickServer.jar -load conf/EchoServer.xml
