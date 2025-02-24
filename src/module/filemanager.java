package module;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class filemanager {
    private static String inputPath = "/test/input/";

    public static int readM(String data) {
        return extractNumber(data, 0);
    }

    public static int readN(String data) {
        return extractNumber(data, 1);
    }

    public static int readP(String data) {
        return extractNumber(data, 2);
    }

    private static int extractNumber(String data, int index) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(data);
        ArrayList<Integer> numbers = new ArrayList<>();

        while (matcher.find()) {
            numbers.add(Integer.parseInt(matcher.group()));
        }

        return (numbers.size() > index) ? numbers.get(index) : -1;
    }

    public static String readFile(String nama) {
        StringBuilder result = new StringBuilder();
        try {
            String curDir = System.getProperty("user.dir");
            File myObj = new File(curDir + inputPath + nama + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                result.append(data).append("\n");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Tidak Dapat Ditemukan.");
        }
        return result.toString();
    }

    public static List<char[][]> parseBlocks(String input) {
        String[] lines = input.split("\n");
        List<char[][]> blockList = new ArrayList<>();
        int i = 2;

        while (i < lines.length) {
            List<char[]> tempBlock = new ArrayList<>();

            tempBlock.add(lines[i].toCharArray());
            i++;

            while (i < lines.length && isSameBlock(tempBlock, lines[i])) {
                tempBlock.add(lines[i].toCharArray());
                i++;
            }

            char[][] block = new char[tempBlock.size()][];
            for (int j = 0; j < tempBlock.size(); j++) {
                block[j] = tempBlock.get(j);
            }
            blockList.add(block);
        }

        return blockList;
    }

    public static boolean isSameBlock(List<char[]> tempBlock, String newLine) {
        if (tempBlock.isEmpty() || newLine.isEmpty()) {
            return false;
        }
        char[] firstBlockLine = tempBlock.get(0);
        char firstChar = findFirstNonSpaceCharacter(firstBlockLine);
        char newFirstChar = findFirstNonSpaceCharacter(newLine.toCharArray());

        return firstChar == newFirstChar;
    }

    public static char findFirstNonSpaceCharacter(char[] arr) {
        for (char c : arr) {
            if (c != ' ')
                return c;
        }
        return ' ';
    }

    public static char getChar(char[][] block) {
        for (char[] row : block) {
            for (char c : row) {
                if (c != '_') {
                    return c;
                }
            }
        }
        return '_';
    }

    public static void printBlock(char[][] block) {
        for (char[] row : block) {
            System.out.println(new String(row));
        }
    }

    public static int maxLength(char[][] blocks) {
        int max = 0;
        for (char[] block : blocks) {
            if (block.length > max) {
                max = block.length;
            }
        }
        return max;
    }

    public static List<char[][]> convertBlock(List<char[][]> blocks) {
        List<char[][]> newBlocks = new ArrayList<>();

        for (char[][] block : blocks) {
            int maxLen = maxLength(block);
            char[][] newBlock = new char[block.length][maxLen];
            for (int i = 0; i < block.length; i++) {
                System.arraycopy(block[i], 0, newBlock[i], 0, block[i].length);
                for (int j = block[i].length; j < maxLen; j++) {
                    newBlock[i][j] = '_';
                }
                for (int j = 0; j < maxLen; j++) {
                    if (newBlock[i][j] == ' ') {
                        newBlock[i][j] = '_';
                    }
                }
            }
            newBlocks.add(newBlock);
        }
        return newBlocks;
    }

    public static void saveFile(String fileName, String data) {
        try {
            String curDir = System.getProperty("user.dir");
            File myObj = new File(curDir + "/test/output/" + fileName + ".txt");
            myObj.createNewFile();
            java.io.FileWriter myWriter = new java.io.FileWriter(myObj);
            myWriter.write(data);
            myWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String board2String(char[][] board) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            sb.append(row).append("\n");
        }
        return sb.toString();
    }
}