#include <stdio.h>
#include <string.h>

void encrypt(char *, const char *);
void decrypt(char *, const char *);
int len(const char *);
char key(const char *, int);

int main(){
  char s[] = {"JimPanse123 12"};
  char k[] = {"Affenstall 123"};
  encrypt(s,k);
  printf("%s\n",s);
  decrypt(s,k);
  printf("%s\n",s);

  return 0;
}

void encrypt(char *s, const char *k){
  char *w = s;
  int length = len(k);
  int i = 0;
  int j;
  char crypt;
  while(*w){
    j = (i % length); /*Oder strlen(k)*/
    crypt = *w ^ key(k,j); /*Oder *(K+j)*/
    if((*w == crypt) || (crypt == '\0')){
       w += 1;
       i += 1;    
    }else{
      *w++ = crypt;
      i += 1;
    }
  }  
  *w = '\0';
}

void decrypt(char *s, const char *k){
  encrypt(s, k);
}

int len(const char *k){
  const char *z = k;
  int len = 0;
  for(;*z; z += 1)
    len += 1;
  return len;
}

char key(const char *k, int i){
  const char *p = k;
  while(i--)
    p += 1;
  return *p;
}
