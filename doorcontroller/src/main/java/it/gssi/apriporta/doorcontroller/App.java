package it.gssi.apriporta.doorcontroller;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.apache.groovy.parser.antlr4.GroovyParser.ThisFormalParameterContext;

import com.tinkerforge.BrickletIndustrialQuadRelayV2;
import com.tinkerforge.BrickletLCD128x64;
import com.tinkerforge.BrickletNFC;
import com.tinkerforge.BrickletPiezoSpeakerV2;
import com.tinkerforge.IPConnection;
import com.tinkerforge.TinkerforgeException;

import groovyjarjarantlr4.v4.parse.ANTLRParser.finallyClause_return;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class App 
{
	private static final String HOST = ApplicationProperties.INSTANCE.getHost();
    private static final int PORT = 4223;
    static boolean found;
    private final static Properties properties = new Properties();

    private static final String NFCUID = ApplicationProperties.INSTANCE.getNFCUid();
    private static final String speakerUID = ApplicationProperties.INSTANCE.speakerUID();
    private static final int volume = ApplicationProperties.INSTANCE.getVolume();
    private static final String relayUID = ApplicationProperties.INSTANCE.relayUID();
    private static final String lcdUID = ApplicationProperties.INSTANCE.lcdUID();

    private static final int millisRelayOpen = ApplicationProperties.INSTANCE.millisRelayOpen();
    private static final int room_id = Integer.parseInt(ApplicationProperties.INSTANCE.getRoomId());
    private static final String baseAPIurl = ApplicationProperties.INSTANCE.getbaseAPIUrl();
    private static final String TOKEN= ApplicationProperties.INSTANCE.getToken();

    public static void main(String args[]) throws Exception {
        IPConnection ipcon = new IPConnection(); // Create IP connection
        // Note: Declare nfc as final, so the listener can access it
        final BrickletNFC nfc = new BrickletNFC(NFCUID, ipcon); // Create device object
        final BrickletPiezoSpeakerV2 ps = new BrickletPiezoSpeakerV2(speakerUID, ipcon); // Create device object
        final BrickletIndustrialQuadRelayV2 iqr =
                new BrickletIndustrialQuadRelayV2(relayUID, ipcon); 
        
        final BrickletLCD128x64 lcd= new BrickletLCD128x64(lcdUID, ipcon); // Create device object

        ipcon.connect(HOST, PORT); // Connect to brickd
       
        ps.setBeep(1000, volume, 200);
        Thread.sleep(200);
        ps.setBeep(1000, volume, 200);
        Thread.sleep(200);
        ps.setBeep(1000, volume, 200);
       
        // Don't use device before ipcon is connected
        displayDesign(lcd,iqr);
        // Add reader state changed listener
        nfc.addReaderStateChangedListener(new BrickletNFC.ReaderStateChangedListener() {
            public void readerStateChanged(int state, boolean idle) {
                if(state == BrickletNFC.READER_STATE_REQUEST_TAG_ID_READY) {
                    try {
                        int i = 0;
                        StringBuilder tag = new StringBuilder();
                        BrickletNFC.ReaderGetTagID ret = nfc.readerGetTagID();

                        for (int v : ret.tagID) {
							tag.append(Integer.toHexString(v));

							i++;
						}
                       
                        System.out.format("Used tag of type %d with ID [%s]\n", ret.tagType, tag);
                       
                        found = checkAuth(tag.toString(), room_id,  TOKEN);
                        
                        if(found) {
                        	iqr.setValue(new boolean[]{true, true, true, true});
                        	
                            Thread.sleep(millisRelayOpen);
                            
                            iqr.setValue(new boolean[]{false, false, false, false});
                            
                        	ps.setBeep(3200, volume, 500);
                        	//lcd.clearDisplay();
                        	displayDesign(lcd,iqr);
                        	
                        }else {
                        	ps.setBeep(100, volume, 500);
                        	lcd.clearDisplay();
                        	lcd.writeLine(5, 0, tag.toString());
                        	
                        }
                        Thread.sleep(1000);
                    }
                    catch (Exception e) {
                        return;
                    }
                }

                if (idle) {
                    try {
                        nfc.readerRequestTagID();
                    }
                    catch (Exception e) {
                        return;
                    }
                }
            }

			private boolean checkAuth(String tag, int room_id, String token) {
				// TODO Auto-generated method stub
				 // Set the base URI, using JSONPlaceholder as an example
		        RestAssured.baseURI = baseAPIurl+"/api/tags/bycode/";
		        // Send a GET request and save the response
		        Response response = given()
		        		.header("Authorization", "Bearer " + token)
		                .when()
		                .get(tag)
		                .then()
		                .extract()
		                .response();
		      
		       // Validate that the status code is 200
		       int statusCode = response.getStatusCode();
		      
		       if(statusCode==200) {
		    	   JsonPath jsonPathEvaluator = response.jsonPath();
		    	   try {
		    		//System.out.println("Response JSON: " + response.asString()); // Verify that the status code is 200.
				       
		    		int user_id = jsonPathEvaluator.get("employee.id");
		    	 
		    	   
		    	   RestAssured.baseURI = baseAPIurl+"/api/access-rules?eagerload=true";
			        // Send a GET request and save the response
			        Response response2 = given()
			        		.header("Authorization", "Bearer " + token)
			                .when()
			                .get()
			                .then()
			                .extract()
			                .response();
			      
			        // Print the JSON content of the response
			        //System.out.println("Response JSON: " + response2.asString()); // Verify that the status code is 200.
			        // Validate that the status code is 200
			        int statusCode2 = response2.getStatusCode();
			        
			        if(statusCode2==200) {
			        	jsonPathEvaluator = response2.jsonPath();
			        	System.out.println("userid: "+user_id+", room_id:"+room_id);
			        	ArrayList<Date> endDates = (jsonPathEvaluator.get("findAll { a -> a.employee.id == "+user_id+" && a.room.id == "+room_id+" }.endDate"));
			        	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			        	 Iterator iterator= endDates.iterator();
			       
			        	  while(iterator.hasNext()) {
			        		  Date d= sdf.parse(iterator.next().toString());
			        		  //active dates retrieved
			        		  if(d.after(new Date()))return true;
			        	  }
			        }
		    	   }catch(Exception e) {
		    		   e.printStackTrace();
		    	   }
		    	   
		    	   return false;
		       }
		      
		   
		   return false;
			}

			
        });
        
        
        // Enable reader mode
        nfc.setMode(BrickletNFC.MODE_READER);


        
        System.out.println("Press key to exit"); System.in.read();
        ipcon.disconnect();
    }
    
    public static void displayDesign(BrickletLCD128x64 lcd, BrickletIndustrialQuadRelayV2 iqr ) throws TinkerforgeException, IOException {
    	// Add GUI button pressed listener
    	
        lcd.addGUIButtonPressedListener(new BrickletLCD128x64.GUIButtonPressedListener() {
            public void guiButtonPressed(int index, boolean pressed) {
                if(pressed) openDoor(iqr); 
            }

			private void openDoor(BrickletIndustrialQuadRelayV2 iqr) {
				// TODO Auto-generated method stub
				try {
					iqr.setValue(new boolean[]{true, true, true, true});
				
            	
                Thread.sleep(millisRelayOpen);
                
                iqr.setValue(new boolean[]{false, false, false, false});
				} catch (TinkerforgeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        });

        

       

        // Clear display
        lcd.clearDisplay();
        lcd.removeAllGUI();

        // Add GUI elements: Button, Slider and Graph with 60 data points
        lcd.setGUIButton(0, 0, 0, 100, 20, "Click to Open");
        

        // Set period for GUI button pressed callback to 0.1s (100ms)
        lcd.setGUIButtonPressedCallbackConfiguration(100, true);

       
    }
}
