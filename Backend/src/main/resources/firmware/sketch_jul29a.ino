#include <WiFi.h>
#include <AsyncTCP.h>
#include <ESPAsyncWebServer.h>

// Replace with your network credentials
const char* ssid = "";
const char* password = "";
int lastRequest = 0;

AsyncWebServer server(8090);

void setup() {
  Serial.begin(115200);
  pinMode(2, OUTPUT);
  pinMode(4, OUTPUT);
  pinMode(13, OUTPUT);

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi..");
  }

  // Print ESP Local IP Address
  Serial.println(WiFi.localIP());

  server.on("/", HTTP_POST, [](AsyncWebServerRequest *request){
    request->send(200);
    digitalWrite(4, LOW);
    digitalWrite(13, HIGH);
    Serial.println("Green LED ON");
    delay(1000);
    digitalWrite(13, LOW);
    Serial.println("Green LED OFF");
    lastRequest = 0;
  });

  server.begin();
}

void loop() {
  digitalWrite(4, LOW);
  delay(1000);
  lastRequest++;
  
  if (lastRequest >= 10) {
    digitalWrite(4, HIGH);
    Serial.println("Orange LED ON");
    delay(1000);
    digitalWrite(4, LOW);
    Serial.println("Orange LED OFF");
  }
}
