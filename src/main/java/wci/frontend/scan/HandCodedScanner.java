package wci.frontend.scan;

import wci.frontend.scan.tokens.EofToken;
import wci.frontend.scan.tokens.ErrorToken;
import wci.frontend.scan.tokens.NumberToken;
import wci.frontend.scan.tokens.SpecialSymbolToken;
import wci.frontend.scan.tokens.StringToken;
import wci.frontend.scan.tokens.WordToken;

import wci.frontend.*;

import static wci.frontend.ErrorCode.*;
import static wci.frontend.Source.EOF;

/**
 * <h1>PascalScanner</h1>
 *
 * <p>The Pascal scanner.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class HandCodedScanner extends Scanner
{
    /**
     * Constructor
     * @param source the source to be used with this scanner.
     */
    public HandCodedScanner(Source source)
    {
        super(source);
    }

    /**
     * Extract and return the next Pascal token from the source.
     * @return the next token.
     * @throws Exception if an error occurred.
     */
    @Override
    protected Token extractToken()
        throws Exception
    {
        skipWhiteSpace();

        Token token;
        char currentChar = currentChar();

        // Construct the next token.  The current character determines the
        // token type.
        if (currentChar == EOF) {
            token = new EofToken(source);
        }
        else if (Character.isLetter(currentChar)) {
            token = new WordToken(source);
        }
        else if (Character.isDigit(currentChar)) {
            token = new NumberToken(source);
        }
        else if (currentChar == '\'') {
            token = new StringToken(source);
        }
        else if (TokenType.SPECIAL_SYMBOLS
                 .containsKey(Character.toString(currentChar))) {
            token = new SpecialSymbolToken(source);
        }
        else {
            token = new ErrorToken(source, INVALID_CHARACTER,
                                         Character.toString(currentChar));
            nextChar();  // consume character
        }

        return token;
    }

    /**
     * Skip whitespace characters by consuming them.  A comment is whitespace.
     * @throws Exception if an error occurred.
     */
    private void skipWhiteSpace()
        throws Exception
    {
        char currentChar = currentChar();

        while (Character.isWhitespace(currentChar) || (currentChar == '{')) {

            // Start of a comment?
            if (currentChar == '{') {
                do {
                    currentChar = nextChar();  // consume comment characters
                } while ((currentChar != '}') && (currentChar != EOF));

                // Found closing '}'?
                if (currentChar == '}') {
                    currentChar = nextChar();  // consume the '}'
                }
            }

            // Not a comment.
            else {
                currentChar = nextChar();  // consume whitespace character
            }
        }
    }
}
