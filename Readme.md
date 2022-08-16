# JPas - A Pascal compiler written in Java

This project is started from the source code in the book "Writing Compilers and Interpreters (3rd edition): A Modern Software Engineering Approach Using Java®, Third Edition", Copyright © 2009 by Ronald Mak, Published by Wiley Publishing, Inc.
The original code is "Copyright © 2009 by Ronald Mak - For instructional purposes only. No warranties."
No other license is stated in the original sources.
The original source code in the book can be [downloaded](https://apropos-logic.com/books/wci/) from https://apropos-logic.com/books/wci/

## Project objectives

I would like to write a more complete compiler for a "pascalish", not necessarily Pascal (whatever dialect) compatible language. I like Modula statements, for example. I take this project as a playground to learn how language features are implemented.

## Roadmap (hopefully...)

- Extract from the original code the precise grammar of the Pascal subset supported, since it is surprisingly not specified in the book (at least explicitly, in a succint (E)BNF grammar form).
- Rename and reorganize packages and project structure
- Eliminate the framework parts intended to make support for another languages straightforward, since it is not something I intend to do. Keep the parts that make writing alternative scanners, parsers or code generation backends possible.
- Implement Modula style statements (IF <cond> THEN <statements> [[ELSIF <statements>] ELSE <statements>] END instead of IF <cond> THEN (<statement> | BEGIN <statements> END) [ELSE IF... etc.).
- Consider if there is any benefit in building a specific language AST instead of the generic ICode* implementation, do it if appropiate.
- Write an LLVM based code generator backend.
- Add some missing Pascal/Modula style language features (sets, variant records, pointers, jedi force, etc...)
- Dominate the world...
