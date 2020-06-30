import java.util.*;

public class Parser {


    private int count = 0;

    private List<Token> tokens = new ArrayList<>();
    List<String> tableOfVar = new ArrayList<>();
    List<String> tokensOPIS = new ArrayList<>();
    private Stack<String> stack = new Stack<>();

    public boolean lang(List<Token> tokens) {

        for (Token token : tokens) {
            if (token.getLexeme() != Lexeme.WS) {
                this.tokens.add(token);
            }
        }

        if (this.tokens.size() == 0) {
            error(12, 1);
            return false;
        }

        if (body())
            return true;

        return false;
    }

    private boolean body() {
        if (getCurrentTokenLexemeInc() == Lexeme.L_F_SQU) {
            while (getCurrentTokenLexemeInc() != Lexeme.R_F_SQU) {
                count--;
                if (!expr()) {
                    error(13, 2);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean expr() {
        return init() || assign() || if_loop() || for_loop();
    }

    private boolean init() {
        if (getCurrentTokenLexemeInc() == Lexeme.TYPE) {
            if (getCurrentTokenLexemeInc() == Lexeme.VAR) {
                String var = getLastTokenValue();
                for (String s :
                        tableOfVar) {
                    if (var.equals(s)) {
                        error(13, 3);
                        return false;
                    }
                }
                tableOfVar.add(var);
                count--;
                if (assign_op()) {
                    if (getCurrentTokenLexemeInc() == Lexeme.SEM) {
                        return true;
                    }
                }
            }
        } else {
            count--;
            return false;
        }

        error(10, 4);
        return false;
    }

    private boolean assign() {
        if (assign_op()) {
            if (getCurrentTokenLexemeInc() == Lexeme.SEM) {
                return true;
            }
        } else {
            count--;
            return false;
        }

        error(10, 5);
        return false;
    }


    private boolean assign_op() {
        if (getCurrentTokenLexemeInc() == Lexeme.VAR) {
            check();
            tokensOPIS.add(getLastTokenValue());
            if (getCurrentTokenLexemeInc() == Lexeme.ASSIGN_OP) {
                stack.push(getLastTokenValue());
                if (value()) {
                    while (!stack.empty()) {
                        tokensOPIS.add(stack.pop());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean if_loop() {

        if (getCurrentTokenLexemeInc() == Lexeme.IF) {
            if (getCurrentTokenLexemeInc() == Lexeme.L_R_SQU) {
                if (value()) {
                    if (getCurrentTokenLexemeInc() == Lexeme.LOG_OP) {
                        String log_op = getLastTokenValue();
                        if (value()) {
                            tokensOPIS.add(log_op);
                            int p1 = tokensOPIS.size();
                            tokensOPIS.add("p1");
                            tokensOPIS.add("!F");
                            if (getCurrentTokenLexemeInc() == Lexeme.R_R_SQU) {
                                int a = tableOfVar.size();
                                if (body()) {
                                    while (tableOfVar.size() != a) {
                                        tableOfVar.remove(tableOfVar.size() - 1);
                                    }
                                    tokensOPIS.set(p1, String.valueOf(tokensOPIS.size() - 1));
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            count--;
            return false;
        }

        error(15, 6);
        return false;
    }

    private boolean for_loop() {

        if (getCurrentTokenLexemeInc() == Lexeme.FOR) {
            if (getCurrentTokenLexemeInc() == Lexeme.L_R_SQU) {
                int a = tableOfVar.size();
                if (init() || assign()) {
                    int p1 = tokensOPIS.size();
                    if (value()) {
                        if (getCurrentTokenLexemeInc() == Lexeme.LOG_OP) {
                            String log_op = getLastTokenValue();
                            if (value()) {
                                if (getCurrentTokenLexemeInc() == Lexeme.SEM) {
                                    tokensOPIS.add(log_op);
                                    int p2 = tokensOPIS.size();
                                    tokensOPIS.add("p1");
                                    tokensOPIS.add("!F");
                                    if (assign_op()) {
                                        if (getCurrentTokenLexemeInc() == Lexeme.R_R_SQU) {
                                            if (body()) {
                                                while (tableOfVar.size() != a) {
                                                    tableOfVar.remove(tableOfVar.size() - 1);
                                                }
                                                tokensOPIS.set(p2, String.valueOf(tokensOPIS.size() + 1));
                                                tokensOPIS.add(String.valueOf(p1));
                                                tokensOPIS.add("!");
                                                return true;
                                            }

                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        } else {
            count--;
            return false;
        }

        error(16, 11);
        return false;
    }


    private boolean value() {
        switch (getCurrentTokenLexemeInc()) {
            case VAR:
                check();
                tokensOPIS.add(getLastTokenValue());
                OPval();
                return true;
            case DIGIT:
                tokensOPIS.add(getLastTokenValue());
                OPval();
                return true;
            case L_R_SQU:
                stack.push(getLastTokenValue());
                if (value()) {
                    if (getCurrentTokenLexemeInc() == Lexeme.R_R_SQU) {
                        while (!stack.peek().equals("(")) {
                            tokensOPIS.add(stack.pop());
                        }
                        stack.pop();
                    }
                }
                OPval();
                return true;
            default:
                count--;
                return false;
        }
    }

    private void OPval() {

        int old_count = count;

        if (getCurrentTokenLexemeInc() == Lexeme.OP) {
            String Op = getLastTokenValue();
            while (getPriority(Op) <= getPriority(stack.peek())) {
                tokensOPIS.add(stack.pop());
            }
            stack.push(Op);
            value();
        } else
            count = old_count;
    }

    private Lexeme getCurrentTokenLexemeInc() {
        try {
            return tokens.get(count++).getLexeme();
        } catch (IndexOutOfBoundsException ex) {
            error(14, 8);
        }
        return null;
    }

    private String getLastTokenValue() {
        return tokens.get(count - 1).getValue();
    }

    private void error(int numberErr, int status) {

        switch (numberErr) {
            case 10:
                System.err.println("Initialization error!");
                break;
            case 11:
                System.err.println("Variable not initialized!");
                break;
            case 12:
                System.err.print("Error: Null input!");
                break;
            case 13:
                System.err.println(" Error: Syntax mistake!");
                break;
            case 14:
                System.err.println("Repletion");
                break;
            case 15:
                System.err.println("IF error!");
                break;
            case 16:
                System.err.println("FOR error!");
                break;
            default:
                System.err.println("Unknown error!");
                break;
        }
        System.exit(status);
    }

    private int getPriority(String str) {
        switch (str) {
            case "+":
                return 1;
            case "*":
                return 2;
            case "-":
                return 1;
            case "/":
                return 2;
            case "=":
                return 0;
            case "(":
                return 0;
            default:
                error(100, 9);
                return 0;
        }
    }

    private void check() {
        if (!tableOfVar.contains(getLastTokenValue())) {
            error(11, 10);
        }
    }

}

