import paho.mqtt.client as mqtt
import json
import requests

import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522
reader =SimpleMFRC522()


headers = {'Content-type': 'application/json'}
url = "http://192.168.137.1:8082/write"
client = mqtt.Client()


def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))
    client.subscribe("rfid/write")


def on_message(client, userdata, msg):
	message = str(msg.payload.decode())
	print(message)
	dic = json.loads(message)
	id,text = reader.read()
	data = {
		"idCart" :id,
		"nom": dic["name"],
		"email":dic["email"],
	 	"bday":dic["date"]
	}

	data_json = json.dumps(data)
	print("begin writing ..")
	response = requests.post(url, data=data_json, headers=headers) 


client.on_connect = on_connect
client.on_message = on_message

client.connect("192.168.137.1", 1883, 60)

client.loop_forever()


	
