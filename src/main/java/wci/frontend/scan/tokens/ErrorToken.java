package wci.frontend.scan.tokens;

import wci.frontend.ErrorCode;
import wci.frontend.scan.Token;

import wci.frontend.*;

import static wci.frontend.scan.TokenType.*;

/**
 * <h1>PascalErrorToken</h1>
 *
 * <p>Pascal error token.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class ErrorToken extends Token
{
    /**
     * Constructor.
     * @param source the source from where to fetch subsequent characters.
     * @param errorCode the error code.
     * @param tokenText the text of the erroneous token.
     * @throws Exception if an error occurred.
     */
    public ErrorToken(Source source, ErrorCode errorCode,
                            String tokenText)
        throws Exception
    {
        super(source);

        this.text = tokenText;
        this.type = ERROR;
        this.value = errorCode;
    }

    /**
     * Do nothing.  Do not consume any source characters.
     * @throws Exception if an error occurred.
     */
    @Override
    protected void extract()
        throws Exception
    {
    }
}
