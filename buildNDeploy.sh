#!/bin/bash
# build the calabash-driver
mvn clean package 

#deploy output artefact:
scp calabash-driver-server/target/calabash-driver-server-*.jar euqe@10.250.57.200:/home/share/file/android-driver/calabash-driver-server.jar
cp calabash-driver-server/target/calabash-driver-server-*.jar ../calabash-grid/
