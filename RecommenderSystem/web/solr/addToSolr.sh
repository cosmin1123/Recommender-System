for id in {3000000..3043700}
do
	/home/cosmin/Downloads/solr-5.3.0/bin/solr -c recomm_core formatedSolrArticles/$id.json	

done
