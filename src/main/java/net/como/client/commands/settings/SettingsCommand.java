package net.como.client.commands.settings;

import java.util.ArrayList;
import java.util.List;

import net.como.client.ComoClient;
import net.como.client.commands.structures.Command;
import net.como.client.commands.structures.CommandNode;
import net.como.client.structures.Mode;
import net.como.client.structures.settings.*;
import net.como.client.utils.ChatUtils;

public class SettingsCommand extends CommandNode {
    private Settings settings;

    private static Command getSettingCommand(Setting setting) {
        String type = setting.defaultValue.getClass().getName();
        switch (type) {
            case "java.lang.String": {
                return new StringCommand(setting);
            }
            case "java.lang.Boolean": {
                return new BooleanCommand(setting);
            }
            case "java.lang.Float": {
                return new FloatCommand(setting);
            }
            case "java.lang.Double": {
                return new DoubleCommand(setting);
            }
            case "java.lang.Integer": {
                return new IntegerCommand(setting);
            }
            case "java.util.HashMap": {
                return new HashMapCommand(setting);
            }
            case "net.como.client.structures.Mode": {
                return new ModeCommand(setting);
            }
            default: {
                System.err.println(String.format("Attempted to generate settings command from type %s", type));
                return null;
            }
        }
    }

    public SettingsCommand(Settings settings) {
        super("settings", "Change settings 'n' stuff.");

        this.settings = settings;
        
        for (String settingName : this.settings.getSettings()) {
            Setting setting = this.settings.getSetting(settingName);

            this.addSubCommand(getSettingCommand(setting));
        }
    }

    private static abstract class GenericSettingCommand extends Command {
        public Setting setting;

        public GenericSettingCommand(Setting setting) {
            // TODO Change when setting descriptions are added
            super(setting.name, "A Modifiable Setting", "A Modifiable Setting");
            this.setting = setting;
        }

        public void showChange(Object before) {
            this.displayChatMessage(String.format("Updated '%s': %s%s%s -> %s%s%s", this.setting.name, ChatUtils.RED, before.toString(), ChatUtils.WHITE, ChatUtils.GREEN, setting.value.toString(), ChatUtils.WHITE));
        }

        public void showValue() {
            ComoClient.displayChatMessage(String.format("%sSetting Value: %s", ChatUtils.WHITE, setting.value.toString()));
        }

        public abstract Boolean setValue(String value);

        @Override
        public Boolean trigger(String[] args) {
            if (args.length == 0) {
                this.showValue();
                return true;
            }

            // See what the object's value was originally
            String before = ((Object)(this.setting.value)).toString();

            // Set the new value
            String value = String.join(" ", args);
            if (!this.setValue(value)) {
                return true;
            }

            // Show the change
            this.showChange(before);

            return true;
        }

        @Override
        public List<String> getSuggestions() {
            List<String> suggestions = new ArrayList<>();

            for (String sug : this.setting.getSuggestions()) suggestions.add(sug);

            return suggestions;
        }
    }
    private static abstract class NumericalSettingCommand extends GenericSettingCommand {

        public NumericalSettingCommand(Setting setting) {
            super(setting);
        }

        public void showNaNMessage() {
            ComoClient.displayChatMessage(String.format("%sInvalid Value: received NaN as a parameter", ChatUtils.RED));
        }

        public abstract Object valueOf(String value);

        @Override
        public Boolean setValue(String value) {
            Object realValue = null;
                
            try {
                realValue = this.valueOf(value);
            } catch (Exception e) { }

            if (realValue == null) {
                this.showNaNMessage();
                return false;
            }

            setting.value = realValue;

            return true;
        }
    }

    private static class StringCommand extends GenericSettingCommand {
        public StringCommand(Setting setting) {
            super(setting);
        }

        @Override
        public Boolean setValue(String value) {
            this.setting.value = value;
            return true;
        }
    }
    private static class BooleanCommand extends GenericSettingCommand {

        public BooleanCommand(Setting setting) {
            super(setting);
        }

        @Override
        public Boolean setValue(String value) {
            this.setting.value = value.toLowerCase().equals("true");
            return true;
        }

        @Override
        public List<String> getSuggestions() {
            return List.of("true", "false");
        }
    }
    private static class ModeCommand extends GenericSettingCommand {
        Mode mode;

        public ModeCommand(Setting setting) {
            super(setting);

            this.mode = (Mode)setting.value;
        }

        @Override
        public Boolean setValue(String value) {
            if (!this.mode.setState(value)) {
                this.displayChatMessage(String.format("%sInvalid state '%s,' please check that it exists and try again.", ChatUtils.RED, value));
                return false;
            }

            return true;
        }

        @Override
        public List<String> getSuggestions() {
            List<String> items = new ArrayList<>();
            for (String entry : this.mode.getEntries()) items.add(entry);
            
            return items;
        }

    }

    @Override
    public Boolean trigger(String[] args) {
        if (args.length == 0) {
            ComoClient.displayChatMessage("Current Settings:");
            for (String name : settings.getSettings()) {
                ComoClient.displayChatMessage(
                    String.format("-> %s - %s", name, settings.getSetting(name).value.toString())
                );
            }

            return true;
        }

        return super.trigger(args);
    }
    
    private static class FloatCommand extends NumericalSettingCommand {

        public FloatCommand(Setting setting) {
            super(setting);
            //TODO Auto-generated constructor stub
        }

        @Override
        public Object valueOf(String value) {
            return Float.valueOf(value);
        }

    }
    private static class DoubleCommand extends NumericalSettingCommand {

        public DoubleCommand(Setting setting) {
            super(setting);
            //TODO Auto-generated constructor stub
        }

        @Override
        public Object valueOf(String value) {
            return Double.valueOf(value);
        }

    }
    private static class IntegerCommand extends NumericalSettingCommand {

        public IntegerCommand(Setting setting) {
            super(setting);
            //TODO Auto-generated constructor stub
        }

        @Override
        public Object valueOf(String value) {
            return Integer.valueOf(value);
        }

    }
}
