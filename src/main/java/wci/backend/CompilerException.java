package wci.backend;

/**
 * <h1>PascalCompilerException</h1>
 *
 * <p>Error during the Pascal compiler's code generation.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class CompilerException extends Exception
{
    public CompilerException(String message)
    {
        super(message);
    }
}
