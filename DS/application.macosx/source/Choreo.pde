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
  
  int getID(){
    return id;
  }
  
  String getTheName(){
    return name;
  }
  
  void addDancer(Dancer theDancer){
          
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
  
  int getRank(Dancer daDancer){
    for(int i = 0; i < rank.length; i++){
        if(rank[i] == daDancer.id)
        {
          return (i+1);
        }
    }
    return 0;
  }
  
  void putAtHighest(Dancer theDancer, Dancer[] dList){
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
  
  void makeRoom(int index, Dancer[] dList){
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
  
  void removeLast(int num, Dancer[] dList){
      for(int i = (dList.length-1); i >= (dList.length - num); i--){
        //dList[i].heldIn--;
        println("REMOVED " + dList[i].name + " from dance number " + id);
        dList[i] = null;
        
      }
  }
  
  int nonNull(Dancer[] dList){
    int counter = 0;
    for(int i = 0; i < dList.length; i++){
      if(dList[i] != null)
        counter++;
    }
    return counter;
  }
  
  Dancer[] getChosenList(){
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
  
  void display(){
    if(fillPercent == 1)
    fill(0, 62, 37);
    else
    fill(35+(fillPercent*(255-35)), 10+(fillPercent*(227-10)), fillPercent*21);
    rect(x, y, 60, 60);
  }
  
  void setPosition(float myx, float myy){
    this.x = myx;
    this.y = myy;
  }
  void updateFillPercent(){
    if(mixed)
     fillPercent = float(filled)/float(guys+girls);
     else
     fillPercent = float(filled)/float(dancers); 
     
     println("==================================================================================================fillPercent = " + fillPercent);
  }
}
