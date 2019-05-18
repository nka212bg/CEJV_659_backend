package util;

import java.util.HashMap;
import java.util.Map;

public class SessionLinker {

    private static SessionLinker instance;
    private static Map< Integer, HashMap> session;

    public static SessionLinker getInstance() {
        if (instance == null) {
            instance = new SessionLinker();
            session = new HashMap<>();
        }
        return instance;
    }

    public void setAttribute(int sessionKey, String attributeKey, String attributeValue) {

        if (session.containsKey(sessionKey)) {
            session.get(sessionKey).put(attributeKey, attributeValue);
        }
    }

    public String getAttribute(int sessionKey, String attributeKey) {
        if (session.containsKey(sessionKey)) {
            return String.valueOf(session.get(sessionKey).get(attributeKey));
        }
        return null;
    }

    public void invalidate(int sessionKey) {
        if (session.containsKey(sessionKey)) {
            session.remove(sessionKey);
        }
    }

    public int setSession() {
        int sessionKeyGen = (int) (System.currentTimeMillis() / 1000 / 60);
        if (!session.containsKey(sessionKeyGen)) {
            session.put(sessionKeyGen, new HashMap< String, String>());
            return sessionKeyGen;
        }
        return 0;
    }

    public HashMap getSession(int sessionKey) {
        if (session.containsKey(sessionKey)) {
            return session.get(sessionKey);
        }
        return null;
    }

    public Map<Integer, HashMap> getSessionAll() {
        return session;
    }
}
