#!/bin/sh

source /etc/profile

BASEDIR=`dirname $0`/..
BASEDIR=`(cd $BASEDIR; pwd)`
echo "BASEDIR="$BASEDIR
#main class
MAIN_CLASS=com.zhht.cloud.EurekaApplication
echo "mainclass="$MAIN_CLASS
#JAVACMD=$JAVA_HOME/bin/java
if [ -z $JAVACMD ] ; then
  if [ -n $JAVA_HOME  ] ; then
      JAVACMD=$JAVA_HOME/bin/java
  else
    JAVACMD=`which java`
  fi
fi
echo "JAVACMD="$JAVACMD
#set CLASSPATH
CLASSPATH="$BASEDIR"/classes/*:"$BASEDIR"/conf:"$BASEDIR"/lib/*:$CLASSPATH
echo "CLASSPATH=$CLASSPATH"

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit 1
fi

if [ -z "$OPTS_MEMORY" ] ; then
    OPTS_MEMORY="-server -Xms1G -Xmx1G -Xmn512m  -Xss512k -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m "
fi

projectName="microservice-sleuth-stream-logstash-0.0.1-SNAPSHOT.jar"
nohup $JAVACMD $JAVA_OPTS  $OPTS_MEMORY   -classpath $CLASSPATH   -Dbasedir=$BASEDIR   -Dfile.encoding="UTF-8"  -jar $BASEDIR/$projectName >/data/logs/catalina-out.log 2>&1 &
  echo $$ > server.pid
  echo "server start finish"