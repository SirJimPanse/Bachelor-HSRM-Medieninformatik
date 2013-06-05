#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>

typedef struct _ring{
  struct _ring *next;
  char name[10];
}ring;
typedef ring *rptr;

rptr fill(rptr, char*);
void show_list(rptr);
void show(rptr);
void del(rptr, int);

int main(int argc, char *argv[]){
  if(argc > 1){
    int silben;
    sscanf(argv[1],"%i",&silben);
    rptr rng = NULL;
    char *personen[] ={"Uwe","manfred","Klaus","Horst","Inge","Elke"};
    int i;
    for(i=0;i<6;i++){
      rng = fill(rng,personen[i]);
    }
    show_list(rng);
    printf("\n");
    del(rng,silben);
    show_list(rng);
   printf("\n");
    del(rng,silben);
    show_list(rng);
   printf("\n");
    del(rng,silben);
    show_list(rng);
   printf("\n");
    del(rng,silben);
    show_list(rng);
   printf("\n");
    del(rng,silben);
    show_list(rng);
   printf("\n");
    del(rng,silben);
    show_list(rng);
  }
  return 0;
}

rptr fill(rptr r, char *s){
  rptr new = malloc(sizeof(rptr));
  strcpy(new->name,s);

  if(r == NULL){
    new->next = new;  
    return new;
  }
  new->next = r->next;
  r->next = new;
  return new;
}

void show_list(rptr r){
  rptr s = r;
  if(r!=NULL){
    do{
      printf("%s\n",s->name);
      s = s->next;
    }while(s != r);
  }
}

void show(rptr r){
  printf("%s\n",r->name);
}

void del(rptr r, int count){
  rptr s = r;
  rptr temp;
  if(r->next!=r){
    while(count-- > 1){
      s = s->next;
    }
    temp = s->next;
    s->next = temp->next;
    free(temp);
  }
  r = NULL;
}

