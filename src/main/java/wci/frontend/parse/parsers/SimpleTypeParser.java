package wci.frontend.parse.parsers;

import java.util.EnumSet;

import wci.frontend.parse.TopDownParser;
import wci.frontend.scan.Token;
import wci.frontend.scan.TokenType;

import wci.intermediate.*;
import wci.intermediate.symtabimpl.*;

import static wci.frontend.ErrorCode.*;
import static wci.frontend.scan.TokenType.*;
import static wci.intermediate.symtabimpl.DefinitionImpl.*;

/**
 * <h1>SimpleTypeParser</h1>
 *
 * <p>Parse a simple Pascal type (identifier, subrange, enumeration)
 * specification.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
class SimpleTypeParser extends TypeSpecificationParser
{
    /**
     * Constructor.
     * @param parent the parent parser.
     */
    protected SimpleTypeParser(TopDownParser parent)
    {
        super(parent);
    }

    // Synchronization set for starting a simple type specification.
    static final EnumSet<TokenType> SIMPLE_TYPE_START_SET =
        ConstantDefinitionsParser.CONSTANT_START_SET.clone();
    static {
        SIMPLE_TYPE_START_SET.add(LEFT_PAREN);
        SIMPLE_TYPE_START_SET.add(COMMA);
        SIMPLE_TYPE_START_SET.add(SEMICOLON);
    }

    /**
     * Parse a simple Pascal type specification.
     * @param token the current token.
     * @return the simple type specification.
     * @throws Exception if an error occurred.
     */
    @Override
    public TypeSpec parse(Token token)
        throws Exception
    {
        // Synchronize at the start of a simple type specification.
        token = synchronize(SIMPLE_TYPE_START_SET);

        switch ((TokenType) token.getType()) {

            case IDENTIFIER: {
                String name = token.getText().toLowerCase();
                SymTabEntry id = symTabStack.lookup(name);

                if (id != null) {
                    Definition definition = id.getDefinition();

                    // It's either a type identifier
                    // or the start of a subrange type.
                    if (definition == DefinitionImpl.TYPE) {
                        id.appendLineNumber(token.getLineNumber());
                        token = nextToken();  // consume the identifier

                        // Return the type of the referent type.
                        return id.getTypeSpec();
                    }
                    else if ((definition != CONSTANT) &&
                             (definition != ENUMERATION_CONSTANT)) {
                        errorHandler.flag(token, NOT_TYPE_IDENTIFIER, this);
                        token = nextToken();  // consume the identifier
                        return null;
                    }
                    else {
                        SubrangeTypeParser subrangeTypeParser =
                            new SubrangeTypeParser(this);
                        return subrangeTypeParser.parse(token);
                    }
                }
                else {
                    errorHandler.flag(token, IDENTIFIER_UNDEFINED, this);
                    token = nextToken();  // consume the identifier
                    return null;
                }
            }

            case LEFT_PAREN: {
                EnumerationTypeParser enumerationTypeParser =
                    new EnumerationTypeParser(this);
                return enumerationTypeParser.parse(token);
            }

            case COMMA:
            case SEMICOLON: {
                errorHandler.flag(token, INVALID_TYPE, this);
                return null;
            }

            default: {
                SubrangeTypeParser subrangeTypeParser =
                    new SubrangeTypeParser(this);
                return subrangeTypeParser.parse(token);
            }
        }
    }
}
