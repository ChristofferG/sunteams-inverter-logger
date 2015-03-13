# Introduction #
This is a java console application that retrieves data from the Sunteams inverter via serial port and logs to a daily file output every 30 seconds.

It will also to upload data to http://www.pvoutput.org via the API
http://www.pvoutput.org/help.html#api-spec

The second stage will to run (attempt to) this via an Atmel atmega328p chip to remove the need for a PC

This should work with other models as well but only tested on 2000/2800 model

# News #
## 2013-11-24 ##
### Raspberry Pi ###
The Raspberry version has been running a while now trouble free, will upload the code in the next few days.

## 2013-09-15 ##
### Raspberry Pi ###
Project had been shelved for a while so dragged it all out and found the display has stopped working, I had also recently seen the Raspberry Pi that looked like would be easier, so one was promptly ordered.
After a few hours the data is retrieved from the inverted and logged locally, still plane to using cUrl for the upload as per the Java version as that works well. Should be complete next weekend

## 2012-02-27 ##
Arduino - Serial routines tested against a serial modem using hardware flow-control.<br>
To code inverter commands and do basic testing.<br>
<h2>2011-10-12</h2>
Arduino - Still need to handle the serial number of the inverter but work started on Arduino version using the <a href='http://www.freetronics.com/products/etherten'>Etherten</a> Board.<br>
Tested:<br>
SD Card functionality<br>
Network interface<br>
<a href='http://www.ebay.com.au/itm/Nokia-5110-LCD-84x84-dot-martix-backlight-PCB-Blue-/390354481502?pt=LH_DefaultDomain_0&hash=item5ae2f0b15e'>Nokia LCD</a><br>
<a href='http://www.futurlec.com/Mini_DS1307.shtml'>RTC</a><br>
<h2>2011-01-29</h2>
Java - A release version is nearly complete, need to complete error handler & use parameters for all relevant values.<br>
<br>
<h1>Dependencies</h1>
The first version uses cURL to upload the data.<br>
cURL <a href='http://www.gknw.de/mirror/curl/win32/curl-7.21.3-ssl-sspi-zlib-static-bin-w32.zip'>Available Here</a><br>
commons-collections-3.2.1.jar<br>
commons-lang-2.5.jar<br>
commons-configuration-1.6.jar<br>
commons-logging-1.1.1.jar<br>
log4j-1.2.15.jar<br>
JCommWin32 <a href='http://www.caerustech.com/JCommWin32.php'>Available Here</a><br>

<h1>Documentation</h1>
<a href='http://code.google.com/p/sunteams-inverter-logger/wiki/ComunicationDetails'>Inverter Serial Specs</a>

<a href='http://code.google.com/p/sunteams-inverter-logger/wiki/propertiesfile'>Properties File</a>