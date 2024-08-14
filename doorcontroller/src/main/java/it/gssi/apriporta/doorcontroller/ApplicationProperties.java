package it.gssi.apriporta.doorcontroller;
 
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum ApplicationProperties {
    INSTANCE;

    private final Properties properties;

    ApplicationProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public String getHost() {
        return properties.getProperty("app.host");
    }
    public String getNFCUid() {
        return properties.getProperty("app.nfcuid");
    }
    public String speakerUID() {
        return properties.getProperty("app.speakeruid");
    }
    public String relayUID() {
        return properties.getProperty("app.relayuid");
    }
    public int millisRelayOpen() {
    	 return Integer.parseInt(properties.getProperty("app.millisrelayopen"));
    }
    public String lcdUID() {
   	 return properties.getProperty("app.lcduid");
   }
    public String getRoomId() {
        return properties.getProperty("app.roomid");
    }
    public String getbaseAPIUrl() {
        return properties.getProperty("app.baseAPIurl");
    }
    public String getToken() {
        return properties.getProperty("app.token");
    }
    public int getVolume() {
    	  return Integer.parseInt(properties.getProperty("app.volume"));
    }
}

