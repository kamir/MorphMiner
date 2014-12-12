solrctl --solr 127.0.0.1:8983/solr --zk 127.0.0.1:2181/solr instancedir --update FAQMails02Collection /home/cloudera/FAQMails02SearchConfig
solrctl --solr 127.0.0.1:8983/solr --zk 127.0.0.1:2181/solr collection --reload FAQMails02Collection
