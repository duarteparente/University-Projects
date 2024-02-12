/**
 * @file parser.c 
 * \brief Funções da stack.
 */ 
#include <stdio.h>
#include <stdlib.h>
#include "stack.h"

/**
 * \brief Função que se verifica o tipo de determinado elemento corresponde à mask.
 * 
 * @param elem Elemento a ser verificado.
 *
 * @param mask Máscara que permite verificar o tipo do elemento.
 *
 * @returns 1 se o tipo do elemento corresponder à mask.
 */ 
int has_type(DATA elem, int mask) {
  return (elem.type & mask) != 0;
}

/**
 * \brief Função que cria a stack.
 *
 */
STACK *create_stack() {
  STACK *s = (STACK *) calloc(1, sizeof(STACK));
  //s->n_elems = 0;
  s->size = 100;
  s->stack = (DATA *) calloc(s->size, sizeof(DATA));
  return s;
}
/**
 * \brief Função que adiciona um elemento ao topo da stack.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * @param elem Elemento a ser adicionado à stack.
 */

void push(STACK *s, DATA elem) {
  if(s->size == s->n_elems) {
    s->size += 100;
    s->stack = (DATA *) realloc(s->stack, s->size * sizeof(DATA));
  }
  s->stack[s->n_elems] = elem;
  s->n_elems++;
}

/**
 * \brief Função que remove um elemento to topo da stack.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 */
DATA pop(STACK *s) {
  s->n_elems--;
  return s->stack[s->n_elems];
}

/**
 * \brief Função que determina o elemento no topo da stack.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 */
DATA top(STACK *s) {
  return s->stack[s->n_elems - 1];
}

/**
 * \brief Função que mostra se a stack está vazia.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 */
int is_empty(STACK *s) {
  return s->n_elems == 0;
}
/**
 * \brief Função que imprime a stack.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 */
void print_stack(STACK *s) {
  for(int K = 0; K < s->n_elems; K++) {
    DATA elem = s->stack[K];
    TYPE type = elem.type;
    switch(type) {
      case LONG:
        printf("%ld", elem.LONG); break;
      case DOUBLE:
        printf("%g", elem.DOUBLE); break;
      case CHAR:
        printf("%c", elem.CHAR); break;
      case STRING:
        printf("%s", elem.STRING); break;
    }
  }
  printf("\n");
}
/**
 * \brief Função que permite criar uma variante das funções pop e push, restritas a um tipo.
 * 
 * @param _type Tipo do elemento.
 *
 * @param _name Nome do elemento.
 */
#define STACK_OPERATION(_type, _name)         \
  void push_##_name(STACK *s, _type val) {    \
    DATA elem;                                \
    elem.type = _name;                        \
    elem._name = val;                         \
    push(s, elem);                            \
  }                                           \
  _type pop_##_name(STACK *s) {               \
    DATA elem = pop(s);                       \
    assert(elem.type == _name);               \
    return elem._name;                        \
  }

STACK_OPERATION(long, LONG)
STACK_OPERATION(double, DOUBLE)
STACK_OPERATION(char, CHAR)
STACK_OPERATION(char *, STRING)