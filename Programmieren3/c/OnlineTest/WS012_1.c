#include <stdio.h>

int check_aeo(char);
int check_u(char);
int check_smile(char *);
char *del(char *);

int main(){
  char string[] = {"as;-(|gasd::-)uoaiaoksade:u-(beaku:-|"};

  del(string);
  printf("%s\n",string);
  return 0;
}

char *del(char *s){
  char *out = s;
  char *w = s;
  char *r = s;

  while(*r){
    if(check_aeo(*r)){
      *w++ = 'i';
      r += 1;
    }else if(check_u(*r)){
      r += 1;
    }else{
      *w++ = *r++;
    }
  }
  *w = '\0';
  char *w2 = s;
  char *r2 = s;
  while(*r2){
    if(check_smile(r2)){
      r2 += 3;
      w2 += 2;
      *w2++ = ')';
    }else{
      *w2++ = *r2++;
    }
  }
  *w2 = '\0';
  return out;
}


int check_aeo(char c){
  if((c == 'a') | (c == 'e') | (c == 'o'))
    return 1;
  return 0;
}

int check_u(char c){
  if(c == 'u')
    return 1;
  return 0;
}

int check_smile(char *s){
  char *r = s;
  if((*r == ':') | (*r == ';')){
    r +=1;
    if(*r == '-'){
      r += 1;
      if((*r == '|') | (*r == '(')){
	return 1;
      }
    }
  }
  return 0;
}
