#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int normalize(char *s){
  char *r = s;
  char *w = s;
  int count = 0;
  while(*r){
    if(*r != ' ' && *r != '\t'){
      *w = *r;
    }else{
      while(*(r+1) == ' ' || *(r+1) == '\t')
	  r++;
      *w = ' ';
      count += 1;
    }
    w++;
    r++;
  }
  *w = '\0';
  return count;
  }

void norms(char *s){
  char *r = s;
  char *w = s;
  while(*r++)
    if(*w <= *r) *++w = *r;
  *++w = '\0';
}

typedef struct _list{
   struct _list *next;
   int data;
} list;

typedef list *slist;

slist append(slist t, int elem){
   slist new = malloc(sizeof(slist));
   new->data = elem;
   new->next = NULL;
   if(!t) 
      return new;
   slist ret = t;
   while(t->next)
      t = t->next;
   t->next = new;
   return ret;
}

void mapf(slist t, int(*map)(int i) ){
	printf("list: [");
	if (t == NULL){
	   printf(" ]\n");
	   return;
	}
	while(t->next){
	   printf("%d, ", map(t->data));
	   t = t->next;
	}
	printf("%d]\n", map(t->data));
}

int quad(int i){
   return i*i;
}

void optimiere(char *s){
  char *w = s;
  while(*w){
    if(*w == 'a' || *w == 'e' || *w == 'o' || *w == 'u'){
      *w = 'i';
    }
    w += 1;
  }
  *++w = '\0';
}

int main(void){
  /*
  char s[100] = "Hallo";
  char *p = s;
  char t[] = " und so.";
  char *q = t;

  while(*p)
    p++;
  while(*q)
    *p++ = *q++;
  p = s;
  printf("strcat: %s\n",p);

  int i;
  for(i = 0; *p++; i++)
    ;
  printf("strlen: %i\n",i);
 
  char s[] = "abcde";
  normalize(s);
  printf("%s\n",s);

  int a = 14;
  int b = 2;
  printf("%i %i %i %i %i %i",a&b, a|b, a^b, ~(~a&~b), a >> 2, b<<1);

   int(*map)(int i) = 0;
   slist t = NULL;
   map = &quad;
   t = append(t, 2);
   t = append(t, 9);
   t = append(t, 10);
   mapf(t, map);

 char s[]= "ACAabcxabz";
  norms(s);
  printf("%s\n",s);
  char s[] = "abcdegkiasosdu";
  char *p = s;
  optimiere(s);
  printf("%s\n",s);
  printf("%i \n",strlen(p));

  int a = 2;
  int b = 3;
  printf("%i %i %i %i %i %i\n",a&b, a|b, a^b, ~(~a&~b), a >> 2, a<<1);
  */
  
  char s[] = "ab c  d\te";
  int i = normalize(s);
  printf("%s %i\n",s, i);

 return 0;
}
