package net.como.client.commands.nbt;

import net.como.client.commands.structures.CommandNode;

public class NbtCommand extends CommandNode {

    public NbtCommand() {
        super("nbt", "Manipulate items' NBT data");

        this.addSubCommand(new MaxEnchant());
    }


}
