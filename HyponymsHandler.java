package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {

    private WordNet wn;
    private NGramMap ngMap;

    public HyponymsHandler(WordNet wn, NGramMap ngMap) {
        this.wn = wn;
        this.ngMap = ngMap;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        List<String> hyponyms = wn.retrieveHyponyms(words);

        if (k != 0) {
            Map<String, Double> hyponymCounts = new HashMap<>(); // map stores total count per hyponym
            for (String word : hyponyms) {
                TimeSeries ts = ngMap.countHistory(word, startYear, endYear);
                double totalOccurrences = 0;

                for (Map.Entry<Integer, Double> entry : ts.entrySet()) {
                    totalOccurrences += entry.getValue();
                    // entry.getValue() gives the number of occurrences of the word for that year
                }
                if (totalOccurrences > 0) {
                    hyponymCounts.put(word, totalOccurrences);
                }
                // this just stores the word and its respective count in the Map I made
            }

            List<Map.Entry<String, Double>> sortedOnes = new ArrayList<>(hyponymCounts.entrySet());
            Comparator<Map.Entry<String, Double>> compareCounts = new Comparator<>() {
                @Override
                public int compare(Map.Entry<String, Double> first, Map.Entry<String, Double> second) {
                    return Double.compare(second.getValue(), first.getValue());
                }
                // this first comparator compares two diff entries and gets the ones we want.
            };
            sortedOnes.sort(compareCounts);
            // convert Map to an ArrayList so I can sort them descending
            List<Map.Entry<String, Double>> topKEntries = sortedOnes.subList(0, Math.min(k, sortedOnes.size()));
            // for loop gets top k hyponyms, loops until it reaches k
            // now that I have the top k hyponyms from the entries I want I now want to sort alphabetically
            Comparator<Map.Entry<String, Double>> alphabeticalComparator = new Comparator<>() {
                @Override
                public int compare(Map.Entry<String, Double> first, Map.Entry<String, Double> second) {
                    return first.getKey().compareTo(second.getKey());
                }
            };
            topKEntries.sort(alphabeticalComparator);

            // now I want to extract the top k hyponyms that are sorted alphabetically for result that we want
            List<String> finalTopKhyponyms = new ArrayList<>();
            for (Map.Entry<String, Double> entry : topKEntries) {
                finalTopKhyponyms.add(entry.getKey());
            }
            return finalTopKhyponyms.toString();
        } else {
            return hyponyms.toString();
        }
    }
}
