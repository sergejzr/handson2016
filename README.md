# handson2016

This repositity contains examples for the Introduction to Information Extraction tutorial Hands On Session.

NER and Temporal Annotations using Stanford NER and SUTime.

Run uk.soton.examples.nlp.NLPParserDemo.java

Pipeline configuration example:

public NLPParserDemo() {

Properties props = new Properties();
props.setProperty("annotators", 
"tokenize, ssplit, pos, lemma, ner, parse, dcoref");
props.setProperty("parse.model", 
"edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
props.setProperty("ssplit.isOneSentence", "false");

pipeline = new StanfordCoreNLP(props);
pipeline.addAnnotator(new TimeAnnotator("sutime", new Properties()));}
