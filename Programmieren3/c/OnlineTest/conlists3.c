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
list *copy(list *);

int main(){
  list *list1 = NULL;
  list *list2 = NULL;

  int lis1[] = {1,2,2,3,4,1};
  int lis2[] = {5,6,2,7,6,8,8,3,9,1,10,4};
  int i;
  for(i = 0; i < 6; i++)
    list1 = add(list1, lis1[i]);
  int j;
  for(j = 0; j < 12; j++)
    list2 = add(list2, lis2[j]);

  show(list1);
  connect(list1, list2);
  show(list2);
  show(list1);

  return 0;
}

list *connect(list *l1, list *l2){
  list *first = l1;
  list *w = copy(l1);
  list *start = w;
  list *r = l2;
  while(r){
    w = start;
    while(w && r){
      if(w->value == r->value){
	r = r->next;
	w = start;
      }else{
	w = w->next;
      }
    }
    if(r){
    first = add(first, r->value);
    r = r->next;
    }
  }
  return first;
}

list *copy(list *l){
  list *newl = NULL;
  list *p = l;
  for(;p; p = p->next)
    newl = add(newl, p->value);
  return newl;
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
