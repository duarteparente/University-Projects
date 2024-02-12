/**
 * @file guiao4.h 
 * \brief Módulo das funções de suporte ao guião 4.
 */ 
#include "stack.h"
#include <string.h>

char * go_get_maior(char *s, int x);
char * go_get_menor(char *s, int x);
char  go_get_elem(char *s, int c);

void choose_elem_igual(STACK *s);
void choose_nelems_maior(STACK *s);
void choose_nelems_menor(STACK *s);

char * reverse( char * s);

void choose_range(STACK *s);

int itoc (char *token);

void choose_concatena_string(STACK *s);
void choose_concatena_stringc(STACK *s);
void choose_concatena_cstring(STACK *s);
void choose_remove_first(STACK *s);
void choose_remove_last(STACK *s);

int is_var(char *token);

void choose_arithmetic(STACK *s, char *token);
int is_arithmetic(char *token);
int is_opstack(char *token);
void choose_opstack(STACK *s, char *token);
void choose_ooc(STACK *s, STACK *var, char*token);

int is_double(char *token);

void choose_repete(STACK *s);

int procura (char principal[], char wanted[]);
int procura_char (char principal[], char wanted);

int compare_lexical_maior(char *s1, char *s2);
int compare_lexical_menor(char *s1, char *s2);
int compare_lexical_igual(char *s1, char *s2);

int is_string(char *token, char s1);

char* tail(char* s);
char* head(char* s);

void get_string (STACK *s, char *token);
int conta_aspas(char *s);

int conta_parentesis(char *s);
