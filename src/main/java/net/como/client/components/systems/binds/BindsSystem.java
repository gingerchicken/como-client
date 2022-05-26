package net.como.client.components.systems.binds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

import net.como.client.utils.ChatUtils;

public class BindsSystem implements Iterable<Integer> {
    protected HashMap<Integer, Queue<Bind>> binds = new HashMap<>();
    protected boolean hideMessage = true;

    /**
     * Set the hideMessage flag.
     * @param hideMessage The flag to set.
     */
    public void setHideMessage(boolean hideMessage) {
        this.hideMessage = hideMessage;
    }

    /**
     * Get the hideMessage flag.
     * @return The hideMessage flag.
     */
    public boolean getHideMessage() {
        return this.hideMessage;
    }

    /**
     * Adds a new bind to the system.
     * @param key The key to bind to.
     * @param bind The bind to add.
     */
    public void addBind(int key, Bind bind) {
        if (!this.hasKeyBound(key)) {
            binds.put(key, new LinkedList<Bind>());
        }

        binds.get(key).add(bind);
    }

    /**
     * Removes a bind from the system.
     * @param key The key to unbind.
     * @param bind The bind to remove.
     * @return True if the bind was removed, false otherwise.
     */
    public boolean removeBind(int key, Bind bind) {
        if (!this.hasKeyBound(key)) return false;

        // Remove the bind from the queue.
        binds.get(key).remove(bind);

        return true;
    }

    /**
     * Removes a bind from all queues in the system
     * @param predicate The predicate to use to find the bind.
     * @return True if at least one bind was removed, false otherwise.
     */
    public boolean removeBinds(Predicate<Bind> predicate) {
        boolean removed = false;

        for (Queue<Bind> queue : binds.values()) {
            for (Iterator<Bind> iterator = queue.iterator(); iterator.hasNext();) {
                Bind bind = iterator.next();

                if (predicate.test(bind)) {
                    iterator.remove();
                    removed = true;
                }
            }
        }

        return removed;
    }

    /**
     * Removes a key from the system.
     * @param key The key to remove.
     * @return True if the key was removed, false otherwise.
     */
    public boolean removeBind(int key) {
        if (!this.hasKeyBound(key)) return false;

        // Remove the bind completely.
        binds.remove(key);

        return true;
    }

    /**
     * Fires all binds for the given key.
     * @param key The key to fire binds for.
     * @return True if any binds were fired, false otherwise.
     */
    public boolean fireBinds(int key) {
        if (!this.hasKeyBound(key)) return false;

        // Fire all binds in the queue.
        for (Bind bind : binds.get(key)) {
            // Hide the chat message
            ChatUtils.hideNextChat = this.getHideMessage();
            
            // Trigger the bind
            bind.fire();
        }

        return true;
    }

    public boolean hasKeyBound(int key) {
        return binds.containsKey(key);
    }

    public Queue<Bind> getKeyBinds(int key) {
        return binds.get(key);
    }

    public List<Integer> searchForKeys(Predicate<Bind> pred) {
        List<Integer> keys = new ArrayList<>();

        for (int key : binds.keySet()) {
            for (Bind bind : binds.get(key)) {
                if (pred.test(bind)) {
                    keys.add(key);
                    break;
                }
            }
        }

        return keys;
    }

    @Override
    public Iterator<Integer> iterator() {
        return binds.keySet().iterator();
    }
}
