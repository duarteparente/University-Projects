/**  
* @file ui.c   
* \brief Module in charge of the command line user interface (ideally in a 100x50 window). 
*/ 

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <ctype.h>
#include "../include/validation.h"
#include "../include/queries.h"
#include "../include/ui.h"
#include "../include/parsing.h"
#include "../include/auxiliar.h"

/**
 * \brief Get user input.
 */
char *get_option(){
    printf("\n  »» Insira a opção: ");
    char str[20];
    scanf(" %[^\n]s",str);
    return strdup(str);  
}


/**
 * \brief Get the user input for parameter 'N'.
 */
int get_N_param(int query_id){
    (query_id == 8) ? printf(" > Número de linguagens: ") : printf(" > Número de utilizadores: ");
    char str[15];
    scanf(" %[^\n]s",str);
    return atoi(str);
}

/**
 * \brief Get the user input for parameter 'data' or 'data_inicial'.
 */
char *get_date_inicio(int query_id){
    (query_id == 7 || query_id == 8) ? printf(" > Data (AAAA-MM-DD): ") : printf(" > Data inicial (AAAA-MM-DD): ");
    char str[13];
    scanf(" %[^\n]s",str);
    return strdup(str);
}

/**
 * \brief Get the user input for parameter 'data_final'.
 */
char *get_date_final(){
    printf(" > Data final (AAAA-MM-DD): ");
    char str[13];
    scanf(" %[^\n]s",str);
    return strdup(str);
}

/**
 * \brief Get the user input for language parameter.
 */
char *get_lang_param(){
    printf(" > Linguagem: ");
    char str[50];
    scanf(" %[^\n]s",str);
    return strdup(str);
}

/**
 * \brief Get the page to jump.
 */
int jump_to (){
    printf(" > Insira a página: ");
    char str[50];
    scanf(" %[^\n]s",str);
    return atoi(str);
}

/**
 * Get the user or repo id to search in pages.
 */
int find_id (int user_flag, int query_id, PAGES pages){
    switch(user_flag){
        case 0: printf(" > Insira o ID do utilizador: "); break;
        case 1: printf(" > Insira o ID do repositório: "); break;
        case 2: printf(" > Insira o Username do utilizador: "); break;
    }
    char str[70];
    scanf(" %[^\n]s",str);
    if (user_flag == 2) return get_pageByUsername(str,pages);
    return get_pageById(query_id,str,pages);
}

/**
 * Enquire again in case of error in the parameters provided .
 */
void parameters_error(int query_id, STATS stats, USERS users, COMMITS commits, REPOS repos){
    puts("\n   Parâmetros inválidos! Por favor, insira novamente.\n");
    parameter_query_UI(query_id,stats,users,commits,repos);
}

/**
 * \brief Generator of the page information.
 */
void show_page_number(int page, int page_length){;
    char p[100];
    snprintf(p, 100, "\n\n--------------------------- Página %d de %d ---------------------------\n", page, page_length);
    puts(p);
}

/**
 * \brief Generator of the output menu.
 */
void show_postOutput(int query_id, int current_page, int total_pages){
    puts(" == Opções ==\n");
    if (total_pages == 1){
        puts( "    M   -> Voltar ao menu\n"
              "    L   -> Sair"       );
    }
    else if (current_page == 1){
        switch(query_id){
            case 5: case 6: case 9: puts( "    P  -> Próxima página\n    S  -> Saltar para a página\n    U  -> Procurar utilizador(Por Username)\n    I  -> Procurar utilizador(Por ID)\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
            case 7: puts( "    P  -> Próxima página\n    S  -> Saltar para a página\n    R  -> Procurar repositório\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
            case 10: puts( "    P  -> Próxima página\n    S  -> Saltar para a página\n    U  -> Procurar utilizador(Por Username)\n    I  -> Procurar utilizador(Por ID)\n    R  -> Procurar repositório\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
            default: puts( "    P  -> Próxima página\n    S  -> Saltar para a página\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
        }
    }
    else if (current_page == total_pages){
        switch(query_id){
            case 5: case 6: case 9: puts( "    A  -> Página anterior\n    S  -> Saltar para a página\n    U  -> Procurar utilizador(Por Username)\n    I  -> Procurar utilizador(Por ID)\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
            case 7: puts( "    A  -> Página anterior\n    S  -> Saltar para a página\n    R  -> Procurar repositório\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
            case 10: puts( "    A  -> Página anterior\n    S  -> Saltar para a página\n    U  -> Procurar utilizador(Por Username)\n    I  -> Procurar utilizador(Por ID)\n    R  -> Procurar repositório\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
            default: puts( "    A  -> Página anterior\n    S  -> Saltar para a página\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
        }
    }
    else{
        switch(query_id){
            case 5: case 6: case 9: puts( "    A  -> Página anterior\n    P  -> Próxima página\n    S  -> Saltar para a página\n    U  -> Procurar utilizador(Por Username)\n    I  -> Procurar utilizador(Por ID)\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
            case 7: puts( "    A  -> Página anterior\n    P  -> Próxima página\n    S  -> Saltar para a página\n    R  -> Procurar repositório\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
            case 10: puts( "    A  -> Página anterior\n    P  -> Próxima página\n    S  -> Saltar para a página\n    U  -> Procurar utilizador(Por Username)\n    I  -> Procurar utilizador(Por ID)\n    R  -> Procurar repositório\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
            default: puts( "    A  -> Página anterior\n    P  -> Próxima página\n    S  -> Saltar para a página\n    M  -> Voltar ao menu\n    L  -> Sair"       ); break;
        }
    }
}

/**
 * \brief Generator of the query output opening.
 */
void init_output(int query_id){
    clear_window();
    char opening[100];
    snprintf(opening, 100, "\n\n==================================  QUERY %d ==================================\n", query_id);
    puts(opening);
    switch(query_id){
        case 5: case 6: puts("Lugar: ID do Utilizador - Username - Quantidade de Commits\n"); break;
        case 7:  puts("Lugar: ID do Repositório - Descrição do Repositório\n"); break;
        case 8:  puts("Lugar: Linguagem\n"); break;
        case 9:  puts("Lugar: ID do Utilizador - Username\n"); break;
        case 10: puts("Lugar: ID do Utilizador - Username - Tamanho da mensagem de Commit\n\n"); break;
    }
}

/**
 * \brief Query 1 display.
 */
void query1_UI(STATS stats, USERS users, COMMITS commits, REPOS repos){
    init_output(1);
    int bot = get_bot_count(stats);
    int org = get_org_count(stats);
    int user = get_user_count(stats);
    printf("   Número de:\n\n       > Bots: %d\n       > Organizações: %d\n       > Utilizadores: %d\n", bot, org, user);
    show_page_number(1,1);
    end_output_1page(stats,users,commits,repos);
}

/**
 * \brief Query 2 display.
 */
void query2_UI(STATS stats, USERS users, COMMITS commits, REPOS repos){
    init_output(2);
    int collab = get_colaboradores(stats);
    int rl = get_rl_stats(stats);
    float media = (float) collab / (float) rl;
    float two_decimal = ((int)(media * 100)) / 100.0; 
    printf("\n >> Em média há %g colaborador(es) por repositório.\n", two_decimal);
    show_page_number(1,1);
    end_output_1page(stats,users,commits,repos);
}

/**
 * \brief Query 3 display.
 */
void query3_UI(STATS stats, USERS users, COMMITS commits, REPOS repos){
    init_output(3);
    int nr = get_repos_w_bot(stats);
    printf("\n >> Há %d repositório(s) contendo Bots como colaboradores.\n", nr);
    show_page_number(1,1); 
    end_output_1page(stats,users,commits,repos);
}

/**
 * \brief Query 4 display.
 */
void query4_UI(STATS stats, USERS users, COMMITS commits, REPOS repos){
    init_output(4);
    int cl = get_cl_stats(stats);
    int ul = get_ul_stats(stats);
    float media = (float) cl / (float) ul;
    float two_decimal = ((int)(media * 100)) / 100.0; 
    printf("\n >> Em média 1 utilizador é autor de %g commit(s).\n", two_decimal);
    show_page_number(1,1);
    end_output_1page(stats,users,commits,repos);
}

/**
 * \brief Query 5 execution (UI).
 */
void query5_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos){
    int N = get_N_param(5);
    char *date_from = get_date_inicio(5);
    char *date_to = get_date_final();
    if (validate_parameters(N,date_from,date_to,5)) query_5(1,file,date_to,date_from,N,commits,users);
    else parameters_error(5,stats,users,commits,repos);
}

/**
 * \brief Query 6 execution (UI).
 */
void query6_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos){
    int N = get_N_param(6);
    char *language = get_lang_param();
    if (validate_parameters(N,language,language,6)) query_6(1,file,language,N,commits,users,repos);
    else parameters_error(6,stats,users,commits,repos);
}

/**
 * \brief Query 7 execution (UI).
 */
void query7_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos){
    char *date_from = get_date_inicio(7);
    if (validate_parameters(1,date_from,date_from,7)) query_7(1,file,date_from,commits,repos);
    else parameters_error(7,stats,users,commits,repos);
}

/**
 * \brief Query 8 execution (UI).
 */
void query8_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos){
    int N = get_N_param(8);
    char *date_from = get_date_inicio(8);
    if (validate_parameters(N,date_from,date_from,8)) query_8(1,file,date_from,N,commits,repos);
    else parameters_error(8,stats,users,commits,repos);
}

/**
 * \brief Query 9 execution (UI).
 */
void query9_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos){
    int N = get_N_param(9);
    if (validate_parameters(N,file,file,9)) query_9(1,file,N,commits,users);
    else parameters_error(9,stats,users,commits,repos);
}

/**
 * \brief Query 10 execution (UI).
 */
void query10_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos){
    int N = get_N_param(10);
    if (validate_parameters(N,file,file,10)) query_10(1,file,N,commits,users);
    else parameters_error(10,stats,users,commits,repos);
}

/**
 * Queries 5 to 10 controller. 
 */
void parameter_query_UI(int query_id, STATS stats, USERS users, COMMITS commits, REPOS repos){
    char file[22] = "saida/ui_output.csv";
    switch(query_id){
        case 5: query5_UI(file, stats, users, commits, repos); break;
        case 6: query6_UI(file, stats, users, commits, repos); break;
        case 7: query7_UI(file, stats, users, commits, repos); break;
        case 8: query8_UI(file, stats, users, commits, repos); break;
        case 9: query9_UI(file, stats, users, commits, repos); break;
        case 10: query10_UI(file, stats, users, commits, repos); break;
        default: break;
    }
    PAGES pages = fill_pages();
    delete_OutputFile();
    page_controller(1, query_id, pages, stats, users, commits, repos);
}


/**
 * \brief Generator of the final menu for statistic queries.
 */
void end_output_1page(STATS stats, USERS users, COMMITS commits, REPOS repos){
    show_postOutput(1,1,1);
    char* op = get_option();
    clear_window();
    if (op[0] == 'M' || op[0] == 'm') input_controller(stats,users,commits,repos);
    else { destroy_STATS(stats); destroy_COMMITS(commits); destroy_REPOS(repos); destroy_USERS(users); }
}

/**
 * \brief Controls the page output respective menu.
 * In case of invalid input returns to first page.
 */
void page_controller(int page_number, int query_id, PAGES pages, STATS stats, USERS users, COMMITS commits, REPOS repos){
    if(page_number < 1 || page_number > get_pages_length(pages)) page_controller(1, query_id, pages, stats, users, commits, repos);
    init_output(query_id);
    show_page(page_number, pages);
    show_page_number(page_number, get_pages_length(pages));
    show_postOutput(query_id,page_number, get_pages_length(pages));
    char *option = get_option();
    switch(*option){
        case 'P': case 'p': page_controller(page_number+1, query_id, pages, stats, users, commits, repos); break;
        case 'A': case 'a': page_controller(page_number-1, query_id, pages, stats, users, commits, repos); break;
        case 'S': case 's': page_controller(jump_to(), query_id, pages, stats, users, commits, repos); break;
        case 'M': case 'm': clear_window(); input_controller(stats,users,commits,repos); break;
        case 'U': case 'u': page_controller(find_id(2,query_id,pages), query_id, pages, stats, users, commits, repos); break;
        case 'R': case 'r': page_controller(find_id(1,query_id,pages), query_id, pages, stats, users, commits, repos); break;
        case 'I': case 'i': page_controller(find_id(0,query_id,pages), query_id, pages, stats, users, commits, repos); break;
        default: destroy_STATS(stats); destroy_COMMITS(commits); destroy_REPOS(repos); destroy_USERS(users); clear_window(); break;
    }
}


/**
 * \brief Shows queries menu on command line.
 */
void display_Menu(){
    puts ("\n\n      === MENU ===\n\n"   
             "   +--------------------------------------------------------------------------------------------+\n"
             "   | 1  |  Quantidade de bots, organizações e utilizadores                                      |\n"
             "   +--------------------------------------------------------------------------------------------+\n"
             "   | 2  |  Número médio de colaboradores por repositório                                        |\n"
             "   +--------------------------------------------------------------------------------------------+\n"
             "   | 3  |  Quantidade de repositórios com bots                                                  |\n"
             "   +--------------------------------------------------------------------------------------------+\n"
             "   | 4  |  Quantidade média de commits por utilizador                                           |\n"
             "   +--------------------------------------------------------------------------------------------+\n"
             "   | 5  |  Top N de utilizadores mais ativos num determinado intervalo de datas                 |\n"
             "   +--------------------------------------------------------------------------------------------+\n"
             "   | 6  |  Top N de utilizadores com mais commits em repositórios de uma determinada linguagem  |\n"
             "   +--------------------------------------------------------------------------------------------+\n"
             "   | 7  |  Repositórios inativos a partir de uma determinada data                               |\n"
             "   +--------------------------------------------------------------------------------------------+\n"
             "   | 8  |  Top N de linguagens mais utilizadas a partir de uma determinada data                 |\n"
             "   +--------------------------------------------------------------------------------------------+\n"
             "   | 9  |  Top N de utilizadores com mais commits em repositórios cujo owner é um amigo seu     |\n"
             "   +--------------------------------------------------------------------------------------------+\n"
             "   | 10 |  Top N de utilizadores com as maiores mensagens de commit por repositório             |\n"
             "   +--------------------------------------------------------------------------------------------+\n"
    );
}

/**
 * Execute the query chosen by user input 
 */
void choose_querieUI(int query_id, STATS stats, USERS users, COMMITS commits, REPOS repos){
    switch(query_id){
        case 1:   query1_UI(stats,users,commits,repos); break;
        case 2:   query2_UI(stats,users,commits,repos); break;
        case 3:   query3_UI(stats,users,commits,repos); break;
        case 4:   query4_UI(stats,users,commits,repos); break;
        case 5: case 6: case 7: case 8: case 9: case 10:  parameter_query_UI(query_id,stats,users,commits,repos); break;
        default: break;
    }
}


/**
 * Get user input and execute respective query   
 */
void input_controller(STATS stats, USERS users, COMMITS commits, REPOS repos){
    display_Menu();
    char* query_id = get_option();
    while (strlen(query_id)>2 || atoi(query_id) < 1 || atoi(query_id) > 10) { 
        puts("\n   Escolha inválida! Por favor, insira a opção novamente.");
        query_id = get_option();
    }    
    choose_querieUI(atoi(query_id), stats, users, commits, repos);
}

/**
 * Fill each respective catalogue and start the user interface   
 */
void load_files(){
    clear_window();
    puts ("\n      === FICHEIROS ===\n\n");
    STATS stats = create_STATS();
    puts(" > Ficheiro de utilizadores carregado.........................");
    USERS users = read_users(stats);
    puts(" > Ficheiro de commits carregado..............................");
    REPOS repos_valid = validate_fields_repos();
    COMMITS commits = read_commits(stats,users,repos_valid);
    puts(" > Ficheiro de repositórios carregado.........................");
    REPOS repos = read_repos(stats,repos_valid,users,commits);
    input_controller(stats, users, commits, repos);
}