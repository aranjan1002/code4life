class TrieNode {
    // Initialize your data structure here.
    public TrieNode() {
    }
    
    boolean isLastLetter = false;
    TrieNode[] children = new TrieNode[26];
}

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        char[] ch_arr = word.toCharArray();
        TrieNode curr_node = root;
        for (int i = 0; i <= word.length() - 1; i++) {
            if (curr_node.children[ch_arr[i] - 'a'] == null) {
                curr_node.children[ch_arr[i] - 'a'] = new TrieNode();
            }
            if (i == word.length() - 1) {
                curr_node.children[ch_arr[i] - 'a'].isLastLetter = true;
            }
            curr_node = curr_node.children[ch_arr[i] - 'a'];
        }
    }

    // Returns if the word is in the trie.
    public boolean search(String word) {
        TrieNode node = lastNodeWith(word);
        return node != null && node.isLastLetter == true;
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        return lastNodeWith(prefix) != null;
    }
    
    private TrieNode lastNodeWith(String word) {
        char[] ch_arr = word.toCharArray();
        TrieNode curr_node = root;
        for (int i = 0; i <= ch_arr.length - 1; i++) {
            if (curr_node.children[ch_arr[i] - 'a'] == null) {
                return null;
            }
            curr_node = curr_node.children[ch_arr[i] - 'a'];
        }
        return curr_node;
    }
}

// Your Trie object will be instantiated and called as such:
// Trie trie = new Trie();
// trie.insert("somestring");
// trie.search("key");