package Common;

import java.util.Arrays;

public class Piece {
    private byte[] data;
    public Piece(byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    public byte[] getData() {
        return this.data;
    }
}
