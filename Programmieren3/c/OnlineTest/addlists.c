#include <stdio.h>
#include <stdlib.h>

struct list{
  struct list *next;
  int val;
};

typedef struct list list;

list *add(list *, int );
void show(list *);
int list_to_int(list *);
list *addlists(list *, list *);
list *addlast(list *, int);

int main(){
  list *list1 = NULL;
  list *list2 = NULL;
  list *list3 = NULL;

  int lis1[] = {3,2,5,0};
  int lis2[] = {2,2,5,1};

  int i;
  for(i = 0; i < 4; i++)
    list1 = add(list1, lis1[i]);

  int j;
  for(j = 0; j < 4; j++)
    list2 = add(list2, lis2[j]);

  show(list1);
  show(list2);
  list3 = addlists(list1,list2);
  show(list3);

  return 0;
}

list *addlists(list *l1, list *l2){
  list *newl = NULL;
  int z;
  z = list_to_int(l1) + list_to_int(l2);
  printf("%d\n",z);
  while(z != 0){
    newl = addlast(newl, (z%10));
    z /=10;
  }

  return newl;
}

list *addlast(list *l, int val){
  list *new = malloc(sizeof(list));
  new->val = val;
  new->next = NULL;
  if(l == NULL)
    return new;
  new->next = l;
  return new;
}

int list_to_int(list *l){
  list *p = l;
  int zahl = 0;
  for(;p;p = p->next)
    zahl = zahl * 10 + p->val;
  printf("%d\n",zahl);
  return zahl;
}

list *add(list *l, int val){
  list *new = malloc(sizeof(list));
  list *first = l;
  new->val = val;
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
    printf("%d ",p->val);
  printf("\n");
}

