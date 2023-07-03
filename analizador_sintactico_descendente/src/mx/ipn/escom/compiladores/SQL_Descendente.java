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
            throw new SyntaxErrorException("Error de Sintaxis en el índice " + index);
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
            skipWhiteSpace();
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
    skipWhiteSpace();
    if (index >= input.length() || !(Character.isLetterOrDigit(input.charAt(index)) || input.charAt(index) == '_')) {
        throw new SyntaxErrorException("Error de Sintaxis en el índice " + index);
    }
    while (index < input.length() && (Character.isLetterOrDigit(input.charAt(index)) || input.charAt(index) == '_')) {
        index++;
    }
    if (index < input.length() && input.charAt(index) == '.') {
        index++; // Avanzar el índice para omitir el punto "."
        parseIdentificador(); // Llamar recursivamente para analizar el siguiente identificador
    }
}

    private void parseOperador() throws SyntaxErrorException {
        // Aquí puedes agregar lógica para manejar otros operadores SQL
        if (matchSymbol("=") || matchSymbol("<>") || matchSymbol("<") || matchSymbol(">") || matchSymbol("<=") || matchSymbol(">=")) {
            // Operador válido
        } else {
            throw new SyntaxErrorException("Error de Sintaxis en el índice " + index);
        }
    }

    private void parseValor() throws SyntaxErrorException {
        if (index >= input.length() || !Character.isDigit(input.charAt(index))) {
            throw new SyntaxErrorException("Error de Sintaxis en el índice " + index);
        }
        while (index < input.length() && Character.isDigit(input.charAt(index))) {
            index++;
        }
    }

    private boolean matchKeyword(String keyword) {
        if (index + keyword.length() <= input.length() && input.substring(index, index + keyword.length()).equalsIgnoreCase(keyword)) {
            index += keyword.length();
            skipWhiteSpace();
            return true;
        }
        return false;
    }

    private boolean matchSymbol(String symbol) {
        if (index + symbol.length() <= input.length() && input.substring(index, index + symbol.length()).equals(symbol)) {
            index += symbol.length();
            skipWhiteSpace();
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
            parser.validateEndOfQuery();
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
            throw new SyntaxErrorException("Error de Sintaxis en el indice " + (index - keyword.length()));
        }
    }

    private static class SyntaxErrorException extends Exception {
        public SyntaxErrorException(String message) {
            super(message);
        }
    }
    
    private void validateEndOfQuery() throws SyntaxErrorException {
        // Verificar si hay algún símbolo o palabra clave adicional al final de la consulta
        skipWhiteSpace();
        if (index < input.length()) {
            throw new SyntaxErrorException("Error de Sintaxis: Elementos adicionales después de la consulta en el índice " + index);
        }
    }
}