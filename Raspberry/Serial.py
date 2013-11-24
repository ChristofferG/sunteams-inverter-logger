import serial
import os
from time import sleep
from Logger import *

#init1 = {0x43, 0x5A, 0x00, 0x00, 0x7F, 0x00, 0x00, 0x03, 0x00, 0x01, 0x1F}
#init2 = {0x43, 0x5A, 0x00, 0x00, 0x7F, 0x00, 0x00, 0x00, 0x00, 0x01, 0x1C}
#sendSerial = {0x43, 0x5A, 0x00, 0x00, 0x7F, 0x01, 0x00, 0x01, [SIZE],[SERIAL], 0x01, [chksum MSB], [chksum LSB]}
#getValues = {0x43, 0x5A, 0x00, 0x00, 0x01, 0x01, 0x01, 0x02, 0x00, 0x00, 0xA2}
#sendSerial = {0x43, 0x5A, 0x00, 0x00, 0x7F, 0x01, 0x00, 0x01, 0x0B, 0x41, 0x33, 0x47, 0x53, 0x33, 0x32, 0x31, 0x30, 0x37, 0x34, 0x01, 0x03, 0x69};
#getmodel = {0x43, 0x5A, 0x00, 0x00, 0x01, 0x01, 0x01, 0x03, 0x00, 0x00, 0xA3};
#getValues = {0x43, 0x5A, 0x00, 0x00, 0x01, 0x01, 0x01, 0x02, 0x00, 0x00, 0xA2};
#getParms = {0x43, 0x5A, 0x00, 0x00, 0x01, 0x01, 0x01, 0x04, 0x00, 0x00, 0xA4};

#This is true when the next 10 minute upload can occurs
CanUpload = True
LastWasZero = False
ePeak = 0
NoDataCount = 0

def SendInit1():
    hexText = "435A00007F00000300011F"
    txt = hexText.decode("hex")
    lg.WriteHexLog(txt, "Sent ")
    port.write(txt)

def SendInit2():
    hexText = "435A00007F00000000011C"
    txt = hexText.decode("hex")
    lg.WriteHexLog(txt, "Sent ")
    port.write(txt)


def WaitInput(WaitTime):
    count = 0
    txt = ""
    while count < WaitTime:
        if (port.inWaiting() == 0):
            sleep(1)
            count += 1
        else:
            txt += port.read(1)
            count = 0
    if len(txt) == 0:
        lg.WriteHexLog(txt, "No Reply")
    else:
        lg.WriteHexLog(txt, "Reply")
    return txt

def GetSerialReply():
    lg.WriteLog(lg.DEBUG_ECHO,"Send Init Sequence")
    SendInit1()
    sleep(1)
    SendInit1()
    sleep(1)
    SendInit1()
    sleep(1)
    SendInit1()
    sleep(1)
    SendInit1()
    sleep(1)
    lg.WriteLog(lg.DEBUG_ECHO,"Ask Serial Number")
    SendInit2()
    return WaitInput(10)

def ValidateReply(reply):
    return True

def SendSerialBack():
    lg.WriteLog(lg.DEBUG_ECHO,"Send Serial Back")
    hexText = "435A00007F0100010B41334753333231303734010369"
    txt = hexText.decode("hex")
    lg.WriteHexLog(txt, "Sent ")
    port.write(txt)

def GetModel():
    lg.WriteLog(lg.DEBUG_ECHO,"Get Model")
    hexText = "435A0000010101030000A3"
    txt = hexText.decode("hex")
    lg.WriteHexLog(txt, "Sent ")
    port.write(txt)

def GetValues():
    lg.WriteLog(lg.DEBUG_ECHO,"Get Values")
    hexText = "435A0000010101020000A2"
    txt = hexText.decode("hex")
    lg.WriteHexLog(txt, "Sent ")
    port.write(txt)

#def ExtractSerial():
    
    
def ProcessValues(Up,reply):
    global ePeak
    Start = 9
    Temp = ((ord(reply[Start+0])*256) + (ord(reply[Start+1]))) / 10.0
    Volts = ((ord(reply[Start+2])*256) + (ord(reply[Start+3]))) / 10.0
    eNow = ((ord(reply[Start+10])*256) + (ord(reply[Start+11])))
    eToday = ((ord(reply[Start+12])*256) + (ord(reply[Start+13]))) / 10.0
    eTotal = ((ord(reply[Start+16])*256) + (ord(reply[Start+17]))) / 10.0
    if eNow > ePeak:
        ePeak = eNow
        
    lg.WriteHistory(Up,eTotal, eToday, eNow, Volts, Temp, ePeak)
    #Upload(eTotal, eToday, eNow, Volts, Temp, ePeak)
    
    if Up:
        Upload(eTotal, eToday, eNow, Volts, Temp, ePeak)
        ePeak = 0
    return eNow

def Upload(eTotal, eToday, eNow, Volts, Temp, ePeak):
    SystemID = <Your System>
    APIKey = <Your Key>
    URL =  "http://pvoutput.org/service/r1/addstatus.jsp"
    
    Command = "curl "
    Command += "-d \"d=" + getDate() + "\" "
    Command += "-d \"t=" + getTime() + "\" "
    Command += "-d \"v1=" + "{:08.0f}".format(eToday*1000) + "\" "
    Command += "-d \"v2=" + "{0}".format(eNow) + "\" "
    Command += "-H \"X-Pvoutput-SystemId: " + SystemID + "\" "
    Command += "-H \"X-Pvoutput-Apikey: " + APIKey + "\" "
    Command += "-d \"v6=" + "{:03.1f}".format(Volts) + "\" "
    Command += "-d \"v5=" + "{:2.1f}".format(Temp) + "\" "

    Command += URL

    lg.WriteLog(lg.DEBUG,Command)
    os.system(Command)
       
def getDate():
    now = DateTime.now()
    return now.strftime('%Y%m%d')

def getTime():
    now = DateTime.now()
    return now.strftime('%H:%M')

       
def Is10Mins():
    global CanUpload
    global LastWasZero    
    now = DateTime.now()
    if now.strftime('%M')[1] == "0":
        if LastWasZero:
            CanUpload = False
        else:
            CanUpload = True
            LastWasZero = True
    else:
        CanUpload = False
        LastWasZero = False
        
    return CanUpload    
     

lg = Logger()
sPort = '/dev/ttyAMA0'
sPause = 40
lg.WriteLog(lg.DEBUG_ECHO,"Start")
lg.WriteLog(lg.DEBUG,"Serial Port   :" + sPort)
lg.WriteLog(lg.DEBUG,"Poll Interval :" + str(sPause))

port = serial.Serial(sPort , 9600)
port.xonxoff = False
port.timeout = 1
port.rtscts = False
port.dsrdtr = False


port.open()
reply = GetSerialReply()
if len(reply) == 0:
    lg.WriteLog(lg.DEBUG, "No Serial - Shutdown")
else:
    if ValidateReply(reply) == False:
        lg.WriteLog(lg.DEBUG, "Bad Reply - Shutdown")
    else:
#        ExtractSerial(reply)
        SendSerialBack()
        GetModel()
        reply = WaitInput(10)
        # as lonng as we have reply then keep looping
        while len(reply) > 0 and NoDataCount < 6:
            GetValues()
            reply = WaitInput(20)
            if ValidateReply(reply) == False:
                lg.WriteLog(lg.DEBUG, "Bad Reply - Shutdown")
                break
            # only process values when there is data
            if len(reply) > 0:
                if ProcessValues(Is10Mins(),reply) == 0:
                    NoDataCount=NoDataCount+1
                else:
                    NoDataCount=0
            else:    
                NoDataCount=NoDataCount+1
                
            lg.WriteLog(lg.DEBUG,"Pause")
            sleep(sPause - 21)    
            
        lg.WriteLog(lg.DEBUG, "No Values - Shutdown")
    
port.close() 
    


