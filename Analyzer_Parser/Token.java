package Analyzer_Parser;

public class Token {
    public TokenType type;
    public String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

}

enum TokenType {
    KEYWORD,
    IDENTIFIER,
    INTEGER,
    OPERATOR,
    STRING,
    PUNCTUATION,
    DELETE,
    EndOfTokens
}
