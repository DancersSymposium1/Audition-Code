class Dancer {
  
  int dances, id;
  int[] rank;
  Boolean sex;
  String name;
  int turn = 0;
  int heldIn = 0;
  float beginX = 20.0 + random(0, 100);  // Initial x-coordinate
  float beginY = 10.0 + random(0, 100);  // Initial y-coordinate
  float endX = 570.0;   // Final x-coordinate
  float endY = 320.0;   // Final y-coordinate
  float distX;          // X-axis distance to move
  float distY;          // Y-axis distance to move
  float x = 0.0;        // Current x-coordinate
  float y = 0.0;        // Current y-coordinate
  float exponent = 2;
  float step = 0.01;    // Size of each step along the path
  float pct = 0.0;      // Percentage traveled (0.0 to 1.0)
  
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
  
  int getZeros(){
    int counter = 0;
    for(int i = 0; i < rank.length; i++){
      if(rank[i] == 0)
        counter++;
    }
    return counter;
  }
  
  String printInfo(){
    return ("" + id + "," + name + "," + dances + "," + sex);
  }
  
  void move(){
    pct += step;
    if (pct < 1.0) {
    x = beginX + (pct * distX);
    y = beginY + (pow(pct, exponent) * distY);
    }
  }
  
  void display(){
    fill(255);
    ellipse(x, y, 10, 10);
  }
  
  void change(float xnew, float ynew){
    pct = 0.0;
    beginX = x;
    beginY = y;
    endX = xnew;
    endY = ynew;
    distX = endX - beginX;
    distY = endY - beginY;
  }
  
}

