var http = require('http');
var fs = require('fs');

var through = require('through');

http.createServer(function (req, res) {
    console.log(req.url.substring(1) + "\n");

   if(req.url.indexOf("relatedarticles") == -1 &&
        req.url.indexOf("getallitems") == -1) {

        var index = fs.readFileSync("index.html");
   	    res.writeHead(200, {'Content-Type': 'text/html'});
    	// change the to 'text/plain' to 'text/html' it will work as your index page
    	res.end(index);
   } else {
    	var departureProcessor = through(function write(requestData){

            //log or modify requestData here
            console.log("=======REQUEST FROM CLIENT TO SERVER CAPTURED========");
            console.log(requestData);

            this.queue(requestData);
        });

        var proxy = http.request({ hostname: "127.0.0.1", port: 8080, path: req.url, method: 'GET'}, function (serverResponse) {

            var arrivalProcessor = through(function write(responseData){

                //log or modify responseData here
                console.log("======RESPONSE FROM SERVER TO CLIENT CAPTURED======");
                console.log(responseData);

                this.queue(responseData);
            });

            serverResponse.pipe(arrivalProcessor);
            arrivalProcessor.pipe(res);
        });

        req.pipe(departureProcessor, {end: true});
        departureProcessor.pipe(proxy, {end: true});
   }
}).listen(8000);
