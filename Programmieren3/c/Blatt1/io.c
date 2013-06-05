#include <stdio.h>

int main(void){
  int num;
  int numlist[32];
  int akt;
  int i = 0;  
  scanf("%i", &num); //unsigned int verwenden
  int zahl = num;
  int count = 0;
    
    //ganze positive Zahl -> Binärzahl
    while(num >= 1){ 
	  akt = num % 2;
	  num = (num - akt) >> 1;
	  numlist[i] = akt;
	  i+=1;	 
	  count +=1;
    }
    //Ausgabe Binär
      printf("\n");
      for (i = count-1; i >= 0; i--){
	  putchar((numlist[i]) + '0');
      }
 
    printf("\n\n");

     void ausdez(int z){
      int akt = z/10;
      if(akt > 0)
		ausdez(akt);//Da Rekursion -> merkt er sich Rücksprungaddressen
                       //(Addresse von 12 u 123 und gibt diese ebenfalls %10 aus  
      	putchar((z%10)+'0');   
    }

    //Ausgabe Dezimal
    ausdez(zahl);

    printf("\n");

    /* Binär -> Dezimal
     for(i = 0; i <= count-1; i++){
       if(i == 0){
	 aktdez = numlist[i] * (1 * pot);
       }else{ 
	 aktdez = aktdez + (numlist[i] * (1 * pot));
       }
       pot = pot * 2 ;
     }
    */
   
  return 0;
}
