#!/bin/bash
cd "$( dirname "$0" )"
exec java -Xmx1g -XX:MaxPermSize=256m \
  -jar project/strap/gruj_vs_sbt-launch-0.13.x.jar "$@"
