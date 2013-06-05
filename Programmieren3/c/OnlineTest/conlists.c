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
list *fill(list *, list *);

int main(){
  list *list1 = NULL;
  list *list2 = NULL;
  list *list3 = NULL;

  int lis1[] = {1,2,2,3,4,1};
  int lis2[] = {6,5,6,7,8,8,9,1,10};
  int i;
  for(i = 0; i < 6; i++)
    list1 = add(list1, lis1[i]);
  int j;
  for(j = 0; j < 9; j++)
    list2 = add(list2, lis2[j]);

  list3 = connect(list1, list2);
  show(list1);
  show(list2);
  show(list3);
  return 0;
}

list *connect(list *l1, list *l2){
  list *newl = malloc(sizeof(list));
  fill(newl, l1);
  fill(newl, l2);
  return newl->next;
}

list *fill(list *ret, list *l1){
  list *first = ret;
  list *akt;
  list *p1 = l1;
  while(p1){
    akt = first; 
    while(akt && p1){  
      if(akt->value == p1->value){
	p1 = p1->next;
	akt = first;
      }else{
	akt = akt->next;
      }
    }
    if(p1){
    add(first, p1->value);
    p1 = p1->next;
    }
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


