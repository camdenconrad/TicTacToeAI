public class Seed
{
    private final String seed;

    private int seats = 0;

    public Seed(String seed) {
        this.seed = seed;
    }

    public String getSeed() {
        return seed;
    }

    public boolean joinSeed(Seed seed) throws SeatOverflowException {
        if (seats < 2) {
            seed.increaseSeat();
            return true;
        }
        else
            throw new SeatOverflowException();

    }

    public void increaseSeat(){
        this.seats++;
    }
}
