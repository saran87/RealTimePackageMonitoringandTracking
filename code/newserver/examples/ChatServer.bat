@rem java -cp %classpath%;.\..\dist\QuickServer.jar;.\dist\chatserver.jar chatserver.ChatServer
@java -server -ea -jar .\..\dist\QuickServer.jar -load .\conf\ChatServer.xml
