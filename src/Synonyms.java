import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Synonyms Class.
 * Contains a constructor, getters and setters for the two class-level variables.
 * Also contains methods that create the object's descriptor map, and calculate the cosine similarity of two words.
 * */
public class Synonyms {

    /**
     * @param descriptorMap contains a String as a key whose value is a hashmap of words that occur in the
     *                      same sentence as well as how frequently
     **/
    public HashMap<String, HashMap<String, Integer>> descriptorMap;
    /**
     * @param cosValue double meant to contain the calculated cosine value between two Strings.
     * */
    private double cosValue;

    /**
     * */
    public Synonyms(URL[] corpus) {
        super();
        parseCorpus(corpus);
    }


    // getters, setters
    public HashMap<String, HashMap<String, Integer>> getDescriptorMap() { return this.descriptorMap; }
    public void setDescriptorMap(HashMap<String, HashMap<String, Integer>> hashmap) { this.descriptorMap = hashmap; }

    public double getCosSimilarity() {
        return this.cosValue;
    }
    public void setCosSimilarity(String word1, String word2) {
        this.cosValue = cosineSimilarity(word1, word2);
    }

    // parse the contents of array of URLs
    private void parseCorpus(URL[] corpus) {
        HashMap<String, HashMap<String, Integer>> descriptors = new HashMap<>();
        for (URL url : corpus) {
            try {
                Scanner urlScanner = new Scanner(url.openStream());
                urlScanner.useDelimiter("[\\.\\?\\!]|\\Z");
                while (urlScanner.hasNext()) {
                    String sentence = urlScanner.next().replaceAll("\\W+", " ").toLowerCase(); // grab a sentence
                    String[] words = sentence.split("\\s+"); //split it into words

                    HashMap<String, Integer> map = new HashMap<>(); //hashmap that holds descriptors for words in ONE sentence.

                    for (String word : words) { // first iteration fills a map with descriptors.
                        if (map.containsKey(word)) {
                            map.replace(word, map.get(word) + 1);
                        } else {
                            map.putIfAbsent(word, 1);
                        }
                    }
                    for (String word : words) { // second iteration fills in all descriptor keys, and updates its maps
                        if (descriptors.containsKey(word)) {
                            map.forEach((key, value)
                                    -> descriptors.get(word).merge(key, value, Integer::sum));
                            descriptors.replace(word, map);
                        }
                        else {
                            descriptors.putIfAbsent(word, map);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setDescriptorMap(descriptors);
    }

    private double cosineSimilarity(String word1, String word2) {
        if (descriptorMap.get(word1) != null && descriptorMap.get(word2) != null) {
            HashMap<String, Integer> aMap = this.descriptorMap.get(word1);
            HashMap<String, Integer> bMap = this.descriptorMap.get(word2);
            return dotProduct(aMap, bMap) / Math.sqrt(sumOfSquares(aMap.values()) * sumOfSquares(bMap.values()));
        } else { // the words were never present in the same sentence, so return -1
            return -1.0;
        }
    }

    // Implementation of the dot product calculation that gets used in the cosine similarity calculation.
    private double dotProduct(HashMap<String, Integer> descriptors1, HashMap<String, Integer> descriptors2) {
        double dotProduct = 0.0;
        for (Map.Entry<String, Integer> d1 : descriptors1.entrySet()) {
            dotProduct += d1.getValue() * descriptors2.getOrDefault(d1.getKey(), 0); //allows us to add 0 when neccessary
        }
        return dotProduct;
    }

    //Implementation of the summation portion of the cosine similarity calculation.
    private double sumOfSquares(Collection<Integer> values) {
        double sum = 0.0;
        for (int count : values) {
            sum += count * count;
        }
        return sum;
    }

    private double calculateCosineSimilarity(String word1, String word2) {
        double dot = 0.0;
        HashMap<String, Integer> aMap = descriptorMap.get(word1);
        HashMap<String, Integer> bMap = descriptorMap.get(word2);

        if (aMap != null && bMap != null) {
            double modA = sumOfSquares(aMap.values());
            double modB = sumOfSquares(bMap.values());
            for (Map.Entry<String, Integer> a : aMap.entrySet()) {
                int b = bMap.getOrDefault(a.getKey(), 0);
                dot += (a.getValue() * b);
            }
            return dot / (modA * modB);
        } else {
            return -1.0;
        }
    }
}
