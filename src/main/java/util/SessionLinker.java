package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SessionLinker {

    private static SessionLinker instance;
    private static Map< Integer, HashMap> session;

    public static SessionLinker getInstance() throws InterruptedException {
        if (instance == null) {
            instance = new SessionLinker();
            session = new HashMap<>();
            sessionCleaner();
        }
        return instance;
    }

    public static String setSession() {
        int sessionKeyGen = (int) (System.currentTimeMillis() / 1000);
        if (!session.containsKey(sessionKeyGen)) {
            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
            String secret = "";
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                int randomNum = rand.nextInt(AlphaNumericString.length());
                secret += AlphaNumericString.charAt(randomNum);
            }
            HashMap<String, String> sessionVal = new HashMap< String, String>();
            sessionVal.put("secret", secret);
            sessionVal.put("lastActive", String.valueOf(sessionKeyGen));
            session.put(sessionKeyGen, sessionVal);
            return sessionKeyGen + "-" + secret;
        } else {
            setSession();
        }
        return null;
    }

    public static void setAttribute(String sessionKeyAndSecret, String attributeKey, String attributeValue) {
        int sessionKey = Integer.parseInt(sessionKeyAndSecret.split("-")[0]);
        String secret = sessionKeyAndSecret.split("-")[1];
        if (session.containsKey(sessionKey)) {
            session.get(sessionKey).put(attributeKey, attributeValue);
        }
    }

    public static String getAttribute(String sessionKeyAndSecret, String attributeKey) {
        int sessionKey = Integer.parseInt(sessionKeyAndSecret.split("-")[0]);
        String secret = sessionKeyAndSecret.split("-")[1];
        if (session.containsKey(sessionKey)) {
            if (session.get(sessionKey).get("secret").equals(secret)) {
                session.get(sessionKey).put("lastActive", String.valueOf(System.currentTimeMillis() / 1000 / 60));
                return String.valueOf(session.get(sessionKey).get(attributeKey));
            }
        }
        return null;
    }

    public static void invalidate(String sessionKeyAndSecret) {
        int sessionKey = Integer.parseInt(sessionKeyAndSecret.split("-")[0]);
        String secret = sessionKeyAndSecret.split("-")[1];
        if (session.containsKey(sessionKey)) {
            if (session.get(sessionKey).get("secret").equals(secret)) {
                session.remove(sessionKey);
            }
        }
    }

    public static int lastActive(int sessionKey) {
        if (!session.containsKey(sessionKey)) {
            int currentTime = (int) (System.currentTimeMillis() / 1000 / 60);
            session.get(sessionKey).put("lastActive", currentTime);
            return currentTime;
        }
        return 0;
    }

    public static HashMap getUserSession(int sessionKey) {
        if (session.containsKey(sessionKey)) {
            return session.get(sessionKey);
        }
        return null;
    }

    public static Map<Integer, HashMap> getSessionAll() {
        return session;
    }

    public static void sessionCleaner() throws InterruptedException {
        while (true) {
            System.out.println("--|_O‿O_|-- ehooo, spq ");
            Thread.sleep(1000 * 10);
        }
    }

}
