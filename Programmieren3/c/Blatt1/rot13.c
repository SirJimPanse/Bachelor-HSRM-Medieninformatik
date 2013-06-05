#include <stdio.h>

int main(void){	
	int i;
	
      /*char list[1000];
	int groesse;

	gets(list);
        groesse = sizeof(list);
	for(i = 0; i <= groesse; i++){
	  if (list[i] >='A' && list[i] <='Z'){
	    list[i] = (((list[i] - 65) + 13) % 26) + 65;
	    
	  }else if(list[i] >= 'a' && list[i] <= 'z'){
	    list[i] = (((list[i] - 97) + 13) % 26) + 97;
	  }

	}
        printf("% i\n %s\n",groesse, list);*/

	//Warum funktioniert es nicht bei erster Zeile!?
	while((i = getchar()) != EOF ){
	  if (i >='A' && i <='Z'){
	    i = (((i - 65) + 13) % 26) + 65;
	    putchar(i);
	    
	  }else if(i >= 'a' && i <= 'z'){
	    i = (((i - 97) + 13) % 26) + 97;
	    putchar(i);
	  }else {
	    putchar(i);
	  }
	}

 return 0;
}
