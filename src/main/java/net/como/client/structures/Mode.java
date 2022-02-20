package net.como.client.structures;

import java.util.ArrayList;
import java.util.List;

public class Mode {
    private List<String> entires = new ArrayList<>();

    private Integer state;
    public final Integer defaultState ;

    /**
     * Sets the current state to the default state
    */
    public void reset() {
        this.setState(this.defaultState);
    }

    /**
        * Getter for the current state
        * @return active state
    */
    public Integer getState() {
        return this.state;
    }

    /**
        * Getter for the default state
        * @return default state
    */
    public Integer getDefaultState() {
        return this.defaultState;
    }

    /**
        * Getter for the current state's name
        * @return active state's name
    */
    public String getStateName() {
        return this.getStateName(this.getState());
    }

    /**
        * Getter of a given state id
        * @param id An id of a state 
        * @return 
        ** given state's name with a valid id
        ** null if the state wasn't found
    */
    public String getStateName(Integer id) {
        if (!this.isValidId(id)) return null;

        return this.entires.get(id);
    }

    /**
     * Checks a given id
     * @param id
     * @return if an id can be mapped to an entry
     */
    private Boolean isValidId(int id) {
        return !(this.entires.size() >= id || id < 0);
    }

    /**
     * Sets the current active state
     * @param id
     * @return if the state could be found
     */
    public Boolean setState(int id) {
        if (!this.isValidId(id)) return false;

        state = id;
        return true;
    }

    /**
     * Sets the current state from a name
     * @param state the name of a state
     * @return if the state could be found
     */
    public Boolean setState(String state) {
        return this.setState(this.getId(state));
    }

    /**
     * Gets the id of a given entry name
     * @param entry state/entry name
     * @return the id or -1 if it wasn't found
     */
    private Integer getId(String entry) {
        for (int i = 0; i < entires.size(); i++) {
            if (entires.get(i).equals(entry)) return i;
        }

        return -1;
    }

    /**
     * Adds an entry to the state
     * @param entry the entry to be added
     * @return true when it was added, false if it wasn't
     */
    private Boolean addEntry(String entry) {
        if (this.getId(entry) != -1) return false;

        this.entires.add(entry);
        return true;
    }

    public Iterable<String> getEntries() {
        return this.entires;
    }
    
    /**
     * 
     * @param state the id of the default state
     * @param entires
     */
    public Mode(Integer state, String... entires) {
        for (String entry : entires) {
            this.addEntry(entry);
        }

        this.defaultState = this.state = state;
    }

    /**
     * Adds all of the entries
     * @param entries
     */
    private void addEntries(String... entries) {
        for (String entry : entires) {
            this.addEntry(entry);
        }
    }

    /**
     * Defaults the state to be the first entry
     * @param entries
     */
    public Mode(String... entries) {
        this.addEntries(entries);

        this.defaultState = this.state = 0;
    }

    /**
     * Compares the current state with a provided state
     * @param state the state's name
     * @return if it is the current state
     */
    public Boolean is(String state) {
        return this.getStateName().equals(state);
    }


    /**
     * Compares the current state with a provided state id
     * @param id the id of the state
     * @return if the current state has the id
     */
    public Boolean is(Integer id) {
        return id == this.getState();
    }

    public String toString() {
        // I am currently just toStringing just incase the stateName type changes!
        return this.getStateName().toString();
    }
}
