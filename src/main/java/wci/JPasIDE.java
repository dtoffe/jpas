package wci;

import wci.ide.*;

/**
 * <h1>PascalIDE</h1>
 *
 * <p>The simple Pascal Integrated Development Environment.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class JPasIDE
{
    public JPasIDE()
    {
        new IDEFrame();
    }

    public static void main(String[] args)
    {
        new JPasIDE();
    }
}
