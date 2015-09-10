public class Diet {
   internal class Data {
      internal int nFoods;
      internal int nNutrs;
      internal double[]   foodCost;
      internal double[]   foodMin;
      internal double[]   foodMax;
      internal double[]   nutrMin;
      internal double[]   nutrMax;
      internal double[][] nutrPerFood; 
    
      internal Data(string filename) {
         InputDataReader reader = new InputDataReader(filename);
         
         foodCost = reader.ReadDoubleArray();
         foodMin  = reader.ReadDoubleArray();
         foodMax  = reader.ReadDoubleArray();
         nutrMin  = reader.ReadDoubleArray();
         nutrMax  = reader.ReadDoubleArray();
         nutrPerFood = reader.ReadDoubleArrayArray();
       
         nFoods = foodMax.Length;
         nNutrs = nutrMax.Length;
       
         if ( nFoods != foodMin.Length  ||
              nFoods != foodMax.Length    )
            throw new ILOG.Concert.Exception("inconsistent data in file " 
                                             + filename);
         if ( nNutrs != nutrMin.Length    ||
              nNutrs != nutrPerFood.Length  )
            throw new ILOG.Concert.Exception("inconsistent data in file " 
                                             + filename);
         for (int i = 0; i < nNutrs; ++i) {
            if ( nutrPerFood[i].Length != nFoods )
               throw new ILOG.Concert.Exception("inconsistent data in file " 
                                             + filename);
         }
      }
   }
