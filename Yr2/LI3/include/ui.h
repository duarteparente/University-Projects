#ifndef UI_H__
#define UI_H__

#include "cat_users.h"
#include "cat_commits.h"
#include "cat_repos.h"
#include "stats.h"
#include "pages.h"
#include "parameters.h"




void query1_UI(STATS stats, USERS users, COMMITS commits, REPOS repos);
void query2_UI(STATS stats, USERS users, COMMITS commits, REPOS repos);
void query3_UI(STATS stats, USERS users, COMMITS commits, REPOS repos);
void query4_UI(STATS stats, USERS users, COMMITS commits, REPOS repos);
void query5_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos);
void query6_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos);
void query7_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos);
void query8_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos);
void query9_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos);
void query10_UI(char *file, STATS stats, USERS users, COMMITS commits, REPOS repos);

void parameters_error(int query_id, STATS stats, USERS users, COMMITS commits, REPOS repos);
int jump_to ();
char *get_option();
int get_N_param(int query_id);
char *get_lang_param();
char *get_date_inicio(int query_id);
char *get_date_fim();
int get_inputQuery();

void init_output(int query_id);
void show_page_number(int page, int page_length);
void show_legenda(int query_id, int page_number, PAGES pages);
void show_postOutput(int query_id, int current_page, int total_pages);
void end_output_1page(STATS stats, USERS users, COMMITS commits, REPOS repos);
void page_controller(int page_number, int query_id, PAGES pages, STATS stats, USERS users, COMMITS commits, REPOS repos);
void input_controller(STATS stats, USERS users, COMMITS commits, REPOS repos);
void choose_querieUI(int query_id, STATS stats, USERS users, COMMITS commits, REPOS repos);
void parameter_query_UI(int query_id, STATS stats, USERS users, COMMITS commits, REPOS repos);
void display_Menu();
void load_files();


#endif