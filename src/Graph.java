package main;

import java.util.*;

// from spec: "One which implements the idea of a directed graph."
public class Graph {
    private class Node {
        private String[] listOfWords;
        private int id;

        public Node(String[] listOfWords, int id) {
            this.id = id;
            this.listOfWords = listOfWords;
        }
    }

    private HashMap<Node, Set<Node>> adjList; // spec: "Given a word (e.g. “change”), what nodes contain that word?"
    private HashMap<Integer, Node> nodeLookupMap; // spec: "Given an integer, what node goes with that index?"
    private HashMap<String, ArrayList<Integer>> wordsInNodeMap; // spec: "Given a node, what words are in that node?"

    public Graph() {
        adjList = new HashMap<>();
        nodeLookupMap = new HashMap<>();
        wordsInNodeMap = new HashMap<>();
    }

    // spec: "Creating a node, e.g. each line of synsets16.txt contains the information for a node."
    public Node creatingANode(String[] words, int synset) {
        Node node = new Node(words, synset);
        nodeLookupMap.put(synset, node);
        for (String word : words) {
            if (!wordsInNodeMap.containsKey(word)) {
                wordsInNodeMap.put(word, new ArrayList<>());
            }
            wordsInNodeMap.get(word).add(synset);
        }
        adjList.putIfAbsent(node, new HashSet<>());
        adjList.get(node).add(node);
        return node;
    }

    // spec: "Adding an edge to a node, e.g. each line of hyponyms16.txt contains one or more edges
    // that should be added to the corresponding node."
    public void addEdge(int parentId, int childId) {
        Node parentNode = nodeLookupMap.get(parentId);
        Node childNode = nodeLookupMap.get(childId);
        adjList.get(parentNode).add(childNode);
    }

    // spec:"Finding reachable vertices, e.g. the vertices reachable from vertex #7 in hyponyms16.txt are 7, 8, 9, 10."
    public Set<Node> findingReachableVerts(int id) {
        Node node = nodeLookupMap.get(id);
        Set<Node> verts = adjList.get(node);
        if (verts.size() == 1) {
            return new HashSet<>(verts);
        }
        Set<Node> childVerts = new HashSet<>();
        childVerts.add(node);
        for (Node oneVertex : verts) {
            if (node != oneVertex) {
                childVerts.addAll(findingReachableVerts(oneVertex.id));
            }
        }
        return childVerts;
    }
    // now with those three core methods (creating node, adding edges, then finding the reachable vertices), I'm going
    // to create a last method in the Graph class that retrieves the hyponyms from a given word
    public Set<String> retrieveHyponyms(String word) {
        if (!wordsInNodeMap.containsKey(word)) {
            return new TreeSet<>(); // if word isn't in the graph then we return an empty treeset (cuz alphabetical)
        }
        ArrayList<Integer> synsets = wordsInNodeMap.get(word);
        Set<String> words = new TreeSet<>();
        for (int id : synsets) {
            Set<Node> children = findingReachableVerts(id);
            if (children == null) {
                continue;
            }
            for (Node child : children) {
                words.addAll(Arrays.asList(child.listOfWords));
            }
        }
        return words;
    }
}
    // variables: what is our graph representation?
    // adjacency list, need to build it (more efficient than adjacency Matrix when you think about resizing)
    //

