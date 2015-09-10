      }
      catch (ILOG.Concert.Exception ex) {
         System.Console.WriteLine("Concert Error: " + ex);
      }
      catch (InputDataReader.InputDataReaderException ex) {
         System.Console.WriteLine("Data Error: " + ex);
      }
      catch (System.IO.IOException ex) {
         System.Console.WriteLine("IO Error: " + ex);
      }
   }
