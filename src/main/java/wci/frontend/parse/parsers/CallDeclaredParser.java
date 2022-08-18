package wci.frontend.parse.parsers;

import wci.frontend.parse.TopDownParser;
import wci.frontend.scan.Token;
import wci.intermediate.*;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;

/**
 * <h1>DeclaredCallParser</h1>
 *
 * <p>Parse a called to a declared procedure or function.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class CallDeclaredParser extends CallParser
{
    /**
     * Constructor.
     * @param parent the parent parser.
     */
    public CallDeclaredParser(TopDownParser parent)
    {
        super(parent);
    }

    /**
     * Parse a call to a declared procedure or function.
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token)
        throws Exception
    {
        // Create the CALL node.
        ICodeNode callNode = ICodeFactory.createICodeNode(CALL);
        SymTabEntry pfId = symTabStack.lookup(token.getText().toLowerCase());
        callNode.setAttribute(ID, pfId);
        callNode.setTypeSpec(pfId.getTypeSpec());

        token = nextToken();  // consume procedure or function identifier

        ICodeNode parmsNode = parseActualParameters(token, pfId,
                                                    true, false, false);

        callNode.addChild(parmsNode);
        return callNode;
    }
}
