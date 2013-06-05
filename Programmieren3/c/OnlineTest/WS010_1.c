#include <stdio.h>

double sqrt(double, int);
double sqrtn(int, int, double);
double betrag(double);

int main(){
 
  printf("%lg\n",sqrtn(16,4,0.0));
  return 0;
}

double sqrtn(int x, int n, double epsilon){
  double ai;
  double an = (double)x;

  do{
    ai = an;
    an = ai - ((sqrt(ai, n) - x)/(n * (sqrt(ai, n-1))));
  }while(betrag((an) - ai) > epsilon);


  return an;
}

double betrag(double wert){
  if(wert < 0)
    wert *= -1;
  return wert;
}

double sqrt(double wert, int potenz){
  double pot = wert;
  while(--potenz > 0)
    wert *=pot;
  return wert;
}
