package gameplayHook.MachinePackage;

import gameplayHook.CodeModulePackage.CodeModule;
import gameplayHook.MachinePackage.components.MachineContext;
import gameplayHook.SimUIPackage.SimEventPackage.SimEventsHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class Machine {

    private final MachineContext context;
    private final List<CodeModule> codeModules;

    public Machine(String name) {
        this.context = new MachineContext(name);
        this.codeModules = new ArrayList<>();

        if(!name.startsWith("_temp_"))
            SimEventsHandler.triggerEvent(SimEventsHandler.EVENT_ON_ADD_MACHINE, context);
    }

    public void executeCodeModules(TriggerType triggerType) {
        for (CodeModule codeModule : codeModules) {
            codeModule.runIfTriggered(context, triggerType);
        }
    }

    public void attachCodeModule(CodeModule codeModule) {
        codeModules.add(codeModule);
    }

    public MachineContext getCtx() {
        return context;
    }
}
