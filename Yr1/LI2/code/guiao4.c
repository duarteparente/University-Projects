/**
 * @file guiao4.c 
 * \brief Funções de suporte ao guião 4.
 */ 
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "auxiliar.h"
#include "stack.h"
#include "guiao4.h"
#include <ctype.h>

/**
 * \brief Função que retorna o elemento em determinada posição.
 * 
 * @param s String em questão.
 * @param n Inteiro que dita quantos elementos se vai buscar.
 * @returns x caracteres do fim da string.
 */
char * go_get_maior(char *s, int x){
  return &s[strlen(s)-x]; 
}

/**
 * \brief Função que retorna o elemento em determinada posição.
 * 
 * @param s String em questão.
 * @param n Inteiro que dita quantos elementos se vai buscar.
 * @returns x caracteres do inicio da string.
 */
char * go_get_menor(char *s, int x){
  char *res = go_get_maior(reverse(s),(strlen(s)-(strlen(s) -x)));
  return reverse(res);
}

/**
 * \brief Função que realiza a operação de ir buscar x elems do fim da string.
 * 
 * A função vai buscar o inteiro de controlo e a string a ser percorrida, e
 * chama a função go_get_maior.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_nelems_maior(STACK *s){
  long x = pop_LONG(s);
  char *s1 = pop_STRING(s);
  push_STRING(s,go_get_maior(s1,x));
}

/**
 * \brief Função que realiza a operação de ir buscar x elems do inicio da string.
 * 
 * A função vai buscar o inteiro de controlo e a string a ser percorrida, e
 * chama a função go_get_menor.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_nelems_menor(STACK *s){
  long x = pop_LONG(s);
  char *s1 = pop_STRING(s);
  push_STRING(s,go_get_menor(s1,x));
}

/**
 * \brief Função que retorna o elemento/caracter em determinada posição.
 * 
 * @param s String em questão.
 * @param n Posição do elemento que se quer.
 * @returns o caracter que se encontra na posição c da string s.
 */
char go_get_elem(char *s, int c){
  return s[c];
}

/**
 * \brief Função que realiza a operação de ir buscar o elemento da string na posição x.
 * 
 * A função vai buscar o inteiro de controlo e a string a ser percorrida, e
 * chama a função go_get_elem.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_elem_igual(STACK *s){
  long x = pop_LONG(s);
  char *s1 = pop_STRING(s);
  push_CHAR(s,go_get_elem(s1,x));
}

/**
 * \brief Função que inverte uma string.
 * 
 * @param s String que irá ser invertida.
 * @returns A string invertida.
 */
char * reverse( char * s ) {
    int length = strlen(s) ;
    int c, i, j;
    for (i = 0, j = length - 1; i < j; i++, j--){
        c = s[i];
        s[i] = s[j];
        s[j] = c;
    }
    return s;
}

/**
 * \brief Função que testa se um token será uma string.
 * 
 * @param token Token a ser analisado.
 * @param s1 Char que será '"'. 
 * 
 * @returns 0 se falso, !0 se verdadeiro.
 */
int is_string(char *token, char s1){
    return((token[0] == s1));
}

/**
 * \brief Função que realiza a operação de range.
 * 
 * Se atua sobre uma string o range será o tamanho da string.
 * Caso atue sobre um inteiro, irá formar um array com o tamanho do inteiro. 
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_range(STACK *s){
    if (has_type(top(s),STRING)) push_LONG(s,strlen(pop_STRING(s)));
    else {
      long x = pop_LONG(s);
      for(int i=0;i<x;i++){
          push_LONG(s,i);
      }
    }  
}

/**
 * \brief Função que procura um char numa string.
 * 
 * @param principal String onde se irá procurar.
 * @param wanted Char que será procurado.
 * 
 * @returns O indice do char ou -1 se este não existir.
 */
int procura_char (char principal[], char wanted) {
    int res = 0;
    while(*principal) {
        if(*principal != wanted) {principal++;res++;}
        else break;
    }
    if (*principal) return res;
    else return -1;   
}

/**
 * \brief Função que procura uma string numa string.
 * 
 * @param principal String onde se irá procurar.
 * @param wanted String que será procurada.
 * 
 * @returns O indice da string ou -1 se esta não existir.
 */
int procura (char principal[], char wanted[]) {
    int isContained = 1;
    int res = 0;
    char* keeper = wanted;
    while(*wanted && *principal) {
        if(*principal != *wanted) {
            isContained = 0;
            wanted = keeper; res++;
        }
        else {
            if(!isContained) {
                isContained = 1;
            }
            wanted++;
        }
        principal++;
    }
    if (isContained && !(*wanted)) return res;
    else return -1;
}

/**
 * \brief Função que testa se uma string é lexicalmente maior que outra.
 * 
 * @param s1 String base.
 * @param s2 String que irá comparar. 
 * 
 * @returns 0 se falso, 1 se verdadeiro.
 */
int compare_lexical_maior(char *s1, char *s2){
  int r = strcmp(s1,s2);
  if (r<=0) return 0;
  else return 1; 
}

/**
 * \brief Função que testa se uma string é lexicalmente menor que outra.
 * 
 * @param s1 String base.
 * @param s2 String que irá comparar. 
 * 
 * @returns 0 se falso, 1 se verdadeiro.
 */
int compare_lexical_menor(char *s1, char *s2){
  int r = strcmp(s1,s2);
  if (r>=0) return 0;
  else return 1;
}  

/**
 * \brief Função que testa se uma string é lexicalmente igual a outra.
 * 
 * @param s1 String base.
 * @param s2 String que irá comparar. 
 * 
 * @returns 0 se falso, 1 se verdadeiro.
 */
int compare_lexical_igual(char *s1, char *s2){
  int r = strcmp(s1,s2);
  if (r!=0) return 0;
  else return 1;
}   

/**
 * \brief Função que irá retornar a "cauda" de uma string.
 * 
 * @param s String à qual irá ser extraída a cabeça. 
 * 
 * @returns A cauda da string.
 */
char* tail(char* s) {
  return &s[1];  
}

/**
 * \brief Função que irá retornar a "cabeça" de uma string.
 * 
 * @param s String à qual irá ser extraída a cauda. 
 * 
 * @returns A cabeça da string.
 */
char* head(char* s){
  char *p = tail(reverse (s));
  return reverse(p);
}

/**
 * \brief Função que concatena um char a uma string (pela esquerda).
 * 
 * @param s SEstrutura stack onde são guardados os elementos. 
 * 
 */
void choose_concatena_cstring(STACK *s){
  char *s1 = reverse(pop_STRING(s));
  char s2 = pop_CHAR(s);
  push_STRING(s,reverse(strncat(s1,&s2,1))); 
}

/**
 * \brief Função que extrai a string do token com aspas.
 *
 * A função recebe o token com as aspas e extrai a string nele contida.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * @param token Token a ser analisado.
 * 
 */
void get_string (STACK *s, char *token) {
    if (conta_aspas(token)%2 == 0) push_STRING(s,head(tail(token)));
    else 
    {
      push_STRING(s,head((token)));
      choose_concatena_cstring(s);
    }     
}

/**
 * \brief Função que irá percorrer o token e contar o numero de aspas.
 * 
 * @param s String(token) que irá ser percorrida. 
 * 
 * @returns o numero de aspas.
 */
int conta_aspas(char *s){
  int contador = 0;
  char *p = s;
  while (*p){
    if (*p == '\"') contador ++;
    p++;
  }
  return contador;
}

/**
 * \brief Função que irá inferir o começo e fim do array.
 * 
 * @param s String(token) que irá ser percorrida. 
 * 
 * @returns !0 se o array fechar, 1 se não.
 */
int conta_parentesis(char *s){
  int contador = 0;
  char *p = s;
  while (*p){
    if (*p == '[') contador ++;
    else if (*p == ']') contador--;
    p++;
  }
  return (!contador);
}

/**
 * \brief Função que concatena duas strings.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_concatena_string(STACK *s){
  char *s1 = pop_STRING(s);
  char *s2 = pop_STRING(s);
  push_STRING(s,strcat(s2,s1));
}

/**
 * \brief Função que concatena um char a uma string (pela direita).
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_concatena_stringc(STACK *s){
  char s1 = pop_CHAR(s);
  char * s2 = pop_STRING(s);
  push_STRING(s,strncat(s2,&s1,1)); 
}

/**
 * \brief Função que remove o primeiro elemento de uma string e o coloca na stack.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_remove_first(STACK *s){
  char * s1 = pop_STRING(s);
  push_STRING(s,tail(s1));
  push_CHAR(s,*s1);
}

/**
 * \brief Função que remove o ultimo elemento de uma string e o coloca na stack.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_remove_last(STACK *s){
  char * s1 = pop_STRING(s);
  char * keeper = reverse(s1);
  push_STRING(s,head(reverse(s1)));
  push_CHAR(s,*keeper);
}

/**
 * \brief Função que testa se um token é composto por um único char.
 * 
 * @param token String(token) que irá ser testada. 
 * 
 * @returns !0 se for falso, 1 se verdadeiro.
 */
int itoc (char *token){
    return (strlen(token)==1);
}

/**
 * \brief Função que testa se o token é uma string de um double.
 * 
 * @param token String(token) que irá ser testada. 
 * 
 * @returns !0 se for falso, 1 se verdadeiro.
 */
int is_double(char *token){
   return  (isdigit(token[0]) || isdigit(token[1]));
}

/**
 * \brief Função que testa se o token é do tipo <Letra_Maiuscula> (representação das variáveis).
 * 
 * @param token String(token) que irá ser testada. 
 * 
 * @returns !0 se for falso, 1 se verdadeiro.
 */
int is_var(char *token){
    return (isupper(*token)); 
}


/**
 * \brief Função que repete n vezes uma determinada string
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_repete(STACK *s){
  long x = pop_LONG(s);
  char *s1 = pop_STRING(s);
  for(int i=0; i<x; i++){
    push_STRING(s,s1);
  }
}

/**
 * \brief Função que testa se o o token é um operador aritmético.
 * 
 * @param token String(token) que irá ser testada. 
 * 
 * @returns !0 se for falso, 1 se verdadeiro.
 */
int is_arithmetic(char *token){
    return (   strcmp(token,"+")==0 || strcmp(token,"*") == 0 || strcmp(token,"/") == 0 || strcmp(token,"(") == 0
            || strcmp(token,")")==0 || strcmp(token,"&") == 0 || strcmp(token,"|") == 0 || strcmp(token,"%") == 0
            || strcmp(token,"^")==0 || strcmp(token,"~") == 0 || strcmp(token,"#") == 0 || strcmp(token,"-") == 0 );
}

/**
 * \brief Função auxiliar que infere a operação a realizar.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * @param token Token que será analisado.
 * 
 */
void choose_arithmetic(STACK *s, char *token){
  if(strcmp (token, "-") == 0) {
    choose_subtrai (s);
  }
  else switch (*token){
                case '+':
                  choose_soma_ou_concat (s); break;
                case '*':
                  choose_multiplica_ou_repete (s); break;
                case '/':
                  choose_divide(s); break;
                case '(':
                  choose_decrementa_ou_removefirst (s); break;
                case ')':
                  choose_incrementa_ou_removelast (s); break;
                case '&':
                  choose_E(s); break;
                case '|':
                  choose_ou(s); break;
                case '%':
                  choose_modulo(s); break;
                case '^':
                  choose_xor(s); break;
                case '~':
                  choose_not(s); break;
                case '#':
                  choose_potencia(s); break;
  }
}  

/**
 * \brief Função que testa se o o token é um operador de modificação da stack.
 * 
 * @param token String(token) que irá ser testada. 
 * 
 * @returns !0 se for falso, 1 se verdadeiro.
 */
int is_opstack(char *token){
    return (   strcmp(token,"_")==0 || strcmp(token,";") == 0 || strcmp(token,"\\") == 0 || strcmp(token,"@") == 0
            || strcmp(token,"$")==0 || strcmp(token,"c") == 0 || strcmp(token,"i") == 0 || strcmp(token,"f") == 0
            || strcmp(token,"l")==0) ;
}

/**
 * \brief Função auxiliar que infere se a operação a realizar.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * @param token Token que será analisado.
 * 
 */
void choose_opstack(STACK *s, char *token){
  switch (*token){
                case '_':
                  choose_duplica(s); break;
                case ';':
                  choose_pop(s); break;
                case '\\':
                  choose_troca(s); break;
                case '@':
                  choose_roda(s); break;
                case '$':
                  choose_copia(s); break;
                case 'c':
                  choose_converteC(s); break;
                case 'i':
                  choose_converteI(s); break;
                case 'f':
                  choose_converteF(s); break;
                case 'l':
                  choose_L(s); break; 
  }
}                  

/**
 * \brief Função auxiliar que infere a operação em questão quando o token só tem um char.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * @param token Token que será analisado.
 * 
 */
void choose_ooc(STACK *s, STACK *var, char*token){
    if (is_var(token)){
        choose_letter(s,var,25-(*token-'A'));
    }  
    else if(is_arithmetic(token)){
      choose_arithmetic(s,token);
    }
    else if(is_opstack(token)){
      choose_opstack(s,token);
    }     
    else switch (*token){
                case '?': if_then_else(s); break;
                case '=': choose_igual_ou_elemigual (s); break;
                case '<': choose_menor_ou_nelemsmenor (s); break;
                case '>': choose_maior_ou_nelemsmaior (s); break;
                case '!': choose_nao(s); break;
                case ',': choose_range(s); break;
                case '"': push_CHAR(s,' '); break;
                case '[':
                case ']':
                  break;      
                default: push_STRING(s,token); break;
        }                
}


