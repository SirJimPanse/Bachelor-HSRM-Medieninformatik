#include <stdio.h>
#include "rot13lib.h"

int rot;
int rotate(int ch){
  if(rot == NULL){
    rot = 0;
  }
  if(ch >= 'A' && ch <= 'Z'){
    ch = (((ch - 'A')+rot)%26)+'A';
  }else if(ch >= 'a' && ch <= 'z'){
    ch = (((ch -'a')+rot)%26)+'a';
  }
  return ch;
}

void set_rotate(int n){
  rot = n;
}
