#!/bin/bash
# This bootstap script support minimum of JDK 1.8 ,with inner webserver of Tomcat8.
# please regarding copyright ownership.
# author:ningquan
# writted:2018-06-26
# -----------------------------------------------------------------------------
# Start Script for the Springboot Server
# -----------------------------------------------------------------------------
#set -x
#set JAVA_OPTS

JAVA_OPTS="-server -Xms1G -Xmx1G -Xss512k -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m"
BOOT_LOGDIR='/data/logs'
source /etc/profile
#JAVACMD=$JAVA_HOME/bin/java,search jre.
echofont(){
 echo -e "\033[37;"$1"m $2 \033[0m"
}
if [ -z $JAVACMD ] ; then
  if [ -n $JAVA_HOME  ] ; then
      JAVACMD=$JAVA_HOME/bin/java
  else
    JAVACMD=`which java`
  fi
fi

if [ -z $JAVACMD ] ;then
  echo "please check JAVA_HOME,not found JAVA runtime environment......"
  exit 1
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit 1
fi
echofont 32 "JAVACMD="$JAVACMD

#setting perform path
BASEDIR=`dirname $0`/..
BASEDIR=`(cd $BASEDIR; pwd)`
echofont 32 "BASEDIR="$BASEDIR

#grep -w 'port:' application.yml |sed s/[[:space:]]//g |cut -d: -f 2
APPLICATION_CONFIG_FILE="$BASEDIR/conf/application.yml"
if [ -f $APPLICATION_CONFIG_FILE ] ;then
 if [ ! -r $APPLICATION_CONFIG_FILE ] ; then
   echofont 31 "Error: current user can't read file application.yml,suggest use root user"
   exit 1
 fi
else
  echofont 31 "Error: not found application.yml in your config path /conf"
  exit 1
fi

SERVER_PORT=`grep -E '^[^#]*+port:' $APPLICATION_CONFIG_FILE |sed s/[[:space:]]//g |awk -F':' '/70|71|72|73|74|80|81|82|83|84|90|91|92|93|94/{print $2}'`
#echo "server.port="$SERVER_PORT
#profiles active
PROFILE_ACTIVE=`grep -E '^[^#max_]*+active:' ../conf/application.yml | sed s/[[:space:]]//g | awk -F':' '{print $2}'`
echofont 32 "spring.profiles.active="$PROFILE_ACTIVE
SERVER_PROFILE_ACTIVE_FILE=""
if [ -n $PROFILE_ACTIVE ] ; then
 eval SERVER_PROFILE_ACTIVE_FILE='$BASEDIR/conf/application-"$PROFILE_ACTIVE".yml'
 echofont 32 "spring.profiles.active.file="$SERVER_PROFILE_ACTIVE_FILE
 if [ -f $SERVER_PROFILE_ACTIVE_FILE ] ;then
   if [ ! -r $SERVER_PROFILE_ACTIVE_FILE ] ; then
     echofont 31 "Error: current user can't read file:application-"$PROFILE_ACTIVE".yml,suggest use root user"
     exit 1
   fi
 fi 
else
  echofont 33 "Warning: not found profile active file:application-"$PROFILE_ACTIVE".yml"
fi

#according to profile active of environment that get ralative config
if [ ! -z $SERVER_PROFILE_ACTIVE_FILE ] ;then
 SERVER_PORT=`grep -E '^[^#]*+port:' $SERVER_PROFILE_ACTIVE_FILE |sed s/[[:space:]]//g |awk -F':' '/70|71|72|73|74|80|81|82|83|84|90|91|92|93|94/{print $2}'`
 echofont 32 "final server.port="$SERVER_PORT
fi 

#get spring boot application main class,through springboot param 'spring.application.mainclass' property obtain.
MAIN_CLASS=`grep -w 'mainclass:' $APPLICATION_CONFIG_FILE | sed s/[[:space:]]//g | awk -F':' '{print $2}'`
APPLICATION_NAME=`grep -w 'name:' $APPLICATION_CONFIG_FILE | sed s/[[:space:]]//g | awk -F':' '/micro|api/{print $2}'`
echofont 32 "application-name="$APPLICATION_NAME
if [ -z $MAIN_CLASS ] ;then
 echofont 31 "Error: springboot application not set main class,please check config file of application.yml and set property: spring.application.mainclass"
 exit 1
fi
echofont 32 "main-class:"$MAIN_CLASS

CLASSPATH="$BASEDIR"/conf:"$BASEDIR"/boot/*:"$BASEDIR"/lib/*:$CLASSPATH
#echo "CLASSPATH=$CLASSPATH"
#according to diffrent case run someone scripts
#CHECK APPLICATION PROCESS STATUS
APPLICATION_PID="" #`netstat -anp |grep $SERVER_PORT |grep LISTEN |awk '{print $7}' | awk -F'/' '{print $1}'`
check_exist(){
 APPLICATION_PID=`netstat -anp |grep $SERVER_PORT |grep LISTEN |awk '{print $7}' | awk -F'/' '{print $1}'`
 if [ -z $APPLICATION_PID ] ; then
   return 1
 fi
 return 0
}
status(){
 echofont 32 "start check current server status:"
 check_exist
 if [ $? -eq 0 ] ; then
   echofont 32 "application:"$APPLICATION_NAME" is running. The process pid:"$APPLICATION_PID
 else
   echofont 33 "application:"$APPLICATION_NAME" is not running"
 fi
}

start(){
 echofont 32 "=================start application server====================="
 if [ ! -d "$BOOT_LOGDIR/$APPLICATION_NAME/" ] ; then
   `mkdir -p "$BOOT_LOGDIR/$APPLICATION_NAME/"`
 fi
 check_exist
 if [ $? -eq 0 ] ; then
   echofont 31 "Error:application:"$APPLICATION_NAME" is already running. The process pid="$APPLICATION_PID
   exit 1
 fi
 if [ -z "$OPTS_MEMORY" ] ; then
    JAVA_OPTS="-server -Xms1G -Xmx1G -Xss512k -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m"
 fi
 nohup $JAVACMD $JAVA_OPTS \
  -classpath "$CLASSPATH" \
  -Dbasedir="$BASEDIR" \
  -Dfile.encoding="UTF-8" \
  -Djava.awt.headless="true" \
  -Dsun.net.client.defaultConnectTimeout="60000" \
  -Dsun.net.client.defaultReadTimeout="60000" \
  -Djmagick.systemclassloader="no" \
  -Dnetworkaddress.cache.ttl="300" \
  -Dsun.net.inetaddr.ttl=300 \
  -XX:+UseG1GC \
  -XX:+DisableExplicitGC \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:-OmitStackTraceInFastThrow \
  -XX:HeapDumpPath="$BOOT_LOGDIR/" \
  -XX:ErrorFile="$BOOT_LOGDIR/$APPLICATION_NAME/javadump_error_%p.log" \
  $MAIN_CLASS \
  "$@" >$BOOT_LOGDIR/$APPLICATION_NAME/catalina-out.log 2>&1 &
  #APPLICATION_PID=$$
  #echo "checking process......"
  sleep 5
  #status
  runpid=`ps aux |grep java |grep $APPLICATION_NAME |awk '{print $2}'`
  if [ ! -z $runpid ] ; then
    echo $runpid > server.pid
    echofont 32 "===================server start success!======================"
    echofont 32 "===========Ning Quan wish you have a good job!================"
  else
    echofont 31 "Error:server start fail,please cat /data/logs/cataout.log"
  fi
}

restart(){
 echofont 32 "==============restart application server================="
 stop
 start
}

stop(){
 echofont 32 "===========stop application server start================="
 check_exist
 if [ $? -eq 0 ] ; then 
   `kill  $APPLICATION_PID`
   sleep 5
   check_exist
   if [ $? -eq 0 ] ; then
     stop
   fi
   #exit 1
 fi
 echofont 32 "=========stop application server finish!================="
}

usage(){
 echofont 33 "Usage: sh bootstrap.sh [start|stop|restart|status]"
 echofont 33 "eg: ./bootstrap.sh start"
 exit 1 
}
#CHECK APPLICATION PROCESS STATUS
APPLICATION_PID="" #`netstat -anp |grep $SERVER_PORT |grep LISTEN |awk '{print $7}' | awk -F'/' '{print $1}'`
check_exist(){
 APPLICATION_PID=`netstat -anp |grep $SERVER_PORT |grep LISTEN |awk '{print $7}' | awk -F'/' '{print $1}'`
 if [ -z $APPLICATION_PID ] ; then
   return 1
 fi
 return 0
}
echofont(){
 echo -e "\033[37;"$1"m $2 \033[0m"
}
case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac
exit 0

