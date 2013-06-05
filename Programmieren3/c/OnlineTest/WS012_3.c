#include <stdio.h>
#include <stdlib.h>

struct node{
  int zahl;
  struct node *next;
};

struct warteschlange{
  struct node *head;
  struct node *tail;
};

typedef struct warteschlange *wptr;
typedef struct node *node;

wptr offer(wptr, int);
int is_empty(wptr);
void has_element(wptr);
void show(wptr);
void pull(wptr);

int main(){
  wptr w = NULL;
  int lis[] = {1,2,3,4,5};
  
  if(is_empty(w)){
    printf("Liste leer!\n");
  }else{
    printf("Liste befüllt!\n");
  }

  has_element(w);

  int i;
  for(i = 0; i < 5; i++)
    w = offer(w, lis[i]);

  if(is_empty(w)){
    printf("Liste leer!\n");
  }else{
    printf("Liste befüllt!\n");
  }
  
  has_element(w);
  show(w);
  pull(w);
  show(w);
  pull(w);
  pull(w);
  show(w);
  pull(w);
  show(w);
  pull(w);
  return 0;
}

wptr offer(wptr w, int val){
  node newn = malloc(sizeof(node));
  newn->zahl = val;
  if(w == NULL){
    wptr newq = malloc(sizeof(wptr));
    newq->head = newn;
    newq->tail = newn;
    return newq;
  }
  node nod = w->tail;
  nod->next = newn;
  newn->next = NULL;
  w->tail = newn;
  return w;
}

void pull(wptr w){
  if(w->head == w->tail){
    free(w->head);
    w = NULL;
  }else{
  node temp = w->head->next;
  free(w->head);
  w->head = temp;
  }
}

void has_element(wptr w){
  if(!w){
    printf("Kein Element vorhanden!\n");
  }else{
  printf("%d\n", w->head->zahl);
  }
}

int is_empty(wptr w){
  if(!w)
    return 1;
  return 0;
}

void show(wptr w){
  node n = w->head;
    for(;n; n = n->next)
      printf("%d ",n->zahl);
    printf("\n");
}

