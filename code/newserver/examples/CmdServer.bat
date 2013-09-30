@echo Change cmd.exe to command.com for win 9x in the batch file
@java -server -cp %classpath%;.\..\dist\QuickServer.jar;.\dist\cmdserver.jar cmdserver.CmdServer cmd.exe
