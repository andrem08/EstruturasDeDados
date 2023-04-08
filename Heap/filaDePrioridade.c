/*********************************************************************/
/**   ACH2023 - Algoritmos e Estruturas de Dados I                  **/
/**   EACH-USP - Segundo Semestre de 2020                           **/
/**   <04> - Prof. Luciano Antonio Digiampietri                  **/
/**                                                                 **/
/**   EP3 - Fila de Prioridade (utilizando heap)                    **/
/**                                                                 **/
/**   <Andre Miyazawa>                   <11796187>          **/
/**                                                                 **/
/*********************************************************************/

#include "filaDePrioridade.h"

PFILA criarFila(int max){
    PFILA res = (PFILA) malloc(sizeof(FILADEPRIORIDADE));
    res->maxElementos = max;
    res->arranjo = (PONT*) malloc(sizeof(PONT)*max);
    res->heap = (PONT*) malloc(sizeof(PONT)*max);
    int i;
    for (i=0;i<max;i++) {
        res->arranjo[i] = NULL;
        res->heap[i] = NULL;
    }
    res->elementosNoHeap = 0;
    return res;
}

void exibirLog(PFILA f){
    printf("Log [elementos: %i]\n", f->elementosNoHeap);
    PONT atual;
    int i;
    for (i=0;i<f->elementosNoHeap;i++){
        atual = f->heap[i];
        //printf("[%i;%f;%i] ", atual->id, atual->prioridade, atual->posicao);
    }
    printf("\n\n");
}

int tamanho(PFILA f){
    //Caso especifico em que o tamanho é maximo.
    if(f->heap[f->maxElementos - 1] != NULL)
        return f->maxElementos;
    //Caso especifico em que o tamanho eh zero.
    if(f->heap[0] == NULL)
        return 0;
    //Realiza uma busca binaria ate encontrar um elemento nao nulo, e com o seu proximo elemento nulo.
    int base = 0;
    int topo = f->maxElementos - 1;
    int meio;
    while (base <= topo)
    {
        meio = (base + topo)/2;
        //Caso encontrou o elemento buscado.
        if(f->heap[meio] != NULL && f->heap[meio + 1] == NULL)
            return meio + 1;
        //Caso o elemento seja NULL.
        if(f->heap[meio] == NULL)
            topo = meio - 1;
        else
            base = meio + 1;
    }
    return -1;
}

//Funcao que reorganiza o heap de baixo para cima.
void heapMaxBaixo(PFILA f, int indice){
    indice++;
    //Se o elemeno nao pussuir um pai:
    if(indice <= 1)
        return;
    //Se a prioridade do elemento for maior do que a de seu pai, trocam de posicao.
    if(f->heap[indice - 1]->prioridade > f->heap[indice/2 - 1]->prioridade){
        int posicaoAux = f->heap[indice/2 - 1]->posicao;
        f->heap[indice/2 -1]->posicao = f->heap[indice - 1]->posicao;
        f->heap[indice - 1]->posicao = posicaoAux;

        PONT pontAux = f->heap[indice/2 - 1];
        f->heap[indice/2 - 1] = f->heap[indice - 1];
        f->heap[indice - 1] = pontAux;
        heapMaxBaixo(f, indice/2 - 1);
    }
}
//Funcao que reorganiza o heap de cima para baixo.
void heapMaxCima(PFILA f, int indice){
    indice++;
    if(indice*2 - 1 > f->maxElementos)
        return;
    if (f->heap[indice*2 - 1] == NULL)
        return;
    int maior = indice - 1;
    if(f->heap[indice - 1]->prioridade < f->heap[indice*2 - 1]->prioridade)
        maior = indice*2 - 1;
    if(indice*2 + 1 - 1 <= f->maxElementos){
        if(f->heap[indice*2 + 1 - 1] != NULL) {
            if (f->heap[maior]->prioridade < f->heap[indice * 2 + 1 - 1]->prioridade)
                maior = indice * 2 + 1 - 1;
        }
    }
    if (maior != indice - 1){
        int posicaoAux = f->heap[maior]->posicao;
        f->heap[maior]->posicao = f->heap[indice - 1]->posicao;
        f->heap[indice - 1]->posicao = posicaoAux;

        PONT pontAux = f->heap[maior];
        f->heap[maior] = f->heap[indice - 1];
        f->heap[indice - 1] = pontAux;
        heapMaxCima(f, maior);
    }
}
bool inserirElemento(PFILA f, int id, float prioridade){
    //Caso id seja menor do que zero, ou maior ou igual a maxElementos, retorna false
    if(id < 0 || id >= f->maxElementos)
        return false;
    //Caso ja exista um mesmo id, retorna false.
    if(f->arranjo[id] != NULL)
        return false;
    PONT novo = (PONT) malloc(sizeof (ELEMENTO));
    novo->id = id;
    novo->prioridade = prioridade;
    novo->posicao = f->elementosNoHeap;

    f->heap[f->elementosNoHeap] = novo;
    heapMaxBaixo(f, novo->posicao);
    f->elementosNoHeap++;

    f->arranjo[id] = novo;

    return true;
}

bool aumentarPrioridade(PFILA f, int id, float novaPrioridade){
    //Caso id seja menor do que zero, ou maior ou igual a maxElementos, retorna false
    if(id < 0 || id >= f->maxElementos)
        return false;
    //Caso nao exista o id, retorna false.
    if(f->arranjo[id] == NULL)
        return false;
    //Caso a sua prioridade já seja maior ou igual à nova prioridade.
    if(f->arranjo[id]->prioridade >= novaPrioridade)
        return false;
    //Atualiza a prioridade e modifica os ponteiros se necessario
    f->arranjo[id]->prioridade = novaPrioridade;
    heapMaxBaixo(f, f->arranjo[id]->posicao);
    return true;
}

bool reduzirPrioridade(PFILA f, int id, float novaPrioridade){
    //Caso id seja menor do que zero, ou maior ou igual a maxElementos, retorna false
    if(id < 0 || id >= f->maxElementos)
        return false;
    //Caso nao exista oid, retorna false.
    if(f->arranjo[id] == NULL)
        return false;
    //Caso a sua prioridade já seja menor ou igual à nova prioridade.
    if(f->arranjo[id]->prioridade <= novaPrioridade)
        return false;
    //Atualiza a prioridade e modifica os ponteiros se necessario
    f->arranjo[id]->prioridade = novaPrioridade;
    heapMaxCima(f, f->arranjo[id]->posicao);
    return true;
}

PONT removerElemento(PFILA f){
    //Se a fila estiver vazia
    if(f->elementosNoHeap <= 0)
        return NULL;
    PONT remover = f->heap[0];
    //Retira o elemento do arranjo.
    f->arranjo[remover->id] = NULL;
    //Coloca no lugar o ultimo elemento no array de heap e reorganiza.
    f->heap[0] = f->heap[f->elementosNoHeap - 1];
    f->heap[f->elementosNoHeap - 1] = NULL;
    if(f->heap[0] != NULL)
        f->heap[0]->posicao = 0;
    f->elementosNoHeap--;
    heapMaxCima(f, 0);


    return remover;
}

bool consultarPrioridade(PFILA f, int id, float* resposta){
    //Caso id seja menor do que zero, ou maior ou igual a maxElementos, retorna false
    if(id < 0 || id >= f->maxElementos)
        return false;
    //Caso nao exista o id, retorna false.
    if(f->arranjo[id] == NULL)
        return false;
    *resposta = f->arranjo[id]->prioridade;
    return true;
}


