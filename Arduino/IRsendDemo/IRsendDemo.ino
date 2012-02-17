#include <SerialCommand.h>

/*
 * IRremote: IRsendDemo - demonstrates sending IR codes with IRsend
 * An IR LED must be connected to Arduino PWM pin 3.
 * Version 0.1 July, 2009
 * Copyright 2009 Ken Shirriff
 * http://arcfn.com
 */

#include <IRremote.h>

IRsend irsend;
SerialCommand SCmd;  

unsigned int data[128];
int dataLength = 0;
int freq = 38;

void setup()
{
  Serial.begin(115200);
  //SCmd.addCommand("send",sendNEC);
  SCmd.addCommand("raw",sendRAW);
  SCmd.addCommand("data",readData);
  SCmd.addCommand("d",readD);
  SCmd.addCommand("send",sendData);
  SCmd.addCommand("nikon",sendRAWNikon);
}

void sendNEC()
{
  char *arg;

  if (arg != NULL) 
  {
    int n;
    sscanf (arg,"%x",&n);
    unsigned long data = 0x77E10097 | (n << 8) & 0xff00;
    Serial.println(data,HEX);
    irsend.sendNEC(data, 32);
  } 
}

void readData(){
  char *arg;
  while((arg = SCmd.next())!=NULL){
    Serial.println(arg);
  } 

  char *str;
  dataLength = 0;
  while ((str = strtok_r(arg, ",", &arg)) != NULL) {
    int n;
    Serial.println(str);
    if (sscanf (str,"%d",&n)){
      data[dataLength++] = n;
      Serial.print(dataLength);
      Serial.print(":");
      Serial.println(n);
    }

  }
}

void sendData()
{
  char *arg;
  arg = SCmd.next();

  if (arg != NULL) 
  {
    int n;
    sscanf (arg,"%d",&n);
    irsend.sendRaw(data, n, freq);
    delay(65);
    irsend.sendRaw(data, n, freq);
  }
}

void sendRAW()
{
  unsigned int leng[] = {
    //9065,4484,574,574,574,1668,574,1668,574,1668,574,574,574,1668,574,1668,574,1668,574,1668,574,1668,574,1668,574,574,574,574,574,574,
    3437,1634,500,345,500,1200,500,345,500,345,500,345,500,345,500,345,500,345,500,345,500,345,500,345,500,345,500,345,500,1200,500,345,500,345,500,345,500,345,500,345,500,345,500,1200,500,1200,500,345,500,1200,500,345,500,345,500,345,500,345,500,345,500,345,500,345,500,345,500,345,500,345,500,1200,500,345,500,1200,500,1200,500,345,500,345,500,345,500,345,500,1200,500,345,500,345,500,345,500,345,500,1200,479
  };

  irsend.sendRaw(leng, sizeof(leng)/sizeof(int), 38);
  Serial.print("SENDRAW ");
  Serial.println(sizeof(leng)/sizeof(int));
}

void readD()
{
  char *arg;
  arg = SCmd.next();
  if (arg != NULL) 
  {
    int index = 0;
    sscanf (arg,"%d",&index);
    arg = SCmd.next();
    if (arg != NULL) 
    {
      int d = 0;
      sscanf (arg,"%d",&d);
      data[index] = d;
    }
  }   
}


void sendRaw(int n)
{
  irsend.sendRaw(data, n, freq);
  Serial.println("SENDRAW ");
}


void sendRAWNikon()
{
  unsigned int leng[7] = {
    2000,27800,400,1580,460,3580,400    };

  irsend.sendRaw(leng, 7, 38);
  delay(65);
  irsend.sendRaw(leng, 7, 38);

  Serial.println("SENDRAW");
}

void loop() {
  SCmd.readSerial();  
}





