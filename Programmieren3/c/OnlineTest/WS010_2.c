#include <stdio.h>

int str_del_lowervowels(char *);
int str_del_uppervowels(char *);
int str_del_vowels(char *);
int check_low(char);
int check_up(char);

int main(){
  char string[] = {"chAekklEiu"};
  printf("%d\n",str_del_lowervowels(string));
  char string2[] = {"chAekklEiu"};
  printf("%d\n",str_del_uppervowels(string2));
  char string3[] = {"chAekklEiu"};
  printf("%d\n",str_del_vowels(string3));
  return 0;
}

int str_del_lowervowels(char *string){
  int anzahl = 0;
  char *out = string;
  char *r = string, *w = string;

  while(*r){
    if(check_low(*r)){
      anzahl += 1;
      r += 1;
    }else{
      *w++ = *r++;
    }
  }
  *w = '\0';
  printf("%s\n",out);
  return anzahl;
}

int str_del_uppervowels(char *string){
  int anzahl = 0;
  char *out = string;
    char *r = string, *w = string;

  while(*r){
    if(check_up(*w)){
      anzahl += 1;
      r += 1;
      while(check_up(*r))
	r += 1;
      *w++ = *r++;
    }else{
      w++;
      r++;
    }
  }
  *w = '\0';
  printf("%s\n",out);
  return anzahl;
}

int str_del_vowels(char *string){
  int anzahl = 0;
  char *out = string;
    char *r = string, *w = string;

  while(*r){
    if((check_up(*r)) || (check_low(*r))){
      anzahl += 1;
      r += 1;
    }else{
      *w++ = *r++;
    }
  }
  *w = '\0';
  printf("%s\n",out);
  return anzahl;
}


int check_low(char c){
  if((c == 'a') || (c == 'e') || (c == 'i') || (c == 'o') || (c == 'u'))
    return 1;
  return 0;
}

int check_up(char c){
  if((c == 'A') || (c == 'E') || (c == 'I') || (c == 'O') || (c == 'U'))
    return 1;
  return 0;
}

