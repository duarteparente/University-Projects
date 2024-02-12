#ifndef PARAMETERS_H__
#define PARAMETERS_H__

#include "cat_users.h"
#include "cat_commits.h"
#include "cat_repos.h"

/**
 * \brief Structure used to answer Queries 5 to 10.
 * 
 * This structure holds 3 int arrays, all used in different ways, according to what the Querie needed to store and evaluate.
 */
typedef struct  aux_parameters * PARAM;

void heapSort_qtty (PARAM comms_date, int N);
void heapSort_3WAY (PARAM repos_lang, int N);
int posBinarySearch_PARAM(PARAM comms_date, int l, int r, int x);

PARAM create_PARAM();
PARAM create_3WAYPARAM();
void add_id (PARAM comms_date, int id);
void add_3WAY(PARAM topN_msg, int repo_id, int commiter_id, int msg_size);
void destroy_PARAM (PARAM comms_date);
void create_qtty_array (PARAM comms_date, int sorted_flag);
void organize_id (PARAM comms_date);


void fill_commitsByDate (PARAM comms_date, COMMITS commits, char *from, char *to);
void fill_commitsByLang(PARAM comms_date, COMMITS commits, REPOS repos, char *language);
void fill_reposByLang(PARAM no_comms, COMMITS commits, char *date_from);
void fill_Lang(PARAM lang, COMMITS commits, REPOS repos, char *date_from);
void fill_friends(COMMITS commits, USERS users, PARAM friends);


void output_UserLoginQtty(char *file, PARAM comms_date, USERS users,  int N);
void output_UserLoginQtty_UI(char *file, PARAM comms_date, USERS users,  int N);
void output_q7(char *file, PARAM no_comms, REPOS repos);
void output_q7_UI(char *file, PARAM no_comms, REPOS repos);
void output_q8(char *file, PARAM repos_lang, REPOS repos, int N);
void output_q8_UI(char *file, PARAM repos_lang, REPOS repos, int N);
void output_q9(char *file, PARAM comms_date, USERS users, int N);
void output_q9_UI(char *file, PARAM comms_date, USERS users, int N);
void output_q10(char *file, int N, COMMITS commits, USERS users);
void output_q10_UI(char *file, int N, COMMITS commits, USERS users);


#endif