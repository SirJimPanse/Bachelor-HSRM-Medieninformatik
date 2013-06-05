#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

char *komprimier(const char *string);
int lenKomp(const char *string);
char *dekomprimier(const char *string);
int lenDekomp(const char *string);

int main(int argc, char *argv[]){
  if(strcmp(argv[0],"./komprimier") == 0){
    printf("%s\n",komprimier(argv[1]));
  }else if(strcmp(argv[0],"./dekomprimier") == 0){
    printf("%s\n",dekomprimier(argv[1]));
  }
  return 0;
}

char *toString(int i){
  char *string = malloc(sizeof(int));
  char ps[sizeof(int)];
  char *mem = ps;
  char *p = string;

  while(i != 0){
    *mem++ = (i % 10) + '0';
    i /= 10;
  }
  while(mem != ps){
    *string++ = *(mem-1);
    mem -= 1;
  }
 
  return p;
}

int lenKomp(const char *string){
  int mem = 0;
  while(*string++){
    if(*string == *(string + 1)){
      mem += 1;
      while(*string == *(string + 1)){
    	string += 1;
      }
    }
    mem +=1;
  }
  return mem;
}

char *komprimier(const char *string){
  int count = 1;
  char *komp = malloc(lenKomp(string));
  char *out = komp;
  int len = 0;

  while(*string){
    *komp++ = *string;
    while(*string == *(string + 1)){
      count += 1;
      string += 1;
    }
    if(count > 1){
      int i;
      char *link = toString(count);
      for(i = 0; *link;i++){
    len += 1;
    link += 1;
      }
      strcat(komp,toString(count));
      komp += len;
      count = 1;
      len = 0;
    }
    string += 1;
  }
  *komp = '\0';

  return out;
}

int lenDekomp(const char *string){
  return 0;
}

char *dekomprimier(const char *string){
  char *dekomp = malloc(sizeof(int) * 20);
  char *out = dekomp;
  int count = 0;
  int z;

  while(*string){
    z = 0;
    if(sscanf(string,"%d",&count)){
      while(count-- > 1){
	*dekomp++ = *(string-1);
      }
       while(count > 0){
	count /= 10;
	z += 1;
      }
      string += z;
    }else{ 
      *dekomp++ = *string;
    }
    string += 1;
  }
  *dekomp = '\0';

  return out;
}
