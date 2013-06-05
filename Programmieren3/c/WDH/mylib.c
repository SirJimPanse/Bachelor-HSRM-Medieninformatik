#include <stdio.h>
#include "mylib.h"

char *my_index(const char *s, int c){
  while(*s != '\0'){
    if(*s == c){
      return (char *) s;
    }
    s +=1;  
  }
  return 0;
}

int my_strcasecmp(const char *s1, const char *s2){
  int scase = 0;

  while((*s1 != '\0') | (*s2 != '\0')){
    if(*s1 >= 'A' && *s1 <= 'Z'){
      if(*s2 >= 'A' && *s2 <= 'Z'){
	scase = *s1 - *s2;
      }else{
	scase = *s1 - (*s2 - 32);	
      }
      if((scase > 0) | (scase < 0)){
	return scase;
      }
      s1 +=1;
      s2 +=1;
    }else{
      if(*s2 >= 'A' && *s2 <= 'Z'){
	scase = *s1 - (*s2 + 32);
      }else{
	scase = *s1 - *s2;
      }
      if((scase > 0) | (scase < 0)){
	return scase;
      }
      s1 +=1;
      s2 +=1;
    }
  }
 return scase;
}
