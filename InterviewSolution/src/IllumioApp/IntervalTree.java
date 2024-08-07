package IllumioApp;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


public class IntervalTree {
	
	private IntervalTreeNode root;

    public void insert(int start, int end, Rule rule) {
        root = insert(root, start, end, rule);
    }

    private IntervalTreeNode insert(IntervalTreeNode node, int start, int end, Rule rule) {
        if (node == null) {
            IntervalTreeNode newNode = new IntervalTreeNode(start, end);
            newNode.rules.add(rule);
            return newNode;
        }
        if (start < node.start) {
            node.left = insert(node.left, start, end, rule);
        } else {
            node.right = insert(node.right, start, end, rule);
        }
        node.maxEnd = Math.max(node.maxEnd, end);
        node.rules.add(rule);
        return node;
    }

    public List<Rule> search(int point) {
        return search(root, point);
    }

    private List<Rule> search(IntervalTreeNode node, int point) {
        if (node == null) return Collections.emptyList();

        List<Rule> results = new ArrayList<>();
        if (point >= node.start && point <= node.end) {
            results.addAll(node.rules);
        }
        if (node.left != null && point <= node.left.maxEnd) {
            results.addAll(search(node.left, point));
        }
        if (node.right != null) {
            results.addAll(search(node.right, point));
        }
        return results;
    }

}
