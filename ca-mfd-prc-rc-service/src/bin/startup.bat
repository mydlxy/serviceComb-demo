@echo off
rem ======================================================================
rem windows startup script
rem
rem author: geekidea
rem date: 2018-12-2
rem ======================================================================



rem startup jar
java -jar ../boot/@project.build.finalName@.jar --spring.config.location=../config/

pause
