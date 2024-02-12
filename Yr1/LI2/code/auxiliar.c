/**
 * @file auxiliar.c 
 * \brief Funções auxiliares do programa.
 */ 
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <ctype.h>
#include "auxiliar.h"
#include "stack.h"
#include "guiao4.h"

/**
 * \brief Função que retorna o elemento em determinada posição.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * @param n Posição, na stack, do elemento.
 * @returns Elemento na posição n da stack.
 */
DATA get_elem(STACK *s,int n){
  return s->stack[s->n_elems - n-1];
}

/**
 * \brief Função que realiza a operação soma.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack o elemento resultante.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_soma (STACK *s){
  switch(checks_type(s)){
    case 0:
      push_LONG(s,pop_LONG(s)+pop_LONG(s)); break;
    case 1:
      push_DOUBLE(s,pop_DOUBLE(s)+pop_DOUBLE(s)); break;
    case 2:
      push_DOUBLE(s,pop_LONG(s)+pop_DOUBLE(s)); break;
    case 3:
      push_DOUBLE(s,pop_DOUBLE(s)+pop_LONG(s)); break;
    default:
      break;
  }
}  

/**
 * \brief Função que realiza a operação subtração.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack o elemento resultante.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_subtrai (STACK *s){
  switch(checks_type(s)){
    case 0: ;
      long y=pop_LONG(s); push_LONG(s,pop_LONG(s)-y);
      break;
    case 1: ;
      double w=pop_DOUBLE(s); push_DOUBLE(s,pop_DOUBLE(s)-w);
      break;
    case 2: ;
      long z=pop_LONG(s); push_DOUBLE(s,pop_DOUBLE(s)-z);
      break;
    case 3: ;
      double t=pop_DOUBLE(s); push_DOUBLE(s,pop_LONG(s)-t);
      break;  
    default:
      break;
  }
}

/**
 * \brief Função que realiza a operação multiplicação.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack o elemento resultante.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_multiplica (STACK *s){
  switch(checks_type(s)){
    case 0:
      push_LONG(s,pop_LONG(s)*pop_LONG(s)); break;
    case 1:
      push_DOUBLE(s,pop_DOUBLE(s)*pop_DOUBLE(s)); break;
    case 2:
      push_DOUBLE(s,pop_LONG(s)*pop_DOUBLE(s)); break;
    case 3:
      push_DOUBLE(s,pop_DOUBLE(s)*pop_LONG(s)); break;
    default:
      break;
  }
}

/**
 * \brief Função que realiza a operação divisão.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack o elemento resultante.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_divide (STACK *s){
  switch(checks_type(s)){
    case 0: ;
      long y=pop_LONG(s); push_LONG(s,pop_LONG(s)/y);
      break;
    case 1: ;
      double w=pop_DOUBLE(s); push_DOUBLE(s,pop_DOUBLE(s)/w);
      break;
    case 2: ;
      long z=pop_LONG(s); push_DOUBLE(s,pop_DOUBLE(s)/z);  
      break;
    case 3: ;
      double t=pop_DOUBLE(s); push_DOUBLE(s,pop_LONG(s)/t);
      break;  
    default:
      break;
  }
}

/**
 * \brief Função que realiza a operação de decremento.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack o elemento resultante.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_decrementa (STACK *s){
    switch(top(s).type) {
      case LONG:
        push_LONG(s,pop_LONG(s)-1); break;
      case DOUBLE:
        push_DOUBLE(s,pop_DOUBLE(s)-1); break;
      case CHAR:
        push_CHAR(s,pop_CHAR(s)-1); break;
      default: 
        break;  
    }
}

/**
 * \brief Função que realiza a operação de incremento.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack o elemento resultante.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 *
 */
void choose_incrementa (STACK *s){
    switch(top(s).type) {
      case LONG:
        push_LONG(s,pop_LONG(s)+1); break;
      case DOUBLE:
        push_DOUBLE(s,pop_DOUBLE(s)+1); break;
      case CHAR:
        push_CHAR(s,pop_CHAR(s)+1); break;
      default: 
        break;  
    }
}

/**
 * \brief Função que realiza a disjunção.
 * 
 * A função é equivalente ao 'e' lógico.  
 *
 * @param s Estrutura stack onde são guardados os elementos.
 *
 */
void choose_E(STACK *s){
   push_LONG(s,pop_LONG(s)&pop_LONG(s));
}

/**
 * \brief Função que realiza a conjunção.
 * 
 * A função é equivalente ao 'ou' lógico.  
 *
 * @param s Estrutura stack onde são guardados os elementos.
 *
 */
void choose_ou(STACK *s){
   push_LONG(s,pop_LONG(s)|pop_LONG(s));
}

/**
 * \brief Função que realiza a operação de xor(bitwise).
 * 
 * A função é equivalente ao 'ou' exclusivo.  
 *
 * @param s Estrutura stack onde são guardados os elementos.
 *
 */
void choose_xor(STACK *s){
   push_LONG(s,pop_LONG(s)^pop_LONG(s));
}

/**
 * \brief Função que realiza a operação de not(bitwise).
 * 
 * A função é equivalente à negação.  
 *
 * @param s Estrutura stack onde são guardados os elementos.
 *
 */
void choose_not(STACK *s){
   push_LONG(s,~pop_LONG(s));
}

/**
 * \brief Função que realiza a operação módulo.
 * 
 * A função retira o elemento do topo da stack.  
 * Realiza a operação e retorna ao topo da stack o elemento resultante.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 *
 */
void choose_modulo (STACK *s){
    long x = pop_LONG(s);
    push_LONG(s,pop_LONG(s)%x);
}

/**
 * \brief Função que realiza a operação potência.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack o elemento resultante.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_potencia (STACK *s){
  switch(checks_type(s)){
    case 0: ;
      long y=pop_LONG(s); push_LONG(s,pow(pop_LONG(s),y));
      break;
    case 1: ;
      double w=pop_DOUBLE(s); push_DOUBLE(s,pow(pop_DOUBLE(s),w));
      break;
    case 2: ;
      long z=pop_LONG(s); push_DOUBLE(s,pow(pop_DOUBLE(s),z));  
      break;
    case 3: ;
      double t=pop_DOUBLE(s); push_DOUBLE(s,pow(pop_LONG(s),t));
      break;
    case 9: ;
      char* s1 = pop_STRING(s); char* s2 = pop_STRING(s);
      push_LONG(s,procura(s2,s1)); break;    
    default:
      break;
  }
}

/**
 * \brief Função que realiza a operação de duplicação.
 * 
 * A função duplica o elemento que se encontra no topo da stack.  
 *
 * @param s Estrutura stack onde são guardados os elementos.
 *
 */
void choose_duplica (STACK *s){
    push(s,get_elem(s,0));
}

/**
 * \brief Função que realiza a operação de pop.
 *  
 * @param s Estrutura stack onde são guardados os elementos.
 *
 */
void choose_pop (STACK *s){
    pop(s);
}

/**
 * \brief Função que troca a posição dos elementos.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack os elementos resultantes.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_troca (STACK *s){
        DATA x = pop (s);
        DATA y = pop (s);
        push (s,x);
        push (s,y);   
}

/**
 * \brief Função que roda a posição dos elementos do topo da stack.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack os elementos resultantes.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_roda(STACK *s){
        DATA x = pop (s);
        DATA y = pop (s);
        DATA z = pop (s);
        push (s,y);
        push (s,x);
        push (s,z);   
}

/**
 * \brief Função que copia o n-ésimo elemento da stack.
 * 
 * A função verifica o indice do elemento indicado e copia o elemento correspondente para o topo da stack. 
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_copia(STACK *s){
       push(s,get_elem(s,pop_LONG(s)));
}


/**
 * \brief Função que converte o elemento do topo da stack num char.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack o elemento resultante.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_converteC (STACK *s){
    switch(top(s).type) {
      case LONG:
        push_CHAR (s,(char)pop_LONG(s)); break;
      case DOUBLE:
        push_CHAR (s,(char)pop_DOUBLE(s)); break;
      default:
        break;  
    }
}

/**
 * \brief Função que converte o elemento do topo da stack num long.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack o elemento resultante.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_converteI (STACK *s){
    switch(top(s).type) {
      case CHAR:
        push_LONG (s,(long)pop_CHAR(s)); break;
      case DOUBLE:
        push_LONG (s,(long)pop_DOUBLE(s)); break;
      case STRING:
        push_LONG(s,atol(pop_STRING(s))); break;
      default: 
        break;   
    }
}

/**
 * \brief Função que converte o elemento do topo da stack num double.
 * 
 * A função verifica o tipo do elemento do topo da stack e retira-o. 
 * Realiza a operação e retorna ao topo da stack o elemento resultante.
 *
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_converteF (STACK *s){
    switch(top(s).type) {
      case CHAR:
        push_DOUBLE (s,(double)pop_CHAR(s)); break;
      case LONG:
        push_DOUBLE(s,(double)pop_LONG(s)); break;
      case STRING:
        push_DOUBLE(s,atof(pop_STRING(s)));break;
      default:
        break;    
    }
}

/**
 * \brief Função que permite ler a linha seguinte.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_L (STACK *s){
    char linha[10240];
    assert( fgets(linha, 10240,stdin) != NULL);
    assert( linha[strlen(linha)-1] == '\n');
    push_STRING(s,&linha[0]);
}


/**
 * \brief Função que testa a disjunção entre dois elementos.
 *
 * Função correspondente ao "E" lógico mas com shortcut. 
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void short_E (STACK *s) {
  switch(checks_type(s)){
    case 0: ; 
      long y = pop_LONG(s);
      if (pop_LONG(s) != 0 && y) push_LONG (s,y);
      else push_LONG (s,0);
      break;
    case 1: ;
      double w = pop_DOUBLE(s);
      if (pop_DOUBLE(s) != 0 && w) push_DOUBLE (s,w);
      else push_LONG (s,0); 
      break;
    case 2: ;
      long z = pop_LONG(s); 
      if (pop_DOUBLE(s) != 0 && z) push_LONG (s,z);
      else push_LONG (s,0);
      break;
    case 3: ;
      double t = pop_DOUBLE(s); 
      if (pop_LONG(s) != 0 && t) push_DOUBLE (s,t);
      else push_LONG (s,0);
      break;  
    default: break;
  }
}

/**
 * \brief Função auxiliar do operador e| para dois longs.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_c0 (STACK *s){
      long y = pop_LONG(s);
      long w = pop_LONG(s);
      if (w != 0) push_LONG (s,w);
      else if (y!=0) push_LONG (s,y);
           else push_LONG(s,0);
}      

/**
 * \brief Função auxiliar do operador e| para um long e um char.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_c1 (STACK *s) {
      double z = pop_DOUBLE (s);
      double t = pop_DOUBLE (s);
      if (t != 0) push_DOUBLE(s,t);
      else if (z!=0) push_DOUBLE (s,z);
           else push_LONG(s,0);
}

/**
 * \brief Função auxiliar do operador e| para um long e um char.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_c2 (STACK *s) {
      long a = pop_LONG (s);
      double b = pop_DOUBLE (s);
      if (b != 0) push_DOUBLE (s,b);
      else if (a!=0) push_LONG (s,a);
           else push_LONG(s,0);
}

/**
 * \brief Função auxiliar do operador e| para dois char.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_c3 (STACK *s) {
      double c = pop_DOUBLE (s);
      long d = pop_LONG (s);
      if (d != 0) push_LONG (s,d);
      else if (c!=0) push_DOUBLE (s,c);
           else push_LONG(s,0);
}


/**
 * \brief Função que testa a conjunção entre dois elementos.
 *
 * Função correspondente ao "Ou" lógico mas com shortcut. 
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void short_OU (STACK *s) {
  switch(checks_type(s)){
    case 0:
      aux_c0(s); break;
    case 1:
      aux_c1(s); break;
    case 2:
      aux_c2(s); break;
    case 3:
      aux_c3(s); break;
    default:
      break;
  }
}

/**
 * \brief Função auxiliar do operador e< para dois longs.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_minor_c0(STACK *s){
  long y = pop_LONG(s);
  long w = pop_LONG(s);
  switch (y<w) {
		case 0 : push_LONG (s,w); break;
		default : push_LONG (s,y); break; 
  }
}

/**
 * \brief Função auxiliar do operador e< para um long e um char.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_minor_c1(STACK *s){
  double z = pop_DOUBLE (s);
  double t = pop_DOUBLE (s);
  switch (z<t) {
    case 0 : push_DOUBLE (s,t); break;
    default : push_DOUBLE (s,z); break; 
  }
}

/**
 * \brief Função auxiliar do operador e< para um long e um char.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_minor_c2(STACK *s){
  long a = pop_LONG (s);
  double b = pop_DOUBLE (s);
  switch (a < b) {
      case 0 : push_DOUBLE (s,b); break;
      default : push_LONG (s,a); break; 
  }
}

/**
 * \brief Função auxiliar do operador e< para dois char.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_minor_c3(STACK *s){
  double c = pop_DOUBLE (s);
  long d = pop_LONG (s);
  switch (c < d) { 
    case 0 : push_LONG (s,d); break;
    default : push_DOUBLE (s,c); break; 
  }
}

/**
 * \brief Função que procura o menor entre dois elementos.
 *
 * A função compara os dois elementos e coloca o menor na stack.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void  short_minor (STACK *s) {
  switch(checks_type(s)){
    case 0:
      aux_minor_c0(s); break;
    case 1:
      aux_minor_c1(s); break;
    case 2:
      aux_minor_c2(s); break;
    case 3: 
      aux_minor_c3(s); break;
    case 9: ;
      char* s1 = pop_STRING(s); char* s2 = pop_STRING(s);
      if (compare_lexical_menor(s2,s1)) push_STRING(s,s2);
      else push_STRING(s,s1); 
      break;     
    default: break;
  }
}

/**
 * \brief Função auxiliar do operador e> para dois longs.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_higher_c0(STACK *s){
  long y = pop_LONG(s);
  long w = pop_LONG(s);
  switch (y>w) {
		case 0 : push_LONG (s,w); break;
		default : push_LONG (s,y); break; 
  }
}

/**
 * \brief Função auxiliar do operador e> para um long e um char.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_higher_c1(STACK *s){
  double z = pop_DOUBLE (s);
  double t = pop_DOUBLE (s);
  switch (z>t) {
    case 0 : push_DOUBLE (s,t); break;
    default : push_DOUBLE (s,z); break; 
  }
}

/**
 * \brief Função auxiliar do operador e> para um long e um char.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_higher_c2(STACK *s){
  long a = pop_LONG (s);
  double b = pop_DOUBLE (s);
  switch (a > b) {
      case 0 : push_DOUBLE (s,b); break;
      default : push_LONG (s,a); break; 
  }
}

/**
 * \brief Função auxiliar do operador e> para dois char.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void aux_higher_c3(STACK *s){
  double c = pop_DOUBLE (s);
  long d = pop_LONG (s);
  switch (c > d) { 
    case 0 : push_LONG (s,d); break;
    default : push_DOUBLE (s,c); break; 
  }
}


/**
 * \brief Função que procura o maior entre dois elementos.
 *
 * A função compara os dois elementos e coloca o maior na stack.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void short_higher (STACK *s) {
  switch(checks_type(s)){
    case 0:
      aux_higher_c0(s); break;
    case 1:
      aux_higher_c1(s); break;
    case 2:
      aux_higher_c2(s); break;
    case 3: 
      aux_higher_c3(s); break;
    case 9: ;
      char* s1 = pop_STRING(s); char* s2 = pop_STRING(s);
      if (compare_lexical_maior(s2,s1)) push_STRING(s,s2);
      else push_STRING(s,s1); 
      break;  
    default:
      break;
  }
}

/**
 * \brief Função correspondente à condição if-then-else.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void if_then_else (STACK *s) {
  DATA x = pop(s); DATA y = pop(s); DATA cond = pop(s);
  switch (cond.type){
    case CHAR:{ 
      if (cond.CHAR != 0) push (s,y);
      else push (s,x);
      break;
    } 
    case LONG:{
      if (cond.LONG != 0) push (s,y);
      else push (s,x);
      break;
    } 
    case DOUBLE:{
      if (cond.DOUBLE != 0) push (s,y);
      else push(s,x);
      break;
    }
    case STRING:{ 
      if (strcmp(cond.STRING, "")!= 0) push (s,y);
      else push (s,x);
      break;
    }   
  } 
}

/**
 * \brief Função que testa a igualdade entre dois elementos.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_igual (STACK *s){
  switch(checks_type(s)){
    case 0: push_LONG (s, pop_LONG (s) == pop_LONG (s)); break;
    case 1: push_LONG (s, pop_DOUBLE (s) == pop_DOUBLE (s)); break;
    case 2: push_LONG (s, pop_LONG (s) == pop_DOUBLE (s)); break;
    case 3: push_LONG (s, pop_DOUBLE (s) == pop_LONG (s)); break;
    case 9: ;
      char* s1 = pop_STRING(s); char* s2 = pop_STRING(s);
      push_LONG(s,compare_lexical_igual(s2,s1)); break;   
    default:
      break;
  }
}

/**
 * \brief Função que testa se um elemento é menor que outro.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_menor (STACK *s){
  switch(checks_type(s)){
    case 0: push_LONG (s, pop_LONG (s) > pop_LONG (s)); break;
    case 1: push_LONG (s, pop_DOUBLE (s) > pop_DOUBLE (s)); break;
    case 2: push_LONG (s, pop_LONG (s) > pop_DOUBLE (s)); break;
    case 3: push_LONG (s, pop_DOUBLE (s) > pop_LONG (s)); break;
    case 4: push_LONG (s, pop_CHAR (s) > pop_CHAR (s)); break;
    case 6: push_LONG (s, pop_CHAR (s) > pop_LONG (s)); break;
    case 5: push_LONG (s, pop_LONG (s) > pop_CHAR (s)); break;
    case 9: ;
      char* s1 = pop_STRING(s); char* s2 = pop_STRING(s);
      push_LONG(s,compare_lexical_menor(s2,s1)); break;       
    default:
      break;
  }
}

/**
 * \brief Função que testa se um elemento é maior que outro.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_maior (STACK *s){
  switch(checks_type(s)){
    case 0: push_LONG (s, pop_LONG (s) < pop_LONG (s)); break;
    case 1: push_LONG (s, pop_DOUBLE (s) < pop_DOUBLE (s)); break;
    case 2: push_LONG (s, pop_LONG (s) < pop_DOUBLE (s)); break;
    case 3: push_LONG (s, pop_DOUBLE (s) < pop_LONG (s)); break;
    case 4: push_LONG (s, pop_CHAR (s) < pop_CHAR (s)); break;
    case 6: push_LONG (s, pop_CHAR (s) < pop_LONG (s)); break;
    case 5: push_LONG (s, pop_LONG (s) < pop_CHAR (s)); break;
    case 9: ;
      char* s1 = pop_STRING(s); char* s2 = pop_STRING(s);
      push_LONG(s,compare_lexical_maior(s2,s1)); break;        
    default:
      break;
  }
}

/**
 * \brief Função corresponte à negação.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_nao(STACK *s){
    DATA elem = pop(s);
    switch(elem.type) {
      case CHAR:
        if(elem.CHAR !=0) push_LONG(s,0);
        else push_LONG(s,1); 
        break;
      case LONG:
        if(elem.LONG !=0) push_LONG(s,0);
        else push_LONG(s,1); 
        break;
      case DOUBLE:
        if(elem.DOUBLE !=0) push_LONG(s,0);
        else push_LONG(s,1); 
        break;
      default:
        break;    
    } 
}

/**
 * \brief Função que preenche a stack das variaveis.
 * 
 * @param var Estrutura stack onde estão guardadas as variaveis.
 * 
 */
void fill_var(STACK *var){
  push_LONG(var,10); push_LONG(var,11); push_LONG(var,12);
  push_LONG(var,13); push_LONG(var,14); push_LONG(var,15);
  push_LONG(var,0); push_LONG(var,0); push_LONG(var,0); 
  push_LONG(var,0); push_LONG(var,0); push_LONG(var,0);
  push_LONG(var,0); push_CHAR(var,'\n'); push_LONG(var,0);
  push_LONG(var,0); push_LONG(var,0); push_LONG(var,0);
  push_CHAR(var,' '); push_LONG(var,0); push_LONG(var,0);
  push_LONG(var,0); push_LONG(var,0); push_LONG(var,0);
  push_LONG(var,1); push_LONG(var,2);
}

/**
 * \brief Função que substitui uma variavel pelo topo da stack.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 * @param var Estrutura stack onde estão guardadas as variaveis.
 * 
 * @param x Indice do elemento a ser trocado.
 * 
 */
void replace_elem(STACK *s, STACK *var, int x){
  var->stack[x] = top(s); 
}

/**
 * \brief Função que vai buscar o valor de determinada variavel.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 * @param var Estrutura stack onde estão guardadas as variaveis.
 * 
 * @param x Indice da variavel.
 * 
 */
void choose_letter(STACK *s,STACK *var, int x){
  push(s,get_elem(var,x));
}

/**
 * \brief Função que verifica o tipo dos dois elementos do topo da stack.
 * 
 * Esta função cria identificadores para os diferentes pares de tipos dos elementos.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 *
 * @returns O identificador correspondente. 
 */
int checks_type(STACK *s){
  int r=11;
  switch(get_elem(s,0).type){
    case LONG: switch (get_elem(s,1).type){
                  case LONG: r=0; break;
                  case DOUBLE: r=2; break;
                  default: r=5; break;      
               } break;  
    case DOUBLE: switch (get_elem(s,1).type){
                  case LONG: r=3; break;
                  default: r=1; break;  
                } break;
    case CHAR: switch (get_elem(s,1).type){
                 case LONG: r=6; break;
                 case CHAR: r=4; break; 
                 default: r=10; break;      
               } break;
    case STRING: switch(get_elem(s,1).type){
                   case LONG: r=7; break;
                   case CHAR: r=8; break;
                   case STRING: r=9; break;  
                   default: break;
                 } break;            
    default: break;
  } return r;
}

/**
 * \brief Função que verfica se o input é do tipo e{<,>,|,&}.
 * 
 * @param token Token a ser verificado.
 * 
 */
int is_elogic (char *token){
  return (strcmp(token,"e&")==0 || strcmp(token,"e|") == 0 || strcmp(token,"e>") == 0 || strcmp(token,"e<") == 0);
}

/**
 * \brief Função que verfica se o input é do tipo :<Letra>.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 * @param token Token a ser verificado.
 * 
 */
void choose_elogic(STACK *s, char *token){
  if(strcmp(token,"e|") == 0) {
    short_OU (s);
  } 
  else if(strcmp(token,"e&") == 0) {
    short_E (s);
  } 
  else if(strcmp(token,"e>") == 0) {
    short_higher (s);
  } 
  else if(strcmp(token,"e<") == 0) {
    short_minor (s);
  }
}

/**
 * \brief Função que verfica se o input é do tipo :<Letra_Maiuscula>
 * 
 * @param token Token a ser verificado.
 * 
 */
int is_adletter (char *token){
  return (token[0]==':' && isupper(token[1]));
}

/**
 * \brief Função que verfica o input do tipo :<Letra>, de :A a :M.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 * @param var Estrutura stack onde estão guardadas as variaveis.
 * 
 * @param token Token a ser verificado.
 * 
 */
void choose_adletter(STACK *s ,STACK *var,char *token){
  replace_elem(s,var,(token[1]-'A'));
}

/**
 * \brief Função que escolhe o qual o tipo de operação a reaizar.
 * 
 * A função infere qual o tipo de operação a realizar pelo operador conforme o que se encontra na Stack. 
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_soma_ou_concat(STACK *s) {
  if (has_type(get_elem(s,1),STRING) && has_type(get_elem(s,0),STRING)) choose_concatena_string(s); 
  else if (has_type(get_elem(s,1),STRING) && has_type(get_elem(s,0),CHAR)) choose_concatena_stringc(s);
  else if (has_type(get_elem(s,1),CHAR) && has_type(get_elem(s,0),STRING)) choose_concatena_cstring(s);
  else choose_soma(s); 
}

/**
 * \brief Função que escolhe o qual o tipo de operação a reaizar.
 * 
 * A função infere qual o tipo de operação a realizar pelo operador conforme o que se encontra na Stack. 
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_multiplica_ou_repete (STACK *s){
  if (has_type(get_elem(s,1),STRING) && has_type(get_elem(s,0),LONG)) choose_repete(s);
  else choose_multiplica(s); 
}

/**
 * \brief Função que escolhe o qual o tipo de operação a reaizar.
 * 
 * A função infere qual o tipo de operação a realizar pelo operador conforme o que se encontra na Stack. 
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_decrementa_ou_removefirst (STACK *s){
  if (has_type(get_elem(s,0),STRING)) choose_remove_first(s);
  else choose_decrementa(s); 
}

/**
 * \brief Função que escolhe o qual o tipo de operação a reaizar.
 * 
 * A função infere qual o tipo de operação a realizar pelo operador conforme o que se encontra na Stack. 
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_incrementa_ou_removelast (STACK *s){
  if (has_type(get_elem(s,0),STRING)) choose_remove_last(s);
  else choose_incrementa(s);
}

/**
 * \brief Função que escolhe o qual o tipo de operação a reaizar.
 * 
 * A função infere qual o tipo de operação a realizar pelo operador conforme o que se encontra na Stack. 
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_igual_ou_elemigual (STACK *s){
  if (has_type(get_elem(s,1),STRING) && has_type(get_elem(s,0),LONG)) choose_elem_igual(s);
  else choose_igual(s); 
}

/**
 * \brief Função que escolhe o qual o tipo de operação a reaizar.
 * 
 * A função infere qual o tipo de operação a realizar pelo operador conforme o que se encontra na Stack. 
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_menor_ou_nelemsmenor (STACK *s){
  if (has_type(get_elem(s,1),STRING) && has_type(get_elem(s,0),LONG)) choose_nelems_menor(s);
  else choose_menor(s);
}

/**
 * \brief Função que escolhe o qual o tipo de operação a reaizar.
 * 
 * A função infere qual o tipo de operação a realizar pelo operador conforme o que se encontra na Stack. 
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 */
void choose_maior_ou_nelemsmaior (STACK *s){
  if (has_type(get_elem(s,1),STRING) && has_type(get_elem(s,0),LONG)) choose_nelems_maior(s);
  else choose_maior(s);
}