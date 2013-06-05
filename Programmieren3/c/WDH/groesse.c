#include <stdio.h>

char c;
short s;
int i;
unsigned int ui;
int *in;
char *ch;
long l;
float f;
double d;
long double ld;
int size = 0;
int a[2][6];

int main(){
  printf("char: %d\n",sizeof(c));
  printf("short: %d\n",sizeof(s));
  printf("int: %d\n",sizeof(i));
  printf("unsigned int: %d\n",sizeof(ui));
  printf("int *: %d\n",sizeof(in));
  printf("char *: %d\n",sizeof(ch));
  printf("long: %d\n",sizeof(l));
  printf("float: %d\n",sizeof(f));
  printf("double: %d\n",sizeof(d));
  printf("long double: %d\n",sizeof(ld));

  size += sizeof(c)+sizeof(s)+sizeof(i)+sizeof(ui)+sizeof(in)+sizeof(ch)+          sizeof(l)+sizeof(f)+sizeof(d)+sizeof(ld);

  printf("Gesamtgroeße: %d\n",size);
  printf("ArrayGroesse: %d\n",sizeof(a));
  

  return 0;
}
