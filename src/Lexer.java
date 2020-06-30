import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Lexer {

    public List<Token> recognize(String code) {

        List<Token> tokens = new ArrayList<>();

        boolean waitForSuccess = false;

        int count = 0;

        String partCode = "";

        Lexeme lexeme = null;

        if (code.length() == 0) {
            System.err.print("Error: Null input!");
            System.exit(1);
        }

        while (count < code.length()) {
            partCode += code.charAt(count);
            lexeme = findLexeme(partCode);
            if (lexeme == null) {
                if (waitForSuccess) {
                    waitForSuccess = false;
                    partCode = format(partCode);
                    lexeme = findLexeme(partCode);
                    tokens.add(new Token(lexeme, partCode));
                    partCode = "";
                    continue;
                } else {
                    System.err.print("Error: Lexeme '" + partCode + "' no find!");
                    System.exit(2);
                }
            } else {
                waitForSuccess = true;
            }
            count++;
        }
        tokens.add(new Token(lexeme, partCode));

        return tokens;

    }

    private Lexeme findLexeme(String text) {

        for (Lexeme lexeme :
                Lexeme.values()) {
            Matcher matcher = lexeme.getPattern().matcher(text);
            if (matcher.matches())
                return lexeme;
        }

        return null;
    }

    private String format(String s) {
        return s.substring(0, s.length() - 1);
    }

}

