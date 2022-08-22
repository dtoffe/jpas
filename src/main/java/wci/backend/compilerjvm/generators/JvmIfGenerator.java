package wci.backend.compilerjvm.generators;

import wci.backend.compilerjvm.JvmCodeGenerator;
import wci.backend.compilerjvm.Label;
import wci.backend.CompilerException;
import java.util.ArrayList;

import wci.intermediate.*;

import static wci.backend.compilerjvm.Instruction.*;

/**
 * <h1>IfGenerator</h1>
 *
 * <p>Generate code for an IF statement.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class JvmIfGenerator extends JvmStatementGenerator
{
    /**
     * Constructor.
     * @param parent the parent executor.
     */
    public JvmIfGenerator(JvmCodeGenerator parent)
    {
        super(parent);
    }

    /**
     * Generate code for an IF statement.
     * @param node the root node of the statement.
     */
    @Override
    public void generate(ICodeNode node)
        throws CompilerException
    {
        ArrayList<ICodeNode> children = node.getChildren();
        ICodeNode expressionNode = children.get(0);
        ICodeNode thenNode = children.get(1);
        ICodeNode elseNode = children.size() > 2 ? children.get(2) : null;
        JvmExpressionGenerator expressionGenerator = new JvmExpressionGenerator(this);
        JvmStatementGenerator  statementGenerator  = new JvmStatementGenerator(this);
        Label nextLabel = Label.newLabel();

        // Generate code for the boolean expression.
        expressionGenerator.generate(expressionNode);

        // Generate code for a THEN statement only.
        if (elseNode == null) {
            emit(IFEQ, nextLabel);
            localStack.decrease(1);

            statementGenerator.generate(thenNode);
        }

        // Generate code for a THEN statement and an ELSE statement.
        else {
            Label falseLabel = Label.newLabel();

            emit(IFEQ, falseLabel);
            localStack.decrease(1);

            statementGenerator.generate(thenNode);
            emit(GOTO, nextLabel);

            emitLabel(falseLabel);
            statementGenerator.generate(elseNode);
        }

        emitLabel(nextLabel);
    }
}
