package postfixeval;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.*;
import java.lang.Math;
import java.util.stream.Stream;

public class PostFixCalculator {
    public static void main(String[] args) {
        String fileToParse = "SampleCSVFile.csv";
        if (args.length > 0) {
            fileToParse = args[0];
        }
        final String DELIMITER = ",";
        LinkedHashMap<String, String> mapping = new LinkedHashMap<String, String>();
        List<String> collectTokens = new ArrayList<String>();
        try {
            Scanner fileReader = new Scanner(new File(fileToParse));
            int Rows = 1;
            while (fileReader.hasNext()) {
                String[] tokens = fileReader.nextLine().split(DELIMITER);
                int Cols = 1;
                for (String token : tokens) {
                    Scanner tokenScanner = new Scanner(token);
                    if (tokenScanner.hasNextInt()) {
                        int result = evaluatePostfix(token);
                        if (result == 0) {
                            collectTokens.add("#ERR");
                        } else {
                            collectTokens.add(String.valueOf((int) result));
                        }
                        mapping.put(columnToLetter(Cols) + String.valueOf(Rows), String.valueOf(result));
                    } else {
                        collectTokens.add("#ERR");
                        mapping.put(columnToLetter(Cols) + String.valueOf(Rows), "#ERR");
                    }
                    tokenScanner.close();
                    Cols++;
                }
                collectTokens.add(System.lineSeparator());
                Rows++;
            }fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(collectTokens.toString().replaceAll(",",""));
    }
    public static int evaluatePostfix(String expression) {

        Scanner scanner = new Scanner(expression);
        Stack<Integer> stack = new Stack<Integer>();
        if (expression.split(" ").length > 1 && Stream.of("+", "-", "/").noneMatch(expression::contains)) {
            return 0;
        }
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                stack.push(scanner.nextInt());
            } else {
                int b = stack.pop();
                int a = stack.pop();
                char operator = scanner.next().charAt(0);
                switch (operator) {
                    case '+':
                        stack.push(a + b);
                        break;

                    case '-':
                        stack.push(a - b);
                        break;

                    case '*':
                        stack.push(a * b);
                        break;

                    case '/':
                        stack.push(a / b);
                        break;

                    case '%':
                        stack.push(a % b);
                        break;

                    case '^':
                        stack.push((int) Math.pow(a, b));
                        break;
                }
            }
        }
        scanner.close();
        return stack.pop();
    }
 public static String columnToLetter(int columnNumber) {
        StringBuilder columnName = new StringBuilder();
        while (columnNumber > 0) {
            int remainder = columnNumber % 26;
            if (remainder == 0) {
                columnName.append("Z");
                columnNumber = (columnNumber / 26) - 1;
            } else {
                columnName.append((char) ((remainder - 1) + 'A'));
                columnNumber = columnNumber / 26;
            }
        }
        return columnName.reverse().toString().toLowerCase();
    }
}