/**
 * @file parser.h 
 * \brief Módulo das funções de filtragem de input.
 */ 
#include "stack.h"
#include <string.h>


int is_aux_parser(char *token);
void aux_parser (STACK *s, STACK *var, char *token);
void parser (char *line);
