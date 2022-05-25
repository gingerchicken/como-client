package net.como.client.interfaces;
import net.como.client.events.Event;
import net.como.client.misc.Module;

public interface ModulePlugin {
    public void addListeners(Module parentModule);
    public void removeListeners(Module parentModule);
    public boolean fireEvent(Event event);
}
