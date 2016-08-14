import java.util.ArrayList;

public class vertex implements Comparable<vertex>{
   
   private int id;
   private int x_var;
   private int y_var;
   private int priority;
   private vertex parent;
   public ArrayList<vertex> children;
   private int neighbors;  
   private int distance; 
   
   void setDistance(int distance){
      this.distance = distance;
   }
   
   int getDistance(){
      return distance;
   }
   
   int getID(){
      return id;
   }

   int getX(){
      return x_var;
   }
   
   int getY(){
      return y_var;
   }
   
   public vertex(int id, int x_var, int y_var){
      this.id = id;
      this.x_var = x_var;
      this.y_var = y_var;
      this.priority = Integer.MAX_VALUE;
      this.children = new ArrayList<vertex>();
      this.neighbors = 0;
   }

   public vertex(int id, vertex parent, int distance){
      this.id = id;
      this.parent = parent;
      this.distance = distance;   
   }


   int getPriority(){
      return priority;
   }

   void setPriority(int priority){
      this.priority = priority;
   }

   int getNeighbors(){
      return neighbors;
   }
    
   void addNeighbor(){
      neighbors++;
   }

   vertex getParent(){
      return parent;
   }

   void setParent(vertex parent){
      this.parent = parent;
      neighbors++;
   }

   public int compareTo(vertex other){
      if(other.getPriority() > this.getPriority()){
         return -1;
      }
      else if(other.getPriority() < this.getPriority()){
         return 1;
      }
      return 0;
   }
}
