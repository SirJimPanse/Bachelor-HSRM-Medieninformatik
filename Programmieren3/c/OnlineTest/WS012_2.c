#include <stdio.h>
#include <stdlib.h>

typedef struct list{
  struct list *next;
  int value;
}_list;

typedef _list *list;

list add(list, int);
void show_list(list);
list add_lists(list, list, list);

int main(){
  list list1 = NULL;
  list list2 = NULL;
  int lis1[] = {1,2,3,4};
  int lis2[] = {5,6,7,8};

  int i;
  for(i = 0; i < 4; i++)
    list1 = add(list1,lis1[i]);
  
  int j;
  for(j = 0; j < 4; j++)
    list2 = add(list2,lis2[j]);

  show_list(list1);
  show_list(list2);

  list list3 = NULL;
  list3 = add_lists(list3,list1,list2);
  show_list(list3);
  
  return 0;
}

list add_lists(list l, list l1, list l2){
  list p1 = l1;
  list p2 = l2;
  while(p1 && p2){
    l = add(l,(p1->value + p2->value));
    p1 = p1->next;
    p2 = p2->next;
  }
  return l;
}

list add(list l, int val){
  list new = malloc(sizeof(list));
  new->value = val;
  if(l == NULL){
    new->next = NULL;
    return new;
  }
  list first = l;
  while(l->next){
    l = l->next;
  }
  new->next = l->next;
  l->next = new;
  return first;
}

void show_list(list l){
  list p = l;
  for(;p; p = p->next)
    printf("%d ",p->value);
  printf("\n");
}
