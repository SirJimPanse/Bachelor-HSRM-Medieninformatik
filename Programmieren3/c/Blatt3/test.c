#include <stdio.h>

int main(int argc, char *argv[]){
  char a = 7;
  char b = 3;
  printf("%d, ",a | b);
  printf("%d, ",a ^ b);
  printf("%d, ",~(~a & ~b));
  printf("%d, ",a << 1); 
  printf("%d\n",a >> 2);

  return 0;
}
