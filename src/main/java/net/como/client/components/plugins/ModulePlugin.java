package net.como.client.components.plugins;
import net.como.client.events.Event;
import net.como.client.modules.Module;

public interface ModulePlugin {
    public void addListeners(Module parentModule);
    public void removeListeners(Module parentModule);
    public boolean fireEvent(Event event);
}
