package Spring.controllers;

import algorithms.related.CollectionRelatedArticles;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utils.Item;
import utils.LinkedListWrapper;
import utils.SimilarityWeights;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by didii on 6/5/15.
 */
@RestController
public class RelatedCollectionControler {
    @RequestMapping(method = RequestMethod.GET, value = "/relatedCollection")
    public LinkedListWrapper<Item> greeting(@RequestParam(value = "relatedList", defaultValue = "") String relateList,
                                            @RequestParam(value = "maxValue", defaultValue = "10") String maxArticle,
                                            @RequestParam(value = "publicationId", defaultValue = "")
                                            String publicationId,
                                            @RequestParam(value = "dateCreatedWeight", defaultValue = "")
                                            String dateCreatedWeight,
                                            @RequestParam(value = "titleWeight", defaultValue = "")
                                            String titleWeight,
                                            @RequestParam(value = "departmentWeight", defaultValue = "")
                                            String departmentWeight,
                                            @RequestParam(value = "shortTitleWeight", defaultValue = "")
                                            String shortTitleWeight,
                                            @RequestParam(value = "categoryWeight", defaultValue = "")
                                            String categoryWeight,
                                            @RequestParam(value = "ratingsWeight", defaultValue = "")
                                            String ratingsWeight,
                                            @RequestParam(value = "authorWeight", defaultValue = "")
                                            String authorWeight,
                                            @RequestParam(value = "collectionReferenceWeight", defaultValue = "")
                                            String collectionReferenceWeight,
                                            @RequestParam(value = "TFIDFWeight", defaultValue = "")
                                            String TFIDFWeight,
                                            @RequestParam(value = "keywordWeight", defaultValue = "")
                                            String keywordWeight) {

        SimilarityWeights similarityWeights = new SimilarityWeights();
        similarityWeights.setDateCreatedWeight(dateCreatedWeight);
        similarityWeights.setTitleWeight(titleWeight);
        similarityWeights.setDepartmentWeight(departmentWeight);
        similarityWeights.setShortTitleWeight(shortTitleWeight);
        similarityWeights.setCategoryWeight(categoryWeight);
        similarityWeights.setRatingsWeight(ratingsWeight);
        similarityWeights.setAuthorWeight(authorWeight);
        similarityWeights.setKeywordWeight(keywordWeight);
        similarityWeights.setCollectionReferenceWeight(collectionReferenceWeight);
        similarityWeights.setTFIDFWeight(TFIDFWeight);

        LinkedList<String> targetIds = new LinkedList<String>();
        relateList = relateList.replace("\"", "");
        relateList = relateList.substring(1, relateList.length() - 1);
        Collections.addAll(targetIds, relateList.split(","));


        return new LinkedListWrapper<Item>(CollectionRelatedArticles.related(targetIds, publicationId,
                Integer.parseInt(maxArticle), similarityWeights));
    }
}
