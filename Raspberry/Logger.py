from datetime import datetime as DateTime


class Logger:

    DEBUG = 1
    DEBUG_ECHO = 2
    LOG = 3
    Start = True
    Heading = False
    LastWasHour = False

        
###############################

    def GetFileName(self):
        dt = self.GetDate()
        return dt + '.log'
###############################

    def GetTime(self):
        now = DateTime.now()
        return now.strftime('%H:%M:%S')
###############################

    def GetDate(self):
        now = DateTime.now()
        return now.strftime('%y-%m-%d')
###############################

    def WriteHeader(self):
        fo = open(self.GetFileName(), "a+")
        fo.write("\n Time          Total     Today      Now     Peak     Volts  Temp\n")
        fo.close()

    
    def IsHour(self):
        now = DateTime.now()
        if now.strftime('%M')[0] == "0" and now.strftime('%M')[1] == "0":
            if self.LastWasHour:
                self.Heading = False
            else:
                self.Heading = True
                self.LastWasHour = True
        else:
            self.Heading = False
            self.LastWasHour = False
        
        return self.Heading
        
###############################

    def WriteHistory(self, Up, eTotal, eToday, eNow, Volts, Temp, ePeak):
        
        if self.Start == True or self.IsHour():
            self.WriteHeader()
            self.Start = False
            
        fo = open(self.GetFileName(), "a+")
        if Up:
            fo.write("*")
        else:
            fo.write(" ")
        
        fo.write(self.GetTime() + " ")
        fo.write("{:10.2f}".format(eTotal))
        fo.write("{:10.1f}".format(eToday))
        fo.write("{:9}".format(eNow))
        fo.write("{:9}".format(ePeak))
        fo.write("{:10.2f}".format(Volts))
        fo.write("{:6.2f}".format(Temp))
        fo.write("\n")
        fo.close()

###############################
    def WriteLog(self, Type, str):
        if  Type == self.DEBUG_ECHO:
            print str
            Type = self.DEBUG
        if  Type == self.DEBUG:
            fo = open('D-' + self.GetFileName(), "a+")
        else:
            fo = open(self.GetFileName(), "a+")
        fo.write(self.GetTime() + " ")
        fo.write(str)
        fo.write("\n")
        fo.close()

###############################

    def WriteHexLog(self, str, direction):
        fo = open('D-' + self.GetFileName(), "a+")
        fo.write(self.GetTime() + " " + direction + " ")
        counter = 0
        for x in str:
            fo.write(x.encode("hex") + " ")
            counter = counter + 1
            if counter >= 40:
                fo.write("\n               ")
                counter = 0
        fo.write("\n")
        fo.close()
