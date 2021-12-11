package model;

/*Functions that are common to models which contain a list of events*/
public interface ContainsEvents {
    //EFFECTS: Look for event with given eventName; if found, return the events object; if not, return null
    PhysicalEvent findEventByName(String eventName);

    //EFFECTS: return the names of all observedEvents
    String eventNameList();
}
