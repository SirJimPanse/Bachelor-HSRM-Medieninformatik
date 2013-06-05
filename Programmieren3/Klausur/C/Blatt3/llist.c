#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct _llist{
  struct _llist *next;
  struct _llist *prev;
  int data;
};
typedef struct _llist *lptr;

lptr add_first(lptr, int);
lptr add_ith(lptr, int, int);
lptr add_last(lptr, int);
lptr del_first(lptr);
lptr del_ith(lptr, int);
lptr del_last(lptr);
void del_all(lptr);
lptr copy(lptr);
void show_list(lptr);

int main(void){
  lptr l1 = NULL;
  
  int i;
  for(i = 1; i<12;i++)
    l1 = add_last(l1,i);
  show_list(l1);
  printf("add_first 0: ");
  l1 = add_first(l1,0);
  show_list(l1);
  printf("add_ith 5,17: ");
  l1 = add_ith(l1,5,17);
  show_list(l1);
  printf("del_first : ");
  l1 = del_first(l1);
  show_list(l1);
  printf("del_last : ");
  l1 = del_last(l1);
  show_list(l1);
  printf("del_ith 5: ");
  l1 = del_ith(l1,5);
  show_list(l1);
  lptr l2 = NULL;
  l2 = copy(l1);
  printf("Copy List: ");
  show_list(l2);
  del_all(l1);
  return 0;
}

lptr add_first(lptr l, int value){
  lptr new = malloc(sizeof(lptr));
  new->data = value;
  if(l == NULL){
    new->next = NULL;
    new->prev = NULL;
    return new;
  }
  new->next = l;
  new->prev = NULL;
  return new;
}

lptr add_last(lptr l, int value){
  lptr new = malloc(sizeof(lptr));
  new->data = value;
  if(l == NULL){
    new->next = NULL;
    new->prev = NULL;
    return new;
  }
  lptr first = l;
  while(l->next)
    l = l->next;
  l->next = new;
  new->prev = l;
  new->next = NULL;
  return first;
}

lptr add_ith(lptr l, int i, int value){
  lptr new = malloc(sizeof(lptr));
  new->data = value;
  if(l == NULL){
    new->next = NULL;
    new->prev = NULL;
    return new;
  }
  lptr temp = l;
  while(i-- > 1){
    temp = temp->next;
  }
  new->next = temp->next;
  temp->next->prev = new;
  new->prev = temp->prev;
  temp->prev->next = new;
  return l;
}

lptr del_first(lptr l){
  lptr first = l->next;
  first->prev = NULL;
  free(l);
  return first;
}

lptr del_last(lptr l){
  lptr temp = l;
  while(temp->next)
    temp = temp->next;
  temp->prev->next = NULL;
  free(temp);
  return l;
}

lptr del_ith(lptr l, int i){
  lptr temp = l;
  while(i-- > 1)
    temp = temp->next;
  temp->prev->next = temp->next;
  temp->next->prev = temp->prev;
  free(temp);
  return l;
}

void del_all(lptr l){
  lptr temp;
  while(l->next){
    temp = l->next;
    free(l);
    l = temp;
  }
  free(l);
  printf("Liste geloescht\n");
}

lptr copy(lptr l){
  lptr temp = l;
  lptr new = NULL;
  while(temp){
    new = add_last(new,temp->data);
    temp = temp->next;
  }
  return new;
}

void show_list(lptr l){
  lptr temp;
  for(temp = l;temp->next != NULL;temp = temp->next)
    printf("%i ",temp->data);
  printf("\n");
}
