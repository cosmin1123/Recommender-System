curl 'http://localhost:8983/solr/articleCore/select?q=%223000099%22&wt=json&indent=true&mlt=true&mlt.count=50&mlt.fl=author,body,category,publised,keywords,shortTitle,title&mlt.mindf=1&mlt.mintf=1&fl=id,score&mlt.boost=true'
curl 'http://localhost:8983/solr/articleCore/select?q=%223000099%22&wt=json&indent=true&mlt=true&mlt.count=10&mlt.fl=author,body,category,publised,keywords,shortTitle,title&mlt.mindf=1&mlt.mintf=1&mlt.boost=true&fl=id,score,title&mlt.qf=author*30+body*36+category*41+publised*74+keywords*64+shortTitle*89+title*98'