# JPas - A Pascal(ish) language compiler written in Java

This project is started from the source code in the book "Writing Compilers and Interpreters (3rd edition): A Modern Software Engineering Approach Using Java®, Third Edition", Copyright © 2009 by Ronald Mak, Published by Wiley Publishing, Inc.
The original code is "Copyright © 2009 by Ronald Mak - For instructional purposes only. No warranties."
No other license is stated in the original sources.
The original source code in the book can be [downloaded](https://apropos-logic.com/books/wci/) from https://apropos-logic.com/books/wci/

## Project objectives

I would like to write a more complete compiler for a "pascalish", not necessarily Pascal (whatever dialect) compatible language. I like Modula statements, for example. I take this project as a playground to learn how language features are implemented, specially the LLVM backend.

## Roadmap (hopefully...)

- DONE (kind of, see /docs/Original Grammar.txt) ~Extract from the original code the precise grammar of the Pascal subset supported, since it is surprisingly not specified in the book (at least explicitly, in a succint (E)BNF grammar form).~
- Partially DONE (more can come later). ~Rename and reorganize packages and project structure.~
- Mostly DONE ~Eliminate the framework parts intended to make support for another languages straightforward, since it is not something I intend to do. Keep the parts that make writing alternative scanners, parsers or code generation backends possible.~
- (WORKING ON IT...) Write an LLVM based code generator backend.
- Implement Modula style statements (IF <cond> THEN <statements> [[ELSIF <statements>] ELSE <statements>] END instead of IF <cond> THEN (<statement> | BEGIN <statements> END) [ELSE IF... etc.).
- Consider if there is any benefit in building a specific language AST instead of the generic ICode* implementation, do it if appropiate.
- Add some missing Pascal/Modula style language features (sets, variant records, pointers, jedi force, etc...)
- Dominate the world...

## Usage

Build the project, then open a terminal in the \target subdirectory and run this:

```
> java -jar .\classes\jpas-1.0-SNAPSHOT-jar-with-dependencies.jar compile ..\examples\HelloOnce.pas
```

You should see this output printed:

```
001 PROGRAM HelloOnce;
002
003 BEGIN
004     writeln('Hello, world.')
005 END.

                   5 source lines.
                   0 syntax errors.
                0,04 seconds total parsing time.

                   0 instructions generated.
                0,65 seconds total code generation time.
```

Within the \target subdirectory you should see a file named "helloonce.ll" with the following content:

```
; ModuleID = 'helloonce'
source_filename = "helloonce"
target datalayout = "e-m:w-p270:32:32-p271:32:32-p272:64:64-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-windows-msvc"

define void @main() {
entry:
  ret void
}
```

That's all the LLVM code generator does as of now.
