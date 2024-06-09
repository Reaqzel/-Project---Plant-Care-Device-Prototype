//Thermometer with thermistor
 
/*thermistor parameters:
 * RT0: 10 000 Ω
 * B: 3977 K +- 0.75%
 * T0:  25 C
 * +- 5%
 */
 
//These values are in the datasheet
#define RT0 5000   // Ω
#define B 3950      // K
//--------------------------------------
 
 
#define VCC 5    //Supply voltage
#define R 3300  //R=10KΩ

double ldr_val= 0;
double hygrometer_val= 0;
double TX;

//Variables
float RT, VR, ln,  T0, VRT;
 
void setup() {
  Serial.begin(9600);
  T0 = 25 + 273.15;                 //Temperature T0 from datasheet, conversion from Celsius to kelvin
}
 


 
void loop() {
  ldr_val = analogRead('A0'); 
  hygrometer_val = analogRead('A2');
  VRT = analogRead(A1);              //Acquisition analog value of VRT
  VRT = (5.00 / 1023.00) * VRT;      //Conversion to voltage
  VR = VCC - VRT;
  RT = VRT / (VR / R);               //Resistance of RT
 
  ln = log(RT / RT0);
  TX = (1 / ((ln / B) + (1 / T0))); //Temperature from thermistor
 
  TX = TX - 273.15;                 //Conversion to Celsius
  
  delay(500);
  Serial.println(((ldr_val)/1023)*100);
  Serial.println((1-(hygrometer_val/1023))*100);
  Serial.println(TX);
 
  if(Serial.available() > 0){
    String command = Serial.readStringUntil('\n');
    if(command == "IRRIGATE"){
      irrigate();
    }
  }
 
}