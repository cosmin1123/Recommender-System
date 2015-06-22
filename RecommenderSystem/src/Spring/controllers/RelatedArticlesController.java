package Spring.controllers;

import database.Database;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utils.Item;
import utils.LinkedListWrapper;
import utils.SimilarityWeights;

@RestController
public class RelatedArticlesController {

    @RequestMapping(method = RequestMethod.GET, value = "/relatedarticles")
    public LinkedListWrapper<Item> greeting(@RequestParam(value = "itemId", defaultValue = "World") String itemId,
                                            @RequestParam(value = "maxArticle", defaultValue = "1000") String maxArticle,
                                            @RequestParam(value = "useCollaborativeFiltering", defaultValue = "false")
                                            boolean useCollaborativeFiltering,
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

        return Database.getRelatedArticles(itemId, Integer.parseInt(maxArticle),
                publicationId, useCollaborativeFiltering, similarityWeights);
    }
}
