#include <stdio.h>
#include <strings.h>
#include <string.h>
#include "mylib.h"

int main(){
  char *strings1[]={"Hallo","hallo","Hallali"};
  char *strings2[]= {"hello","Hallo","Hallo"};
  char chars[] = {'a','l','e'};

  int i;
  for(i = 0; i <= 2; i++){
    if(strcasecmp(strings1[i],strings2[i]) != my_strcasecmp(strings1[i],strings2[i])){
	printf("%s\n %s\n",strings1[i],strings2[i]);
    }else if(index(strings1[i],chars[i]) != my_index(strings1[i],chars[i])){
	 printf("%s\n %c\n",strings1[i],chars[i]);
    }else{
       printf("ALLES SUUUPER!!\n");
    }   
  }


  return 0;
}
