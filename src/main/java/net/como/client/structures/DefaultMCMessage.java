package net.como.client.structures;

// TODO when you have looked at the API mroe closely maybe check if
// CheatClient.getClient().inGameHud.extractSender(message); exists lol
public class DefaultMCMessage {
    private final String rawMessage;

    private String[] words;

    private String username;
    private String message;

    private void getWords() {
        this.words = rawMessage.split(" ");
    }

    public boolean isDefaultMCMessage() {
        if (this.words.length <= 1) return false;

        String firstWord = this.words[0];
        return (firstWord.startsWith("<") && firstWord.endsWith(">"));
    }

    public String getMessage() {
        if (!this.isDefaultMCMessage()) return null;

        if (this.message == null) this.message = String.join(" ", this.words);

        return this.message;
    }

    public String getUsername() {
        if (!this.isDefaultMCMessage()) return null;

        if (this.username == null) this.username = this.words[0].substring(1, this.words[0].length() - 1);

        return this.username;
    }

    public DefaultMCMessage(String message) {
        this.rawMessage = message;

        // Get the words
        this.getWords();
    }
}
