@echo off

for %%? in ("%~dp0.") do set PROJECT=%%~n?

call "%~dp0\..\sbt.bat" "project %PROJECT%" %*
