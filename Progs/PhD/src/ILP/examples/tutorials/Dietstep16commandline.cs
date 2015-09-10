         for (int i = 0; i < args.Length; i++) {
            if ( args[i].ToCharArray()[0] == '-') {
               switch (args[i].ToCharArray()[1]) {
               case 'c':
                  byColumn = true;
                  break;
               case 'i':
                  varType = NumVarType.Int;
                  break;
               default:
                  Usage();
                  return;
               }
            }
            else {
               filename = args[i];
               break;
            }
         }
        
         Data data = new Data(filename);
