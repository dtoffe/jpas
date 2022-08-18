package wci.backend.interpreter.debuggerimpl;

import java.util.ArrayList;

import wci.backend.*;
import wci.backend.interpreter.*;
import wci.intermediate.*;
import wci.message.Message;

import static wci.ide.IDEControl.*;

/**
 * <h1>GUIDebugger</h1>
 *
 * <p>GUI version of the interactive source-level debugger.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class GUIDebugger extends Debugger
{
    private CommandProcessor commandProcessor;

    /**
     * Constructor.
     * @param backend the back end.
     * @param runtimeStack the runtime stack.
     */
    public GUIDebugger(Backend backend, RuntimeStack runtimeStack)
    {
        super(backend, runtimeStack);
        commandProcessor = new CommandProcessor(this);
    }

    /**
     * Process a message from the back end.
     * @param message the message.
     */
    @Override
    public void processMessage(Message message)
    {
        commandProcessor.processMessage(message);
    }

    /**
     * Display a prompt for a debugger command.
     */
    @Override
    public void promptForCommand() {}

    /**
     * Parse a debugger command.
     * @return true to parse another command immediately, else false.
     */
    @Override
    public boolean parseCommand()
    {
        return commandProcessor.parseCommand();
    }

    /**
     * Process a source statement.
     * @param lineNumber the statement line number.
     */
    @Override
    public void atStatement(Integer lineNumber)
    {
        System.out.println(DEBUGGER_AT_TAG + lineNumber);
    }

    /**
     * Process a breakpoint at a statement.
     * @param lineNumber the statement line number.
     */
    @Override
    public void atBreakpoint(Integer lineNumber)
    {
        System.out.println(DEBUGGER_BREAK_TAG + lineNumber);
    }

    /**
     * Process the current value of a watchpoint variable.
     * @param lineNumber the current statement line number.
     * @param name the variable name.
     * @param value the variable's value.
     */
    @Override
    public void atWatchpointValue(Integer lineNumber,
                                  String name, Object value)
    {
    }

    /**
     * Process assigning a new value to a watchpoint variable.
     * @param lineNumber the current statement line number.
     * @param name the variable name.
     * @param value the new value.
     */
    @Override
    public void atWatchpointAssignment(Integer lineNumber,
                                       String name, Object value)
    {
    }

    /**
     * Process calling a declared procedure or function.
     * @param lineNumber the current statement line number.
     * @param routineName the routine name.
     */
    @Override
    public void callRoutine(Integer lineNumber, String routineName)
    {
    }

    /**
     * Process returning from a declared procedure or function.
     * @param lineNumber the current statement line number.
     * @param routineName the routine name.
     */
    @Override
    public void returnRoutine(Integer lineNumber, String routineName)
    {
    }

    /**
     * Display a value.
     * @param valueString the value string.
     */
    @Override
    public void displayValue(String valueString)
    {
        System.out.println(valueString);
    }

    /**
     * Display the call stack.
     * @param stack the list of elements of the call stack.
     */
    @Override
    public void displayCallStack(ArrayList stack)
    {
        // Call stack header.
        System.out.println(DEBUGGER_ROUTINE_TAG + -1);

        for (Object item : stack) {

            // Name of a procedure or function.
            if (item instanceof SymTabEntry) {
                SymTabEntry routineId = (SymTabEntry) item;
                String routineName = routineId.getName();
                int level = routineId.getSymTab().getNestingLevel();
                Definition definition = routineId.getDefinition();

                System.out.println(DEBUGGER_ROUTINE_TAG + level + ":" +
                                   definition.getText().toUpperCase() + " " +
                                   routineName);
            }

            // Variable name-value pair.
            else if (item instanceof NameValuePair) {
                NameValuePair pair = (NameValuePair) item;
                System.out.print(DEBUGGER_VARIABLE_TAG +
                                 pair.getVariableName() + ":");
                displayValue(pair.getValueString());
            }
        }

        // Call stack footer.
        System.out.println(DEBUGGER_ROUTINE_TAG + -2);
    }

    /**
     * Terminate execution of the source program.
     */
    @Override
    public void quit()
    {
        System.out.println("!INTERPRETER:Program terminated.");
        System.exit(-1);
    }

    /**
     * Handle a debugger command error.
     * @param errorMessage the error message.
     */
    @Override
    public void commandError(String errorMessage)
    {
        runtimeError(errorMessage, 0);
    }

    /**
     * Handle a source program runtime error.
     * @param errorMessage the error message.
     * @param lineNumber the source line number where the error occurred.
     */
    @Override
    public void runtimeError(String errorMessage, Integer lineNumber)
    {
        System.out.print("*** RUNTIME ERROR");
        if (lineNumber != null) {
            System.out.print(" AT LINE " +
                             String.format("%03d", lineNumber));
        }
        System.out.println(": " + errorMessage);
    }
}
