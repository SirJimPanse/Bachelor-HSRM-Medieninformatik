#include <stdio.h>
#include <string.h>
#include <strings.h>

int bin_to_int(int);
void out(int);

int main(int argc, char *argv[]){
  int op1;
  int op2;
  char *operant = argv[2];
  int ret;

  sscanf(argv[1],"%i",&op1);
  sscanf(argv[3],"%i",&op2);
  op1 = bin_to_int(op1);
  op2 = bin_to_int(op2);

  if(strcmp(operant,"and") == 0){
    ret = op1 & op2;
  }else if(strcmp(operant,"or") == 0){
    ret = op1 | op2;
  }else if(strcmp(operant,"xor") == 0){
    ret = op1 ^ op2;
  }else if(strcmp(operant,"lshift") == 0){
    ret = op1 << op2;
  }else if(strcmp(operant,"rshift") == 0){ 
    ret = op1 >> op2;
  } 
  out(ret);
  return 0;
}

int bin_to_int(int bin){
  int dec = 0;
  int i = 1;
  while(bin > 0){
    dec += (bin%2) * i;
    bin /= 10;
    i *= 2;
  }
  return dec;
}

void out(int dec){
  int akt;
  int temp[32];
  int i = 0;
  int count = 0;
  if(dec == 0){
    putchar(0 + '0');
  }else{
  while(dec >= 1){
    akt = dec % 2;
    temp[i] = akt;
    dec = (dec - akt) >> 1;
    i += 1;
    count += 1;
  }
  for(i = count-1; i>=0;i--)
    putchar(temp[i]+ '0');
  }
  printf("\n");
}
