#include <stdio.h>
#include "rot13lib.h"

int main(){
  char c;
  while((c = getchar()) != EOF){
  set_rotate(1);
  c = rotate(c);
  putchar(c);
  }

  return 0;
}
