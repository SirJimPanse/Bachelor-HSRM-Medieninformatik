#include <stdio.h>
#include <stdlib.h>

struct _list{
  struct _list *next;
  int value;
};

typedef struct _list list;

list *connect(list *, list *);
list *add(list *, int);
void show(list *);
list *remdup(list *);

int main(){
  list *list1 = NULL;
  list *list2 = NULL;

  int lis1[] = {1,2,2,3,4,1};
  int lis2[] = {5,6,7,6,8,8,9,1,10};
  int i;
  for(i = 0; i < 6; i++)
    list1 = add(list1, lis1[i]);
  int j;
  for(j = 0; j < 9; j++)
    list2 = add(list2, lis2[j]);

  show(list1);
  connect(list1, list2);
  show(list2);
  show(list1);

  return 0;
}

list *connect(list *l1, list *l2){
  for(;l2; l2 = l2->next)
    l1 = add(l1, l2->value);
  l1 = remdup(l1);
  return l1;
}

list *remdup(list *l){
  list *first = l;
  list *w = l;
  list *r = l;
  while(w->next){
    r = w;
    while(r->next){
      if(r->next->value == w->value){
	r->next = r->next->next;
	r = r->next;
      }else{
	r = r->next;
      }
    }
    w = w->next;
  }
  return first;
}

list *add(list *l, int val){
  list *new = malloc(sizeof(list));
  list *first = l;
  new->value = val;
  new->next = NULL;
  if(l == NULL)
    return new;
  while(l->next)
    l = l->next;
  l->next = new;
  return first;
}

void show(list *l){
  list *p = l;
  for(;p; p = p->next)
    printf("%d ",p->value);
  printf("\n");
}
