import javax.swing.ImageIcon;

int totalNumOfChos;
int totalNumOfDancers;
Boolean incomplete = true;
Dancer[] myDancers;
Choreo[] myChos;
int counter;
int lastTime;


void setup(){
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

void draw(){
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
  
  if (currentTime > lastTime+2400){
  for(int i = 1; i<myChos.length; i++){
       myChos[i].updateFillPercent();
    }
    
}
  
  //println(mouseX + "," + mouseY);
}


void algo(){
  
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
    
  //}
  //Last Check to see if we need to truncate due to uneven guys and girls
      
  //printResults();
}

Boolean lastCheck(){
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


void sendToCho(Dancer theDancer){
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

void printResults(){
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

void printToExcel(){
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
         holderArray[cr] = tempp[k].printInfo((i-1),(myChos[i].getRank(tempp[k])));
         cr++;
       }  
     }
   }
  String outputter = selectOutput();
  saveStrings(outputter,holderArray);
}

void printArray(Dancer[] bob){
  for(int j = 0; j < bob.length; j++){
     if(bob[j] != null)
      println(bob[j].name);
     else println("null");
    }
}

