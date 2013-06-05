#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>

typedef struct _tree{
  struct _tree *l;
  struct _tree *r;
  char s[30];
  int count;
}stree;

typedef stree *tree;

typedef struct _list{
  struct _list *next;
  int val;
  char s[30];
}llist;

typedef llist *list;

tree fill(tree, char *);
void show(tree );
void out(tree, int);
int check_anz(list );
list t2l(list ,tree );
list tree2list(tree );
void show_list(list );


int main(int argc, char *argv[]){
  if(argc > 1){
    tree t = NULL;
    list lis = NULL;
    int i;
    for(i = 1; i < argc; i++)
       t = fill(t, argv[i]);

    show(t);
    lis = tree2list(t);
    int anz = check_anz(lis);
    out(t,anz);
    show_list(lis);
  
  }else{
    printf("zu wenig Argumente\n");
  }
  return 0;
}

tree fill(tree t, char *str){
  if(t == NULL){
    tree new = malloc(sizeof(stree));
    new->l = NULL;
    new->r = NULL;
    strcpy(new->s,str);
    new->count = 1;
    return new;
  }
  /*printf("%s %s\n",t->s,str);*/
  if(strcmp(t->s, str) == 0){
    t->count += 1;
  }else if(strlen(t->s) >= strlen(str)){
    t->l = fill(t->l,str);
  }else if(strlen(t->s) < strlen(str)){
    t->r = fill(t->r,str);
  }
  return t;
}

list tree2list(tree t){
  list newl = malloc(sizeof(llist));
  t2l(newl,t);
  return newl->next;
}

list t2l(list l,tree t){
  if(t->l)
    l = t2l(l,t->l);
  l->next = malloc(sizeof(llist));
  l->next->next = NULL;
  strcpy(l->next->s,t->s);
  l->next->val = t->count;
  l = l->next;
  if(t->r)
    l = t2l(l, t->r);
  return l;
}

int check_anz(list l){
  list p = l;
  int z = 0;
  for(;p; p= p->next){
    if(z < p->val)
      z = p->val;
  }
  return z;
}

void show(tree t){
  if(!t)
    return;
  show(t->l);
  /*printf("str: %s anzahl: %d\n", t->s, t->count );*/
  show(t->r);  
}

void out(tree t, int z){
  if(!t)
    return;
  if(t->count == z)
    printf("%d: %s\n",z,t->s);
  out(t->l, z);
  out(t->r, z);
}

void show_list(list l){
  list p = l;
  for(;p; p = p->next)
    printf("%s ",p->s);
  printf("\n");
}tree fill(tree, char *);
void show(tree );
void out(tree, int);
int check_anz(list );
list t2l(list ,tree );
list tree2list(tree );
void show_list(list );
