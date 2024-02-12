/**
 * @file parser.c 
 * \brief Filtragem de inputs.
 */ 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "parser.h"
#include "stack.h"
#include "auxiliar.h"
#include "guiao4.h"
#include <math.h>

/**
 * \brief Função auxiliar para a filtragem de inputs no parser.
 * 
 * @param token Token a ser analisado.
 * 
 * @returns 0 se falso, !0 se verdadeiro.
 */
int is_aux_parser(char *token){
  return (is_adletter(token) || is_elogic(token) || itoc(token) || is_string(token,'\"') || token[strlen(token)-1]=='\"');
}

/**
 * \brief Função auxiliar para a filtragem de inputs no parser.
 * 
 * @param s Estrutura stack onde são guardados os elementos.
 * 
 * @param var Estrutura stack onde estão guardadas as variaveis.
 * 
 * @param token Token a ser verificado.
 * 
 */
void aux_parser (STACK *s, STACK *var, char *token){
  if (is_adletter(token)){
      choose_adletter(s,var,token);
  }  
  else if (is_elogic(token)){
      choose_elogic(s,token);
  }
  else if (itoc(token)) {
      choose_ooc(s,var,token);
  }
  else if (is_string(token,'\"') || token[strlen(token)-1]=='\"'){
          get_string(s,token);
  } 
}

/**
 * \brief Função que recebe os inputs do utilizador e realiza as operações em conformidade.
 * 
 * Esta função separa os inputs por espaços, tabs ou mudanças de linha, através do strtok e filtra esses mesmos tokens.
 * 
 * @param line A linha de inputs que foi lida.
 */ 
void parser (char *line){ 
    STACK *s = create_stack();
    STACK *var = create_stack();
    fill_var(var);
    char *limits = " \t\n";
    for (char *token = strtok(line,limits); token != NULL; token = strtok(NULL, limits)) { // everytime there's a " ", "\t" or "\n" the elements in between are saved as tokens
        char *rem;
        double val_d = strtod(token,&rem);
        long val_i = strtol(token, &rem, 10); // strtol is used to find INT's on the input
        if (strlen(rem) == 0){
           push_LONG(s,val_i);
        }
        else if (is_aux_parser(token)){
          aux_parser(s,var,token);
        }
        else if (is_double(token)){
          push_DOUBLE(s,val_d);
        } 
        else push_STRING(s,token);
    }  
    print_stack(s);
}