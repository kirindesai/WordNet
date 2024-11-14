WordNet Hyponym Finder

This project implements a WordNet-based hyponym finder that identifies the hyponyms (more specific terms) of given words or sets of words. Leveraging a graph data structure, it processes and organizes synsets and hyponym relationships, allowing for efficient retrieval and filtering of hyponyms by frequency over specific time periods.

Features I Implemented:

Graph-based Synset and Hyponym Storage: Builds a directed graph of WordNet synsets and hyponyms from input files, allowing for fast traversal and querying.

Hyponym Retrieval:
For a single word or a list of words, retrieves common hyponyms, providing refined results.
Optionally filters top hyponyms based on occurrence frequency over a specified time range, leveraging a time-series dataset.
Frequency Filtering: Supports filtering of top k hyponyms by their historical frequency between specified years.
