package net.como.client.mixin.botch.commandautofill;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.commands.structures.CommandHandler;
import net.como.client.commands.structures.CommandNode;
import net.como.client.modules.chat.CommandAutoFill;
import net.como.client.utils.ChatUtils;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;

@Mixin(CommandSuggestor.class)
public abstract class CommandSuggestorMixin {
    @Shadow TextFieldWidget textField;
    @Shadow CompletableFuture<Suggestions> pendingSuggestions;

    private CommandAutoFill getCommandAutoFill() {
        return (CommandAutoFill)ComoClient.Modules.get("commandautofill");
    }

    @Shadow abstract void showSuggestions(boolean narrateFirstSuggestion);
    
    @Inject(at = @At("TAIL"), method="refresh()V", cancellable = true)
    void onRefresh(CallbackInfo ci) {
        if (!this.getCommandAutoFill().isEnabled()) return;

        String text = this.textField.getText();

        // Make sure that the command starts with a . or ,
        CommandHandler handler = ComoClient.commandHandler;
        if (!text.startsWith(handler.delimiter)) return;

        // We're taking control here!
        ci.cancel();

        // Get the command dispatcher
        String dispatcher = text.substring(0, this.textField.getCursor());
        Integer wordStart = ChatUtils.getStartOfCurrentWord(text);

        // Get the matching suggestions to the current input
        this.pendingSuggestions = CommandSource.suggestMatching(
            this.getCommandAutoFill().getSuggestions(dispatcher, handler),
            new SuggestionsBuilder(text, wordStart)
        );
        
        this.pendingSuggestions.thenRun(() -> {
            if (!this.pendingSuggestions.isDone()) {
                return;
            }
            
            // We cannot use show since it is not a typical command
            this.showSuggestions(true);
        });
    }
}
