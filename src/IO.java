
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

public class IO {
    static RandomAccessFile raf = null;
    static ArrayList<IOData> list = new ArrayList<>();

    public IO(Host host, ArrayList<Occurrences> uniqueMoves) throws IOException {
        raf = new RandomAccessFile("sets/" + host.printBoard(), "rw");
        raf.seek(0);
        raf.writeInt(1);
        raf.writeInt(5);
        raf.seek(0);

        //read
        list.add(new IOData(raf.readInt(),raf.readInt()));


        //write
        raf.seek(0);
        for (Occurrences occurrence : uniqueMoves) {
            raf.writeInt(occurrence.result().index());
            raf.writeInt(occurrence.getCount());
        }

        System.out.print(list.get(0));

        raf.close();

    }
}
