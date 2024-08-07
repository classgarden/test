package IllumioApp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;


public class Firewall {
	
	private TrieNode root;
	
	// default constructor for testing purpose
	public Firewall() {    
			root = new TrieNode();
		}
    
    public Firewall(String filePath ) {
    	 root = new TrieNode();
        String[] rows = processFile(filePath);
    	for(String row: rows) {
    		String[] tokens = row.split(",");
    		addRule(tokens[0],tokens[1], tokens[2],tokens[3]);	
    	}
    }
    
    public String[] processFile(String filePath) {
     	List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(filePath));
			if (!lines.isEmpty()) {
				lines = lines.subList(1, lines.size());
			}

		} catch (IOException e) {
			System.out.println("Could not load file." + e.getMessage());
		}
		return lines.toArray(new String[0]);
	}
    
   

	public void addRule(String direction, String protocol, String portRange, String ipRange) {
        String[] portParts = portRange.split("-");
        int portStart = Integer.parseInt(portParts[0]);
        int portEnd = portParts.length > 1 ? Integer.parseInt(portParts[1]) : portStart;

        String[] ipParts = ipRange.split("-");
        String startIp = ipParts[0];
        String endIp = ipParts.length > 1 ? ipParts[1] : ipParts[0]; 
        if(startIp.equals(endIp)) {
        	insertRule(startIp, portStart, portEnd, direction, protocol);
        }else {
        	insertRule(startIp, endIp, portStart, portEnd,  direction, protocol);
        }

    }
    

    public void insertRule(String ip, int startPort, int endPort, String direction, String protocol) {
        TrieNode node = root;
        String covertoLongIp = IPUtils.ipToBitString(ip);
        for (char bit : covertoLongIp.toCharArray()) {
            node.children.putIfAbsent(bit, new TrieNode());
            node = node.children.get(bit);
        }
        node.intervalTree.insert(startPort, endPort, new Rule(direction, protocol));
    }

    public void insertRule(String startIP, String endIP, int startPort, int endPort, String direction, String protocol) {
    	List<String> ipRange = IPUtils.getIPRange(startIP, endIP);
        for (String ip : ipRange) {
            insertRule(ip, startPort, endPort, direction, protocol);
         }
    
    }

    public List<Rule> searchRules(String ip, int port) {
        TrieNode node = root;
        for (char bit : ip.toCharArray()) {
            node = node.children.get(bit);
            if (node == null) {
                return Collections.emptyList();
            }
        }
        return node.intervalTree.search(port);
    }
    
    public boolean acceptPacket(String direction, String protocol, int port, String ip) {
        List<Rule> rules = searchRules(IPUtils.ipToBitString(ip), port);
        for (Rule rule : rules) {
            if (rule.direction.equals(direction) && rule.protocol.equals(protocol)) {
                return true;
            }
        }
        return false;
    }
    
    // test 
    public static void main(String[] args) {
  	    // test loading rule.csv file
    	String filePath = "rule.csv";
        Firewall fw = new Firewall(filePath);
       
        System.out.println(fw.acceptPacket("inbound", "tcp", 80, "192.168.1.2")); // first rule true
        System.out.println(fw.acceptPacket("inbound", "udp", 53, "192.168.2.1")); // true third rule
        System.out.println(fw.acceptPacket("outbound", "tcp", 10234, "192.168.10.11")); // second true
        System.out.println(fw.acceptPacket("inbound", "tcp", 81, "192.168.1.2")); // false
        System.out.println(fw.acceptPacket("inbound", "udp", 24, "52.12.48.92")); // false
        
        System.out.println();
        
        // test with rules hard code  
        Firewall fw2 = new Firewall();
        
        fw2.insertRule("192.168.1.2", 80, 80, "inbound", "tcp");
        fw2.insertRule("192.168.10.11", 10000, 20000, "outbound", "tcp");
        fw2.insertRule("192.168.1.1", "192.168.2.5", 53, 53, "inbound", "udp");
        fw2.insertRule("52.12.48.92", 1000, 2000, "inbound", "udp");

         System.out.println(fw2.acceptPacket("inbound", "tcp", 80, "192.168.1.2"));  // Output: true
         System.out.println(fw2.acceptPacket("outbound", "tcp", 15000, "192.168.10.11"));  // Output: true
         System.out.println(fw2.acceptPacket("inbound", "udp", 53, "192.168.1.1"));  // Output: true        
         System.out.println(fw2.acceptPacket("inbound", "udp", 1500, "52.12.48.92"));  // Output: true
         System.out.println(fw2.acceptPacket("inbound", "tcp", 80, "192.168.1.1"));  // Output: false
    }
    
}
