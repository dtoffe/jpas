package wci.backend.compilerllvm.generators;

import wci.backend.LocalStack;
import wci.backend.LocalVariables;
import wci.backend.CompilerException;
import java.util.ArrayList;
import org.bytedeco.llvm.LLVM.LLVMBasicBlockRef;
import org.bytedeco.llvm.LLVM.LLVMTypeRef;
import org.bytedeco.llvm.LLVM.LLVMValueRef;
import static org.bytedeco.llvm.global.LLVM.LLVMAddFunction;
import static org.bytedeco.llvm.global.LLVM.LLVMAppendBasicBlockInContext;
import static org.bytedeco.llvm.global.LLVM.LLVMBuildRetVoid;
import static org.bytedeco.llvm.global.LLVM.LLVMExternalLinkage;
import static org.bytedeco.llvm.global.LLVM.LLVMFunctionType;
import static org.bytedeco.llvm.global.LLVM.LLVMPositionBuilderAtEnd;
import static org.bytedeco.llvm.global.LLVM.LLVMSetLinkage;
import static org.bytedeco.llvm.global.LLVM.LLVMVoidType;

import wci.backend.compilerllvm.LlvmCodeGenerator;
import wci.intermediate.*;

import static wci.backend.compilerjvm.Directive.*;
import static wci.backend.compilerjvm.Instruction.*;
import static wci.intermediate.symtabimpl.DefinitionImpl.*;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;

/**
 *
 * @author Daniel Toffetti
 */
public class LlvmProgramGenerator extends LlvmCodeGenerator {

    /**
     * Constructor.
     * @param parent the parent generator.
     */
    public LlvmProgramGenerator(LlvmCodeGenerator parent)
    {
        super(parent);
    }

    /**
     * Generate code for the main program.
     * @param node the root node of the program.
     */
    @Override
    public void generate(ICodeNode node) throws CompilerException {
        localVariables = new LocalVariables(0);
        localStack = new LocalStack();

        // Main program returning void means we cannot return exit codes
        LLVMTypeRef mainReturnType = LLVMVoidType();
        // No parameters into main, but we could pass parameters someday (by command line, for example)
        LLVMTypeRef mainParamTypes = null;
        // Build the main function's type (signature)
        LLVMTypeRef mainFunctionType = LLVMFunctionType(mainReturnType, mainParamTypes, 0, 0);
        // Create main function and set Linkage type
        LLVMValueRef mainFunction = LLVMAddFunction(module, "main", mainFunctionType);
        LLVMSetLinkage(mainFunction, LLVMExternalLinkage);
        // Set up a basic block for the main function
        LLVMBasicBlockRef mainEntryBlock = LLVMAppendBasicBlockInContext(context, mainFunction, "entry");
        LLVMPositionBuilderAtEnd(builder, mainEntryBlock);

        // TODO: generate everything else
        // TODO: verify insertion points placement for global vars and nested procedures
//        generateFields();
//        generateConstructor();
//        generateRoutines();
//        generateMainMethod();

        // Instruct builder to return void
        LLVMBuildRetVoid(builder);
    }
//
//    /**
//     * Generate directives for the fields.
//     */
//    private void generateFields()
//    {
//        // Runtime timer and standard in.
//        emitBlankLine();
//        emitDirective(FIELD_PRIVATE_STATIC, "_runTimer",   "LRunTimer;");
//        emitDirective(FIELD_PRIVATE_STATIC, "_standardIn", "LPascalTextIn;");
//
//        SymTab symTab = (SymTab) programId.getAttribute(ROUTINE_SYMTAB);
//        ArrayList<SymTabEntry> ids = symTab.sortedEntries();
//
//        emitBlankLine();
//
//        // Loop over all the program's identifiers and
//        // emit a .field directive for each variable.
//        for (SymTabEntry id : ids) {
//            Definition defn = id.getDefinition();
//
//            if (defn == VARIABLE) {
//                emitDirective(FIELD_PRIVATE_STATIC, id.getName(),
//                              typeDescriptor(id));
//            }
//        }
//    }
//
//    /**
//     * Generate code for the main program constructor.
//     */
//    private void generateConstructor()
//    {
//        emitBlankLine();
//        emitDirective(METHOD_PUBLIC, "<init>()V");
//
//        emitBlankLine();
//        emit(ALOAD_0);
//        emit(INVOKENONVIRTUAL, "java/lang/Object/<init>()V");
//        emit(RETURN);
//
//        emitBlankLine();
//        emitDirective(LIMIT_LOCALS, 1);
//        emitDirective(LIMIT_STACK , 1);
//
//        emitDirective(END_METHOD);
//    }
//
//    /**
//     * Generate code for any nested procedures and functions.
//     */
//    private void generateRoutines()
//        throws CompilerException
//    {
//        JvmDeclaredRoutineGenerator declaredRoutineGenerator =
//            new JvmDeclaredRoutineGenerator(this);
//        ArrayList<SymTabEntry> routineIds =
//            (ArrayList<SymTabEntry>) programId.getAttribute(ROUTINE_ROUTINES);
//
//        // Generate code for each procedure or function.
//        for (SymTabEntry id : routineIds) {
//            declaredRoutineGenerator.generate(id);
//        }
//    }
//
//    /**
//     * Generate code for the program body as the main method.
//     */
//    private void generateMainMethod()
//        throws CompilerException
//    {
//        emitBlankLine();
//        emitDirective(METHOD_PUBLIC_STATIC, "main([Ljava/lang/String;)V");
//
//        generateMainMethodPrologue();
//
//        // Generate code to allocate any arrays, records, and strings.
//        JvmStructuredDataGenerator structuredDataGenerator =
//                                    new JvmStructuredDataGenerator(this);
//        structuredDataGenerator.generate(programId);
//
//        generateMainMethodCode();
//        generateMainMethodEpilogue();
//    }
//
//    /**
//     * Generate the main method prologue.
//     */
//    private void generateMainMethodPrologue()
//    {
//        String programName = programId.getName();
//
//        // Runtime timer.
//        emitBlankLine();
//        emit(NEW, "RunTimer");
//        emit(DUP);
//        emit(INVOKENONVIRTUAL, "RunTimer/<init>()V");
//        emit(PUTSTATIC, programName + "/_runTimer", "LRunTimer;");
//
//        // Standard in.
//        emit(NEW, "PascalTextIn");
//        emit(DUP);
//        emit(INVOKENONVIRTUAL, "PascalTextIn/<init>()V");
//        emit(PUTSTATIC, programName + "/_standardIn LPascalTextIn;");
//
//        localStack.use(3);
//    }
//
//    /**
//     * Generate code for the main method.
//     */
//    private void generateMainMethodCode()
//        throws CompilerException
//    {
//        ICode iCode = (ICode) programId.getAttribute(ROUTINE_ICODE);
//        ICodeNode root = iCode.getRoot();
//
//        emitBlankLine();
//
//        // Generate code for the compound statement.
//        JvmStatementGenerator statementGenerator = new JvmStatementGenerator(this);
//        statementGenerator.generate(root);
//    }
//
//    /**
//     * Generate the main method epilogue.
//     */
//    private void generateMainMethodEpilogue()
//    {
//        // Print the execution time.
//        emitBlankLine();
//        emit(GETSTATIC, programName + "/_runTimer", "LRunTimer;");
//        emit(INVOKEVIRTUAL, "RunTimer.printElapsedTime()V");
//
//        localStack.use(1);
//
//        emitBlankLine();
//        emit(RETURN);
//        emitBlankLine();
//
//        emitDirective(LIMIT_LOCALS, localVariables.count());
//        emitDirective(LIMIT_STACK,  localStack.capacity());
//        emitDirective(END_METHOD);
//    }
//    
}
