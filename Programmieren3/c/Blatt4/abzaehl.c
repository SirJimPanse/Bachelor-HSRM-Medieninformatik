#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include <string.h>

#define MAX 20

typedef struct _ring{
  struct _ring *next;
  char person[MAX];
}ring;

typedef ring *ptr;

ptr insert(ptr , char *);
void showlist(ptr, int);
void delete(ptr, int);

int main(int argc, char *argv[]){
  ptr akt = NULL;
  int silben = argc -1;
  char *personen[] = {"Horst","Hansi","Inge","Elke","Uwe","Manfred","Klaus","Marianne"};
  int i;

  for(i = 0; i < 8 ; i++){
    akt = insert(akt,personen[i]);
  }
  delete(akt, silben);
  return 0;
}

ptr insert(ptr rng, char *s){
  ptr neu = malloc(sizeof(ptr));

  if(rng == NULL){
    neu->next = neu;
    strcpy(neu->person,s);
    return neu;
	}
  strcpy(neu->person,s);
  neu->next = rng->next;
  rng->next = neu;
  return neu;
}

void show(ptr rng){
  printf("%s\n",rng->person);
}

void showlist(ptr rng, int i){
  int j;
  for(j = 0;j++ < i;rng = rng->next){
    printf("%s\n",rng->person);
  }
}

void delete(ptr rng, int silben){
  ptr temp = rng;
  int anzahls;
  while(rng){
    anzahls = silben;
    while(anzahls > 1){
      anzahls -= 1;
      rng = rng->next;
    }
   if(rng->next == rng){
      printf("%s gewinnt\n",rng->person);
      rng = NULL;
   }else{ 
    temp = rng->next->next;
      printf("%s fliegt raus\n",rng->next->person); 
    free(rng->next);
    rng->next = temp;  
   }
  }
}
