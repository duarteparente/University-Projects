/** \mainpage LI2 2021
 * 
 * Grupo: EIPL3G02
 * 
 * Projeto realizado por: Pedro Miguel Castilho Martins, Gabriel Alexandre Monteiro da Silva,
 *                        Duarte Gonçalves Parente, Flávio Alexandre Marques da Silva.
 * 
 */


/**
 * @file main.c 
 * \brief Ficheiro que contém a função principal do programa
 */ 
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include "parser.h"
#include "stack.h"
#include "auxiliar.h"

/**
 * \brief Função Main do programa.
 * 
 * Lê o input do utilizador e invoca a função parser.
 * 
 * @returns Devolve o valor 0.
 */
int main (){
    char line[20480];
    assert( fgets(line, 20480,stdin) != NULL);
    assert( line[strlen(line)-1] == '\n');
    parser(line);
    return 0;
}