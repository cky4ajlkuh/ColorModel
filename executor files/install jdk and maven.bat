@echo off
setlocal enabledelayedexpansion

mkdir "C:\ColorModel"

xcopy "%~dp0\jdk-17.0.0.1" "C:\ColorModel\jdk-17.0.0.1\" /s /i /y

xcopy "%~dp0\apache-maven-3.9.9" "C:\ColorModel\apache-maven-3.9.9\" /s /i /y

setx JAVA_HOME C:\ColorModel\jdk-17.0.0.1\
setx MAVEN_HOME C:\ColorModel\apache-maven-3.9.9\
setx PATH "%PATH%;C:\ColorModel\jdk-17.0.0.1\bin;C:\ColorModel\apache-maven-3.9.9\bin"
