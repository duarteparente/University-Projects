/**
 * @file stack.h 
 * \brief Módulo das funções da stack.
 */ 
 
#ifndef ___STACK_H___
#define ___STACK_H___

#include <assert.h>

/**
* Enumera os diferentes tipos de dados como potências da base 2.
*
*/
typedef enum {LONG = 1,   /**< tipo LONG de valor 2^0*/
              DOUBLE = 2, /**< tipo DOUBLE de valor 2^1*/
              CHAR = 4,   /**< tipo CHAR de valor 2^2*/
              STRING = 8  /**< tipo STRING de valor 2^3*/
              } TYPE;

/**
*
* Máscaras do tipo LONG e CHAR.
*
*/
#define INTEGER  (LONG | CHAR)
/**
*
* Máscaras do tipo LONG, CHAR e DOUBLE.
*
*/
#define NUMBER   (INTEGER | DOUBLE)

/**
*  Estrutura que define os tipos de dados aceites pela stack.
*
*/
typedef struct data {
  TYPE type;     /**< Tipo dos dados */
  // Esta parte devia ser transformada numa union mais tarde
  long LONG;     /**< tipo de dados LONG*/
  double DOUBLE; /**< tipo de dados DOUBLE*/
  char CHAR;     /**< tipo de dados CHAR*/
  char *STRING;  /**< tipo de dados STRING*/
} DATA;

/**
*
* Estrutura stack onde são guardados os elemento.
*
*/
typedef struct stack {
  DATA *stack; /**< estrutura de dados stack*/
  int size;    /**< tamanho da stack*/
  int n_elems; /**< índice do elemento*/
} STACK;

/**
*
* \brief Funções que permitem navegar na stack.
*
* @param elem Elemento a ser adicionado à stack.
*
* @param s Estrutura stack onde são guardados os elementos.
*
* @param mask Máscara que permite verificar o tipo do elemento.
*
*/
int has_type(DATA elem, int mask);
STACK *create_stack();
void push(STACK *s, DATA elem);
DATA pop(STACK *s);
DATA top(STACK *s);
int is_empty(STACK *s);
void print_stack(STACK *s);

/**
*
*
* Permite criar uma variante das funções pop e push, restritas a um tipo.
* 
*
*/
#define STACK_OPERATION_PROTO(_type, _name)   \
  void push_##_name(STACK *s, _type val);     \
  _type pop_##_name(STACK *s);

STACK_OPERATION_PROTO(long, LONG)
STACK_OPERATION_PROTO(double, DOUBLE)
STACK_OPERATION_PROTO(char, CHAR)
STACK_OPERATION_PROTO(char *, STRING)

#endif
