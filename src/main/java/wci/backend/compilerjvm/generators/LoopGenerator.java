package wci.backend.compilerjvm.generators;

import wci.backend.compilerjvm.CodeGenerator;
import wci.backend.compilerjvm.Label;
import wci.backend.CompilerException;
import java.util.ArrayList;

import wci.intermediate.*;
import wci.intermediate.icodeimpl.*;

import static wci.backend.compilerjvm.Instruction.*;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;

/**
 * <h1>LoopGenerator</h1>
 *
 * <p>Generate code for a looping statement.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class LoopGenerator extends StatementGenerator
{
    /**
     * Constructor.
     * @param parent the parent executor.
     */
    public LoopGenerator(CodeGenerator parent)
    {
        super(parent);
    }

    /**
     * Generate code for a looping statement.
     * @param node the root node of the statement.
     */
    @Override
    public void generate(ICodeNode node)
        throws CompilerException
    {
        ArrayList<ICodeNode> loopChildren = node.getChildren();
        ExpressionGenerator expressionGenerator = new ExpressionGenerator(this);
        StatementGenerator statementGenerator = new StatementGenerator(this);
        Label loopLabel = Label.newLabel();
        Label nextLabel = Label.newLabel();

        emitLabel(loopLabel);

        // Generate code for the children of the LOOP node.
        for (ICodeNode child : loopChildren) {
            ICodeNodeTypeImpl childType = (ICodeNodeTypeImpl) child.getType();

            // TEST node: Generate code to test the boolean expression.
            if (childType == TEST) {
                ICodeNode expressionNode = child.getChildren().get(0);

                expressionGenerator.generate(expressionNode);
                emit(IFNE, nextLabel);

                localStack.decrease(1);
            }

            // Statement node: Generate code for the statement.
            else {
                statementGenerator.generate(child);
            }
        }

        emit(GOTO, loopLabel);
        emitLabel(nextLabel);
    }
}
