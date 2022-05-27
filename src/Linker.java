import java.util.ArrayList;

public class Linker {
    private static final ArrayList<Host> hosts = new ArrayList<>();
    private static final ArrayList<String> rooms = new ArrayList<>();

    public static void addHost(Host host) {
        if (!rooms.contains(host.getSeed())) {
            rooms.add(host.getSeed());
            hosts.add(host);
        }

    }

    public static Host linkTo(String seed, UI ui) {
        for (String room : rooms) {
            if (room.equals(seed)) {
                return hosts.get(rooms.indexOf(seed));
            }
        }
        return new Host(seed);
    }
}
