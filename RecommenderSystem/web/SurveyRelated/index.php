<!DOCTYPE html>
<html>
<head>
    <link type="text/css" href="jquery-ui.css" rel="stylesheet"> </link>
    <script src="jquery.js"></script>
    <script src="jquery-ui.js"></script>
    <style>
        body {
        margin: 0 !important;
        padding: 0 !important;
        overflow: hidden;
        background-color: #cfcfcf;
        background-image: -moz-linear-gradient(top, #b3b3b3, #fff);
        background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#b3b3b3), to(#fff));
        background-image: -webkit-linear-gradient(top, #b3b3b3, #fff);
        background-image: -o-linear-gradient(top, #b3b3b3, #fff);
        background-image: linear-gradient(to bottom, #b3b3b3,#ffffff);
        background-repeat: repeat-x;
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#FFB0B0B0', endColorstr='#FFFCFCFC', GradientType=0);
        }

        #downBox0 {
        width: 70%;
        left: 15%;
        height: 50%;
        background: #ccc;
        position: absolute;
        top: 50%;
        }
        #upBox0 {
        width: 70%;
        left: 15%;
        height: 50%;
        background: #caa;
        position: absolute;
        }

        #downBox1 {
        width: 70%;
        left: 15%;
        height: 50%;
        background: #000;
        position: absolute;
        top: 50%;
        }
        #upBox1 {
        width: 70%;
        left: 15%;
        height: 50%;
        background: #fff;
        position: absolute;
        }

        #downBox0Iframe {
        background: #fff;
        font-family: "MuseoSans",Helvetica,Arial,sans-serif;
        font-weight: 300;
        font-style: normal;
        font-size: 14px;
        line-height: 1;
        color: #222;
        position: relative;
        -webkit-font-smoothing: antialiased;
        }
        #upBox0Iframe {
        background: #fff;
        font-family: "MuseoSans",Helvetica,Arial,sans-serif;
        font-weight: 300;
        font-style: normal;
        font-size: 14px;
        line-height: 1;
        color: #222;
        position: relative;
        -webkit-font-smoothing: antialiased;
        }

        #downBox1Iframe {
        background: #fff;
        font-family: "MuseoSans",Helvetica,Arial,sans-serif;
        font-weight: 300;
        font-style: normal;
        font-size: 14px;
        line-height: 1;
        color: #222;
        position: relative;
        -webkit-font-smoothing: antialiased;
        }
        #upBox1Iframe {
        background: #fff;
        font-family: "MuseoSans",Helvetica,Arial,sans-serif;
        font-weight: 300;
        font-style: normal;
        font-size: 14px;
        line-height: 1;
        color: #222;
        position: relative;
        -webkit-font-smoothing: antialiased;
        }

        #leftBox {
        position: absolute;
        left: 0px;
        width: 14%;
        margin: 1%;
        height: 100%;
        }

        #rightBox {
        position: absolute;
        left: 85%;
        width: 14%;
        margin: 1%;
        height: 100%;
        }

        button {
        border: 1px solid #ddd;
        background-color: #f0f0f0;
        padding: 9% 9%;

        -o-transition: background-color .2s ease-in;
        -moz-transition: background-color .2s ease-in;
        -webkit-transition: background-color .2s ease-in;
        transition: background-color .2s ease-in;
        }

        button:hover {
        background-color: #e5e5e5;
        }

        button:active {
        background-color: #ccc;
        }


    </style>
</head>
<body>
    <div id="upBox0"><iframe id="upBox0Iframe" width="100%" height="100%" >&lt;p&gt;Your browser does not support iframes.&lt;/p&gt;</iframe></div>
    <div id="upBox1"><iframe id="upBox1Iframe" width="100%" height="100%" >&lt;p&gt;Your browser does not support iframes.&lt;/p&gt;</iframe></div>
    <div id="downBox0"><iframe id="downBox0Iframe" width="100%" height="100%" >&lt;p&gt;Your browser does not support iframes.&lt;/p&gt;</iframe></div>
    <div id="downBox1"><iframe id="downBox1Iframe" width="100%" height="100%" >&lt;p&gt;Your browser does not support iframes.&lt;/p&gt;</iframe></div>
    <div id="leftBox">Hi, I just want to start by saying thank you very much for accessing this link and
        I hope that you are going to go through with this and enter my hall of fame.
        <br>
        <br>
        I need this for my diploma project, an article recommendation system and I need your help to test it.
        <br>
        <br>
        What you have to do?
        <br>
        <br>
        Read both of the articles and press on the keyboard/click on the screen:
        <ul >
            <li>1 - if the articles are not related </li>
            <li>2 - if the articles are a little bit related</li>
            <li>3 - if the articles are somehow related</li>
            <li>4 - if the articles are really related</li>
        </ul>

    </div>
    <div id="rightBox">
        How related are the items?
        <br>
        <button onclick="handleChange(49)">1</button>
        <button onclick="handleChange(50)">2</button>
        <button onclick="handleChange(51)">3</button>
        <button onclick="handleChange(52)">4</button>
        <br>
        <br>
        Special thank to:
        <br>
                            <?php
                            $file = file_get_contents('./hallOfFame', true);
                            echo $file;
                            ?>
    </div>

<script>
var base = 49;
var count = 0;
var countArticle = 0;
var recommendedArticleList = [];
var loadedArticleList = [];
var loadedArticleRow = 0;
var loadedArticleColumn = 0;


var lastPress = 0;
var userArticleRow = 0;
var userArticleColumn = 0;

var targetDocument = "proxy.php?url=http://www.fastcompany.com/api/v1/posts/";
var saveData = "saveToFile.php/?file=saveData&data=";
var saveHallOfFame = "saveToFile.php/?file=hallOfFame&data=";
var hallOfFame = "hallOfFame";

function httpGet(theUrl, async, func)
{
    if(async == undefined) {
        async = false;
    }
    var xmlHttp = null;

    if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlHttp=new XMLHttpRequest();
    }
    else {// code for IE6, IE5
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    if(async == true) {
        xmlHttp.onloadend = func;
    } else {
        async = false;
    }

    xmlHttp.open( "GET", theUrl, async );
    xmlHttp.send();

    if(async != true) {
        return xmlHttp.responseText;
    }
}

function httpGetJSON(theUrl)
{
    return JSON.parse(httpGet(theUrl));
}

function setIframe( iframe, article) {

        if(loadedArticleList[userArticleRow] != undefined &&
            userArticleColumn >= loadedArticleList[userArticleRow].length) {

            userArticleColumn = 0;
            userArticleRow++;
            if(userArticleRow >= loadedArticleList.length) {
                handleChange(0);
                return;
            }

        }

        var iFrameDoc = iframe.contentDocument || iframe[0].contentWindow.document;

        if(article == undefined && loadedArticleList[userArticleRow] != undefined &&
            loadedArticleList[userArticleRow][userArticleColumn] != undefined) {

             article = "<h1>" + loadedArticleList[userArticleRow][userArticleColumn].title + "</h1>";
             article += "<h2>" + loadedArticleList[userArticleRow][userArticleColumn].teaser.html + "</h2>";
             article += loadedArticleList[userArticleRow][userArticleColumn].body;

        }

        iFrameDoc.write(article);
        iFrameDoc.close();
        $(iframe).contents().keydown(keyListenerFunction);
        userArticleColumn++;
}
var endTheSuffering = function(name) {
    var first = $("#upBox1Iframe").contents().find("#yoName").val();
    var second = $("#upBox0Iframe").contents().find("#yoName").val();
    var name;
    if(first.length != 0) {
        name = first;
    } else {
        if(second.length != 0) {
            name = second;
        }
    }
    httpGet(saveHallOfFame + name + ", ", true, undefined);
    setIframe($("#upBox0Iframe"),'Thank you for everything, you have been added to the hall of fame!');
    setIframe($("#upBox1Iframe"),'Thank you for everything, you have been added to the hall of fame!');
}
var handleChange = function(key) {
        var d = new Date();
        var n = d.getTime();


        if(userArticleRow >= loadedArticleList.length) {
            // It is done
             setTimeout(
                function(){
                 setIframe($("#upBox0Iframe"),
                 'Thank you very much! <br> You may write your name below if you want to be in the hall of fame<br>' +
                 '<br><input id="yoName" ><button onclick="parent.endTheSuffering(this);">Submit<button>');
                 }, 300);
             setTimeout(
                function(){
                 setIframe($("#upBox1Iframe"),
                  'Thank you very much! <br> You may write your name below if you want to be in the hall of fame<br>'+
                  '<br><input id="yoName"><button onclick="parent.endTheSuffering(this);">Submit<button>');
                 }, 300);

            return;
        }

        if((n - lastPress) < 500) {
            return;
        }

        lastPress = n;

    if(key >= base && key <= (base + 3)) {
                userArticleRow + " " + (userArticleColumn - 1) + " " + (key - base)

                httpGet(saveData + recommendedArticleList[userArticleRow][0]
                + " " + recommendedArticleList[userArticleRow][userArticleColumn - 1] + " " +
                (key - base), true, undefined);

                if((count % 2) == 0) {
                    $("#downBox0").hide("slide", { direction: "right" }, 250);
                    $("#downBox1").show("slide", { direction: "left" }, 250);

                    setTimeout(function(){ setIframe( $("#downBox1Iframe")); }, 300);
                    //

                } else {
                    $("#downBox1").hide("slide", { direction: "right" }, 250);
                    $("#downBox0").show("slide", { direction: "left" }, 250);
                    setTimeout(function(){ setIframe($("#downBox0Iframe")); }, 300);
                }

                if(userArticleColumn >= loadedArticleList[userArticleRow].length) {
                    if(countArticle == 0) {
                       $("#upBox0").hide("slide", { direction: "right" }, 250);
                        $("#upBox1").show("slide", { direction: "left" }, 250);
                        setTimeout(function(){ setIframe($("#upBox1Iframe")); }, 300);
                    } else {
                        $("#upBox1").hide("slide", { direction: "right" }, 250);
                        $("#upBox0").show("slide", { direction: "left" }, 250);
                        setTimeout(function(){ setIframe($("#upBox0Iframe")); }, 300);

                    }
                    countArticle = (countArticle + 1) % 2;
                    count = count % (nextArticle);
                }
                count = count + 1;
            }
}

var keyListenerFunction = function(e) {

       var key = e.keyCode ? e.keyCode : e.which;

       handleChange(key);

    }


$( document ).ready(function() {

    $("#upBox1").hide("slide", { direction: "left" }, 1);
    $("#downBox1").hide("slide", { direction: "left" }, 1);

    $(document).keydown(keyListenerFunction);
//    document.getElementById('upBox0Iframe').onload = function() {$("#upBox0Iframe").contents().keydown(keyListenerFunction);};
  //  document.getElementById('upBox1Iframe').onload = function() {$("#upBox1Iframe").contents().keydown(keyListenerFunction);};
  //  document.getElementById('downBox0Iframe').onload = function() {$("#downBox0Iframe").contents().keydown(keyListenerFunction);};
   // document.getElementById('downBox1Iframe').onload = function() {};

    setTimeout(function() {
        var bruteArticleList = httpGet("articleList");

        var splitArticle = bruteArticleList.split("\n\n");
        var count = 0;
        for(i in splitArticle) {
            var idList = splitArticle[i].split("\n");
            recommendedArticleList[count++] = idList;
        }



         var f = function() {
                    if(loadedArticleRow == 0 && loadedArticleColumn == 2) {
                        setIframe($("#upBox0Iframe"));
                        setIframe($("#downBox0Iframe"));
                    }

                    if(this.responseText.length != 0) {
                        if(loadedArticleList[loadedArticleRow] == undefined) {
                            loadedArticleList[loadedArticleRow] = [];
                        }
                        var response = this.responseText;

                        loadedArticleList[loadedArticleRow][loadedArticleColumn] = JSON.parse(response);
                    }
                    if(loadedArticleRow < recommendedArticleList.length) {
                        loadedArticleColumn++;
                        if(loadedArticleColumn >= recommendedArticleList[loadedArticleRow].length) {
                            loadedArticleRow++;
                            loadedArticleColumn = 0;
                        }

                    }

                    if(loadedArticleRow >= recommendedArticleList.length) {
                        return;
                    }

                    httpGet(targetDocument + recommendedArticleList[loadedArticleRow][loadedArticleColumn], true, f);





                }

                httpGet(targetDocument + recommendedArticleList[loadedArticleRow][loadedArticleColumn], true, f);

    }, 0);

});
</script>

</body>
</html>

