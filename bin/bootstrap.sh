cd ..
mvn clean
mvn install:install-file -Durl=file:///home/cloudera/MorphMiner/lib -Dfile=lib/sshxcute-1.0.jar -DgroupId=net.neoremind.sshxcute -DartifactId=sshxcute -Dpackaging=jar -Dversion=1.0
mvn install:install-file -Durl=file:///home/cloudera/MorphMiner/lib -Dfile=lib/json-lib.jar -DgroupId=net.sf.json-lib -DartifactId=json-lib -Dpackaging=jar -Dversion=2.4
mvn compile package assembly:single
cp target/morphminer-0.9.1-SNAPSHOT-jar-with-dependencies.jar bin/mm-latest.jar
java -jar ./bin/mm-latest.jar;./lib/json-lib.jar;./lib/sshxcute-1.0.jar de.bitocean.mm.MorphMinerTool 
cd bin
