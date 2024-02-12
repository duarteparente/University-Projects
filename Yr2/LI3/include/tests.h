#ifndef TESTS_H__
#define TESTS_H__

#include "../include/cat_users.h"
#include "../include/cat_commits.h"
#include "../include/cat_repos.h"
#include "../include/stats.h"


double query_1_time(char *file, STATS stats);
double query_2_time(char *file, STATS stats);
double query_3_time(char *file, STATS stats);
double query_4_time(char *file, STATS stats);
double query_5_time(char *file, char *date_to, char *date_from, int N, COMMITS commits, USERS users);
double query_6_time(char *file, char *language, int N, COMMITS commits, USERS users, REPOS repos);
double query_7_time(char *file, char *date_from, COMMITS commits, REPOS repos);
double query_8_time(char *file, char *date_from, int N, COMMITS commits, REPOS repos);
double query_9_time(char *file, int N, COMMITS commits, USERS users);
double query_10_time(char *file, int N , COMMITS commits, USERS users);

double choose_time_query_op1(int i, STATS stats, USERS users, COMMITS commits, REPOS repos);
double choose_time_query_op2(int i, STATS stats, USERS users, COMMITS commits, REPOS repos);
double choose_time_query_op3(int i, STATS stats, USERS users, COMMITS commits, REPOS repos);
double choose_time_query_op4(int i, STATS stats, USERS users, COMMITS commits, REPOS repos);
double choose_time_query_op5(int i, STATS stats, USERS users, COMMITS commits, REPOS repos);
double choose_time_query_op6(int i, STATS stats, USERS users, COMMITS commits, REPOS repos);
void fill_time_op1(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos);
void fill_time_op2(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos);
void fill_time_op3(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos);
void fill_time_op4(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos);
void fill_time_op5(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos);
void fill_time_op6(double *tests, STATS stats, USERS users, COMMITS commits, REPOS repos);
void tests_controller(int test_option, double load_time, STATS stats, USERS users, COMMITS commits, REPOS repos);
void compare_files_controller(double *tests);
int compare_files(int query_id);
void create_expected_files(STATS stats, USERS users, COMMITS commits, REPOS repos, int test_option);
void create_commands_file(int test_option);
void print_ResultsTable (int test_option, double load_time, double *time);

#endif
