package simpledb.index.query;

import simpledb.query.*;

/**
 * The Scan class for the <i>mergejoin</i> operator.
 * @author Edward Sciore
 */
public class JoinScan implements Scan {
   private Scan s1;
   private SortScan s2;
   private String fldname1, fldname2;
   private Constant joinval = null;
   
   public JoinScan(Scan s1,Scan s2, String fldname1, String fldname2) {
      this.s1 = s1;
      this.s2 = s2;
      this.fldname1 = fldname1;
      this.fldname2 = fldname2;
      beforeFirst();
   }
   

   public void beforeFirst() {
      s1.beforeFirst();
      s2.beforeFirst();
   }
   

   public void close() {
      s1.close();
      s2.close();
   }
   

   public boolean next() {
      boolean h2 = s2.next();
      if (h2 && s2.getVal(fldname2).equals(joinval))
         return true;
      
      boolean h1 = s1.next();
      if (h1 && s1.getVal(fldname1).equals(joinval))
      while (h1 && h2) {
         Constant v1 = s1.getVal(fldname1);
         Constant v2 = s2.getVal(fldname2);
         if (v1.compareTo(v2) < 0)
            h1 = s1.next();
         else if (v1.compareTo(v2) > 0)
            h2 = s2.next();
         else {
            joinval  = s2.getVal(fldname2);
            return true;
         }
      }
      return false;
   }
   
   public Constant getVal(String fldname) {
      if (s1.hasField(fldname))
         return s1.getVal(fldname);
      else
         return s2.getVal(fldname);
   }
   
/
   public int getInt(String fldname) {
      if (s1.hasField(fldname))
         return s1.getInt(fldname);
      else
         return s2.getInt(fldname);
   }
   

   public String getString(String fldname) {
      if (s1.hasField(fldname))
         return s1.getString(fldname);
      else
         return s2.getString(fldname);
   }
   

   public boolean hasField(String fldname) {
      return s1.hasField(fldname) || s2.hasField(fldname);
   }
}