#include <stdio.h>
#include <stdlib.h>
/*as*/
struct queue{
  struct queue *next;
  int value;
};
typedef struct queue *lqueue;

extern lqueue enter(lqueue, int);
extern int is_empty(lqueue);
extern int front(lqueue);
extern lqueue leave(lqueue);

int main(void){
  lqueue l1 = NULL;
  printf("is_empty: %i\n",is_empty(l1));
  int i;
  for(i = 1;i<12;i++)
    l1 = enter(l1,i);
  printf("front: %i\n",front(l1));
  printf("is_empty: %i\n",is_empty(l1));
  printf("leave:\n");
  l1 = leave(l1);
  printf("front: %i\n",front(l1));
  printf("Loesche alle Elemente\n");
  while(l1 != NULL)
    l1 = leave(l1);
  printf("is_empty: %i\n",is_empty(l1));

  return 0;
}

lqueue enter(lqueue l, int data){
  lqueue new = malloc(sizeof(struct queue));
  new->value = data;
  new->next = NULL;
  if(l == NULL)
    return new;
  lqueue temp = l;
  while(temp->next)
    temp = temp->next;
  temp->next = new;
  return l;
}

int is_empty(lqueue l){
  if(l == NULL)
    return 1;
  return 0;
}

int front(lqueue l){
  return l->value;
}

lqueue leave(lqueue l){
  lqueue temp = l;
  l = l->next;
  free(temp);
  return l;
}



