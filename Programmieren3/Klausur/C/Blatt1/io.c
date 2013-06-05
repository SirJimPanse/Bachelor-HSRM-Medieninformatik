#include<stdio.h>
void print_decimal(int);
void print_binary(int);

int main(void){
  int num;
  scanf("%i",&num); 

  print_binary(num);
  print_decimal(num);
  printf("\n");
  
  return 0;
}

void print_decimal(int num){
  int akt =  num/10;
  if(akt > 0){
    print_decimal(akt);
  }
  putchar((num%10) + '0');
}

void print_binary(int num){
  int numlist[32];
  int akt;
  int i = 0;
  int count = 0;

  while(num >= 1){
    akt = num % 2;
    num = (num - akt) >> 1;
    numlist[i] = akt;
    i += 1;
    count += 1;
  }

  int j;
  for(j = count-1; j >= 0 ; j--)
    printf("%i",numlist[j]);
  printf("\n");
}

