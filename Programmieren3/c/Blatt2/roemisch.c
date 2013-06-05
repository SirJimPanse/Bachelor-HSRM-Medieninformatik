#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

char num[21];
int dez = 0;
char *roems[] = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
int vals[] = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4,1};

int getenumvalue(char z){
  switch(z){
  case 'I':
    return 1;break;
  case 'V':
    return 5;break;
  case 'X':
    return 10;break;
  case 'L':
    return 50;break;
  case 'C':
    return 100;break;
  case 'D':
    return 500;break;
  case 'M':
    return 1000;break;
  }
  return 0;
}

int main(int argc, char *argv[]){
  if(argc > 1){

    if(isdigit(*argv[1])){
      sscanf(argv[1],"%d",&dez);
      int i;
      for(i = 0; dez != 0 ;i++){
		while(dez >= vals[i]){
	  		strcat(num,roems[i]);
	  		dez -= vals[i];
		}
      } 
      printf("Roemische Zahl: %s\n",num);

     }else{
      sscanf(argv[1],"%s",num);
      char *ptr = num;
      while(*(ptr+1) != '\0'){
		ptr+=1;
      }
      dez = getenumvalue(*ptr);
      while(ptr != num){
		int akt = getenumvalue(*ptr);
		int next = getenumvalue(*(ptr-1));
		if(akt <= next){
	 		 dez += next;
		}else if(akt > next && (akt/next == 5) | (akt/next == 10)){
	  		dez -= next;
		}else{
	  		printf("Ungültige Zahl!\n");
	  		abort();
		}
		ptr-=1;
      }
      printf("Dezimalzahl: %d\n",dez);
     }
  } 
  return 0;
}
