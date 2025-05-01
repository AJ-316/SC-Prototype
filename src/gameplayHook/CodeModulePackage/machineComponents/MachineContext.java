package gameplayHook.CodeModulePackage.machineComponents;

import java.util.HashMap;
import java.util.Map;

public class MachineContext {
    private final Map<String, Object> variables = new HashMap<>();

    public void set(String name, Object value) {
        variables.put(name, value);
    }

    public Object get(String name) {
        return variables.get(name);
    }
}
