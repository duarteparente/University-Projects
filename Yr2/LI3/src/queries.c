/**
 * @file queries.c 
 * \brief Queries analysis and execution.
 */ 
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "../include/queries.h"
#include "../include/auxiliar.h"
#include "../include/stats.h"
#include "../include/parameters.h"
#include "../include/validation.h"

/**
 * \brief Query 1 execution.
 */
void query_1(char *file, STATS stats){
   FILE *output_1 = fopen(file, "a");
   int bot = get_bot_count(stats);
   int org = get_org_count(stats);
   int user = get_user_count(stats);
   fprintf(output_1, "Bot: %d\nOrganization: %d\nUser: %d\n", bot, org, user);
   fclose (output_1);
}

/**
 * \brief Query 2 execution.
 */
void query_2(char *file, STATS stats){
    FILE *output_2 = fopen(file, "a");
    int collab = get_colaboradores(stats);
    int rl = get_rl_stats(stats);
    float media = (float) collab / (float) rl;
    float two_decimal = ((int)(media * 100)) / 100.0;
    fprintf(output_2, "%g\n", two_decimal);
    fclose(output_2);
}

/**
 * \brief Query 3 execution.
 */
void query_3(char *file, STATS stats){
    FILE *output_3 = fopen(file, "a");
    fprintf(output_3, "%d\n", get_repos_w_bot(stats));
    fclose(output_3);
}

/**
 * \brief Query 4 execution.
 */
void query_4(char *file, STATS stats){
    FILE *output_4 = fopen(file, "a");
    int cl = get_cl_stats(stats);
    int ul = get_ul_stats(stats);
    float media = (float) cl / (float) ul;
    float two_decimal = ((int)(media * 100)) / 100.0;
    fprintf(output_4, "%g\n", two_decimal);
    fclose(output_4);     
}

/**
 * \brief Query 5 execution.
 */
void query_5(int UI_flag, char *file, char *date_to, char *date_from, int N, COMMITS commits, USERS users){
    PARAM comms_date = create_PARAM();
    fill_commitsByDate(comms_date, commits, date_from, date_to);
    (UI_flag == 1) ? output_UserLoginQtty_UI(file, comms_date, users, N) : output_UserLoginQtty(file, comms_date, users, N);
    destroy_PARAM(comms_date);
}

/**
 * \brief Query 6 execution.
 */
void query_6(int UI_flag, char *file, char *language, int N, COMMITS commits, USERS users, REPOS repos){
    PARAM comms_lang = create_PARAM();
    fill_commitsByLang(comms_lang, commits, repos, language);
    (UI_flag == 1) ? output_UserLoginQtty_UI(file, comms_lang, users, N) : output_UserLoginQtty(file, comms_lang, users, N);
    destroy_PARAM(comms_lang);
}


/**
 * \brief Query 7 execution.
 */
void query_7(int UI_flag, char *file, char *date_from, COMMITS commits, REPOS repos){
    PARAM no_comms = create_PARAM();
    fill_reposByLang(no_comms, commits, date_from);
    (UI_flag == 1) ? output_q7_UI(file, no_comms, repos) : output_q7(file, no_comms, repos);
}

/**
 * \brief Query 8 execution.
 */
void query_8(int UI_flag, char *file, char *date_from, int N, COMMITS commits, REPOS repos){
    PARAM lang = create_PARAM();
    fill_Lang(lang, commits, repos, date_from);
    (UI_flag == 1) ? output_q8_UI(file, lang, repos, N) : output_q8(file, lang, repos, N);
}

/**
 * \brief Query 9 execution.
 */
void query_9(int UI_flag, char *file, int N, COMMITS commits, USERS users){
    PARAM friends = create_PARAM();
    fill_friends(commits, users, friends);
    (UI_flag == 1) ? output_q9_UI(file, friends, users, N) : output_q9(file, friends, users, N);
    destroy_PARAM(friends);
}

/**
 * \brief Query 10 execution.
 */
void query_10(int UI_flag, char *file, int N, COMMITS commits, USERS users){
    (UI_flag == 1) ? output_q10_UI(file, N, commits, users) :  output_q10(file, N, commits, users);
}

/**
 * Reads and validates command line, executing query 5 if valid. 
 */
void call_query5(char *file, char *line, COMMITS commits, USERS users){
    strsep (&line, " "); 
    int N = is_empty(strsep (&line, " "));
    char *date_from = strdup(strsep (&line, " "));
    char *date_to = strdup(strsep (&line, "\n"));
    if (validate_parameters(N,date_from,date_to,5)) query_5(0,file,date_to,date_from,N,commits,users);
    else error_file(file);
    free(date_from);
    free(date_to);
}

/**
 * Reads and validates command line, executing query 6 if valid. 
 */
void call_query6(char *file, char *line, COMMITS commits, USERS users, REPOS repos){
    strsep (&line, " "); 
    int N = is_empty(strsep (&line, " "));
    char *language = strdup(strsep (&line, "\n"));
    if (validate_parameters(N,language,language,6)) query_6(0,file,language,N,commits,users,repos);
    else error_file(file);
    free(language);
}

/**
 * Reads and validates command line, executing query 7 if valid. 
 */
void call_query7(char *file, char *line, COMMITS commits, REPOS repos){
    strsep (&line, " ");
    char *date_from = strdup(strsep (&line, "\n"));
    if (validate_parameters(1,date_from,date_from,7)) query_7(0,file,date_from,commits,repos);
    else error_file(file);
    free(date_from);
}

/**
 * Reads and validates command line, executing query 8 if valid. 
 */
void call_query8(char *file, char *line, COMMITS commits, REPOS repos){
    strsep (&line, " ");
    int N = is_empty(strsep (&line, " "));
    char *date_from = strdup(strsep (&line, "\n"));
    if (validate_parameters(N,date_from,date_from,8)) query_8(0,file,date_from,N,commits,repos);
    else error_file(file);
    free(date_from);
}

/**
 * Reads and validates command line, executing query 9 if valid. 
 */
void call_query9(char *file, char *line, COMMITS commits, USERS users){
    strsep (&line, " "); 
    int N = is_empty(strsep (&line, " "));
    if (validate_parameters(N,file,line,9)) query_9(0,file,N,commits,users);
    else error_file(file);
}

/**
 * Reads and validates command line, executing query 10 if valid. 
 */
void call_query10(char *file, char *line, COMMITS commits, USERS users){
    strsep (&line, " "); 
    int N = is_empty(strsep (&line, "\n"));
    if (validate_parameters(N,file,line,10)) query_10(0,file,N,commits,users);
    else error_file(file);
}

/**
 * \brief Selects the query to execute.
 */
void choose_query(int indice, char *line, STATS stats, USERS users, COMMITS commits, REPOS repos){
    int id; 
    char file[60];
    if (isdigit(line[1])) { id = (line[0] - 48)*10 + line[1] - 48; }
    else id = line[0] - 48;
    snprintf(file, 60, "saida/command%d_output.txt", indice);
    switch(id){
        case 1: query_1(file, stats); break;
        case 2: query_2(file, stats); break;
        case 3: query_3(file, stats); break;
        case 4: query_4(file, stats); break;
        case 5: call_query5(file, line, commits, users); break;
        case 6: call_query6(file, line, commits, users, repos); break;
        case 7: call_query7(file, line, commits, repos); break;
        case 8: call_query8(file, line, commits, repos); break;
        case 9: call_query9(file, line, commits, users); break;
        case 10: call_query10(file, line, commits, users); break;
        default: error_file(file); break;
    }    
}

/**
 * \brief Parses the commands file.
 */
void queries (STATS stats, USERS users, COMMITS commits, REPOS repos, char *commands){
    char *line = NULL;
    int i=1;
    size_t len = 0;
    FILE *commands_file = fopen(commands, "r");
    while(getline(&line, &len, commands_file) != -1){
        choose_query(i, line, stats, users, commits, repos);
        i++;   
    }
    fclose(commands_file);
}
