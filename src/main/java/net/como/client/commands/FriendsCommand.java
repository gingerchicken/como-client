package net.como.client.commands;

import java.util.List;

import net.como.client.CheatClient;
import net.como.client.commands.structures.Command;
import net.como.client.commands.structures.CommandNode;
import net.como.client.components.FriendsManager;
import net.como.client.components.FriendsManager.Friend;
import net.como.client.utils.ChatUtils;

public class FriendsCommand extends CommandNode {
    private static class FriendsSubCommand extends Command {
        FriendsManager manager;

        public FriendsSubCommand(String command, String helpText, String description) {
            super(command, helpText, description);
        }
        
        public Friend findFriendWithMessages(String input) {
            Friend target = Friend.fromMiscString(input);

            if (target == null) {
                CheatClient.displayChatMessage(String.format("%sUnable to find player '%s', maybe try again when the player is online or use their UUID.", ChatUtils.RED, input));
            }

            return target;
        }

        public void action(Friend target) {
            // Something...
        }

        @Override
        public Boolean trigger(String args[]) {
            if (args.length == 0) return this.handleHelp(args);

            Friend target = this.findFriendWithMessages(args[0]);
            if (target != null) this.action(target);

            return true;
        }
    }
    
    private static class addCommand extends FriendsSubCommand {
        public addCommand(FriendsManager manager) {
            super("add", "add <name|uuid>", "Add a player to your friends list");

            this.manager = manager;
        }

        @Override
        public void action(Friend target) {
            boolean success = this.manager.addFriend(target);
            
            if (success) {
                CheatClient.displayChatMessage(String.format("Successfully added '%s' to your friends list.", target.getUsernameOrUuid()));
            } else {
                CheatClient.displayChatMessage(String.format("%sUnable to add '%s' to your friends list, check that they are not already on it.", ChatUtils.RED, target.getUsernameOrUuid()));
            }
        }
    }
    private static class removeCommand extends FriendsSubCommand {

        public removeCommand(FriendsManager manager) {
            super("remove", "remove <name|uuid>", "Remove a player from your friends list");

            this.manager = manager;
        }

        @Override
        public void action(Friend target) {
            boolean success = this.manager.removeFriend(target);

            if (success) {
                CheatClient.displayChatMessage(String.format("Successfully removed %s from your friends list.", target.getUsernameOrUuid()));
            } else {
                CheatClient.displayChatMessage(String.format("%sUnable to remove player '%s', make sure that they are on your friends list.", ChatUtils.RED, target.getUsernameOrUuid()));
            }
        }
    }
    private static class listCommand extends FriendsSubCommand {
        public listCommand(FriendsManager manager) {
            super("list", "list", "Remove a player from your friends list");

            this.manager = manager;
        }

        @Override
        public Boolean trigger(String args[]) {
            List<Friend> friends = manager.getFriends();

            if (friends.size() == 0) {
                CheatClient.displayChatMessage("You currently have no friends.");
                return true;
            }

            CheatClient.displayChatMessage("Friends List:");
            for (FriendsManager.Friend friend : friends) {
                String username = friend.getUsername();
                username = username == null ? friend.getUuid().toString() : username;

                String message;
                // TODO this is inefficient as anything, please change this so that it looks up the player in a HashMap or something.
                if (!friend.isOnline()) {
                    message = String.format("[%sOFFLINE%s] %s%s", ChatUtils.RED, ChatUtils.WHITE, ChatUtils.RED, username);
                } else {
                    message = String.format("[%sONLINE%s] %s%s", ChatUtils.GREEN, ChatUtils.WHITE, ChatUtils.GREEN, username);
                }

                CheatClient.displayChatMessage(String.format("-> %s", message));
            }

            return true;
        }
    }

    public FriendsCommand(FriendsManager manager) {
        super("friends", "A Simple friend management system");

        this.addSubCommand(new listCommand(manager));
        this.addSubCommand(new addCommand(manager));
        this.addSubCommand(new removeCommand(manager));
    }
    
}
