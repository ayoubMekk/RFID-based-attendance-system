import paho.mqtt.client as mqtt
import json
import requests
import time
import RPi.GPIO as GPIO
import lcddriver
from mfrc522 import SimpleMFRC522
reader =SimpleMFRC522()
GPIO.cleanup()
headers = {'Content-type': 'application/json'}
url = "http://192.168.137.1:8082/read"
client = mqtt.Client()


def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))
    client.subscribe("rfid/read")


def on_message(client, userdata, msg):
	 message =  str(msg.payload.decode())
	 print(message)
	 while (1):
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(18,GPIO.OUT)
        display = lcddriver.lcd()
        id,text = reader.read()
	    data = {
	       "idCart" :id,
	    }
	    data_json = json.dumps(data)
	    print("begin reading ..")
	    response = requests.post(url, data=data_json, headers=headers)
	    data  = response.json()
	    user = data['utilisateur']
	    if user is None:
            print("utilisateur inconnnu")
      		GPIO.output(18,GPIO.HIGH)
	        display.lcd_display_string(" utilisateur",1)
 		   	display.lcd_display_string(" inconnu ",2)
		    time.sleep(3)
 		    GPIO.output(18,GPIO.LOW)
		    GPIO.cleanup()
	        display.lcd_clear()
		    break
        else:
		  name = user["name"]
		  etat = data["etat"]
		  idc = user["idcart"]
		  display.lcd_display_string("User " + name + " " + etat ,1)
		  display.lcd_display_string("id : " + idc ,2)
          time.sleep(2)
          display.lcd_clear()


client.on_connect = on_connect
client.on_message = on_message

client.connect("192.168.137.1", 1883, 60)

client.loop_forever()



