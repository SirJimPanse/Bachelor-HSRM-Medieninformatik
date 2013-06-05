#include <stdio.h>
#include <stdlib.h>

struct list {
  struct list *next;
  int val;
};

typedef struct list list;

list *remove_duplicates(list *);
void show(list *);
list *add(list *, int);
list *add_first(list *, int);
list *remdup(list *);

int main(){
  list *lis = NULL;
  int list[] = {1,1,1,1,2,2,3,4,3,5,6,4,1,7,8,8,9,1,10};
  int i;
  for(i = 0; i < 19; i++)
    lis = add(lis, list[i]);
     
  show(lis);
  lis = remove_duplicates(lis);
  show(lis);
  return 0;
}

list *remove_duplicates(list *l){
  list *p = l;
  list *l2 = NULL;
  for(;p;p = p->next)
    l2 = add_first(l2, p->val);
  l2 = remdup(l2);
  p = l2;
  list *ret = NULL;
  for(;p;p = p->next)
    ret = add_first(ret, p->val);
  free(l2);
  l = ret;
  return l;
}

list *remdup(list *l){
  list *out = l;
  list *w = l;
  list *r = l;
  while(w){
    r = w;
    while(r->next){
      if(w->val == r->next->val){
	r->next = r->next->next;
      }else{
	r = r->next;
      }
    }
    w = w->next;
  }
  return out;
}

list *add_first(list *l, int val){
  list *new = malloc(sizeof(list));
  new->next = NULL;
  new->val = val;
  if(l == NULL)
    return new;
  new->next = l;
  return new;
}

void show(list *l){
  list *p = l;
  for(;p; p = p->next)
    printf("%d ", p->val);
  printf("\n");
}

list *add(list *l, int val){
  list *new = malloc(sizeof(list));
  list *first = l;  
    new->val = val;
    new->next = NULL;
    if(l == NULL){
      return new;
    }
    while(l->next)
      l = l->next;
    l->next = new; 
 return first;
}
