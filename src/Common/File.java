/**
 * 
 */
package Common;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import Config.CommonInfo;;
/**
 * @author sonal
 *
 */
public class File {
    private Piece[] pieces;
    private int numberOfPieces;

    public File(CommonInfo commonInfo, boolean hasFile) {
        long fileSize = commonInfo.getSharedFileSize();
        int pieceSize = commonInfo.getSharedFilePieceSize();
        this.numberOfPieces = (int) Math.ceil((double) fileSize / (double) pieceSize);
        pieces = new Piece[this.numberOfPieces];
        if(hasFile) {
            String fileName = commonInfo.getSharedFileName();
            try {
                byte[] allBytes = Files.readAllBytes(Paths.get(fileName));
                int i = 0, j = 0;
                while(i < allBytes.length){
                    byte bytes[]= Arrays.copyOfRange(allBytes, i, i + pieceSize);
                    i += pieceSize;
                    pieces[j++] = new Piece(bytes);
                }
            } catch (IOException ioException) {
                System.out.println("Exception while reading file");
                ioException.printStackTrace();
            }
        }
    }

    public Piece[] getPieces() {
        return pieces;
    }

    public void setPieces(Piece[] pieces) {
        this.pieces = pieces;
    }

    public Piece getPieceAtIndex(int index) {
        return pieces[index];
    }

    public void setPieceAtIndex(int index, Piece piece) {
        pieces[index] = piece;
    }

    public int getNumberOfPieces() {
        return numberOfPieces;
    }

}
