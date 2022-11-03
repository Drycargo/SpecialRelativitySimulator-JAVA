package persistence;

import org.json.JSONObject;

/*Feature classes that are allowed to be written into json*/
public interface Writable {
    //EFFECTS: return a JSONObject related to this
    JSONObject toJson();
}
