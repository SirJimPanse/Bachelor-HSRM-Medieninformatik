#include <stdio.h>
#include <stdlib.h>

struct list{
  struct list *next;
  struct list *prev;
  int value;
};

typedef struct list lptr;

lptr *add(lptr *, int);
void show(lptr *);
void delall(lptr *);

int main(){
  lptr *list = NULL;
  int lis[] = {1,2,3,4,5};
  
  int i;
  for(i = 0; i < 5; i++)
    list = add(list, lis[i]);
  show(list);
  delall(list);
  show(list);

  return 0;
}

lptr *add(lptr *l, int val){
  lptr *new = malloc(sizeof(lptr));
  new->value = val;
  if(l == NULL){
    new->next = NULL;
    new->prev = NULL;
    return new;
  }
  lptr *first = l;
  while(l->next)
    l = l->next;
  new->next = l->next;
  new->prev = l;
  l->next = new;
  return first;
}

void show(lptr *l){
  lptr *p = l;
  for(;p; p = p->next)
    printf("%d ",p->value);
  printf("\n");
}

void delall(lptr *l){
  lptr *next;
  while(l){
    next = l->next;
    free(l);
    l = next;
  }
  free(l);
  /*printf("%d\n",l->prev->value);*/
  l = NULL;
}


