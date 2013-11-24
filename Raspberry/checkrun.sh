sleep 120
cd /nfs/scripts

ps -ef | grep Solar.sh | grep -v grep 

if [ $? -eq 1 ] ; then

    echo "Not Running"
    python NotRunning.py
    nohup ./Solar.sh & 
fi
