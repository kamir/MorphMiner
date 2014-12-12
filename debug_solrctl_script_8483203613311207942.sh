solrctl --solr 127.0.0.1:8983/solr --zk 127.0.0.1:2181/solr instancedir --update corpus3Collection /home/cloudera/corpus3SearchConfig
solrctl --solr 127.0.0.1:8983/solr --zk 127.0.0.1:2181/solr collection --reload corpus3Collection
