//Elaborado por: Ramirez Contreras Angel Humberto (5CV1) y Diaz Rosales Mauricio Yael (3CV15)
package mx.ipn.escom.compiladores;

import java.util.Scanner;

public class SQL_Descendente {
    private String input;
    private int index;

    public void parse(String input) throws SyntaxErrorException {
        this.input = input;
        this.index = 0;

        parseSelectStatement();

        if (index != input.length()) {
            throw new SyntaxErrorException("Error de Sintaxis en el indice " + index);
        }

        System.out.println("Sintaxis correcta");
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
        parseIdentificador();
        while (matchSymbol(",")) {
            parseIdentificador();
        }
    }

    private void parseTables() throws SyntaxErrorException {
        parseIdentificador();
        while (matchSymbol(",")) {
            parseIdentificador();
        }
    }

    private void parseCondition() throws SyntaxErrorException {
        parseIdentificador();
        parseOperador();
        parseValor();
    }

    private void parseIdentificador() throws SyntaxErrorException {
        if (index >= input.length() || !Character.isLetter(input.charAt(index))) {
            throw new SyntaxErrorException("Error de Sintaxis en el indice " + index);
        }
        while (index < input.length() && (Character.isLetterOrDigit(input.charAt(index)) || input.charAt(index) == '_')) {
            index++;
        }
    }

    private void parseOperador() throws SyntaxErrorException {
        if (matchSymbol("=") || matchSymbol("<>") || matchSymbol("<") || matchSymbol(">") || matchSymbol("<=") || matchSymbol(">=")) {
            // Operador válido
        } else {
            throw new SyntaxErrorException("Error de Sintaxis en el indice " + index);
        }
    }

    private void parseValor() throws SyntaxErrorException {
        if (index >= input.length() || !Character.isDigit(input.charAt(index))) {
            throw new SyntaxErrorException("Error de Sintaxis en el indice " + index);
        }
        while (index < input.length() && Character.isDigit(input.charAt(index))) {
            index++;
        }
    }

    private boolean matchKeyword(String keyword) {
        if (index + keyword.length() <= input.length() && input.substring(index, index + keyword.length()).equalsIgnoreCase(keyword)) {
            index += keyword.length();
            skipWhiteSpace(); // Agrega la función para omitir espacios en blanco después de una palabra clave
            return true;
        }
        return false;
    }

    private boolean matchSymbol(String symbol) {
        if (index + symbol.length() <= input.length() && input.substring(index, index + symbol.length()).equals(symbol)) {
            index += symbol.length();
            skipWhiteSpace(); // Agrega la función para omitir espacios en blanco después de un símbolo
            return true;
        }
        return false;
    }

    private void skipWhiteSpace() {
        while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
            index++;
        }
    }

    public static void main(String[] args) {
    SQL_Descendente parser = new SQL_Descendente();
    Scanner scanner = new Scanner(System.in);

    System.out.print("Ingrese la consulta SQL: ");
    String input = scanner.nextLine();

    try {
        parser.parse(input);
        System.out.println("Sintaxis correcta");
    } catch (SyntaxErrorException e) {
        System.out.println(e.getMessage());
    }
}

    private void parseKeyword(String keyword) throws SyntaxErrorException {
    skipWhiteSpace();
    if (index + keyword.length() <= input.length() && input.substring(index, index + keyword.length()).equalsIgnoreCase(keyword)) {
        index += keyword.length();
        skipWhiteSpace();
    } else {
        throw new SyntaxErrorException("Error de Sintaxis en el indice " + index);
    }
    }

    private static class SyntaxErrorException extends Exception {
        public SyntaxErrorException(String message) {
            super(message);
        }
    }
}