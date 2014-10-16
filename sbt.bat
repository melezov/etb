@echo off
pushd "%~dp0"
java -Dinput.encoding=Cp1252 ^
  -Xmx1g -XX:MaxPermSize=256m ^
  -jar project\strap\gruj_vs_sbt-launch-0.13.x.jar %*
popd 
