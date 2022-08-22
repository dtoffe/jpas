package wci.backend.compilerjvm.generators;

import wci.backend.compilerjvm.JvmCodeGenerator;
import wci.intermediate.*;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;
import static wci.intermediate.symtabimpl.RoutineCodeImpl.*;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;

/**
 * <h1>CallExecutor</h1>
 *
 * <p>Generate code to call to a procedure or function.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class JvmCallGenerator extends JvmStatementGenerator
{
    /**
     * Constructor.
     * @param parent the parent executor.
     */
    public JvmCallGenerator(JvmCodeGenerator parent)
    {
        super(parent);
    }

    /**
     * Generate code to call a procedure or function.
     * @param node the root node of the call.
     */
    @Override
    public void generate(ICodeNode node)
    {
        SymTabEntry routineId = (SymTabEntry) node.getAttribute(ID);
        RoutineCode routineCode =
                        (RoutineCode) routineId.getAttribute(ROUTINE_CODE);
        JvmCallGenerator callGenerator = routineCode == DECLARED
                                          ? new JvmCallDeclaredGenerator(this)
                                          : new JvmCallStandardGenerator(this);

        callGenerator.generate(node);
    }
}
