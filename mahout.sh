#!/bin/bash -x

set -e

apt-get install subversion maven2

# http://akbarahmed.com/2012/06/26/install-cloudera-cdh4-with-yarn-mrv2-in-pseudo-mode-on-ubuntu-12-04-lts/

# vi /etc/environment
#    JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
#    PATH=...
sudo env | grep JAVA_HOME

mkdir -p ~/Downloads/cloudera
cd ~/Downloads/cloudera/


# install mahout

cd /usr/lib/
svn co ...  mahout
cd mahout/core
mvn install -DskipTests=true
cd ../examples
mvn install

# run mahout

# export CLASSPATH=

export RUNDIR=run1

mahout lucene.vector --dir /var/eddgy/data/assetindex \
                     --output ${RUNDIR}/vectors \
                     --field content \
                     --dictOut ${RUNDIR}/dictionary

mahout kmeans --input ${RUNDIR}/vectors \
              -k 100 --maxIter 20 \
              --output ${RUNDIR}/output-kmeans \
              --clusters ${RUNDIR}/output-clusters

mahout clusterdump --seqFileDir ${RUNDIR}/output-kmeans/clusters-3 \
                   --dictionary ${RUNDIR}/dictionary \
                   --dictionaryType text \
                   --substring 64
