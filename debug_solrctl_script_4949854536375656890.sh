solrctl --solr 127.0.0.1:8983/solr --zk 127.0.0.1:2181/solr instancedir --update corpus1Collection /home/cloudera/corpus1SearchConfig
solrctl --solr 127.0.0.1:8983/solr --zk 127.0.0.1:2181/solr collection --reload corpus1Collection
