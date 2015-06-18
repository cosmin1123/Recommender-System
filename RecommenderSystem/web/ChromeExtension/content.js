var contentUrl = "entity?page=0&pageSize=35000&q=entityType%3D%3Darticle&sortField" +
    "=modified&descending=true";
var currentUrl = "https://pecs-jupiter02.lab.digitalpublishing.adobe.io";
var articleList = new Array();
var currentCollectionListUrl = new Array()
var recommenderServerAddress = "http://127.0.0.1:8080/";
var addArticleEndpoint = "addArticle?article=";
var getRelatedArticles = "relatedCollection?relatedList=";
var getTableExists = "tableexists?tableName=ITEMS";
var relatedCollectionRequestList = ["", "&titleWeight=10000", "&departmentWeight=10000",
                                    "&TFIDFWeight=10000", "&keywordWeight=10000"];
var count = 0;
var resultedCollectionRelatedList = new Array();
var tableExists;
var savedLocalStorage;
var savedCookie;
var savedAccessToken;




//curl 'https://pecs-jupiter02.lab.digitalpublishing.adobe.io/publication/1474aa38-d603-46d8-934f-8c1d15987603/collection/Adobe_Inspire_August_2014_Copy-lZWXW6qcL;version=1433840143314/contentElements'


function createArticleAddRequest(localStorage, cookie, targetUrl, list) {
   var accessToken = JSON.parse(localStorage["adobeid_ims_access_token/DPSPortal1/false/AdobeID,person" +
        ",openid,update_profile.email,update_profile.preferred_languages,update_profile.first_name,update_profile.last" +
        "_name,sao.digital_publishing"]).access_token;
   savedAccessToken = accessToken;
 //'Content-Type: application/json;charset=UTF-8'
    var xhr = new XMLHttpRequest();
    xhr.open("PUT", targetUrl, true);
    xhr.setRequestHeader("X-Dps-Client-Id", "portal-content");
    xhr.setRequestHeader("X-Dps-Client-Session-Id", "e89a92b1-1b9c-cbf1-c506-8898542cf963");
    xhr.setRequestHeader("Authorization", accessToken);
    xhr.setRequestHeader("X-Dps-Api-Key", "Pb-int-Unity-CD");
    xhr.setRequestHeader("X-Dps-Client-Request-Id", "e89a92b1-1b9c-cbf1-c506-8898542cf963");
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

    xhr.onreadystatechange = function() {
          if (xhr.readyState == 4) {

            var resp = JSON.parse(xhr.responseText);


          }
        }
        xhr.send(list);

}

function createXMLHttpRequest(localStorage, cookie, targetUrl) {
    var accessToken = JSON.parse(localStorage["adobeid_ims_access_token/DPSPortal1/false/AdobeID,person" +
        ",openid,update_profile.email,update_profile.preferred_languages,update_profile.first_name,update_profile.last" +
        "_name,sao.digital_publishing"]).access_token;
    savedAccessToken = accessToken;

    var xhr = new XMLHttpRequest();
    xhr.open("GET", targetUrl, true);
    xhr.setRequestHeader("X-Dps-Client-Id", "portal-content");
    xhr.setRequestHeader("X-Dps-Client-Session-Id", "e89a92b1-1b9c-cbf1-c506-8898542cf963");
    xhr.setRequestHeader("Authorization", accessToken);
    xhr.setRequestHeader("X-Dps-Api-Key", "Pb-int-Unity-CD");
    xhr.setRequestHeader("X-Dps-Client-Request-Id", "e89a92b1-1b9c-cbf1-c506-8898542cf963");

    return xhr;
}

function getRelatedArticlesToCollection(articleListIds) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", recommenderServerAddress + getRelatedArticles + JSON.stringify(articleListIds) +
     "&publicationId=" + articleList[0].publicationId + "&maxValue=5" + relatedCollectionRequestList[count], true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            var resp = xhr.responseText;
            var list = JSON.parse(resp).linkedList;
            for(i in list) {
                for(j in articleList) {
                    var id = "";
                    if(articleList[j].entityId != undefined) {
                        id += articleList[j].entityId;
                    }
                    if(articleList[j].articleId != undefined) {
                        id += articleList[j].articleId;
                    }

                    if(list[i].itemId == (id)) {
                        list[i].contentElements = articleList[j]._links.contentUrl.href;
                        list[i].dateCreated = articleList[j].created;
                        list[i].myURL= articleList[j].myURL;
                    }
                }
            }
            count++;
            resultedCollectionRelatedList.push(list);

            if(relatedCollectionRequestList.length == count) {
                  chrome.tabs.query({}, function(tabs) {
                      var message = {
                                        recommendedList: JSON.stringify(resultedCollectionRelatedList[0]),
                                        titleRecommendedList: JSON.stringify(resultedCollectionRelatedList[1]),
                                        departmentRecommendedList: JSON.stringify(resultedCollectionRelatedList[2]),
                                        TFIDFRecommendedList: JSON.stringify(resultedCollectionRelatedList[3]),
                                        keywordRecommendedList: JSON.stringify(resultedCollectionRelatedList[4]),
                                        collectionList: JSON.stringify(currentCollectionListUrl),
                                        accessToken: JSON.stringify(savedAccessToken)};
                      var activeIndex;
                      for (var i=0; i<tabs.length; ++i) {
                          if(tabs[i].active) {
                            activeIndex = i;
                            break;
                          }
                      }
                      chrome.tabs.sendMessage(tabs[activeIndex].id, message);
                  });

                  count = 0;
              } else {
                getRelatedArticlesToCollection(articleListIds);
              }
        }
     }
     xhr.send();
}

function getArticleTextAndSend(article, upperXhr) {
    var folioLink = article._links.articleFolio.href;
    folioLink = folioLink.substring(folioLink.indexOf("folio"));
    var xhr = createXMLHttpRequest(savedLocalStorage, savedCookie, currentUrl + article._links.contentUrl.href  +
     "folio/StackResources/asset_L.pdf");
    xhr.setRequestHeader("Content-Type", "application/pdf");
    xhr.responseType = 'arraybuffer';

    xhr.onreadystatechange = function() {

        if (xhr.readyState == 4) {
             var uInt8Array = new Uint8Array(this.response);
             var i = uInt8Array.length;

            upperXhr.send(uInt8Array);

        }
    }

    xhr.send();
}
function sendRequestToRecomm(article) {
    var xhr = new XMLHttpRequest();
    xhr.open("PUT", recommenderServerAddress + addArticleEndpoint + JSON.stringify(article) , true);
    xhr.setRequestHeader("Content-Type", "application/pdf");

    xhr.onreadystatechange = function() {

        if (xhr.readyState == 4) {
            var resp = JSON.parse(xhr.responseText);
            if(resp instanceof Array) {
                for(i in resp) {
                    getAllArticleRequest(localStorage, cookie, currentUrl + resp[i].href);
                }
            } else {

                    if(resp.entityType == "article" && !resp.isAd && resp.department != "Cover" &&
                     resp.department != "Survey" && resp.department != undefined && resp.accessState == "free") {
                        resp.myURL = targetUrl;
                        articleList.push(resp);
                        sendArticleToRecommender(resp);
                        //makeCollectionRequest(localStorage, cookie, targetUrl + "/" + resp._links.articleFolio.href)
                    }

            }
        }
     }

    if(article != undefined && article.entityType == "article") {
        getArticleTextAndSend(article, xhr)
    } else {
        xhr.send();
    }
}
function sendArticleToRecommender(article) {

    if(tableExists == undefined) {
        var tableXhr = new XMLHttpRequest();
        tableXhr.open("GET", recommenderServerAddress + getTableExists + articleList[0].publicationId, false);

        tableXhr.send();
        tableExists = (tableXhr.responseText == "true");
    }

    if(!tableExists) {
        sendRequestToRecomm(article)
    }

}

function getAllArticleRequest(localStorage, cookie, targetUrl) {

    var xhr = createXMLHttpRequest(localStorage, cookie, targetUrl);

    xhr.onreadystatechange = function() {
      if (xhr.readyState == 4) {
        var resp = JSON.parse(xhr.responseText);

        if(resp instanceof Array) {
            for(i in resp) {
                getAllArticleRequest(localStorage, cookie, currentUrl + resp[i].href);
            }
        } else {

                if(resp.entityType == "article" && !resp.isAd && resp.department != "Cover" &&
                resp.department != "Survey" && resp.department != undefined && resp.accessState == "free") {
                    resp.myURL = targetUrl;
                    articleList.push(resp);

                    sendArticleToRecommender(resp);
                    //makeCollectionRequest(localStorage, cookie, targetUrl + "/" + resp._links.articleFolio.href)
                }

            }

      }
    }
    xhr.send();
}

function makeCollectionRequest(localStorage, cookie, targetUrl) {
    var xhr = createXMLHttpRequest(localStorage, cookie, targetUrl);

    xhr.onreadystatechange = function() {
      if (xhr.readyState == 4) {
        var resp = JSON.parse(xhr.responseText);


        if(resp instanceof Array) {
            for(i in resp) {
                var currentArticle = resp[i];
                currentCollectionListUrl.push(currentArticle.href);
            }

        } else {
            currentCollectionListUrl.push(resp);
            makeCollectionRequest(localStorage, cookie, currentUrl + resp._links.contentElements.href)
        }

      }
    }
    xhr.send();
}



chrome.runtime.onMessage.addListener(
  function(request, sender, sendResponse) {

    if (request.start == "true") {
        var base = request.location;
        base = base.substring(0, base.indexOf("/collection"));
        base += "/";

        count = 0;
        resultedCollectionRelatedList = new Array();

        sendResponse("<button id='allBtn' disabled class='coral-Button coral-Button--primary'>Show all</button>" +
            "<button id='relatedBtn' class='coral-Button coral-Button--primary'>Show related</button>");
        if(articleList.length == 0) {
            getAllArticleRequest(request.localStorage, request.cookie, currentUrl + base + contentUrl);
        } else {
            currentCollectionListUrl = new Array();
        }
        savedLocalStorage = request.localStorage;
        savedCookie = request.cookie;
        // get all the articles
        makeCollectionRequest(request.localStorage, request.cookie, currentUrl + request.location);
        resultedCollectionRelatedList = new Array();
      }

      if(request.clicked == "true") {


          sendRequestToRecomm(currentCollectionListUrl[0]);
          var relatedArticleIds = new Array();

            var res = "";
            if(currentCollectionListUrl[0].entityId != undefined) {
                res += currentCollectionListUrl[0].entityId;
            }

            if(currentCollectionListUrl[0].articleId != undefined) {
                res += currentCollectionListUrl[0].articleId;
            }

            if(res.length != 0) {
                relatedArticleIds.push(res);
            }
          for(i in articleList) {
            for(j in currentCollectionListUrl) {
                if(j == 0) {
                    continue;
                }
                if(articleList[i].myURL.indexOf(currentCollectionListUrl[j]) != -1) {
                    var res = "";
                    if(articleList[i].entityId != undefined) {
                        res += articleList[i].entityId;
                    }

                    if(articleList[i].articleId != undefined) {
                        res += articleList[i].articleId;
                    }

                    if(res.length != 0) {
                        relatedArticleIds.push(res);
                    }
                }
            }
          }

          getRelatedArticlesToCollection(relatedArticleIds);


          }

          if(request.add == "true") {

            collectionList = JSON.parse(request.list);
            var listToSend = new Array();
            for(var i in collectionList) {
                var obj = new Object;
                obj.href = collectionList[i];
                listToSend.push(obj);
            }


            createArticleAddRequest(savedLocalStorage, savedCookie, currentUrl +
            currentCollectionListUrl[0]._links.contentElements.href, JSON.stringify(listToSend));
            current = 0;

          }

      return true;
  });