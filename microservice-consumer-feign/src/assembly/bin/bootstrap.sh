#!/bin/bash
# This bootstap script support mininum of JDK 1.8 ,with inner webserver of Tomcat8.
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
echo "JAVACMD="$JAVACMD

#setting perform path
BASEDIR=`dirname $0`/..
BASEDIR=`(cd $BASEDIR; pwd)`
echo "BASEDIR="$BASEDIR

#grep -w 'port:' application.yml |sed s/[[:space:]]//g |cut -d: -f 2
APPLICATION_CONFIG_FILE="$BASEDIR/conf/application.yml"
if [ -f $APPLICATION_CONFIG_FILE ] ;then
 if [ ! -r $APPLICATION_CONFIG_FILE ] ; then
   echo "Error: current user can't read file application.yml,suggest use root user"
   exit 1
 fi
else
  echo "Error: not found application.yml in your config path /conf"
  exit 1
fi

SERVER_PORT=`grep -w 'port:' $APPLICATION_CONFIG_FILE |sed s/[[:space:]]//g |awk -F':' '{print $2}'`
#echo "server.port="$SERVER_PORT
#profiles active
PROFILE_ACTIVE=`grep -w 'active:' $APPLICATION_CONFIG_FILE | sed s/[[:space:]]//g | awk -F':' '{print $2}'`
echo "spring.profiles.active="$PROFILE_ACTIVE
SERVER_PROFILE_ACTIVE_FILE=""
if [ -n $PROFILE_ACTIVE ] ; then
 eval SERVER_PROFILE_ACTIVE_FILE='$BASEDIR/conf/application-"$PROFILE_ACTIVE".yml'
 echo "spring.profiles.active.file="$SERVER_PROFILE_ACTIVE_FILE
 if [ -f $SERVER_PROFILE_ACTIVE_FILE ] ;then
   if [ ! -r $SERVER_PROFILE_ACTIVE_FILE ] ; then
     echo "Error: current user can't read file:application-"$PROFILE_ACTIVE".yml,suggest use root user"
     exit 1
   fi
 fi 
else
  echo "warning: not found profile active file:application-"$PROFILE_ACTIVE".yml"
fi

#according to profile active of environment that get ralative config
if [ ! -z $SERVER_PROFILE_ACTIVE_FILE ] ;then
 SERVER_PORT=`grep -w 'port:' $SERVER_PROFILE_ACTIVE_FILE |sed s/[[:space:]]//g |awk -F':' '{print $2}'`
 echo "final server.port="$SERVER_PORT
fi 

#get spring boot application main class,through springboot param 'spring.application.mainclass' property obtain.
MAIN_CLASS=`grep -w 'mainclass:' $APPLICATION_CONFIG_FILE | sed s/[[:space:]]//g | awk -F':' '{print $2}'`
APPLICATION_NAME=`grep -w 'name:' $APPLICATION_CONFIG_FILE | sed s/[[:space:]]//g | awk -F':' '{print $2}'`
echo "application-name="$APPLICATION_NAME
if [ -z $MAIN_CLASS ] ;then
 echo "Error: springboot application not set main class,please check config file of application.yml and param is spring.application.mainclass"
 exit 1
fi
echo "main-class:"$MAIN_CLASS

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
 echo "start check current server status:"
 check_exist
 if [ $? -eq 0 ] ; then
   echo "application:"$APPLICATION_NAME "is running. The process pid:"$APPLICATION_PID
 else
   echo "application:"$APPLICATION_NAME "is not running"
 fi
}

start(){
 echo "==============start application server================="
 check_exist
 if [ $? -eq 0 ] ; then
   echo "Error:application:"$APPLICATION_NAME "is already running. The process pid="$APPLICATION_PID
   exit 1
 fi
 if [ -z "$OPTS_MEMORY" ] ; then
    JAVA_OPTS="-server -Xms1G -Xmx1G -Xss512k -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m"
 fi
 nohup "$JAVACMD" $JAVA_OPTS \
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
#  -XX:+PrintGCDetails \
#  -XX:+PrintGCTimeStamps \
#  -XX:+PrintHeapAtGC \
#  -Xloggc:$BOOT_LOGDIR/$APPLICATION_NAME/javagc.log \
  $MAIN_CLASS \
  "$@" >$BOOT_LOGDIR/$APPLICATION_NAME/catalina-out.log 2>&1 &
  #APPLICATION_PID=$$
  #echo "checking process......"
  sleep 5
  #status
  runpid=`ps -aux |grep java |grep $MAIN_CLASS |awk '{print $2}'`
  if [ -n $runpid ] ; then
    echo $runpid > server.pid

    echo "====================server start success!======================"

    echo "===========Ning Quan wish you have a good job!================"

  else
    echo "Error:server start fail,please cat /data/logs/cataout.log"
  fi
}

restart(){
 echo "==============restart application server================="
 stop
 start
}

stop(){
 echo "==============stop application server start================="
 check_exist
 if [ $? -eq 0 ] ; then 
   `kill  $APPLICATION_PID`
   sleep 5
   check_exist
   if [ $? -eq 0 ] ; then
     stop
   else
     echo "==============stop application server finish!================="
   fi
   exit 1
 fi
 echo "==============stop application server finish!================="
}

usage(){
 echo "Usage: sh bootstrap.sh [start|stop|restart|status]"
 echo "eg: ./bootstrap.sh start"
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

