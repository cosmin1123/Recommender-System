#!/bin/bash

for id in {3000000..3043700}
do
	curl -s www.fastcompany.com/api/v1/posts/$id > articles/$id 
	echo $id
done
