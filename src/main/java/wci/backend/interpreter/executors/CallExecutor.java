package wci.backend.interpreter.executors;

import wci.backend.interpreter.*;
import wci.intermediate.*;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;
import static wci.intermediate.symtabimpl.RoutineCodeImpl.*;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;

/**
 * <h1>CallExecutor</h1>
 *
 * <p>Execute a call to a procedure or function.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class CallExecutor extends StatementExecutor
{
    /**
     * Constructor.
     * @param parent the parent executor.
     */
    public CallExecutor(Executor parent)
    {
        super(parent);
    }

    /**
     * Execute procedure or function call statement.
     * @param node the root node of the call.
     * @return null.
     */
    @Override
    public Object execute(ICodeNode node)
    {
        SymTabEntry routineId = (SymTabEntry) node.getAttribute(ID);
        RoutineCode routineCode =
                        (RoutineCode) routineId.getAttribute(ROUTINE_CODE);
        CallExecutor callExecutor = routineCode == DECLARED
                                    ? new CallDeclaredExecutor(this)
                                    : new CallStandardExecutor(this);

        ++executionCount;  // count the call statement
        return callExecutor.execute(node);
    }
}
