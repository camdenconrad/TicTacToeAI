import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Linker {
    private static final ArrayList<Host> hosts = new ArrayList<>();
    private static final ArrayList<String> rooms = new ArrayList<>();

    public static synchronized void addHost(Host host) {
        if (!rooms.contains(host.getSeed())) {
            rooms.add(host.getSeed());
            hosts.add(host);
        }

    }

    public static synchronized Host linkTo(String seed) {
        for (String room : rooms) {
            if (room.equals(seed)) {
                return hosts.get(rooms.indexOf(seed));
            }
        }
        return new Host(seed);
    }

    public static synchronized void removeHost(Host host) {
        if (rooms.contains(host.getSeed())) {
            rooms.remove(host.getSeed());
            hosts.remove(host);
        }
        //System.gc();
    }

    public static synchronized void seeHosts() {
        for (Host host : hosts) {
            System.err.println(host);
        }
    }

    public static void clearSims() {

        // clean up

        try {
            for (Host sim : hosts) {
                if (sim instanceof SimHost) {
                    rooms.remove(sim.getSeed());
                    hosts.remove(sim);

                }
            }
        } catch (ConcurrentModificationException ignored) {
            clearSims();
        }

    }

    public static void clearSims(ArrayList<Host> host) {

        // clean up

        try {
            for (Host sim : host) {
                if (sim instanceof SimHost) {
                    rooms.remove(sim.getSeed());
                    hosts.remove(sim);
                }
            }
        } catch (ConcurrentModificationException ignored) {
            clearSims();
        }

    }

    public static ArrayList<Host> getHosts() {
        return hosts;
    }

    public static Host getHost(int index) {
        return hosts.get(index);
    }

    public static ArrayList<String> getRooms() {
        return rooms;
    }
}
