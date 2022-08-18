package wci.frontend.parse.parsers;

import java.util.EnumSet;

import wci.frontend.parse.TopDownParser;
import wci.frontend.scan.Token;
import wci.frontend.scan.TokenType;

import wci.intermediate.*;

import static wci.frontend.ErrorCode.*;
import static wci.frontend.scan.TokenType.*;
import static wci.intermediate.symtabimpl.DefinitionImpl.*;
import static wci.intermediate.typeimpl.TypeFormImpl.RECORD;
import static wci.intermediate.typeimpl.TypeKeyImpl.*;

/**
 * <h1>RecordTypeParser</h1>
 *
 * <p>Parse a Pascal record type specification.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
class RecordTypeParser extends TypeSpecificationParser
{
    /**
     * Constructor.
     * @param parent the parent parser.
     */
    protected RecordTypeParser(TopDownParser parent)
    {
        super(parent);
    }

    // Synchronization set for the END.
    private static final EnumSet<TokenType> END_SET =
        DeclarationsParser.VAR_START_SET.clone();
    static {
        END_SET.add(END);
        END_SET.add(SEMICOLON);
    }

    /**
     * Parse a Pascal record type specification.
     * @param token the current token.
     * @return the record type specification.
     * @throws Exception if an error occurred.
     */
    @Override
    public TypeSpec parse(Token token)
        throws Exception
    {
        TypeSpec recordType = TypeFactory.createType(RECORD);
        token = nextToken();  // consume RECORD

        // Push a symbol table for the RECORD type specification.
        recordType.setAttribute(RECORD_SYMTAB, symTabStack.push());

        // Parse the field declarations.
        VariableDeclarationsParser variableDeclarationsParser =
            new VariableDeclarationsParser(this);
        variableDeclarationsParser.setDefinition(FIELD);
        variableDeclarationsParser.parse(token, null);

        // Pop off the record's symbol table.
        symTabStack.pop();

        // Synchronize at the END.
        token = synchronize(END_SET);

        // Look for the END.
        if (token.getType() == END) {
            token = nextToken();  // consume END
        }
        else {
            errorHandler.flag(token, MISSING_END, this);
        }

        return recordType;
    }
}
