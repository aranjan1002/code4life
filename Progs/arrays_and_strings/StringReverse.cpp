#include <stdio.h>

int main() {
  char x[10];
  scanf("%s", x);
  int length, i;
  for (length = 0; x[length]; length++);
  for (i=0; i<length/2; i++) {
    char temp = x[i];
    x[i] = x[length - 1 - i];
    x[length - 1 - i] = temp;
  }
  printf("%s\n", x);
}
