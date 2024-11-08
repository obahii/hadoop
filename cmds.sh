#!/bin/bash

javac -classpath $(hadoop classpath) -d . CountLinesInHDFS.java

jar cf CountLinesInHDFS.jar *.class

hadoop jar CountLinesInHDFS.jar CountLinesInHDFS
