#include <stdio.h>
void fkt(char *, char *);
int main(void){
  int a = 10;
  int b = 6;
  printf("%i %i\n",a,b);
  printf("%i\n",a|b);
  printf("%i \n",a^b);
  printf("%i \n",a >> 2);

  char s1[] = {"abcd"};
  char s2[] = {"aabbacddded"};
  fkt(s1,s2);
  return 0;
}

void fkt(char *s1, char *s2){
  int count;
  char *p;
  while(*s1){
    count = 0;
    p = s2;
    while(*p){
      if(*p == *s1)
	count += 1;
      p++;
    }
    printf("%c %i\n",*s1,count);
    s1++;
  }
}

tptr fill(tptr t, int value){
  if(!t){
    tptr new = malloc(sizeof(struct tree));
    new->l = NULL;
    new->r = NULL;
    new->data = value;
    return new;
  }
  if(t->data == value){
    ;
  }else if(t->data > value){
    t->l = fill(t-l,value);
  }else if(t->data < value){
    t-r = fill(t->r,value);
  }
}

void del(tptr t){
  if(t){
    del(t->l);
    del(t->r);
    free(t)
  }
}
