package gameplayHook.SimUIPackage.RuntimeCodePackage;

import gameplayHook.CodeModulePackage.CodeModule;
import gameplayHook.CodeModulePackage.components.expressions.Constant;
import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.CodeModulePackage.components.expressions.conditonal.BinaryCondition;
import gameplayHook.CodeModulePackage.components.expressions.conditonal.CompoundCondition;
import gameplayHook.CodeModulePackage.components.tokens.KnowledgeToken;
import gameplayHook.CodeModulePackage.components.tokens.OperatorType;
import gameplayHook.CodeModulePackage.statements.Action;
import gameplayHook.CodeModulePackage.statements.ActionStatement;
import gameplayHook.MachinePackage.TriggerType;
import gameplayHook.MachinePackage.components.MachineContext;

import javax.tools.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

public class RuntimeCodeRunner {
    private final Class<?> runnerInterface;
    private final JavaCompiler compiler;
    private final MemoryJavaFileManager memFileManager;
    private final DiagnosticCollector<JavaFileObject> diagnostics;
    private final Map<String, JavaFileObject> sourcesInMemory;

    public RuntimeCodeRunner(Class<?> runnerInterface) {
        this.runnerInterface = runnerInterface;

        sourcesInMemory = new HashMap<>();
        compiler = ToolProvider.getSystemJavaCompiler();
        diagnostics = new DiagnosticCollector<>();
        memFileManager = new MemoryJavaFileManager(compiler.getStandardFileManager(diagnostics, null, null));
    }

    private String generateSourceCode(String className, String userCode) {
        Method method = null;

        for (Method m : runnerInterface.getMethods()) {
            if(m.isDefault()) continue;
            method = m;
        }

        if(method == null) throw new RuntimeException("Interface should have one non-default method");

        StringBuilder fullSource = new StringBuilder("public class ").append(className).append(" implements ")
                .append(runnerInterface.getCanonicalName()).append(" {\n");

        fullSource.append("\tpublic ").append(method.getReturnType().getCanonicalName())
                .append(" ").append(method.getName()).append("(");

        for(int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];
            fullSource.append(parameter.getParameterizedType()).append(" ").append(parameter.getName());

            if (i < method.getParameters().length-1) {
                fullSource.append(", ");
            }
        }
        fullSource.append(") {\n");

        for (String line : userCode.split("\n")) {
            fullSource.append("\t\t").append(line.trim()).append("\n");
        }

        fullSource.append("\t}\n}");
        return fullSource.toString();
    }

    public void createMethod(String interfaceName, String userCode) {
        if(runnerInterface.getMethods().length > 0 && !Modifier.isPublic(runnerInterface.getMethods()[0].getModifiers()))
            throw new IllegalStateException("The Interface is supposed to have one public method");

        String fullSource = generateSourceCode(interfaceName, userCode);
        System.out.println(CodeModule.C_BLUE + fullSource + CodeModule.C_RESET);

        sourcesInMemory.put(interfaceName, new JavaSourceFromString(interfaceName, fullSource));
    }

    public Map<String, Object> compileMethods() throws Exception {

        JavaCompiler.CompilationTask task = compiler.getTask(null, memFileManager, diagnostics, null, null, sourcesInMemory.values());
        boolean success = task.call();

        if (!success) {
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                String baseError = "\n" + diagnostic.getKind() + ": at " + diagnostic.getLineNumber() + ":" + diagnostic.getColumnNumber() + " " +
                        diagnostic.getMessage(null);
                JavaFileObject source = (JavaFileObject) diagnostic.getSource();
                String line = "Null Source";
                if (source != null) {
                    try (BufferedReader reader = new BufferedReader(source.openReader(true))) {
                        line = reader.lines().toList().get((int) diagnostic.getLineNumber());
                    } catch (IOException e) {
                        line = "Could not read source: " + e.getMessage();
                    }
                }
                System.err.println(baseError + "\n" + line);
            }
            return null;
        }

        Map<String, Object> methods = new HashMap<>();

        for (String className : sourcesInMemory.keySet()) {
            Class<?> compiledClass = memFileManager.getClassLoader(null).loadClass(className);
            methods.put(className, compiledClass.getDeclaredConstructor().newInstance());
        }

        clear();
        return methods;
    }

    public void clear() {
        sourcesInMemory.clear();
        memFileManager.clear();
    }

    public static void main(String[] args) throws Exception {
        RuntimeCodeRunner runtimeCodeRunner = new RuntimeCodeRunner(Action.ActionMethod.class);

        runtimeCodeRunner.createMethod("PrintVarName",
                "System.out.println(getVar(arg0, 0).getName());");
        runtimeCodeRunner.createMethod("PrintVarValue",
                "System.out.println(getVar(arg0, 0).getValue());");

        Map<String, Object> methods = runtimeCodeRunner.compileMethods();

        if(methods == null) return;

        ((Action.ActionMethod) methods.get("PrintVarName")).run(List.of(new Variable("x", 10)));
        ((Action.ActionMethod) methods.get("PrintVarValue")).run(List.of(new Variable("y", 20)));
    }

}
