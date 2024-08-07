package IllumioApp;

import java.util.ArrayList;
import java.util.List;

public class IntervalTreeNode {
	
	int start, end, maxEnd;
    List<Rule> rules;
    IntervalTreeNode left, right;

    public IntervalTreeNode(int start, int end) {
        this.start = start;
        this.end = end;
        this.maxEnd = end;
        this.rules = new ArrayList<>();
    }
}
