package net.como.client.items.impl.string;

import org.apache.commons.lang3.StringUtils;

import net.como.client.items.StringCreativeItem;
import net.como.client.utils.ClientUtils;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class BanBook implements StringCreativeItem {

    @Override
    public String getNbtString() {
        String page = "'{\"text\":\"ⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫ\"}'";
        
        String[] pages = new String[100];
        for (int i = 0; i < 100; i++) {
            pages[i] = page;
        }

        return String.format("{author:\"%s\",pages:[%s],title:\"Bye-bye Book\",display:{Name:'{\"text\":\"Ban Book\",\"color\":\"dark_purple\",\"bold\":true,\"underlined\":true}'}}", ClientUtils.getUsername(), StringUtils.join(pages, ","));
    }

    @Override
    public Item getItem() {
        return Items.WRITTEN_BOOK;
    }
    
}
