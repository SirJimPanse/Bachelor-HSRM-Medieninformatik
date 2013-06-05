#include <stdio.h>
#include <stdlib.h>

/* Copy_Custom Methode muss noch implementiert werden*/

typedef struct _list{
  struct _list *next;
  struct _list *prev;
  void *content;
}list;

typedef list *lptr;

lptr add(lptr, void *);
lptr add_last(lptr, void *);
lptr add_ith(lptr, void *, int);
void show_list(lptr);
lptr del(lptr);
lptr del_ith(lptr, int);
lptr del_last(lptr);
void del_all(void *);
void del_all_custom(lptr, void (*custom)(void *));
lptr copy(void *);
/*
lptr copy_custom(lptr, void *(*copy_custom)(void *));
*/
void show(void *);

typedef void(*custom)(void *);
custom fkt;
/*
typedef void *(*copy_custom)(void *);
copy_custom fkt2;
*/
int main(){
  lptr akt = NULL;
  char c[] = {'a','b','c','f','g'}; 
  fkt = del_all;
  /*fkt2 = copy;*/
 
  /*Liste füllen*/
  int i;
  for(i = 0; i < 5; i++){
    char *p = malloc(sizeof(char));
    *p = c[i];
    akt = add(akt, (void *)p);
  }

  show_list(akt);
  char *pt = malloc(sizeof(char));
  *pt = 'd';
  akt = add_last(akt,(void *)pt);
  show_list(akt);
  char *ptr = malloc(sizeof(char));
  *ptr = 'e';
  akt = add_ith(akt,(void *)ptr,3);
  show_list(akt);
  akt = del(akt);
  show_list(akt);
  akt = del_ith(akt,3);
  show_list(akt);
  akt = del_last(akt);
  show_list(akt);
  lptr copy_akt = NULL;
  copy_akt = copy(akt);
  printf("Kopie von Liste\n");
  show_list(copy_akt);
  del_all_custom(copy_akt,fkt);
  return 0;
}

/*Fügt Element an Listenende an*/
lptr add_last(lptr llist, void *cont){
  lptr new = malloc(sizeof(list));
  new->content = cont;
  lptr first = llist;
  if(llist == NULL){
    new->next = NULL;
    new->prev = NULL;
    return new;
  }else{
    while(llist->next)
      llist = llist->next;
    llist->next = new;
    new->next = NULL;
    new->prev = llist;
  }
  return first;
}

/*Fügt Element an Listenanfang an*/
lptr add(lptr llist, void *cont){
  lptr new = malloc(sizeof(list));
  new->content = cont;
  if(!llist){
    new->next = NULL;
    new->prev = NULL;
    return new;
  }
  llist->prev = new;
  new->next = llist;
  new->prev = NULL;
  return new;
}

/*Fügt Element an i-te Stelle an*/
lptr add_ith(lptr llist, void *cont, int pos){
  lptr new = malloc(sizeof(list));
  new->content = cont;
  lptr first = llist;
  /*Laufe bist zur i-ten Stelle und füge ein*/
  while(--pos)
    llist = llist->next;  
    new->next = llist;
    new->prev = llist->prev;
    llist->prev->next = new;
    llist->prev = new;
    /*Wenn i = 1. Stelle, rufe add Methode auf*/
    if(!llist->prev){
    first = add(first, cont);
    /*Wenn i ausserhalb der Liste*/
  }else if(!llist){
    printf("Position liegt ausserhalb der Liste!");
  }
  return first;
}

/*Löscht erstes Element der Liste*/
lptr del(lptr llist){
  lptr first = llist->next;
  first->prev = NULL;
  free(llist);
  return first;
}

/*Löscht i-tes Element*/
lptr del_ith(lptr llist, int pos){
  lptr first = llist;
  if(pos == 1){
    first = llist->next;
    del(llist);
  }else{
  while(--pos)
  llist = llist->next;
  if(!llist->next){
    del_last(llist);
  }else if(llist->next && llist->prev){
  llist->next->prev = llist->prev;
  llist->prev->next = llist->next;
  free(llist);
   }else{
    printf("Position liegt ausserhalb der Liste!\n");
   }
  }
  return first;
}

/*Löscht letztes Element*/
lptr del_last(lptr llist){
  lptr first = llist;
  while(llist->next)
    llist = llist->next;
  llist->prev->next = NULL;
  free(llist);
  return first;
}

/*Löscht alle Elemente der Liste*/
void del_all(void *llist){
  lptr next;
  while(llist){
    next = ((lptr)llist)->prev;
    free(llist);
    llist = next;
  }
  printf("\n");
}

/*Custom Methode um beliebigen Inhalt einer Liste zu löschen*/
void del_all_custom(lptr llist, void (*custom)(void *)){
    custom(llist); 
    printf("Liste gelöscht\n");
}

/*Erstellt tiefe Kopie der Liste*/
lptr copy(void *llist){
  lptr newl = NULL;
  while(llist){
    newl = add_last(newl,((lptr)llist)->content);
    llist = (((lptr)llist)->next);
  }
  return newl;
}
/*
lptr copy_custom(lptr llist, (void *)(*custom)(void)){
  custom(llist);
  printf("Liste kopiert\n");
}
*/
/*Gibt gesamte Liste aus*/
void show_list(lptr llist){
  lptr ptr = llist;
  if(!ptr)
    printf("Liste leer!\n");
  for(;ptr; ptr = ptr->next)
    printf("%c ",*((char *)ptr->content));
  printf("\n");
}

/*Kann einzelnes Element ausgeben*/
void show(void *llist){
  printf("Content: %c\n",*((char *)((lptr) llist)->content));
}


