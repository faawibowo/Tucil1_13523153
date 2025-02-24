package module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class block {
    public static char[][] rotate(char[][] block) {
        int row = block.length;
        int col = block[0].length;
        char[][] rotated = new char[col][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                rotated[j][row - 1 - i] = block[i][j];
            }
        }
        return rotated;
    }

    public static char[][] flip(char[][] block) {
        int row = block.length;
        int col = block[0].length;
        char[][] flipped = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                flipped[i][col - 1 - j] = block[i][j];
            }
        }
        return flipped;
    }

    // Menghasilkan semua kemungkinan rotasi dan pencerminan untuk suatu blok
    public static List<char[][]> generateTransformations(char[][] piece) {
        List<char[][]> transformations = new ArrayList<>();
        Set<String> seen = new HashSet<>(); // Menghindari duplikasi

        char[][] currentPiece = piece;
        for (int i = 0; i < 4; i++) { // Coba rotasi 0°, 90°, 180°, 270°
            if (seen.add(Arrays.deepToString(currentPiece))) {
                transformations.add(currentPiece);
            }
            currentPiece = rotate(currentPiece);
        }

        currentPiece = flip(piece);
        for (int i = 0; i < 4; i++) {
            if (seen.add(Arrays.deepToString(currentPiece))) {
                transformations.add(currentPiece);
            }
            currentPiece = rotate(currentPiece);
        }

        return transformations;
    }

}
