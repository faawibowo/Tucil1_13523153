import java.util.List;
import java.util.Scanner;

import module.filemanager;
import module.block;

public class App {
    private static char[][] board;
    private static List<char[][]> blocks;
    private static int N, M; // Ukuran papan
    private static int attemptCount = 0; // Jumlah percobaan

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        // while (true) {
        System.out.println("Welcome to IQ Puzzler Solver!");
        System.out.println("Masukkan Nama File: ");
        String namaFile = input.nextLine();
        String data = filemanager.readFile(namaFile);
        N = filemanager.readN(data);
        M = filemanager.readM(data);

        System.out.println("M:" + filemanager.readM(data));
        System.out.println("N:" + filemanager.readN(data));
        System.out.println("P:" + filemanager.readP(data));

        board = new char[filemanager.readM(data)][filemanager.readN(data)];
        for (int i = 0; i < filemanager.readM(data); i++) {
            for (int j = 0; j < filemanager.readN(data); j++) {
                board[i][j] = '_';
            }
        }

        List<char[][]> rawblocks = filemanager.parseBlocks(data);

        System.out.println("List Blok :");
        for (int i = 0; i < rawblocks.size(); i++) {
            char[][] block = rawblocks.get(i);
            char x = filemanager.findFirstNonSpaceCharacter(block[0]);
            System.out.println("Blok " + (char) (x) + ":");
            filemanager.printBlock(rawblocks.get(i));
            System.out.println();

        }
        blocks = filemanager.convertBlock(rawblocks);
        long startTime = System.nanoTime();

        boolean foundSolution = solvePuzzle(0);

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        // **Cetak hasil**
        if (foundSolution) {
            System.out.println("Solusi ditemukan:");
            printColorBoard();
        } else {
            System.out.println("Tidak ada solusi.");
        }

        System.out.println("Total percobaan: " + attemptCount);
        System.out.printf("Waktu eksekusi: %.3f ms\n", executionTimeMs);

        System.out.println("Apakah ingin save file? (y/n)");
        String save = input.nextLine();
        if (save.equals("y")) {
            System.out.println("Masukkan nama file: ");
            String saveFile = input.nextLine();
            String BoardData = filemanager.board2String(board);
            filemanager.saveFile(saveFile, BoardData);
        }
    }

    public static boolean solvePuzzle(int pieceIndex) {
        if (pieceIndex == blocks.size()) {
            if (isBoardFull()) {
                return true;
            }
            return false; // Jika papan belum penuh, backtrack
        }

        char[][] piece = blocks.get(pieceIndex);

        // Coba semua posisi di papan
        for (int row = 0; row <= N - piece.length; row++) {
            for (int col = 0; col <= M - piece[0].length; col++) {
                for (char[][] transformedPiece : block.generateTransformations(piece)) {

                    if (canPlaceBlock(board, transformedPiece, row, col)) {
                        char blockChar = filemanager.getChar(transformedPiece);
                        placeBlock(board, transformedPiece, row, col, blockChar);
                        attemptCount++;
                        if (solvePuzzle(pieceIndex + 1)) {
                            return true;
                        }

                        removeBlock(board, transformedPiece, row, col);

                    }
                }
            }
        }

        return false; // Jika semua kemungkinan gagal
    }

    public static boolean canPlaceBlock(char[][] board, char[][] piece, int row, int col) {
        if (row + piece.length > board.length || col + piece[0].length > board[0].length) {
            return false;
        }

        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] != '_' && board[row + i][col + j] != '_') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void placeBlock(char[][] board, char[][] piece, int row, int col, char blockChar) {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] != '_') {
                    board[row + i][col + j] = blockChar;
                }
            }
        }
    }

    public static void removeBlock(char[][] board, char[][] piece, int row, int col) {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] != '_') {
                    board[row + i][col + j] = '_';
                }
            }
        }
    }

    public static void printBoard() {
        for (char[] row : board) {
            System.out.println(new String(row));
        }
        System.out.println();
    }

    public static void printColorBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '_') {
                    System.out.print("  ");
                } else {
                    int index = cell - 'A';
                    if (index >= 0 && index < 26) {
                        System.out.print(COLORS[index] + cell + RESET + " ");
                    } else {
                        System.out.print(cell + " ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static boolean isBoardFull() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (board[i][j] == '_') {
                    return false;
                }
            }
        }
        return true;
    }

    public static final String[] COLORS = {
            "\u001B[31m", // A - Merah
            "\u001B[32m", // B - Hijau
            "\u001B[33m", // C - Kuning
            "\u001B[34m", // D - Biru
            "\u001B[35m", // E - Magenta
            "\u001B[36m", // F - Cyan
            "\u001B[91m", // G - Terang Merah
            "\u001B[92m", // H - Terang Hijau
            "\u001B[93m", // I - Terang Kuning
            "\u001B[94m", // J - Terang Biru
            "\u001B[95m", // K - Terang Magenta
            "\u001B[96m", // L - Terang Cyan
            "\u001B[90m", // M - Abu-abu Gelap
            "\u001B[97m", // N - Putih Terang
            "\u001B[38;5;208m", // O - Oranye
            "\u001B[38;5;202m", // P - Merah Bata
            "\u001B[38;5;82m", // Q - Hijau Neon
            "\u001B[38;5;226m", // R - Kuning Neon
            "\u001B[38;5;21m", // S - Biru Laut
            "\u001B[38;5;201m", // T - Pink
            "\u001B[38;5;123m", // U - Biru Tosca
            "\u001B[38;5;214m", // V - Kuning Keemasan
            "\u001B[38;5;99m", // W - Ungu Tua
            "\u001B[38;5;124m", // X - Merah Darah
            "\u001B[38;5;190m",
            "\u001B[38;5;50m"
    };
    public static final String RESET = "\u001B[0m";

}
