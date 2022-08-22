package wci.backend.compilerjvm.generators;

import wci.backend.compilerjvm.JvmCodeGenerator;
import wci.backend.CompilerException;
import java.util.ArrayList;

import wci.intermediate.*;

import static wci.backend.compilerjvm.Instruction.*;

/**
 * <h1>CompoundExecutor</h1>
 *
 * <p>Generate code for a compound statement.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class JvmCompoundGenerator extends JvmStatementGenerator
{
    /**
     * Constructor.
     * @param parent the parent executor.
     */
    public JvmCompoundGenerator(JvmCodeGenerator parent)
    {
        super(parent);
    }

    /**
     * Generate code for a compound statement.
     * @param node the root node of the compound statement.
     */
    @Override
    public void generate(ICodeNode node)
        throws CompilerException
    {
        ArrayList<ICodeNode> children = node.getChildren();

        // Loop over the statement children of the COMPOUND node and generate
        // code for each statement. Emit a NOP is there are no statements.
        if (children.size() == 0) {
            emit(NOP);
        }
        else {
            JvmStatementGenerator statementGenerator =
                                   new JvmStatementGenerator(this);

            for (ICodeNode child : children) {
                statementGenerator.generate(child);
            }
        }
    }
}
