package com.example.attendancesystemapi.mqtt;

import com.example.attendancesystemapi.doa.LogsRepository;
import com.google.gson.JsonObject;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.UUID;

@Controller
public class MQttController {

    private String topicRead = "rfid/read";
    private String topicWrite  = "rfid/write";
    private String content = "WRITE";
    int qos  =  1;
    String broker = "tcp://localhost:1883";



    public MqttClient mqttConnect() throws MqttException {

        String clientId     = UUID.randomUUID().toString();
        MemoryPersistence persistence = new MemoryPersistence();
        System.out.println("About to connect to MQTT broker  tcp://localhost:1883");
        MqttClient sampleClient = new MqttClient(broker,clientId,persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(false);
        sampleClient.connect(connOpts);
        System.out.println("Connected");
        return sampleClient;
    }




    @Autowired
    LogsRepository logsRepository;


    @PostMapping(value = "/executeWrite")
    @ResponseBody
    public String executeWrite(@RequestBody Map<String,String> payload){

        String name = payload.get("name");
        String date = payload.get("date");
        String email = payload.get("email");

        JsonObject jsonObject =  new JsonObject();
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("date",date);
        jsonObject.addProperty("email",email);
        try {
            MqttClient client =  mqttConnect();
            MqttMessage message = new MqttMessage(jsonObject.toString().getBytes(Charset.forName("UTF-8")));
            message.setQos(qos);
            client.publish(topicWrite, message);
        } catch(Exception me) {
            me.printStackTrace();
        }
        return name;
    }



    @GetMapping(value = "/executeRead")
    @ResponseBody
    public String executeRead(){
        String msg = "read";
        try {
            MqttClient client =  mqttConnect();
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setQos(qos);
            client.publish(topicRead, message);
            System.out.println("Message published");
        } catch(Exception me) {
            me.printStackTrace();
        }
        return msg;
    }



//    @GetMapping("/mqtt")
//    @ResponseBody
//    public String mqtt(){
//
//        try {
//            MqttClient sampleClient = mqttConnect();
//            sampleClient.subscribe(topicRead, qos);
//            sampleClient.setCallback(new SimpleCallback());
//
//        } catch(MqttException me){
//
//            me.printStackTrace();
//        }
//        return "mqttt";
//    }

}
