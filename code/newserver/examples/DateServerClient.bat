@rem Exchages object over socket
@java -server -cp %classpath%;.\..\dist\QuickServer.jar;.\dist\dateserver.jar dateserver.DateServerClient 127.0.0.1 125
