package main;

import edu.princeton.cs.algs4.In;

import java.io.File;
import java.util.*;


// from spec: "One which reads in the WordNet dataset and constructs an instance of the directed
// graph class. This second class should also be able to take a word and return its hyponyms."
public class WordNet {
    private Graph graph;
    // ^wrapper for a graph
    // build the graph --> add all the edges

    public WordNet(String fileSynsets, String fileHyponyms) {
        graph = new Graph();
        In synsetFileScanner = new In(new File(fileSynsets));
        // spec: "The first field is the synset id (an integer), the second field is the synonym set
        // (or synset), and the third field is its dictionary definition." --> need an array of strings "fields"
        String fileLine;
        while ((fileLine = synsetFileScanner.readLine()) != null) {
            String[] fields = fileLine.split(",");
            int synsetID = Integer.parseInt(fields[0]); // first elem of fileLine
            String[] wordList = fields[1].split("\\s+"); // splits next field WORDS into individ. words w/spaces
            // got that from this link:
            // https://www3.ntu.edu.sg/home/ehchua/programming/howto/Regexe.html#:~:text=The%20%5Cs%20(lowercase%20s%20)
            graph.creatingANode(wordList, synsetID);
        }
        In hyponymFileScanner = new In(new File(fileHyponyms));
        while ((fileLine = hyponymFileScanner.readLine()) != null) {
            String[] fields = fileLine.split(",");
            int parent = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                graph.addEdge(parent, Integer.parseInt(fields[i]));
            }
        }
    }

    public List<String> retrieveHyponyms(String word) { // FOR ONE SINGLE WORD
        Set<String> hyponyms = graph.retrieveHyponyms(word);
        List<String> sortedHyponyms = new ArrayList<>(hyponyms);
        Collections.sort(sortedHyponyms);
        return sortedHyponyms;
    }

    public List<String> retrieveHyponyms(List<String> words) { // FOR A LIST OF WORDS
        if (words == null || words.isEmpty()) {
            return new ArrayList<>();
        }
        // spec: "should only return common hyponyms of each word"
        Set<String> commonHyponyms = graph.retrieveHyponyms(words.get(0));
        for (int i = 1; i < words.size(); i++) {
            commonHyponyms.retainAll(graph.retrieveHyponyms(words.get(i)));
            // ^^^ https://www.geeksforgeeks.org/arraylist-retainall-method-in-java/
        }
        List<String> sortedHyponyms = new ArrayList<>(commonHyponyms);
        Collections.sort(sortedHyponyms);
        return sortedHyponyms;
    }

    // retrieve hyponyms for the set of words returns a set of strings, iterate over all words and
    // fetch their counthistory timeseries, iterate over the time series and fetch the total number of
    // occurrences of that word, rank and return top k words (built-in sort function; might have to create a comparator)
    //
}
