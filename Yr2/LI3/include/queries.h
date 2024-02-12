#ifndef QUERIES_H__
#define QUERIES_H__

#include "cat_users.h"
#include "cat_commits.h"
#include "cat_repos.h"
#include "stats.h"

void queries (STATS stats, USERS users, COMMITS commits, REPOS repos, char *commands);
void choose_query(int indice, char *line, STATS stats, USERS users, COMMITS commits, REPOS repos);

void call_query5(char *file, char *line, COMMITS commits, USERS users);
void call_query6(char *file, char *line, COMMITS commits, USERS users, REPOS repos);
void call_query7(char *file, char *line, COMMITS commits, REPOS repos);
void call_query8(char *file, char *line, COMMITS commits, REPOS repos);
void call_query9(char *file, char *line, COMMITS commits, USERS users);
void call_query10(char *file, char *line, COMMITS commits, USERS users);

void query_1(char *file, STATS stats);
void query_2(char *file, STATS stats);
void query_3(char *file, STATS stats);
void query_4(char *file, STATS stats);
void query_5(int UI_flag, char *file, char *date_to, char *date_from, int N, COMMITS commits, USERS users);
void query_6(int UI_flag, char *file, char *language, int N, COMMITS commits, USERS users, REPOS repos);
void query_7(int UI_flag, char *file, char *date_from, COMMITS commits, REPOS repos);
void query_8(int UI_flag, char *file, char *date_from, int N, COMMITS commits, REPOS repos);
void query_9(int UI_flag, char *file, int N, COMMITS commits, USERS users);
void query_10(int UI_flag, char *file, int N, COMMITS commits, USERS users);

#endif