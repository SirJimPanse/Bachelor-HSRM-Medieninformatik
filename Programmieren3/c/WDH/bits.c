#include <stdio.h>
#include <stdlib.h>

unsigned int toDez(unsigned int);
void toBinAndOut(unsigned int);
void printOut(unsigned int *, int);

int main(int argc, char *argv[]){
  if(argc == 4){
    unsigned int b1;
    unsigned int b2;
    unsigned int out;

    sscanf(argv[1],"%u",&b1);
    sscanf(argv[3],"%u",&b2);

    switch(*argv[2]){
    case 'o':
      out = toDez(b1) | toDez(b2);
      toBinAndOut(out);
      break;
    case 'a':
	 out = toDez(b1) & toDez(b2);
	 toBinAndOut(out);
      break;
    case 'x':
	out = toDez(b1) ^ toDez(b2);
	toBinAndOut(out);
      break;
    case 'l':
      	out = toDez(b1) << toDez(b2);
	toBinAndOut(out);
      break;
    case 'r':
      	out = toDez(b1) >> toDez(b2);
	toBinAndOut(out);
      break;
    }
  }else{
     printf("Nicht genügend Argumente!\n");
  }

  return 0;
}

unsigned int toDez(unsigned int bit){
  unsigned int dez = 0;
  int pot = 1;
  while(bit != 0){
    if(bit % 10 == 1){
       dez += 1 * pot;
    }
    pot *=2;
    bit /=10;
  }
  return dez;
}

void toBinAndOut(unsigned int out){
  unsigned int prin[32];
  unsigned int num = out;
  unsigned int *ptr = prin;
  unsigned int akt;
  int i = 0;
  if(num != 0){
    while(num >= 1 ){
      akt = num % 2;
      num = (num - akt) >> 1;
      prin[i] = akt;
      i += 1;
    }
  }else{
     printf("%d",0);
  }
  printOut(ptr,i);
}

void printOut(unsigned int *out,int i){
  int k;
  for(k = 0;k < i-1; k++){
    out += 1;
  }
  while(i-1 >= 0){
     putchar(*out + '0');
     out -= 1;
     i -= 1;
  }
  printf("\n");
}
