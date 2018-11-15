package serializedData;

public class Rank implements Comparable<Rank> {
    private int userID, ranking;

    public Rank(int u, int r) {
        this.userID = u;
        this.ranking = r;
    }

    public int getUserID() { return this.userID; }
    public void setUserID(int u) { this.userID = u; }

    public int getRanking() { return this.ranking; }
    public void setRanking(int r) { this.ranking = r; }
    
    @Override
    public int compareTo(Rank other) {
        return Integer.compare(this.ranking, other.getRanking());
    }
}