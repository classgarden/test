package IllumioApp;

import java.util.*;

public class TrieNode { 
	Map<Character, TrieNode> children;
    IntervalTree intervalTree;

    public TrieNode() {
        children = new HashMap<>();
        intervalTree = new IntervalTree();
    }

}
