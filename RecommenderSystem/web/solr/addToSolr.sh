for id in {3000000..3043700}
do
	../../../bin/post -c articleCore formatedSolrArticles/$id.json	

done
