public class WordDictionary {
    class TrieNode {
        TrieNode[] nodes = new TrieNode[26];
        boolean isLeaf = false;
    }
    
    TrieNode root = new TrieNode();
    // Adds a word into the data structure.
    public void addWord(String word) {
        if (word.equals("") == true) {
            root.isLeaf = true;
        }
        TrieNode node = root;
        char[] chars = word.toCharArray();
        for (char c : chars) {
            if (node.nodes[c - 'a'] == null) {
                node.nodes[c - 'a'] = new TrieNode();
            }
            node = node.nodes[c - 'a'];
        }
        node.isLeaf = true;
    }

    // Returns if the word is in the data structure. A word could
    // contain the dot character '.' to represent any one letter.
    public boolean search(String word) {
        return search(word, root);
    }
    
    public boolean search(String word, TrieNode node) {
        if (word.equals("")) {
            return node.isLeaf;
        }
        char[] chars = word.toCharArray();
        for (int i = 0; i <= chars.length - 1; i++) {
            char c = chars[i];
            if (c != '.') {
                if (node.nodes[c - 'a'] == null) {
                    return false;
                }
                node = node.nodes[c - 'a'];
            } else {
                for (int j = 0; j <= 25; j++) {
                    if (node.nodes[j] != null) {
                        if (search(word.substring(i + 1), node.nodes[j]) == true) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        return node.isLeaf;
    }
}

// Your WordDictionary object will be instantiated and called as such:
// WordDictionary wordDictionary = new WordDictionary();
// wordDictionary.addWord("word");
// wordDictionary.search("pattern");