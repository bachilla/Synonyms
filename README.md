# Synonyms
Semantic analyzer that calculates the cosine similarity of words from varying famous pieces of literature. Check the Readme file for a description of how it works.

Basic steps the program takes to perform analysis:

1) Creates an array (corpus) containing links to the URLs which host text files of the literature. 
   ***note: links are hard coded and may or may not be broken***
2) Accesses a URL containing the piece of literature.
3) Parses out one sentence at a time.
4) Splits the sentence into individual words.
5) Creates a hashmap containing each word (descriptor) in the sentence as a key.
6) Assigns the value of each entry as a hashmap containing words that occur in the same sentence as the descriptor as keys and their frequency of occurence as values. 
    --> This allows us to perform fancy mathematical analysis between any two (or more) descriptors.
7) Updates the hashmap by adding any new descriptors (keys) found and updating their hashmap (values).
8) Repeats steps 2-7 until the enctire corpus has been parsed off.
9) Prompts user for an initial word and choices that should be processed.
10) Calculates and prints the cosine similarity between words.
    --> see the code to know how this works! Uses 'dot products' and 'sum of squares' calculations
    ***note: my code's logic for this calculation may be faulty. May or may not fix at a later time.***
