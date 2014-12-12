solrctl --solr 127.0.0.1:8983/solr --zk 127.0.0.1:2181/solr instancedir --update tscorrelationCollection /home/cloudera/tscorrelationSearchConfig
solrctl --solr 127.0.0.1:8983/solr --zk 127.0.0.1:2181/solr collection --reload tscorrelationCollection
