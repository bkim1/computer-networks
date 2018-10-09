import java.io.Serializable;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;

    private String data;
    private int sequenceNum;
    private boolean eof;

    public Packet(String d, int s, boolean e) {
        this.data = d;
        this.sequenceNum = s;
        this.eof = e;
    }

    public Packet(Packet p) {
        this.data = p.getData();
        this.sequenceNum = p.getSequenceNumber();
        this.eof = p.isEndOfFile();
    }

    public String getData() { return this.data; }
    public void setData(String s) { this.data = s; }

    public int getSequenceNumber() { return this.sequenceNum; }
    public void setSequenceNumber(int s) { this.sequenceNum = s; }

    public boolean isEndOfFile() { return this.eof; }
    public void setEndOfFile(boolean e) { this.eof = e; }

    public String toString() { return "Packet Data: " + this.data; }
}
