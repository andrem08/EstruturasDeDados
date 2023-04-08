#include<stdio.h>
#include <time.h>
#include <stdlib.h>
void insertionsort(int *Array, int tam){
    int contagem = 1;
    int aux;
    while(contagem < tam) {
        for(int i = contagem - 1; i >= 0; i--) {
            if (Array[i] > Array[i + 1]) {
                aux = Array[i];
                Array[i] = Array[i + 1];
                Array[i + 1] = aux;
            }
            else break;
        }
        contagem++;
    }
}


void quicksort(int *Array, int tam){
    if(tam <= 100) {
        insertionsort(Array, tam);
        return;
    }
    //srand(time(NULL));
    int k = rand()%(tam + 1);
    int pivo = Array[k];

    int menor[tam];
    int menorCont = 0;
    int maior[tam];
    int maiorCont = 0;

    for(int i = 0; i < tam; i++){
        if(Array[i] < pivo){

            menor[menorCont] = Array[i];
            menorCont++;
        }

        else {

            maior[maiorCont] = Array[i];
            maiorCont++;
        }
    }
    quicksort(maior, maiorCont);
    quicksort(menor,menorCont);

    for(int i = 0;i < menorCont ; i++){
        Array[i] = menor[i];
    }
    for(int i = 0;i < maiorCont; i++){
        Array[i + menorCont] = maior[i];
    }

}

int main(){
    int tamanho = 10000;
    int ArrayExemplo [tamanho];

    srand(time(NULL));

    int i;
    for(i = 0; i < tamanho ; i++)
        ArrayExemplo [i] = rand();

    quicksort(ArrayExemplo, tamanho);

    for(i = 0; i < tamanho ; i++)
        printf("O novo numero na casa %i eh o: %i\n",i + 1, ArrayExemplo[i]);

}