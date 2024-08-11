package it.gssi.apriporta.doorcontroller;

import com.tinkerforge.IPConnection;
import com.tinkerforge.BrickletGPS.DateTime;

import groovyjarjarantlr4.v4.parse.ANTLRParser.finallyClause_return;

import com.tinkerforge.BrickletNFC;
import com.tinkerforge.BrickletPiezoSpeakerV2;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.print.attribute.standard.DateTimeAtCompleted;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class App 
{
	private static final String HOST = "192.168.1.126";
    private static final int PORT = 4223;
    static boolean found;
    // Change XYZ to the UID of your NFC Bricklet
    private static final String UID = "LB1";
    private static final String speakerUID = "KAt";
    private static final int room_id = 1500;
    // Note: To make the example code cleaner we do not handle exceptions. Exceptions
    //       you might normally want to catch are described in the documentation
    public static void main(String args[]) throws Exception {
        IPConnection ipcon = new IPConnection(); // Create IP connection
        // Note: Declare nfc as final, so the listener can access it
        final BrickletNFC nfc = new BrickletNFC(UID, ipcon); // Create device object
        final BrickletPiezoSpeakerV2 ps = new BrickletPiezoSpeakerV2(speakerUID, ipcon); // Create device object
		
        ipcon.connect(HOST, PORT); // Connect to brickd
        // Don't use device before ipcon is connected

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
                       
                        found = checkAuth(tag.toString(),room_id,  "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyMzQ2OTE4MSwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzIzMzgyNzgxfQ.HCgWsR5EmatVVyts-stepyaIgAvIHnIq5TzTKYDKBExHgKGOfR1bQCrJbBq9iBYFk7lVP8Z3mCA9zl5WTm1anQ");
                        
                        if(found) {
                        	
                        	ps.setBeep(3200, 0, 500);
                        
                        }else {
                        	ps.setBeep(100, 0, 500);	
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
		        RestAssured.baseURI = "http://192.168.1.119:8080/api/tags/bycode/";
		        // Send a GET request and save the response
		        Response response = given()
		        		.header("Authorization", "Bearer " + token)
		                .when()
		                .get(tag)
		                .then()
		                .extract()
		                .response();
		      
		        // Print the JSON content of the response
		        //System.out.println("Response JSON: " + response.asString()); // Verify that the status code is 200.
		        // Validate that the status code is 200
		       int statusCode = response.getStatusCode();
		      
		       if(statusCode==200) {
		    	   JsonPath jsonPathEvaluator = response.jsonPath();
		    	   try {
		    		//System.out.println("Response JSON: " + response.asString()); // Verify that the status code is 200.
				       
		    		int user_id = jsonPathEvaluator.get("id");
		    	 
		    	   
		    	   RestAssured.baseURI = "http://192.168.1.119:8080/api/access-rules?eagerload=true";
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
			        	ArrayList<Date> endDates = (jsonPathEvaluator.get("findAll { a -> a.employee.id == "+user_id+" && a.room.id == "+room_id+" }.endDate"));
			        	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			        	 Iterator iterator= endDates.iterator();
			        	
			        	  while(iterator.hasNext()) {
			        		  Date d= sdf.parse(iterator.next().toString());
			        		  if(d.after(new Date()))return true;
			        	  }
			        	  
			        	
			        }
		    	   }catch(Exception e) {
		    		   e.printStackTrace();
		    	   }
		    	   
		    	   return false;
		       }
		      
		        //response.then().statusCode(200); // validate that the response has a status code of 200.
		        // Validate a specific field value in the response
		        return false;
			}

			
        });
        
        
        // Enable reader mode
        nfc.setMode(BrickletNFC.MODE_READER);

        System.out.println("Press key to exit"); System.in.read();
        ipcon.disconnect();
    }
}
