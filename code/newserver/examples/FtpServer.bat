@rem java -server -cp %classpath%;.\..\dist\QuickServer.jar;.\dist\ftpserver.jar ftpserver.FtpServer %1
@java -server -jar .\..\dist\QuickServer.jar -load conf/FtpServer.xml
