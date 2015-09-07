// Load dependency
var solr = require('solr-client');

var client = solr.createClient();

var fs = require('fs');
for (i = 3000000; i < 3005000; i++) { 
	var filename = '/home/cosmin/Documents/Recommender-System/RecommenderSystem/web/solr/articles/' + i; 
	var buf = fs.readFileSync(filename, "utf8");
		
	if(buf.indexOf('{"id"') == 0) {
		console.log(i);	
  		//the json is ok
		var obj = JSON.parse(buf);
		var newObj = new Object();
		newObj.id = obj.id;
		newObj.body = obj.body;
		if(obj.analytics != undefined &&
			obj.analytics.omniture != undefined) {
			newObj.category  = obj.analytics.omniture.channel;
		}
		newObj.published = obj.published;
		newObj.shortTitle = obj.title;
		if(obj.teaser == null) {
			newObj.title = newObj.shortTitle;
		} else {
			newObj.title = obj.teaser.plain;
		}
		newObj.keywords = obj.keywords.socialtags.concat(obj.keywords.tags);;
		newObj.author = obj.author.display_name;
		var tmp = new Array();
		tmp.push(newObj);
		fs.writeFileSync('/home/cosmin/Documents/Recommender-System/RecommenderSystem/web/solr/formatedSolrArticles/' + i + ".json", JSON.stringify(tmp));
	}

}

