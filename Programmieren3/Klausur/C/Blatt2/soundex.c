#include<stdio.h>

int main(int argc, char* argv[]){
  char letters[] = {"01230120022455012623010202"};

  if(argc > 1){
    int i;
    for(i = 1; i < argc; i++){
      char soundex[6] = {"000000"};
      int j;
      int index = 0;
      char* p = soundex;
      int counter = 0;
      *p++ = argv[i][0];
      for(j = 1; argv[i][j]!='\0'; j++){
	if(counter < sizeof(soundex-1)){
	 if( argv[i][j] >= 'A' &&  argv[i][j] <= 'Z'){
	   index = argv[i][j] - 65;
	 }else if( argv[i][j] >= 'a' && argv[i][j] <= 'z'){
	   index =  argv[i][j] - 97;
	 }
	 if((letters[index] != '0') && (letters[index] != soundex[counter])){ 
	   *p++ = letters[index];
	   counter += 1;
	 }
	}
      }
      printf("%i %s : %s\n",i, argv[i], soundex);
    }
    
  }
  return 0;
}
