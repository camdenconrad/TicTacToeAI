import java.util.ArrayList;

public class Linker {
    private static ArrayList<Host> hosts = new ArrayList<Host>();
    private static ArrayList<String> rooms = new ArrayList<String>();

    public boolean addHost(Host host) {
        if(!rooms.contains(host.getSeed())) {
            rooms.add(host.getSeed());
            hosts.add(host);
            return true;
        }
        return false;

    }

    public Host linkTo(String seed) {
        for(String room : rooms) {
            if(room.equals(seed)) {
                return hosts.get(rooms.indexOf(seed));
            }
        }
        return new Host(seed);
    }
}
