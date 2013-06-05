#include <stdio.h>
#include <stdlib.h>

struct stree{
  int data;
  struct stree *left;
  struct stree *right;
};

typedef struct stree stree;

struct list{
  int data;
  struct list *next;
};

typedef struct list list;

list *stree2list(stree *);
stree *filltree(stree *, int);
void tout(stree *);
list *add(list *, int);
void lout(list *);
list *tree2list(list *, stree *);

int main(){
  stree *tree = NULL;
  list *lis = NULL;
  int list[11] = {6,5,7,3,9,2,4,1,10,8,7};

  int i;
  for(i = 0; i < 11; i++) 
    tree = filltree(tree, list[i]);

  lis = stree2list(tree);
  lout(lis);
  tout(tree);

  return 0;
}

list *stree2list(stree *t){
  if(!t)
    return NULL;
  list *newl = malloc(sizeof(list));
  tree2list(newl,t);
  return newl->next;
};

list *tree2list(list *l, stree *t){
  if(t->left)
    l = tree2list(l,t->left);
  l->next = malloc(sizeof(list));
  l->next->data = t->data;
  l->next->next = NULL;
  l = l->next;
  if(t->right)
    l = tree2list(l,t->right); 
  return l;
}

stree *filltree(stree *t, int data){ 
  if(!t){
    stree *new = malloc(sizeof(stree));
    new->data = data;
    new->right = NULL;
    new->left = NULL;
    return new;
  }
  if(t->data > data){
    t->left = filltree(t->left,data);
  }else if(t->data < data){
    t->right = filltree(t->right,data);
  }
  return t;
}
 
void tout(stree *t){
  printf("%d ",t->data);
  if(t->left)
    tout(t->left);
  if(t->right)
    tout(t->right);
}

void lout(list *l){
  if(!l)
    return;
  for(;l; l = l->next)
    printf("%d ",l->data);
  printf("\n");
}
