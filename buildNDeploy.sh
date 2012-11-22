#!/bin/bash
# build the calabash-driver
mvn clean package 

#deploy output artefact:
cp calabash-driver-server/target/calabash-driver-server-*.jar ../calabash-grid/
