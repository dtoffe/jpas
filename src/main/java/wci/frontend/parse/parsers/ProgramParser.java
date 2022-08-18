package wci.frontend.parse.parsers;

import java.util.EnumSet;

import wci.frontend.parse.TopDownParser;
import wci.frontend.scan.Token;
import wci.frontend.scan.TokenType;

import wci.intermediate.*;

import static wci.frontend.ErrorCode.*;
import static wci.frontend.scan.TokenType.*;

/**
 * <h1>ProgramParser</h1>
 *
 * <p>Parse a Pascal program.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class ProgramParser extends DeclarationsParser
{
    /**
     * Constructor.
     * @param parent the parent parser.
     */
    public ProgramParser(TopDownParser parent)
    {
        super(parent);
    }

    // Synchronization set to start a program.
    static final EnumSet<TokenType> PROGRAM_START_SET =
        EnumSet.of(PROGRAM, SEMICOLON);
    static {
        PROGRAM_START_SET.addAll(DeclarationsParser.DECLARATION_START_SET);
    }

    /**
     * Parse a program.
     * @param token the initial token.
     * @param parentId the symbol table entry of the parent routine's name.
     * @return null
     * @throws Exception if an error occurred.
     */
    @Override
    public SymTabEntry parse(Token token, SymTabEntry parentId)
        throws Exception
    {
        token = synchronize(PROGRAM_START_SET);

        // Parse the program.
        DeclaredRoutineParser routineParser = new DeclaredRoutineParser(this);
        routineParser.parse(token, parentId);

        // Look for the final period.
        token = currentToken();
        if (token.getType() != DOT) {
            errorHandler.flag(token, MISSING_PERIOD, this);
        }

        return null;
    }
}
