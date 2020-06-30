import java.util.List;

public class Main {

    public static void main(String[] args) {

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        StackMachine stackMachine = new StackMachine();

        String code = "{int a = 6; int b = 7; int c = 0; int d = 0; for (int i = 0; i < a; i = i+1) {if((b-a) == 1){b = b+1; if((b-a) == 2){ c = 18; } }c = c + 1;for (int j = 0; j < 1; j = j+1) {d = 10; } } int k = 0; k = (c+a)/d*b+a;}";

        System.out.println("\n" + "Code:");
        System.out.println("[" + code + "]");

        List<Token> tokens = lexer.recognize(code);

        System.out.println("\n" + "Code is: ");
        System.out.println(parser.lang(tokens));

        System.out.println("\n" + "List of command:");
        System.out.println(parser.tokensOPIS);

        System.out.println("\n" + "Implementation of command:");
        System.out.println(stackMachine.stackMachine(parser));
    }

}
