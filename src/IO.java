import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;

public class IO {
    static RandomAccessFile raf = null;
    static ArrayList<IOData> list = new ArrayList<>();

    private int result;

    public IO(Host host, ArrayList<Occurrences> uniqueMoves) throws IOException {
        raf = new RandomAccessFile("sets/" + host.printBoard(), "rw");

        // read
        raf.seek(0);
        System.out.println("BEFORE");
        while (raf.getFilePointer() < raf.length()) {
            int one = raf.readInt();
            int two = raf.readInt();

            list.add(new IOData(one, two));
            System.out.println("\033[31m" + one + " " + two + "\033[0m");
        }

        // write
        raf.seek(0);
        uniqueMoves.sort(Comparator.comparingInt(Occurrences::index));

        // convert data types

        ArrayList<IOData> occToData = new ArrayList<>();

        for (Occurrences occurrence : uniqueMoves) {
            occToData.add(new IOData(occurrence.index(),occurrence.getCount()));
        }

        // merge lists

        ArrayList<IOData> mergedData = new ArrayList<>();

        for(IOData move : occToData) {
            boolean contained = false;
            for (IOData data : list) {
                if(data.getIndex() == move.getIndex()) {
                   move.addCount(data.getCount());
                   mergedData.add(move);
                   contained = true;
                }
            }
            if(!contained) {
                mergedData.add(move);
            }

        }
        // check for missed data
        for(IOData data : list) {
            boolean contained = false;
            for (IOData move : occToData) {
                if (data.getIndex() == move.getIndex()) {
                    contained = true;
                    break;
                }
            }
            if(!contained) {
                mergedData.add(data);
            }

        }

        // sort before right
        mergedData.sort(Comparator.comparingInt(IOData::getIndex));

        // write file
        raf.seek(0);
        for (IOData data : mergedData) {
            raf.writeInt(data.getIndex());
            raf.writeInt(data.getCount());
        }

        //print
        raf.seek(0);
        while (raf.getFilePointer() < raf.length()) {
            System.out.print(raf.readInt());
        }

        raf.seek(0);
        list.clear();
        System.out.println("AFTER");
        for(IOData data : mergedData) {
            System.out.println(data);
        }

        int highestOccurrence = 0;
        int highestOccIndex = 0;
        for (IOData data : mergedData) {
            if (data.getCount() > highestOccurrence) {
                highestOccurrence = data.getCount();
                highestOccIndex = mergedData.indexOf(data);
            }
        }

        this.result = highestOccIndex;
        System.err.println(this.result);

        raf.close();

    }

    public int getHighestOccurrence() {
        return this.result;
    }
}
