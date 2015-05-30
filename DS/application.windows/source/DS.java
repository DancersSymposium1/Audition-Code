import processing.core.*; 
import processing.xml.*; 

import javax.swing.ImageIcon; 

import java.applet.*; 
import java.awt.*; 
import java.awt.image.*; 
import java.awt.event.*; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class DS extends PApplet {



int totalNumOfChos;
int totalNumOfDancers;
Boolean incomplete = true;
Dancer[] myDancers;
Choreo[] myChos;
int counter;
int lastTime;


public void setup(){
  ImageIcon titlebaricon = new ImageIcon(loadBytes("myicon.GIF"));
  frame.setIconImage(titlebaricon.getImage());

  lastTime = 0;
  counter = 0;
  size(500, 500);
  noStroke();
  smooth();
  ///////////////////////
  //LOAD THE DANCER FILE!
  String dancerfile = selectInput("Choose the DANCER file...");  // Opens file chooser
  if (dancerfile == null) {
    // If a file was not selected
    println("No file was selected...");
  } 
  else {
    // If a file was selected, print path to file
    println(dancerfile);
  }
  ///////////////////////
  //LOAD THE CHO FILE!
  String chofile = selectInput("Choose the CHOREOGRAPHER file..."); //
  if (chofile == null) {
    // If a file was not selected
    println("No file was selected...");
  } 
  else {
    // If a file was selected, print path to file
    println(chofile);
  }


  String[] theDancers = loadStrings(dancerfile);
  println("there are " + theDancers.length + " lines");
  for (int i=0; i < theDancers.length; i++) {
    println(theDancers[i]);
  }
  totalNumOfDancers = theDancers.length;
  myDancers = new Dancer[totalNumOfDancers];

  for (int i=1; i < theDancers.length; i++) {
    int[] rankTemp = {};
    Boolean sexy = false;    
    String[] temp = split(theDancers[i], ',');
    println(temp.length);
    for(int j = 4; j<temp.length; j++){
      if(temp[j].equals("")||temp[j].equals(" "))
        rankTemp = append(rankTemp, 0);
      else
        rankTemp = append( rankTemp, Integer.parseInt(temp[j]) );
    }
    if(temp[3].toUpperCase().equals("M")){
      sexy = true;
    }
    if(!(temp[0].equals("0") || temp[0].equals("")))
    myDancers[i] = new Dancer(temp[0], temp[1], temp[2], sexy, rankTemp);
  }


  String[] theChos = loadStrings(chofile);
  println("there are " + theChos.length + " lines");
  for (int i=0; i < theChos.length; i++) {
    println(theChos[i]);
  }
  totalNumOfChos = theChos.length;
  myChos = new Choreo[totalNumOfChos];
  int row = 0;
  int column = 0;
  //Disect row by row (each row corresponds to a dance/choreo)
  for (int i=1; i < theChos.length; i++) {
    
    int[] rankTemp = {};
    //put each 'cell' into its own value in temp   
    String[] temp = split(theChos[i], ',');
    
    //take the elements after the basic information and put them into rank temp
    for(int j = 5; j<temp.length; j++){
      if(temp[j].equals("")){
        rankTemp = append(rankTemp, 0);
      }
      else {
        rankTemp = append(rankTemp, Integer.parseInt(temp[j]));
      }
    }
    
    if(Integer.parseInt(temp[2]) == 0){ 
     println("New Mixed Dance with " + temp[3] + " guys and " + temp[4] + " girls"); 
      myChos[i] = new Choreo(temp[0], temp[1], rankTemp, temp[3], temp[4]);
    }
    else{
      println("New Non-Mixed Dance");
      myChos[i] = new Choreo(temp[0], temp[1], temp[2], rankTemp);
    }
    myChos[i].setPosition(10+(100*column), (10+100*row));
    println("Y POSITION: " + (10+ 100*row));
    println("Y POSITION: " + (10+ 100*row));
    column++;
    if((10+100*column) > width){
      row++;
      column = 0;
    }
    
  }

  for(int i = 1; i < myChos.length; i++){
    println("ID: " + myChos[i].id + " \nName: " + myChos[i].name);
    for(int j = 0; j < myChos[i].rank.length; j++){
      println(myChos[i].rank[j]);  
    }
  }
}

public void draw(){
  fill(0,30);
  rect(0, 0, width, height);
   
  for(int i = 1; i<myDancers.length; i++){
    myDancers[i].move();
    myDancers[i].display();
  }
  for(int i = 1; i<myChos.length; i++){
      myChos[i].display();
    }
  
  int currentTime = millis();
  if (currentTime > lastTime+2500) {
    algo();
    
    lastTime = currentTime;
  }

  
  //println(mouseX + "," + mouseY);
}

public void algo(){
  
  //while(incomplete){
    println("Turn No: " + counter);
    int tester = 0;
    for(int i = 1; i < myDancers.length; i++){ 
      sendToCho(myDancers[i]);
    }
    for(int i = 1; i < myDancers.length; i++){
      println("The length of rank for dancer " + myDancers[i].name + " is " + (myDancers[i].rank.length-myDancers[i].getZeros()));
      println("The turn number for dancer " + myDancers[i].name + " is " + myDancers[i].turn);
      if((myDancers[i].turn >= (myDancers[i].rank.length-myDancers[i].getZeros())) || (myDancers[i].heldIn == myDancers[i].dances)){
        tester++;
      }
    }
     if((tester == (myDancers.length-1))){
      //incomplete = lastCheck();
      printResults();
    }
    if(counter == 150){
        println("------PREMATURE TERMINATION--------");
        //incomplete = false;
        printResults();
      }
   
    counter++;
    for(int i = 1; i<myChos.length; i++){
       myChos[i].updateFillPercent();
    }
  //}
  //Last Check to see if we need to truncate due to uneven guys and girls
      
  //printResults();
}

public Boolean lastCheck(){
  for(int i = 1; i < myChos.length; i++){
    if(myChos[i].mixed){
      
      int diff = myChos[i].nonNull(myChos[i].chosenM) - myChos[i].nonNull(myChos[i].chosenF);
      if(diff < 0){
        //myChos[i].removeLast(abs(diff), myChos[i].chosenF);
      } else if (diff > 0){
        //myChos[i].removeLast(abs(diff), myChos[i].chosenM);
      } 
    }
  }
  return false;
}


public void sendToCho(Dancer theDancer){
  println("Dancer " + theDancer.name + " has been sent to search for a cho.");
  //Search for the turn that the dancer is on
  if(theDancer.heldIn < theDancer.dances){
    theDancer.turn++;
    for(int i = 0; i < theDancer.rank.length; i++){
      if(theDancer.rank[i] == (theDancer.turn)){
        println(theDancer.name + " goes to cho number " + i);
        //move dancer to the dance
        theDancer.change(myChos[i+1].x + random(10,30), myChos[i+1].y + random(10,30));
        myChos[i+1].addDancer(theDancer); //change color of square
        break;
      }
    }
  }

}

public void printResults(){
   for(int i = 1; i < myChos.length; i++){
     println("Cho Number " + i);
    if(myChos[i].mixed){
      printArray(myChos[i].chosenM);
      printArray(myChos[i].chosenF);
    } else {
      printArray(myChos[i].chosen);
    }
   }
   printToExcel();
   exit();
}

public void printToExcel(){
  String[] holderArray = new String[myChos.length + 4*myDancers.length];
  int cr = 0;
   for(int i = 1; i < myChos.length; i++){
     
     holderArray[cr] = ("Dance " + myChos[i].name);
     cr++;
     Dancer[] tempp = myChos[i].getChosenList();
     println("Temp length = " + tempp.length);
     for(int k = 0; k < myChos[i].getChosenList().length; k++){
       //println(k);
       println("CR = " + cr);
       if(tempp[k] == null){
         holderArray[cr] = "";
       } else {
         println(tempp[k].name);
         holderArray[cr] = tempp[k].printInfo();
         cr++;
       }  
     }
   }
  String outputter = selectOutput();
  saveStrings(outputter,holderArray);
}

public void printArray(Dancer[] bob){
  for(int j = 0; j < bob.length; j++){
     if(bob[j] != null)
      println(bob[j].name);
     else println("null");
    }
}

public class Choreo {
  
  int guys, girls, dancers, id;
  int[] rank;
  Dancer[] chosen;
  String name;
  Dancer[] chosenM;
  Dancer[] chosenF;
  Boolean mixed;
  float x;
  float y;
  float fillPercent;
  int filled;
  Choreo(){
  }

  Choreo(String id, String name, String dancers, int[] rank){
    this.name = name;
    this.id = Integer.parseInt(id);
    this.rank = rank;
    this.dancers = Integer.parseInt(dancers);
    chosen = new Dancer[this.dancers];
    mixed = false;
    filled = 0;
  }
  
    Choreo(String id, String name, int[] rank, String guys, String girls){
    this.name = name;
    this.id = Integer.parseInt(id);
    this.rank = rank;
    this.guys = Integer.parseInt(guys);
    this.girls = Integer.parseInt(girls);
    chosenM = new Dancer[this.guys];
    chosenF = new Dancer[this.girls];
    mixed = true;
    filled = 0;
  }
  
  public int getID(){
    return id;
  }
  
  public String getTheName(){
    return name;
  }
  
  public void addDancer(Dancer theDancer){
          
      if(getRank(theDancer) >0){
         println("Attempting to place dancer " + theDancer.name + " into cho no " + id);
         if(mixed){
           println("The dancer " + theDancer.name + "s sex is: " + theDancer.sex);
           if(theDancer.sex){
              putAtHighest(theDancer, chosenM);
           } else {
             putAtHighest(theDancer, chosenF);
           }
         } else {
           putAtHighest(theDancer,chosen);
         }
       } 
    }
  
  public int getRank(Dancer daDancer){
    for(int i = 0; i < rank.length; i++){
        if(rank[i] == daDancer.id)
        {
          return (i+1);
        }
    }
    return 0;
  }
  
  public void putAtHighest(Dancer theDancer, Dancer[] dList){
    for(int i = 0; i < dList.length; i++){
        if(dList[i] == null){
          dList[i] = theDancer;
          theDancer.heldIn++;
          filled++;
          break;
        }
        else if(getRank(theDancer) < getRank(dList[i])){
          makeRoom(i, dList);
          dList[i] = theDancer;
          theDancer.heldIn++;
          break;
        }
    }
    for(int i = 0; i < dList.length; i++){
           if(dList[i] != null)
           println("Slot " + i + "=" + dList[i].name);
         }
  }
  
  public void makeRoom(int index, Dancer[] dList){
      //remove unlucky one
      if(dList[(dList.length-1)] != null){
        dList[(dList.length-1)].heldIn--;
        filled--;
      }
      for(int i = (dList.length-1); i > index; i--){
        dList[i] = dList[i-1];
      }
      filled++;
  }
  
  public void removeLast(int num, Dancer[] dList){
      for(int i = (dList.length-1); i >= (dList.length - num); i--){
        //dList[i].heldIn--;
        println("REMOVED " + dList[i].name + " from dance number " + id);
        dList[i] = null;
        
      }
  }
  
  public int nonNull(Dancer[] dList){
    int counter = 0;
    for(int i = 0; i < dList.length; i++){
      if(dList[i] != null)
        counter++;
    }
    return counter;
  }
  
  public Dancer[] getChosenList(){
    if(mixed){
      Dancer[] both = new Dancer[chosenM.length+chosenF.length];
      for(int i = 0; i<chosenM.length; i++){
        both[i] = chosenM[i];}
      for(int i = chosenM.length; i<(chosenM.length + chosenF.length); i++){
        both[i] = chosenF[i-chosenM.length];}
      return both;
    }else{
      return chosen;
    }
  }
  
  public void display(){
    if(fillPercent == 1)
    fill(0, 62, 37);
    else
    fill(35+(fillPercent*(255-35)), 10+(fillPercent*(227-10)), fillPercent*21);
    rect(x, y, 60, 60);
  }
  
  public void setPosition(float myx, float myy){
    this.x = myx;
    this.y = myy;
  }
  public void updateFillPercent(){
    if(mixed)
     fillPercent = PApplet.parseFloat(filled)/PApplet.parseFloat(guys+girls);
     else
     fillPercent = PApplet.parseFloat(filled)/PApplet.parseFloat(dancers); 
     
     println("==================================================================================================fillPercent = " + fillPercent);
  }
}
class Dancer {
  
  int dances, id;
  int[] rank;
  Boolean sex;
  String name;
  int turn = 0;
  int heldIn = 0;
  float beginX = 20.0f + random(0, 100);  // Initial x-coordinate
  float beginY = 10.0f + random(0, 100);  // Initial y-coordinate
  float endX = 570.0f;   // Final x-coordinate
  float endY = 320.0f;   // Final y-coordinate
  float distX;          // X-axis distance to move
  float distY;          // Y-axis distance to move
  float x = 0.0f;        // Current x-coordinate
  float y = 0.0f;        // Current y-coordinate
  float exponent = 2;
  float step = 0.01f;    // Size of each step along the path
  float pct = 0.0f;      // Percentage traveled (0.0 to 1.0)
  
  Dancer(){
    
  }

  Dancer(String id, String name, String dances, Boolean sex, int[] rank){
    distX = endX - beginX;
    distY = endY - beginY;
    this.name = name;
    this.id = Integer.parseInt(id);
    this.rank = rank;
    this.sex = sex; 
    this.dances = Integer.parseInt(dances);
  }
  
  public int getZeros(){
    int counter = 0;
    for(int i = 0; i < rank.length; i++){
      if(rank[i] == 0)
        counter++;
    }
    return counter;
  }
  
  public String printInfo(){
    return ("" + id + "," + name + "," + dances + "," + sex);
  }
  
  public void move(){
    pct += step;
    if (pct < 1.0f) {
    x = beginX + (pct * distX);
    y = beginY + (pow(pct, exponent) * distY);
    }
  }
  
  public void display(){
    fill(255);
    ellipse(x, y, 10, 10);
  }
  
  public void change(float xnew, float ynew){
    pct = 0.0f;
    beginX = x;
    beginY = y;
    endX = xnew;
    endY = ynew;
    distX = endX - beginX;
    distY = endY - beginY;
  }
  
}


  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "DS" });
  }
}
