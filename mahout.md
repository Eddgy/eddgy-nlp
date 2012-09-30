
Mahout k-means
==============

Use alestic lucid 10.0?

```bash
apt-get install emacs lynx
apt-get install openjdk-6-jdk
# set JAVA_HOME in /etc/environment
```

Install CDH3
------------

Some useful aliases:

```bash
alias shmkdir='sudo -u hdfs hadoop fs -mkdir'
alias shchown='sudo -u hdfs hadoop fs -chown'
alias shchmod='sudo -u hdfs hadoop fs -chmod'
alias shls='sudo -u hdfs hadoop fs -ls'
alias hls='hadoop fs -ls'
alias hmkdir='hadoop fs -mkdir'
alias hcat='hadoop fs -cat'
```

```bash
emacs /etc/apt/sources.list.d/cloudera.list
curl -s http://archive.cloudera.com/debian/archive.key | sudo apt-key add -
apt-get update
apt-get install hadoop-0.20 hadoop-0.20-native
apt-get install hadoop-0.20-namenode hadoop-0.20-datanode hadoop-0.20-jobtracker hadoop-0.20-tasktracker
```

Make sure /etc/hosts and /etc/nodename are correct.

Edit the following files in /etc/hadoop-0.20/conf/

  core-site.xml
  hdfs-site.xml
  mapred-site.xml

```bash
chmod 777 /var/log/hadoop-0.20
chmod 777 /var/run/hadoop-0.20
chmod -R 777 /usr/lib/hadoop-0.20/logs
chmod -R 777 /usr/lib/hadoop-0.20/pids

useradd hadoop -g hadoop
mkdir /home/hadoop
chown hadoop:hadoop /home/hadoop
vi /etc/passwd # change home to /home/hadoop and shell to bash
```

```bash
sudo su - hadoop

ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
ssh localhost

hadoop namenode -format
/usr/lib/hadoop-0.20/bin/start-all.sh 

# namenode:
lynx http://localhost:50070

# job tracker:
lynx http://localhost:50030
```

test hadoop

```bash
cd /usr/lib/hadoop-0.20
hadoop fs -put conf input
hadoop jar hadoop-examples-*.jar grep input output 'dfs[a-z.]+'
hadoop fs -get output /tmp/
cat /tmp/output/*
```

Data
----

```bash
apt-get install git-core
apt-get install -y puppet libapt-pkg-dev fakeroot lintian make libwww-perl
apt-get install -y build-essential libfuse-dev fuse-utils libcurl4-openssl-dev libxml2-dev

git config --global user.name `hostname`
git config --global user.email ops@eddgy.com
# from another production machine, copy id_rsa.pub and id_rsa in ~root/.ssh/

mkdir -p /var/eddgy/git-clones
cd /var/eddgy/git-clones
git clone git@github.com:Eddgy/eddgy-devops.git

# broken on lucid:
#cd eddgy-devops/packagers
#./eddgy-s3fs.sh
#dpkg -i eddgy-s3fs-1.61.deb

s3cmd --configure
mkdir -p /var/eddgy/data/lucene
cd /var/eddgy/data/lucene
s3cmd get s3://release.eddgy.com/lucene/courseindex.15.tar.gz courseindex.15.tar.gz
s3cmd get s3://release.eddgy.com/lucene/assetindex.15.tar.gz assetindex.15.tar.gz
s3cmd get s3://release.eddgy.com/lucene/clipindex.15.tar.gz clipindex.15.tar.gz
tar xvfz courseindex.*.tar.gz
tar xvfz assetindex.*.tar.gz
tar xvfz clipindex.*.tar.gz
```

Mahout
------

```bash
apt-get install mahout
```

Put our asset index version 15 in /var/eddgy/data

```bash
mahout lucene.vector --dir /var/eddgy/data/lucene/assetindex --output assetvectors --field termBucket --dictOut assetdictionary
mahout kmeans  --input assetvectors  -k 50 --maxIter 20 --output kmeans --clusters assetclusters
mahout clusterdump --seqFileDir kmeans/clusters-3 --dictionary assetdictionary --dictionaryType text --substring 128
```

References
----------

https://ccp.cloudera.com/display/CDHDOC/CDH3+Installation
https://ccp.cloudera.com/display/CDHDOC/Mahout+Installation
http://archive.cloudera.com/cdh/3/hadoop/single_node_setup.html
http://hadoop.apache.org/docs/r1.0.3/cluster_setup.html
http://androidyou.blogspot.com/2011/11/mahout-and-hadoop-are-all-java.html
