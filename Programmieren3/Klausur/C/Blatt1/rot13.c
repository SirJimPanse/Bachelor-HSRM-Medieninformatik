#include<stdio.h>

int main(void){
  int i;
  while((i = getchar()) != EOF){
    if(i >= 'A' && i <= 'Z'){
      i = ((((i - 65)+ 13)) % 26) + 65;
      putchar(i);
    }
    else if(i >= 'a' && i <= 'z'){
      i = ((((i - 97)+ 13)) %26) + 97;
      putchar(i);
    }
    else
      putchar(i);
  }
  return 0;
}

