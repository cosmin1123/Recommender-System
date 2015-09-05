// Create StanfordCoreNLP object properties, with POS tagging
// (required for lemmatization), and lemmatization
Properties props;
props = new Properties();
props.put("annotators", "tokenize, ssplit, pos, lemma");

// StanfordCoreNLP loads a lot of models, so you probably
// only want to do this once per execution
StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
// Create an empty Annotation just with the given text
// and convert the text to lower case.
Annotation document = new Annotation(documentText.toLowerCase());
List<String> lemmas = new LinkedList<String>();
// run all Annotators on this text
pipeline.annotate(document);

// Iterate over all of the sentences found
List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
for (CoreMap sentence : sentences) {
    // Iterate over all the tokens in a sentence
    for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
        // Retrieve and add the lemma for each word into the
        // list of lemmas
        lemmas.add(
        	token.get(CoreAnnotations.LemmaAnnotation.class));
    }
}
return lemmas;