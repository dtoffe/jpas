package wci.frontend.parse;

import java.util.EnumSet;

import wci.frontend.ErrorHandler;
import wci.frontend.parse.parsers.ProgramParser;
import wci.frontend.scan.Scanner;
import wci.frontend.scan.Token;
import wci.frontend.scan.tokens.EofToken;

import wci.intermediate.symtabimpl.*;
import wci.message.*;

import static wci.frontend.ErrorCode.*;
import static wci.message.MessageType.PARSER_SUMMARY;

/**
 * <h1>PascalParserTD</h1>
 *
 * <p>The top-down Pascal parser.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class TopDownParser extends Parser
{
    protected static ErrorHandler errorHandler = new ErrorHandler();

    /**
     * Constructor.
     * @param scanner the scanner to be used with this parser.
     */
    public TopDownParser(Scanner scanner)
    {
        super(scanner);
    }

    /**
     * Constructor for subclasses.
     * @param parent the parent parser.
     */
    public TopDownParser(TopDownParser parent)
    {
        super(parent.getScanner());
    }

    /**
     * Getter.
     * @return the error handler.
     */
    public ErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    /**
     * Parse a Pascal source program and generate the symbol table
     * and the intermediate code.
     * @throws Exception if an error occurred.
     */
    @Override
    public void parse()
        throws Exception
    {
        long startTime = System.currentTimeMillis();
        Predefined.initialize(symTabStack);

        try {
            Token token = nextToken();

            // Parse a program.
            ProgramParser programParser = new ProgramParser(this);
            programParser.parse(token, null);
            token = currentToken();

            // Send the parser summary message.
            float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
            sendMessage(new Message(PARSER_SUMMARY,
                                    new Number[] {token.getLineNumber(),
                                                  getErrorCount(),
                                                  elapsedTime}));
        }
        catch (java.io.IOException ex) {
            errorHandler.abortTranslation(IO_ERROR, this);
        }
    }

    /**
     * Return the number of syntax errors found by the parser.
     * @return the error count.
     */
    @Override
    public int getErrorCount()
    {
        return errorHandler.getErrorCount();
    }

    /**
     * Synchronize the parser.
     * @param syncSet the set of token types for synchronizing the parser.
     * @return the token where the parser has synchronized.
     * @throws Exception if an error occurred.
     */
    public Token synchronize(EnumSet syncSet)
        throws Exception
    {
        Token token = currentToken();

        // If the current token is not in the synchronization set,
        // then it is unexpected and the parser must recover.
        if (!syncSet.contains(token.getType())) {

            // Flag the unexpected token.
            errorHandler.flag(token, UNEXPECTED_TOKEN, this);

            // Recover by skipping tokens that are not
            // in the synchronization set.
            do {
                token = nextToken();
            } while (!(token instanceof EofToken) &&
                     !syncSet.contains(token.getType()));
       }

       return token;
    }
}
