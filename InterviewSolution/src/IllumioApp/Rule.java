package IllumioApp;

public class Rule {
	String direction;
    String protocol;

    public Rule(String direction, String protocol) {
        this.direction = direction;
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "(" + direction + ", " + protocol + ")";
    }

}
