#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include "DHT.h"
#include "time.h"
#include <WiFiUdp.h>
#include <NTPClient.h>
#include <ArduinoJson.h>

#define FIREBASE_HOST "plugnet-d688b.firebaseio.com"
#define FIREBASE_AUTH "ap8X2xTGstiJvDPLblJKjuyE9L1GxCsGkFjBoNiM"
#define WIFI_SSID "DESKTOP-98PTUTA 6271"
#define WIFI_PASSWORD "2aW40!00"
#define DHTTYPE DHT11

#define NTP_OFFSET   60 * 60      // In seconds
#define NTP_INTERVAL 60 * 1000    // In miliseconds
#define NTP_ADDRESS  "europe.pool.ntp.org"

//Temperature API
String APIKEY = "076836f4d5f3a9696e7983d56fc515ab";
String CityName;
WiFiClient client;
char servername[] = "api.openweathermap.org";
String result;
int  counter = 300;
float OutsideTemperature;

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, NTP_ADDRESS, NTP_OFFSET, NTP_INTERVAL);

String ID = "002";
String Temperature = "Plugs/" + ID + "/Temperature";
String Mode = "Plugs/" + ID + "/Mode";
String Status = "Plugs/" + ID + "/Plug_Status";
String Threshhold = "Plugs/" + ID + "/Threshhold";
String overrideFB = "Plugs/" + ID + "/Override";

int relayInput = 2;
int n = 0;
int m = 0;
float o = 0;
int z = 0;
String childName;
String childTemp;

const int DHTPin = 5;
DHT dht(DHTPin, DHTTYPE);

static char celsiusTemp[7];
static char fahrenheitTemp[7];
static char humidityTemp[7];

//----------------------------------------------------------------------------------------------------------------------

void setup() {
  Serial.begin(9600);

  pinMode(relayInput, OUTPUT);

  WIFI_Connect();
  dht.begin();
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  timeClient.begin();
}

//----------------------------------------------------------------------------------------------------------------------

void loop() {

  if (WiFi.status() != WL_CONNECTED) {
    WIFI_Connect();
  }

  m = Firebase.getInt(Mode);
  n = Firebase.getInt(Status);
  timeClient.update();

  if (counter == 300)
  {
    CityName = Firebase.getString("Location/Location");
    counter = 0;
    getWeatherData();
  } else {
    counter++;
  }

  int override = Firebase.getInt(overrideFB);
  Serial.println("Override status: " + String(override));
  if (override == 0) {
    Serial.println("Plug is override, switch is off");
    digitalWrite(relayInput, LOW);
    Firebase.setInt(Status, 0);
    delay(1000);
  } else {
    switch (m) {
      case 1:
        Serial.println("Default mode");
        //default mode
        internalTemperature();
        if (n == 1) {
          Serial.println("Plug is on");
          digitalWrite(relayInput, HIGH);
        }
        if (n == 0) {
          Serial.println("Plug is off");
          digitalWrite(relayInput, LOW);
        }
        break;
      case 2:
        //Fan mode
        Serial.println("Fan mode");
        internalTemperature();
        if (OutsideTemperature < temperature()) {
          if (OutsideTemperature > thresholdTemperature()) {
            Firebase.setInt(Status, 1);
            Serial.println("Plug is on");
            digitalWrite(relayInput, HIGH);
          }
          if (OutsideTemperature < thresholdTemperature()) {
            Firebase.setInt(Status, 0);
            Serial.println("Plug is off");
            digitalWrite(relayInput, LOW);
          }
        }
        if (OutsideTemperature >= temperature()) {
          if (temperature() > thresholdTemperature()) {
            Firebase.setInt(Status, 1);
            Serial.println("Plug is on");
            digitalWrite(relayInput, HIGH);
          }
          if (temperature() < thresholdTemperature()) {
            Firebase.setInt(Status, 0);
            Serial.println("Plug is off");
            digitalWrite(relayInput, LOW);
          }
        }

        break;
      case 3:
        //radiator mode
        Serial.println("Radiator mode");
        internalTemperature();
        if (OutsideTemperature <= temperature()) {
          if (temperature() < thresholdTemperature()) {
            Firebase.setInt(Status, 1);
            Serial.println("Plug is on");
            digitalWrite(relayInput, HIGH);
          }
          if (temperature() > thresholdTemperature()) {
            Firebase.setInt(Status, 0);
            Serial.println("Plug is off");
            digitalWrite(relayInput, LOW);
          }
        }
        if (OutsideTemperature > temperature()) {
          if (OutsideTemperature < thresholdTemperature()) {
            Firebase.setInt(Status, 1);
            Serial.println("Plug is on");
            digitalWrite(relayInput, HIGH);
          }
          if (OutsideTemperature > thresholdTemperature()) {
            Firebase.setInt(Status, 0);
            Serial.println("Plug is off");
            digitalWrite(relayInput, LOW);
          }
        }
        break;
      default:
        break;
    }
    delay(1000);
  }
}

//----------------------------------------------------------------------------------------------------------------------

void WIFI_Connect() {
  WiFi.disconnect();
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    digitalWrite(relayInput, LOW);
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
}

//----------------------------------------------------------------------------------------------------------------------

void internalTemperature() {
  //Time & date
  String formattedDate = timeClient.getFormattedDate();
  int split = formattedDate.indexOf("T");
  String dateStamp = formattedDate.substring(0, split);
  String timeStamp = formattedDate.substring(split + 1, formattedDate.length() - 1);

  //Upload current temperature to Firebase
  Firebase.setFloat(Temperature, temperature());
  if (Firebase.failed()) {
    Serial.println("Setting temperature in Firebase falied: ");
    Serial.println(Firebase.error());
    ESP.reset();
  }

  //upload temperature to temperature history in Firebase
  if (z % 100 == 0) {
    //Get time and date stamps
    Serial.println("Time history updated");
    childName = "TemperatureHistory/" + dateStamp;
    String childTime = childName + "/" + timeStamp;
    childTemp = childTime + "/Temperature";

    Firebase.setFloat(childTemp, temperature());
    if (Firebase.failed()) {
      Serial.println("Setting temperature in Firebase falied: ");
      Serial.println(Firebase.error());
      ESP.reset();
    }

    z = 0;
  }
  z++;
}

//----------------------------------------------------------------------------------------------------------------------

float temperature() {
  float h = dht.readHumidity();
  // Read temperature as Celsius (the default)
  float t = dht.readTemperature();
  // Read temperature as Fahrenheit (isFahrenheit = true)
  float f = dht.readTemperature(true);

  if (isnan(h) || isnan(t) || isnan(f)) {
    Serial.println("Failed to read from DHT sensor!");
    strcpy(celsiusTemp, "Failed");
    strcpy(fahrenheitTemp, "Failed");
    strcpy(humidityTemp, "Failed");
  }
  else {
    // Computes temperature values in Celsius + Fahrenheit and Humidity
    float hic = dht.computeHeatIndex(t, h, false);
    dtostrf(hic, 6, 2, celsiusTemp);
    float hif = dht.computeHeatIndex(f, h);
    dtostrf(hif, 6, 2, fahrenheitTemp);
    dtostrf(h, 6, 2, humidityTemp);
    delay(1000);

    return t;
  }
}

//----------------------------------------------------------------------------------------------------------------------

float thresholdTemperature() {
  o = Firebase.getInt(Threshhold);
  return o;
}

//----------------------------------------------------------------------------------------------------------------------

void getWeatherData()
{
  if (client.connect(servername, 80))
  { //starts client connection, checks for connection
    client.println("GET /data/2.5/weather?APPID=076836f4d5f3a9696e7983d56fc515ab&q=nottingham,uk&units=metric");
    client.println("Host: api.openweathermap.org");
    client.println("User-Agent: ArduinoWiFi/1.1");
    client.println("Connection: close");
    client.println();
  }
  else {
    Serial.println("connection failed");        //error message if no client connect
    Serial.println();
  }

  while (client.connected() && !client.available())
    delay(1);                                          //waits for data
  while (client.connected() || client.available())
  { //connected or data available
    char c = client.read();                     //gets byte from ethernet buffer
    result = result + c;
  }

  client.stop();                                      //stop client
  result.replace('[', ' ');
  result.replace(']', ' ');
  char jsonArray [result.length() + 1];
  Serial.println("Results: " + result);
  result.toCharArray(jsonArray, sizeof(jsonArray));
  jsonArray[result.length() + 1] = '\0';
  StaticJsonBuffer<1024> json_buf;
  JsonObject &root = json_buf.parseObject(jsonArray);

  if (!root.success())
  {
    Serial.println("parseObject() failed");
  }

  float temperature = root["main"]["temp"];

  OutsideTemperature = temperature;

  Firebase.setFloat("Plugs/" + ID + "/External_Temp", OutsideTemperature);
  if (Firebase.failed()) {
    Serial.println("Setting temperature in Firebase falied: ");
    Serial.println(String(Firebase.error()));
    ESP.reset();
  }

  Serial.println("Temperature: " + String(OutsideTemperature));

}
