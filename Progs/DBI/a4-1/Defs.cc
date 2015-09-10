#include "Defs.h"

string ToString(Type& t) {
  if (t == Int) {
    return "Int";
  }
  else if (t == String) {
    return "String";
  }
  else if (t == Double) {
    return "Double";
  }
  return "";
}
