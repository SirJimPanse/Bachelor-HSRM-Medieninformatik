#include <stdio.h>
#include <stdlib.h>
#include "myqueue.h"

lqueue enter(lqueue lq, int val){
  lqueue new = malloc(sizeof(lqueue));
  lqueue akt = lq;

  if(lq == NULL){
    new->next = NULL;
    new->value = val;
    return new;
  }
  while(lq->next){
    lq = lq->next;
  }
  lq->next = new;
  new->value = val;
  new->next = NULL;
  return akt;
}

lqueue leave(lqueue lq){
  lqueue akt = lq->next;
  free(lq);
  lq = akt;
  return lq;
}

int is_empty(lqueue lq){
  if(!lq)
    return 1;
  return 0;
}

int front(lqueue lq){
  return lq->value;
}
