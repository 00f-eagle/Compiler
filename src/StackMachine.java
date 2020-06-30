import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class StackMachine {
    Stack<String> stack = new Stack<>();

    Map stackMachine(Parser parser) {
        Map<String, Integer> tableOfVar = new HashMap<>();

        int a, b, d;
        boolean b1;
        for (int i = 0; i <= parser.tokensOPIS.size() - 1; i++) {
            switch (parser.tokensOPIS.get(i)) {
                case "=":
                    a = getInt(tableOfVar);
                    tableOfVar.put(stack.pop(), a);
                    break;
                case "+":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    d = a + b;
                    stack.push(String.valueOf(d));
                    break;
                case "-":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    d = b - a;
                    stack.push(String.valueOf(d));
                    break;
                case "/":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    d = b / a;
                    stack.push(String.valueOf(d));
                    break;
                case "%":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    d = b % a;
                    stack.push(String.valueOf(d));
                    break;
                case "^":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    d = (int) Math.pow(a, b);
                    stack.push(String.valueOf(d));
                    break;
                case "*":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    d = a * b;
                    stack.push(String.valueOf(d));
                    break;
                case "!=":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    b1 = a != b;
                    stack.push(String.valueOf(b1));
                    break;
                case "==":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    b1 = a == b;
                    stack.push(String.valueOf(b1));
                    break;
                case "<":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    b1 = b < a;
                    stack.push(String.valueOf(b1));
                    break;
                case ">":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    b1 = b > a;
                    stack.push(String.valueOf(b1));
                    break;
                case "<=":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    b1 = b <= a;
                    stack.push(String.valueOf(b1));
                    break;
                case ">=":
                    a = getInt(tableOfVar);
                    b = getInt(tableOfVar);
                    b1 = b >= a;
                    stack.push(String.valueOf(b1));
                    break;
                case "!F":
                    a = getInt(tableOfVar);
                    boolean c = stack.pop().equals("true");
                    i = c ? i : a;
                    break;
                case "!":
                    i = getInt(tableOfVar) - 1;
                    break;
                default:
                    stack.push(String.valueOf(parser.tokensOPIS.get(i)));
                    break;
            }
        }

        Map<String, Integer> tableOfVar2 = new HashMap<>();

        for (String s :
                parser.tableOfVar) {
            if (tableOfVar.containsKey(s)) {
                tableOfVar2.put(s, tableOfVar.get(s));
            }
        }

        return tableOfVar2;
    }

    private int getInt(Map<String, Integer> table) {
        Lexer lexer = new Lexer();
        switch (lexer.recognize(stack.peek()).get(0).getLexeme()) {
            case VAR:
                return table.get(stack.pop());
            case DIGIT:
                return Integer.valueOf(stack.pop());
            default:
                System.err.println();
                System.exit(10);
        }
        return -1;
    }
}