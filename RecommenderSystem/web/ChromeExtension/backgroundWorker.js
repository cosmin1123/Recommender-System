var acceptedSiteList = ["portal-jupiter02.lab.digitalpublishing.adobe.com"]
var sampleRow = '<div class="fixed-table-container-inner"  style="display:none"><table class="dpsContent-contentTable coral-Table table coral-Table--hover ng-isolate-scope" id="articleTable" style="width:100%;text-align: left;"><thead><tr class="coral-Table-row"><th class="check coral-Table-headerCell checkbox"></th><th class="coral-Table-headerCell index"></th><th class="coral-Table-headerCell preview"></th><th class="coral-Table-headerCell title"><div class="th-inner">Title</div></th><th class="coral-Table-headerCell type" ><div class="th-inner">Type</div></th><th class="coral-Table-headerCell modified" ><div class="th-inner">Last Modified</div></th><th class="coral-Table-headerCell move"></th></tr></thead><tbody class="ng-pristine ng-untouched ng-valid ng-isolate-scope ui-sortable"><!-- ngRepeat: item in contents --><tr class="coral-Table-row ng-scope" id="sampleRow" style="display:none"><td class="check coral-Table-cell checkbox"><label class="coral-Checkbox" ng-click="$event.stopPropagation();"><input class="coral-Checkbox-input" onclick="if(window.count == undefined) {window.count = 0;}if(this.checked){window.count++;}else{window.count--;}if(window.count == 0){ $(\'#relatedAddBtn\').prop(\'disabled\', true);}else{$(\'#relatedAddBtn\').prop(\'disabled\', false);}" type="checkbox" data-test-id="collectionContent-table-row0.input.selectItem" ng-disabled="false" ng-click="updateSelection($event.target.checked, item)" ng-checked="isSelected(item)"> <span class="coral-Checkbox-checkmark"></span></label></td><td class="coral-Table-cell index ng-binding"></td><td class="check coral-Table-cell preview" ><span class="image dps-Table-cell-Image"><img class="articleImage" dps-content-fallback-src="integration-268/images/preview-tnail.png" src="https://pecs-jupiter02.lab.digitalpublishing.adobe.ioREPLACEHEREimages/preview.img?api_key=Pb-int-Unity-CD&user_token="></span></td><td class="coral-Table-cell title ng-binding articleTitle" >Inspire Magazine - March 2014</td><td class="coral-Table-cell type ng-binding articleType" > article</td><td class="coral-Table-cell modified ng-binding articleDate" >6/4/2015, 7:12 PM</td><td class="coral-Table-cell published ng-binding" ></td></tr><!-- end ngRepeat: item in contents --></tbody></table></div>'
var currentSite = location.host;

var listenerAdded = false;
var addMade = false;


function runBackCheck() {
    if(!listenerAdded) {
        if (acceptedSiteList.indexOf(currentSite) != -1 && location.hash.indexOf("collection") != -1 &&
              location.hash.indexOf("add") != -1) {
              //Creating Elements

              setTimeout(init, 100);
        }
          $(window).on('hashchange', function(oldUrl) {
              if (acceptedSiteList.indexOf(currentSite) != -1 && location.hash.indexOf("collection") != -1 &&
                  location.hash.indexOf("add") != -1) {
                  //Creating Elements

                  window.urlParams = undefined;
                  setTimeout(init, 100);
              }

              if(addMade) {
                location.reload()
                addMade = false;
              }
        });
        listenerAdded = true;
    }
}

var init = function(e) {
    if(document.readyState == "complete") {
        chrome.runtime.onMessage.addListener(
          function(message, messageSender, sendResponse) {

           var tmp = {};
           tmp.recommendedList = message.recommendedList;
           tmp.titleRecommendedList = message.titleRecommendedList;
           tmp.departmentRecommendedList = message.departmentRecommendedList;
           tmp.keywordRecommendedList = message.keywordRecommendedList;
           tmp.TFIDFRecommendedList = message.TFIDFRecommendedList;
           tmp.collectionList = message.collectionList;

           tmp.accessToken = JSON.parse(message.accessToken);

           if(window.urlParams == undefined || tmp.recommendedList != urlParams.recommendedList ||
              tmp.titleRecommendedList != urlParams.titleRecommendedList ||
              tmp.departmentRecommendedList != urlParams.departmentRecommendedList ||
              tmp.keywordRecommendedList != urlParams.keywordRecommendedList ||
              tmp.TFIDFRecommendedList != urlParams.TFIDFRecommendedList ||
              tmp.collectionList != urlParams.collectionList) {
                   window.urlParams = tmp
                   generateContent();
           }
        });
        targetElement = $(".center-section.dpsCore-actionBar-center");

        if(targetElement.size() != 0) {

			var collectionLocation = location.hash.substring(1, location.hash.indexOf("/add"));

			chrome.runtime.sendMessage({start: "true", localStorage: localStorage, cookie: document.cookie,
			location: collectionLocation},
			 function(response) {
                  targetElement.append(response);
                  $(".dpsCore-actionBar-center.dpsCore-actionBar-title.coral-Heading.coral-Heading--3.ng-binding.ng-scope").remove()
                  $(".fixed-table-container").append(sampleRow);

                  function relateClickHandler(e) {

                    chrome.extension.sendMessage({clicked: "true"}, function(response) {});
                    $('#relatedBtn').prop('disabled', true);
                    $('#allBtn').prop('disabled', false);
                    $(".fixed-table-container-inner").toggle();
                    $(".endor-ActionBar-right.dpsCore-actionBar-right").toggle()
                    $(".fixed-table-container-inner").parent().removeClass( "fixed-table-container" )
                    $("#relatedAddBtn").parent().find(".coral-Button.coral-Button--primary").toggle();
                    $(".fixed-table-container-inner").parent().css('background', "#f1f1f1");
                    $(".fixed-table-container-inner").parent().css('height', "100%")
                  }

                  function allClickHandler(e) {
                    $('#allBtn').prop('disabled',true);
                    $('#relatedBtn').prop('disabled', false);
                    $(".fixed-table-container-inner").toggle();
                    $("#relatedAddBtn").parent().find(".coral-Button.coral-Button--primary").toggle();
                    $(".endor-ActionBar-right.dpsCore-actionBar-right").toggle()

                    var currentScroll = $(".fixed-table-container-inner").scrollTop();
                    if(currentScroll > 1) {
                        currentScroll -= 1;
                    } else {
                        currentScroll += 1;
                    }
                    $(".fixed-table-container-inner").parent().addClass("fixed-table-container" )
                    $(".fixed-table-container-inner").scrollTop(currentScroll);
                  }
                  document.getElementById('relatedBtn').addEventListener('click', relateClickHandler);
                  document.getElementById('allBtn').addEventListener('click', allClickHandler);

			});

            return;
        }
    }
    setTimeout(init, 100);
}

//////////////////////////////////// damn


function createRow(sampleRow, imgSrc, title, type, lastModified, myUrl) {
    newRow = sampleRow.cloneNode(true)

    if(imgSrc != undefined) {
        var src =  newRow.getElementsByClassName("articleImage")[0].src.replace("replacehere", imgSrc)
        src += urlParams.accessToken;
        newRow.getElementsByClassName("articleImage")[0].src = src;
    } else {
        newRow.getElementsByClassName("articleImage")[0].src = "";
    }
    newRow.getElementsByClassName("articleTitle")[0].innerHTML = title;
    newRow.getElementsByClassName("articleDate")[0].innerHTML = lastModified;
    newRow.getElementsByClassName("coral-Table-cell type ng-binding articleType")[0].innerHTML = type;

    newRow.style.cssText = "display:block";

    newRow.dataset.myUrl = myUrl;

    return newRow;
}


function getCheckedBoxes(chkboxName) {
  var checkboxes = document.getElementsByClassName(chkboxName);
  var checkboxesChecked = [];

  for (var i=0; i< checkboxes.length; i++) {

     if (checkboxes[i].checked) {
        checkboxesChecked.push(checkboxes[i].parentNode.parentNode);
     }
  }

  return checkboxesChecked.length > 0 ? checkboxesChecked : null;
}

function fillTable(articleTable, row, text, relatedList, commonType) {
    var relatedList = JSON.parse(relatedList);
    var common = [];
    if(commonType != undefined) {
        for(i in relatedList) {
            if(common.length > 5) {
                break;
            }
            var target = relatedList[i][commonType];
            if(common.indexOf(target) == -1) {
                if(target instanceof Array) {
                    if(common.indexOf(target[0]) == -1) {
                        common.push.apply(common, target);
                    }
                } else {
                    common.push(target);
                }
            }
        }
    }

    text = '<div class="dpsCore-actionBar-center dpsCore-actionBar-title coral-Heading coral-Heading--3" style="display: block;align-self: center;width: 100%;">' + text + '<br><div style="line-height: 20px;font-size: 15px;">'+ common + '</div></div>';


    $(articleTable).append(text)
    var headerRow = createRow(row, undefined, "Title", "Type", "Date Created", "");
    $(headerRow).removeClass( "coral-Table-row ng-scope");

    $(headerRow).find(".coral-Checkbox").css('visibility', "hidden");
    //background: #f1f1f1;
    //border-bottom: .0625rem solid #cbcbcb;
    $(headerRow).css('background', "#f1f1f1");
    $(headerRow).css('border-bottom', ".0625rem solid #cbcbcb");
    $(headerRow).css('border-top', ".0625rem solid #cbcbcb");

    articleTable.appendChild(headerRow);



    for(i in relatedList) {
        if(i >= 5) {
            break;
        }
        articleTable.appendChild(createRow(row, relatedList[i].contentElements, relatedList[i].shortTitle, "article",
        relatedList[i].dateCreated, relatedList[i].myURL));
    }}

var generateContent = function() {
    row = document.getElementById("sampleRow")
    row.parentNode.removeChild(row);

    $("#articleTable").empty();
    var articleTable = document.getElementById("articleTable")

    fillTable(articleTable, row, "Recommended Articles", urlParams.recommendedList);

    fillTable(articleTable, row, "Articles Similar by Title", urlParams.titleRecommendedList);

    fillTable(articleTable, row, "Articles Similar by Department", urlParams.departmentRecommendedList, "department");

    fillTable(articleTable, row, "Articles Similar by Keywords", urlParams.keywordRecommendedList, "keywords");

    fillTable(articleTable, row, "Articles Similar by Content",  window.urlParams.TFIDFRecommendedList);

    $(".dpsCore-fullscreen-modal-header-buttons").append
    ("<button id='relatedAddBtn'  class='coral-Button coral-Button--primary' style=\"display:none\" disabled>Add</button>");
    $("#relatedAddBtn").parent().find(".coral-Button.coral-Button--primary").toggle();

    document.getElementById("relatedAddBtn").addEventListener("click", function(){
        var checkedBoxes = getCheckedBoxes("coral-Checkbox-input");
        var collectionUrlList = JSON.parse(urlParams.collectionList);
        collectionUrlList.shift();

        for(i in checkedBoxes) {
            var bruteUrl = ($(checkedBoxes[i]).parent())[0].dataset.myUrl;
            if(bruteUrl == undefined) {
                continue;
            }
            var articleUrl = bruteUrl.substring(bruteUrl.indexOf("/publication"), bruteUrl.length)
            collectionUrlList.push(articleUrl);
        }

        chrome.extension.sendMessage({add: "true", list: JSON.stringify(collectionUrlList)}, function(response) {});
        window.history.go(-1);
        addMade = true;
    });
}

///////////////////////////////////

setTimeout(runBackCheck, 100);

