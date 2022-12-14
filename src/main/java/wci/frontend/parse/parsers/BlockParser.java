package wci.frontend.parse.parsers;

import wci.frontend.parse.TopDownParser;
import wci.frontend.scan.Token;
import wci.frontend.scan.TokenType;
import wci.intermediate.*;

import static wci.frontend.ErrorCode.*;
import static wci.frontend.scan.TokenType.*;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;

/**
 * <h1>BlockParser</h1>
 *
 * <p>Parse a Pascal block.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class BlockParser extends TopDownParser
{
    /**
     * Constructor.
     * @param parent the parent parser.
     */
    public BlockParser(TopDownParser parent)
    {
        super(parent);
    }

    /**
     * Parse a block.
     * @param token the initial token.
     * @param routineId the symbol table entry of the routine name.
     * @return the root node of the parse tree.
     * @throws Exception if an error occurred.
     */
    public ICodeNode parse(Token token, SymTabEntry routineId)
        throws Exception
    {
        DeclarationsParser declarationsParser = new DeclarationsParser(this);
        StatementParser statementParser = new StatementParser(this);

        // Parse any declarations.
        declarationsParser.parse(token, routineId);

        token = synchronize(StatementParser.STMT_START_SET);
        TokenType tokenType = token.getType();
        ICodeNode rootNode = null;

        // Look for the BEGIN token to parse a compound statement.
        if (tokenType == BEGIN) {
            rootNode = statementParser.parse(token);
        }

        // Missing BEGIN: Attempt to parse anyway if possible.
        else {
            errorHandler.flag(token, MISSING_BEGIN, this);

            if (StatementParser.STMT_START_SET.contains(tokenType)) {
                rootNode = ICodeFactory.createICodeNode(COMPOUND);
                statementParser.parseList(token, rootNode, END, MISSING_END);
            }
        }

        return rootNode;
    }
}
