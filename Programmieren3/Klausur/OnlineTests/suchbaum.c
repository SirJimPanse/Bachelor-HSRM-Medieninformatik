#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include <string.h>

struct tree{
  struct tree *left;
  struct tree *right;
  char name[10];
};
typedef struct tree *tptr;

tptr fill(tptr, char *);
void show(tptr);
void del(tptr);

int main(void){
  tptr t1 = NULL;
  char *namen[] = {"Ruffy","Zorro","Ruffy","Sanji","Nami","Lysop","Nami",                     "Chopper","Franky","Robin"};
  int i;
  for(i = 0; i < 10; i++){
    t1 = fill(t1,namen[i]);
  }
  show(t1);
  return 0;
}

tptr fill(tptr t, char *s){
  if(t == NULL){
  tptr new = malloc(sizeof(struct tree));
  new->left = NULL;
  new->right = NULL;
  strcpy(new->name,s);
  return new;
  }
  if(strcmp(s,t->name)==0){
    ;
  }else if(strlen(s) <= strlen(t->name)){
    t->left = fill(t->left,s);
  }else if(strlen(s) > strlen(t->name)){
    t->right = fill(t->right,s);
  }
  return t;
}

void show(tptr t){
  if(!t)
    return;
  show(t->left);
  printf("%s ",t->name);
  show(t->right);
  printf("\n");
}

void del(tptr t){
  if(t){
    del(t->left);
    del(t->right);
    free(t);
  }
}
