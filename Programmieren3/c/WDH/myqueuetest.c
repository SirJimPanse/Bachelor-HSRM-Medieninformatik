#include <stdio.h>
#include <stdlib.h>
#include "myqueue.h"

int main(){
  lqueue list = NULL;
  if(is_empty(list)){
    printf("Liste leer\n");
  }else{
    printf("Liste befüllt\n");
  }
  printf("Liste wird gefüllt\n");
  int i;
  for(i = 0; i <= 10; i++){
    list = enter(list,i);
  }

  if(is_empty(list)){
    printf("Liste leer\n");
  }else{
    printf("Liste befüllt\n");
  }
  printf("Front: %d\n",front(list));
  printf("lösche erstes Element\n");
  list = leave(list);
  printf("Front: %d\n",front(list));
  printf("lösche nächstes Element\n");
  list = leave(list);

  printf("Ganze Liste: ");
  for(;list;list = list->next)
    printf("%d ",list->value);
 
  return 0;
}
