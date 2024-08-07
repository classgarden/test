package IllumioApp;

import java.util.List;
import java.util.ArrayList;

public class IPUtils {
	
	public static String ipToBitString(String ip) {
//        StringBuilder sb = new StringBuilder();
//        String[] parts = ip.split("\\.");
//        for (String part : parts) {
//            int num = Integer.parseInt(part);
//            // convert (0-255) to ASCII char
//            char asciiValue = ((char)num);
//            sb.append(asciiValue);
//           // sb.append(String.format("%08d", Integer.parseInt(Integer.toBinaryString(num))));
//        }
//        return sb.toString();
		return String.valueOf(ipToLong(ip));
    }

    public static List<String> getIPRange(String startIP, String endIP) {
        List<String> ips = new ArrayList<>();
        long start = ipToLong(startIP);
        long end = ipToLong(endIP);
        for (long i = start; i <= end; i++) {
            ips.add(longToIP(i));
        }
        return ips;
    }

    private static long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result |= Long.parseLong(parts[i]) << (24 - (8 * i));
        }
        return result;
    }

    private static String longToIP(long ip) {
        return String.format("%d.%d.%d.%d",
                (ip >> 24) & 0xFF,
                (ip >> 16) & 0xFF,
                (ip >> 8) & 0xFF,
                ip & 0xFF);
    }

}
