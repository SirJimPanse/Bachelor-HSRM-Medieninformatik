#include <stdio.h>
#include <ctype.h>

int main(int argc, char *argv[]){
  char soundex[6];
  char *ptr = soundex;
  char word[99];
  char letters[26] = {"01230120022455012624010202"}; 
   
  int argcounter = 0;
  if(argc > 1){
    int k;
    for(k = 1; k < argc;k++){
      int i;
      for(i = 0; i < sizeof(soundex) ; i++){
	soundex[i] = '0';
      }
      sscanf(argv[k],"%s",word);
      *ptr = word[0];
      ptr+=1;

      int j;
      int z = 1;
      for(j = 1; word[j]!='\0'; j++){
	if(z < sizeof(soundex)){
	  if(word[j] <= 'Z'){
	    if((letters[word[j]-65]!='0') && (letters[word[j]-65]!= soundex[z-1])){
	      *ptr = letters[word[j] - 65];
	      ptr+=1;
	      z+=1;
	    }
	  }else{
	    if((letters[word[j]-97]!='0') && (letters[word[j]-97]!= soundex[z-1])){
	      *ptr = letters[word[j] - 97];
	      ptr+=1;
	      z+=1;
	    }else{
	    }
	  }
	}
      }
      ptr = soundex;
      printf("%i %s: %s\n",argcounter+=1,word,  soundex);
    }
    
  }else{
    printf("Kein Wort eingegeben!");
  }

  return 0;
}
