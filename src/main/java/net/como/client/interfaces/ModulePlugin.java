package net.como.client.interfaces;
import net.como.client.structures.Module;

public interface ModulePlugin {
    public void addListeners(Module parentModule);
    public void removeListeners(Module parentModule);
}
