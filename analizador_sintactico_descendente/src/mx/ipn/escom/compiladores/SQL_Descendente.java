//Elaborado por: Ramirez Contreras Angel Humberto (5CV1) y Diaz Rosales Mauricio Yael (3CV15)
package mx.ipn.escom.compiladores;

public class SQL_Descendente {
    private String input;
    private int index;

    public void parse(String input) throws SyntaxErrorException {
        this.input = input;
        this.index = 0;

        parseSelectStatement();

        if (index != input.length()) {
            throw new SyntaxErrorException("Syntax error at index " + index);
        }

        System.out.println("Syntax is correct!");
    }

    private void parseSelectStatement() throws SyntaxErrorException {
        parseKeyword("SELECT");
        parseColumns();
        parseKeyword("FROM");
        parseTables();
        if (matchKeyword("WHERE")) {
            parseCondition();
        }
    }

    private void parseColumns() throws SyntaxErrorException {
        parseIdentifier();
        while (matchSymbol(",")) {
            parseIdentifier();
        }
    }

    private void parseTables() throws SyntaxErrorException {
        parseIdentifier();
        while (matchSymbol(",")) {
            parseIdentifier();
        }
    }

    private void parseCondition() throws SyntaxErrorException {
        parseIdentifier();
        parseOperator();
        parseValue();
    }

    private void parseIdentifier() throws SyntaxErrorException {
        if (index >= input.length() || !Character.isLetter(input.charAt(index))) {
            throw new SyntaxErrorException("Syntax error at index " + index);
        }
        while (index < input.length() && (Character.isLetterOrDigit(input.charAt(index)) || input.charAt(index) == '_')) {
            index++;
        }
    }

    private void parseOperator() throws SyntaxErrorException {
        if (matchSymbol("=") || matchSymbol("<>") || matchSymbol("<") || matchSymbol(">") || matchSymbol("<=") || matchSymbol(">=")) {
            // Operador válido
        } else {
            throw new SyntaxErrorException("Syntax error at index " + index);
        }
    }

    private void parseValue() throws SyntaxErrorException {
        if (index >= input.length() || !Character.isDigit(input.charAt(index))) {
            throw new SyntaxErrorException("Syntax error at index " + index);
        }
        while (index < input.length() && Character.isDigit(input.charAt(index))) {
            index++;
        }
    }

    private boolean matchKeyword(String keyword) {
        if (index + keyword.length() <= input.length() && input.substring(index, index + keyword.length()).equalsIgnoreCase(keyword)) {
            index += keyword.length();
            return true;
        }
        return false;
    }

    private boolean matchSymbol(String symbol) {
        if (index + symbol.length() <= input.length() && input.substring(index, index + symbol.length()).equals(symbol)) {
            index += symbol.length();
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        SQL_Descendente parser = new SQL_Descendente();
        try {
            parser.parse("SELECT column1, column2 FROM table1, table2 WHERE column1 = 1");
        } catch (SyntaxErrorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void parseKeyword(String select) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static class SyntaxErrorException extends Exception {
        public SyntaxErrorException(String message) {
            super(message);
        }
    }
}