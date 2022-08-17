package wci.frontend.parse.parsers;

import wci.frontend.scan.TokenType;
import wci.frontend.parse.TopDownParser;
import wci.frontend.scan.Token;
import java.util.ArrayList;
import java.util.EnumSet;

import wci.frontend.*;
import wci.intermediate.*;
import wci.intermediate.symtabimpl.*;

import static wci.frontend.scan.TokenType.*;
import static wci.frontend.ErrorCode.*;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;
import static wci.intermediate.typeimpl.TypeFormImpl.*;
import static wci.intermediate.typeimpl.TypeKeyImpl.*;

/**
 * <h1>TypeSpecificationParser</h1>
 *
 * <p>Parse a Pascal type specification.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
class TypeSpecificationParser extends TopDownParser
{
    /**
     * Constructor.
     * @param parent the parent parser.
     */
    protected TypeSpecificationParser(TopDownParser parent)
    {
        super(parent);
    }

    // Synchronization set for starting a type specification.
    static final EnumSet<TokenType> TYPE_START_SET =
        SimpleTypeParser.SIMPLE_TYPE_START_SET.clone();
    static {
        TYPE_START_SET.add(TokenType.ARRAY);
        TYPE_START_SET.add(TokenType.RECORD);
        TYPE_START_SET.add(SEMICOLON);
    }

    /**
     * Parse a Pascal type specification.
     * @param token the current token.
     * @return the type specification.
     * @throws Exception if an error occurred.
     */
    public TypeSpec parse(Token token)
        throws Exception
    {
        // Synchronize at the start of a type specification.
        token = synchronize(TYPE_START_SET);

        switch ((TokenType) token.getType()) {

            case ARRAY: {
                ArrayTypeParser arrayTypeParser = new ArrayTypeParser(this);
                return arrayTypeParser.parse(token);
            }

            case RECORD: {
                RecordTypeParser recordTypeParser = new RecordTypeParser(this);
                return recordTypeParser.parse(token);
            }

            default: {
                SimpleTypeParser simpleTypeParser = new SimpleTypeParser(this);
                return simpleTypeParser.parse(token);
            }
        }
    }
}
