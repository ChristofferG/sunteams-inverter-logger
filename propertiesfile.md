# Introduction #

This details the values in the sunteams.properties file

## Supplied Values ##
```
PORT = COM1
DAILYFILE = c:\temp
SystemId = 
APIKey = 
URL =  http://pvoutput.org/service/r1/addstatus.jsp
cUrl = c:\curl\curl.exe
mode = peak
```
```
PORT - This is the serial port the logger should connect to.
DAILYFILE - The location to create the daily file with every sample taken.
SystemId - You system id
APIkey - You API Key
URL - The url that accepts the live updates
cUrl - The location of the curl executable
mode - The type of instantanious power to record
  peak -> peak power for last 10 minutes
  avg -> average power for last 10 minutes
  act -> actual power when the 10 minute sample was take
```