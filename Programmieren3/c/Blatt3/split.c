#include <stdio.h>
#include <stdlib.h>

struct _split  *split(char *, char *);
int groesse(char *);
void clear(struct _split *);
char *checkSameAndSplit(char *, char *);

typedef struct _split{
  char **out;
  int anzahl;
}splits;

splits  *ptr;
splits *aus;

int main(int argc, char *argv[]){
  ptr = malloc(sizeof(struct _split));
  ptr->out = malloc(sizeof(int));
  ptr->anzahl = 0;
  if(argc > 1){
    int i;
    for(i = 2; i < argc; i++){
      split(argv[1],argv[i]); 
      ptr->out += 1;
    }  
    printf("Splits: %s\n", *(ptr)->out);
    printf("Anzahl Splits: %d\n", ptr->anzahl);
  }
  return 0;
  clear(ptr);
}


/*Ermittelt benötigte Größe des Split-Strings*/
int groesse(char *delim){
  int i;
  for(i = 0; *delim++;i++);
  return i;
}

/*Prüft ob kompletter String in akdelim*/
char *checkSameAndSplit(char *str, char *aktdelim){
  char *out = aktdelim;
  while((*aktdelim) && (*str)){
    if(*aktdelim++ != *str++)
      return out;
  }
   ptr->anzahl += 1;
   *(aktdelim-1) = ' ';
   return (aktdelim-1);
}

struct _split *split(char *str, char *delim){
  char *spliter = malloc(groesse(delim));
  char *out = spliter;
  
  while(*delim){
    if(*str == *delim) 
      delim = checkSameAndSplit(str,delim);      
    *spliter++ = *delim++; 
  }
  *spliter = '\0';
  (*(ptr)->out) = out;

  return ptr;
  free(spliter);
  free(out);
}

void clear(struct _split *delim){
  free(delim);
}
