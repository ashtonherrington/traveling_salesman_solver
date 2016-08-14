import java.util.*;
@SuppressWarnings("unchecked")

class edgeList{

   public ArrayList<singleEdge>[] list;

   public edgeList(int size){
      list = new ArrayList[size];
      for(int i=0; i<size; i++){
         list[i] = new ArrayList();
      }
   }
}
