/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.restful.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL; 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author Tech2Go
 */
public class FireMessage {

    private final String SERVER_KEY = "AAAALXPk5Dg:APA91bHCUerdTZ9Pz5m2biw_MrJrw-XJrD-y_6kizuwGs0IU0Ep2pwjwgPmNNhs9Q8Aw2zvzyn9WG8QBe95IvxBfBkZ4GRmx0pqNUah6-9CCrLCwjT4DJWIS2MESL5ravhRS2zpNgF3Y";
    private final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private JSONObject root;

    public FireMessage(String title, String message) throws JSONException {
        root = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("title", title);
        data.put("message", message);
        root.put("data", data);
    }

    public String sendToTopic(String topic) throws Exception { //SEND TO TOPIC
        System.out.println("Send to Topic");
        root.put("condition", "'" + topic + "' in topics");
        return sendPushNotification(true);
    }

    public String sendToGroup(JSONArray mobileTokens) throws Exception { // SEND TO GROUP OF PHONES - ARRAY OF TOKENS
        root.put("registration_ids", mobileTokens);
        return sendPushNotification(false);
    }

    public String sendToToken(String token) throws Exception {//SEND MESSAGE TO SINGLE MOBILE - TO TOKEN
        root.put("to", token);
        return sendPushNotification(false);
    }

    private String sendPushNotification(boolean toTopic) throws Exception {
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-Type", "application/json");
     ////   conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);

        System.out.println(root.toString());

        try {
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(root.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuilder builder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
            System.out.println(builder);
            String result = builder.toString();

            JSONObject obj = new JSONObject(result);

            if (toTopic) {
                if (obj.has("message_id")) {
                    return "SUCCESS";
                }
            } else {
                System.out.println("obj " + obj.toString());
//                int success = Integer.parseInt(obj.getString("success"));
//                if (success > 0) {
//                    return "SUCCESS";
//                }
            }

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
