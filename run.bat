@echo off
javac CollegeEventTicketingSystem.java
if errorlevel 1 pause & exit /b 1
java CollegeEventTicketingSystem
pause
