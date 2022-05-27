package net.como.client.components.systems.binds.impl;

import net.como.client.components.systems.binds.Bind;
import net.como.client.modules.Module;

public class ModuleBind implements Bind {
    protected final Module module;

    public Module getModule() {
        return module;
    }

    @Override
    public void fire() {
        module.toggle();
    }
    
    public ModuleBind(Module module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return "Toggle module: " + module.getName();
    }
}
