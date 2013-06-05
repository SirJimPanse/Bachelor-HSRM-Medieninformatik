#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>

extern int my_strcasecmp(const char *s1, const char *s2);
extern const char *my_index(const char *s, int c);

int main(void){
  printf("original: %i\n",strcasecmp("hallo,","hallo,"));
 printf("my_scc: %i\n",strcasecmp("Hallo","hallo"));
  printf("original: %i\n",strcasecmp("hAlLo","HaLlO"));
 printf("my_scc: %i\n",strcasecmp("hAlLo","HaLlO"));
  printf("original: %i\n",strcasecmp("hallo","hello"));
  printf("my_scc: %i\n",strcasecmp("hallo","hello"));

  printf("index: %s\n", index("Hallo",'a'));
 printf("my_index: %s\n", index("Hallo",'a'));
  printf("index: %s\n", index("Hallo",'l'));
  printf("my_index: %s\n", index("Hallo",'l'));
  printf("index: %s\n", index("Hallali",'a'));
  printf("my_index: %s\n", index("Hallali",'a'));
  printf("index: %s\n", index("hallo",'e'));
  printf("my_index: %s\n", index("hallo",'e'));
  
  return 0;
};

int my_strcasecmp(const char *s1, const char *s2){
  while((tolower(*s1++) == tolower(*s2++)))
    ;
  if(*s1 > *s2){
    return *s1 * 1;
  }else if(*s1 < *s2){
    return *s1 * (-1);
  }else{
    return 0;
  }
}

const char *my_index(const char *s, int c){
  const char *temp = s;
  while(tolower(*temp++)-97 != c)
    ;
  return temp;
}

