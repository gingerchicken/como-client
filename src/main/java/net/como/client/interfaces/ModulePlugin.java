package net.como.client.interfaces;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;

public interface ModulePlugin {
    public void addListeners(Module parentModule);
    public void removeListeners(Module parentModule);
    public boolean fireEvent(Event event);
}
