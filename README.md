MorphMiner
==========

MorphMiner is in its core a Morphline development tool. But it can also be seen as a data collection tool for scientists.It allows data ingestion into Hadoop clusters.

Prepare the stage ...
=====================

mvn deploy:deploy-file -Durl=file:/// $$$ YOUR PROJECT PATH $$$/lib -Dfile=sshxcute-1.0.jar -DgroupId=com.automatethebox -DartifactId=automatethebox -Dpackaging=jar -Dversion=1.0