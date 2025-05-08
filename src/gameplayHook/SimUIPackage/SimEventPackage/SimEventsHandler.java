package gameplayHook.SimUIPackage.SimEventPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimEventsHandler {

    public static final String EVENT_ON_ADD_MACHINE = "onAddMachine";
    public static final String EVENT_ON_SELECT_MACHINE = "onSelectMachine";
    public static final String EVENT_ON_UPDATE_CONTEXT = "onUpdateContext";

    public static final String EVENT_ON_DW_CREATE_MACHINE = "onDWCreateMachine";
    private static final Map<String, List<SimEventListener>> eventsMap = new HashMap<>();

    public static void triggerEvent(String name, Object... objects) {
        List<SimEventListener> eventListeners = eventsMap.get(name);
        if(eventListeners == null) return;

        for(SimEventListener eventListener : eventListeners) {
            eventListener.onTriggerEvent(objects);
        }
    }

    public static void publishEvent(String name) {
        List<SimEventListener> eventListeners = eventsMap.get(name);
        if(eventListeners != null) return;

        eventsMap.put(name, new ArrayList<>());
    }

    public static void subscribeEvent(String name, SimEventListener eventListener) {
        List<SimEventListener> eventListeners = eventsMap.computeIfAbsent(name, _ -> new ArrayList<>());
        eventListeners.add(eventListener);
    }
}
