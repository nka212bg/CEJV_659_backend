package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Nikolay Krasimirov Atanasov
 */
public class SessionLinker {

    private static SessionLinker instance;
    private static Map< Integer, HashMap> session;

    /**
     * @return Singleton - getting instance of the class and creates HashMap() -
     * one for the entire application
     */
    public static SessionLinker getInstance() {
        if (instance == null) {
            instance = new SessionLinker();
            session = new HashMap();
        }
        return instance;
    }

    /**
     * @return Creates an unique session key made from ([currentTime UTC in
     * seconds]-[random 10 digits string]) and put the time as a key for a
     * Map<String, Map<String, String>>
     *
     */
    public static String setSession() {
        int sessionKeyGen = (int) (System.currentTimeMillis() / 1000);
        /* If by any chanse , 2 people try to get session in the same second 
           the else will call the function recursive ------------*/
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

    /**
     *
     * @param sessionKeyAndSecret
     * @param attributeKey
     * @param attributeValue
     */
    public static void setAttribute(String sessionKeyAndSecret, String attributeKey, String attributeValue) {
        int sessionKey = Integer.parseInt(sessionKeyAndSecret.split("-")[0]);
        String secret = sessionKeyAndSecret.split("-")[1];
        if (session.containsKey(sessionKey)) {
            session.get(sessionKey).put(attributeKey, attributeValue);
        }
    }

    /**
     *
     * @param sessionKeyAndSecret
     * @param attributeKey
     * @return
     */
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

    /**
     *
     * @param sessionKeyAndSecret
     */
    public static void invalidate(String sessionKeyAndSecret) {
        int sessionKey = Integer.parseInt(sessionKeyAndSecret.split("-")[0]);
        String secret = sessionKeyAndSecret.split("-")[1];
        if (session.containsKey(sessionKey)) {
            if (session.get(sessionKey).get("secret").equals(secret)) {
                session.remove(sessionKey);
            }
        }
    }

    /**
     *
     * @param sessionKey
     * @return
     */
    public static int lastActive(int sessionKey) {
        if (!session.containsKey(sessionKey)) {
            int currentTime = (int) (System.currentTimeMillis() / 1000 / 60);
            session.get(sessionKey).put("lastActive", currentTime);
            return currentTime;
        }
        return 0;
    }

    /**
     *
     * @param sessionKey
     * @return
     */
    public static HashMap getUserSession(int sessionKey) {
        if (session.containsKey(sessionKey)) {
            return session.get(sessionKey);
        }
        return null;
    }

    /**
     *
     * @return
     */
    public static Map<Integer, HashMap> getSessionAll() {
        return session;
    }

    /*TODO ------------*/
    public static void sessionCleaner() {

    }

}
