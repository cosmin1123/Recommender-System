package utils;

/**
 * Created by didii on 6/10/15.
 */
public class SimilarityWeights {

    private static final double MAX_RATING = 1000000;
    private static final double bestDateCreatedWeight = 73.4375;
    ;
    private static final double bestTitleWeight = 98.4375;
    private static final double bestShortTitleWeight = 89.0625;
    private static final double bestDepartmentWeight = 4.6875;
    private static final double bestCategoryWeight = 40.625;
    private static final double bestImportanceWeight = 3; // not tested yet
    private static final double bestLanguageWeight = 0;
    private static final double bestAuthorWeight = 29.6875;

    private static final double bestKeywordWeight = 64.0625;
    private static final double bestRatingsWeight = 1.5625;
    private static final double bestCollectionReferenceWeight = 0.0;
    private static final double bestTFIDFWeight = 35.9375;


    private double dateCreatedWeight = 73.4375;
    ;
    private double titleWeight = 98.4375;
    private double shortTitleWeight = 89.0625;
    private double departmentWeight = 4.6875;
    private double categoryWeight = 40.625;
    private double importanceWeight = 3;
    private double languageWeight = 0;
    private double authorWeight = 29.6875;

    private double keywordWeight = 64.0625;
    private double ratingsWeight = 1.5625;
    private double collectionReferenceWeight = 0.0;
    private double TFIDFWeight = 35.9375;

    private Double upperBoundKeywords = null;
    private Double upperBoundCollectionRef = null;

    public double getMaxRating() {
        return MAX_RATING;
    }

    public Double getUpperBoundCollectionRef() {
        return upperBoundCollectionRef;
    }

    public void setUpperBoundCollectionRef(Double val) {
        this.upperBoundCollectionRef = val;
    }

    public Double getUpperBoundKeywords() {
        return upperBoundKeywords;
    }

    public void setUpperBoundKeywords(Double val) {
        this.upperBoundKeywords = val;
    }

    public double getTFIDFWeight() {
        return TFIDFWeight;
    }

    public void setTFIDFWeight(String val) {
        if (val == null || val.length() == 0) {
            this.TFIDFWeight = bestTFIDFWeight;
        } else {
            this.TFIDFWeight = Double.parseDouble(val);
        }
    }

    public double getCollectionReferenceWeight() {
        return collectionReferenceWeight;
    }

    public void setCollectionReferenceWeight(String val) {
        if (val == null || val.length() == 0) {
            this.collectionReferenceWeight = bestCollectionReferenceWeight;
        } else {
            this.collectionReferenceWeight = Double.parseDouble(val);
        }
    }

    public double getRatingsWeight() {
        return ratingsWeight;
    }

    public void setRatingsWeight(String val) {
        if (val == null || val.length() == 0) {
            this.ratingsWeight = bestRatingsWeight;
        } else {
            this.ratingsWeight = Double.parseDouble(val);
        }
    }

    public double getKeywordWeight() {
        return keywordWeight;
    }

    public void setKeywordWeight(String val) {
        if (val == null || val.length() == 0) {
            this.keywordWeight = bestKeywordWeight;
        } else {
            this.keywordWeight = Double.parseDouble(val);
        }
    }

    public double getAuthorWeight() {
        return authorWeight;
    }

    public void setAuthorWeight(String val) {
        if (val == null || val.length() == 0) {
            this.authorWeight = bestAuthorWeight;
        } else {
            this.authorWeight = Double.parseDouble(val);
        }
    }

    public double getLanguageWeight() {
        return languageWeight;
    }

    public void setLanguageWeight(String val) {
        if (val == null || val.length() == 0) {
            this.languageWeight = bestLanguageWeight;
        } else {
            this.languageWeight = Double.parseDouble(val);
        }
    }

    public double getImportanceWeight() {
        return importanceWeight;
    }

    public void setImportanceWeight(String val) {
        if (val == null || val.length() == 0) {
            this.importanceWeight = bestImportanceWeight;
        } else {
            this.importanceWeight = Double.parseDouble(val);
        }
    }

    public double getCategoryWeight() {
        return categoryWeight;
    }

    public void setCategoryWeight(String val) {
        if (val == null || val.length() == 0) {
            this.categoryWeight = bestCategoryWeight;
        } else {
            this.categoryWeight = Double.parseDouble(val);
        }
    }

    public double getDepartmentWeight() {
        return departmentWeight;
    }

    public void setDepartmentWeight(String val) {
        if (val == null || val.length() == 0) {
            this.departmentWeight = bestDepartmentWeight;
        } else {
            this.departmentWeight = Double.parseDouble(val);
        }
    }

    public double getShortTitleWeight() {
        return shortTitleWeight;
    }

    public void setShortTitleWeight(String val) {
        if (val == null || val.length() == 0) {
            this.shortTitleWeight = bestShortTitleWeight;
        } else {
            this.shortTitleWeight = Double.parseDouble(val);
        }
    }

    public double getTitleWeight() {
        return titleWeight;
    }

    public void setTitleWeight(String val) {
        if (val == null || val.length() == 0) {
            this.titleWeight = bestTitleWeight;
        } else {
            this.titleWeight = Double.parseDouble(val);
        }
    }

    public double getDateCreatedWeight() {
        return dateCreatedWeight;
    }

    public void setDateCreatedWeight(String val) {
        if (val == null || val.length() == 0) {
            this.dateCreatedWeight = bestDateCreatedWeight;
        } else {
            this.dateCreatedWeight = Double.parseDouble(val);
        }
    }


}
