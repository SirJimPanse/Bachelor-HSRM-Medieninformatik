#include <stdio.h>
#include <stdlib.h>

typedef struct freelist{
  unsigned int size;
  struct freelist *next;
}freelist;

void mem_compact(freelist *l);
freelist *add(freelist *, int);
void show(freelist *);

int main(){
  unsigned int lis[12] = {0,1,2,NULL,NULL,3,4,NULL,5};
  freelist *fl = malloc(sizeof(freelist));

  int i;
  for(i = 0; i < 13; i++)
    add(fl, lis[i]);
  show(fl->next);
  mem_compact(fl->next);
  show(fl->next);
  
  return 0;
}

freelist *add(freelist *l, int wert){
  freelist *new = malloc(sizeof(freelist));
  if(!l){
    new->size = wert;
    new->next = NULL;
    return new;
  }
  while(l->next)
    l = l->next;
  new->size = wert;
  l->next = new;
  new->next = NULL;
  return new;
}

void show(freelist *l){
  if(!l)
    return;
  for(;l;l = l->next)
    printf("%d ",l->size);
  printf("\n");
}

void mem_compact(freelist *l){
  if(!l)
    return;
  while(l->next){
    int dif = ((char *)l->next) - ((char *)l);
    if(l->size == dif){
      l->size += l->next->size;
      l->next = l->next->next;
      l = l->next;
    }else{
      l = l->next;
    }
  }
}
