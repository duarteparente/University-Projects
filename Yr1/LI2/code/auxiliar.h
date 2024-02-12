/**
 * @file auxiliar.h 
 * \brief Módulo das funções auxiliares.
 */ 
#include "stack.h"
#include <string.h>

DATA get_elem(STACK *s,int n);

void choose_soma (STACK *s);
void choose_subtrai (STACK *s);
void choose_multiplica (STACK *s);
void choose_divide (STACK *s);
void choose_decrementa (STACK *s);
void choose_incrementa (STACK *s);
void choose_E(STACK *s);
void choose_ou(STACK *s);
void choose_modulo (STACK *s);
void choose_xor(STACK *s);
void choose_not(STACK *s);
void choose_potencia (STACK *s);

void choose_duplica (STACK *s);
void choose_pop (STACK *s);
void choose_troca (STACK *s);
void choose_roda(STACK *s);
void choose_copia(STACK *s);
void choose_converteC (STACK *s);
void choose_converteI (STACK *s);
void choose_converteF (STACK *s);
void choose_L (STACK *s);

void aux_c0 (STACK *s);
void aux_c1 (STACK *s);
void aux_c2 (STACK *s);
void aux_c3 (STACK *s);
void short_OU (STACK *s);
void short_E (STACK *s);
void aux_minor_c0(STACK *s);
void aux_minor_c1(STACK *s);
void aux_minor_c2(STACK *s);
void aux_minor_c3(STACK *s);
void short_minor (STACK *s);
void aux_higher_c0(STACK *s);
void aux_higher_c1(STACK *s);
void aux_higher_c2(STACK *s);
void aux_higher_c3(STACK *s);
void short_higher (STACK *s);
void if_then_else (STACK *s);
void choose_igual (STACK *s);
void choose_menor (STACK *s);
void choose_maior (STACK *s);
void choose_nao(STACK *s);



void fill_var(STACK *var);
void replace_elem(STACK *s, STACK *var, int x);
void choose_letter(STACK *s, STACK*var, int x);

int checks_type(STACK *s);

int is_elogic (char *token);
void choose_elogic(STACK *s, char *token);

int is_adletter (char *token);
void choose_adletter(STACK *s ,STACK *var,char *token);

void choose_multiplica_ou_repete (STACK *s);
void choose_decrementa_ou_removefirst (STACK *s);
void choose_incrementa_ou_removelast (STACK *s);
void choose_igual_ou_elemigual (STACK *s);
void choose_menor_ou_nelemsmenor (STACK *s);
void choose_maior_ou_nelemsmaior (STACK *s);
void choose_soma_ou_concat(STACK *s);