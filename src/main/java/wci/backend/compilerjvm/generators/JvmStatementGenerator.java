package wci.backend.compilerjvm.generators;

import wci.backend.compilerjvm.JvmCodeGenerator;
import wci.backend.compilerjvm.Directive;
import wci.backend.CompilerException;
import wci.intermediate.*;
import wci.intermediate.icodeimpl.*;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;

/**
 * <h1>StatementExecutor</h1>
 *
 * <p>Generate code for a statement.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class JvmStatementGenerator extends JvmCodeGenerator
{
    /**
     * Constructor.
     * @param parent the parent executor.
     */
    public JvmStatementGenerator(JvmCodeGenerator parent)
    {
        super(parent);
    }

    /**
     * Generate code for a statement.
     * To be overridden by the specialized statement executor subclasses.
     * @param node the root node of the statement.
     */
    @Override
    public void generate(ICodeNode node)
        throws CompilerException
    {
        ICodeNodeTypeImpl nodeType = (ICodeNodeTypeImpl) node.getType();
        int line = 0;

        if (nodeType != COMPOUND) {
            line = getLineNumber(node);
            emitDirective(Directive.LINE, line);
        }

        // Generate code for a statement according to the type of statement.
        switch (nodeType) {

            case COMPOUND: {
                JvmCompoundGenerator compoundGenerator =
                                      new JvmCompoundGenerator(this);
                compoundGenerator.generate(node);
                break;
            }

            case ASSIGN: {
                JvmAssignmentGenerator assignmentGenerator =
                    new JvmAssignmentGenerator(this);
                assignmentGenerator.generate(node);
                break;
            }

            case IF: {
                JvmIfGenerator ifGenerator = new JvmIfGenerator(this);
                ifGenerator.generate(node);
                break;
            }

            case SELECT: {
                JvmSelectGenerator selectGenerator = new JvmSelectGenerator(this);
                selectGenerator.generate(node);
                break;
            }

            case LOOP: {
                JvmLoopGenerator loopfGenerator = new JvmLoopGenerator(this);
                loopfGenerator.generate(node);
                break;
            }

            case CALL: {
                JvmCallGenerator callGenerator = new JvmCallGenerator(this);
                callGenerator.generate(node);
                break;
            }
        }

        // Verify that the stack height after each statement is 0.
        if (localStack.getSize() != 0) {
            throw new CompilerException(
                String.format("Stack size error: size = %d after line %d",
                              localStack.getSize(), line));
        }
    }

    /**
     * Get the source line number of a parse tree node.
     * @param node the parse tree node.
     * @return the line number.
     */
    private int getLineNumber(ICodeNode node)
    {
        Object lineNumber = null;

        // Go up the parent links to look for a line number.
        while ((node != null) &&
               ((lineNumber = node.getAttribute(ICodeKeyImpl.LINE)) == null)) {
            node = node.getParent();
        }

        return (Integer) lineNumber;
    }
}
