package gameplayHook.MachinePackage;

import gameplayHook.CodeModulePackage.CodeModule;
import gameplayHook.MachinePackage.components.MachineContext;

import java.util.ArrayList;

public abstract class Machine {

    private final MachineContext context;
    private final ArrayList<CodeModule> codeModules;

    public Machine() {
        this.context = new MachineContext();
        this.codeModules = new ArrayList<>();
    }

    public void executeCodeModules() {
        for (CodeModule codeModule : codeModules) {
            codeModule.checkStatements(context);
        }
    }

    public void attachCodeModule(CodeModule codeModule) {
        codeModules.add(codeModule);
    }

    public MachineContext getCtx() {
        return context;
    }
}
