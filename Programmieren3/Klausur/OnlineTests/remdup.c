#include <stdio.h>
#include <stdlib.h>

char *remdup(char *s);

int main(void){
  char s[] = {"aaabbbcccdde"};
  printf("%s\n",remdup(s));
  
  return 0;
}

char *remdup(char *s){
  char *w = s;
  char *r = s;
  while(*r++){
    if(*w != *r)
      *++w = *r;
  }
  *w = '\0';
  return s;
}
