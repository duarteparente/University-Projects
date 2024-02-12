#ifndef STATS_H__
#define STATS_H__


/**
 * \brief Structure used to store the values of statistic values needed to answer Queries 1 to 4.
 * 
 * This structure holds values calculated as the program executes the parsing of the files,
 * 
 */
typedef struct statistics * STATS;

STATS create_STATS();
void destroy_STATS (STATS stats);

void ordena_bot_id(STATS stats);
void add_bot_id (int bot_id, STATS stats);
void update_commits_STATS(int author_id, int commiter_id, int repo_id, STATS stats);
void update_counters(STATS stats, int id, char *type);
void update_users_lenght(STATS stats);
void update_commits_lenght(STATS stats);
void update_repos_lenght(STATS stats);

int get_bot_count(STATS stats);
int get_org_count(STATS stats);
int get_user_count(STATS stats);
int get_colaboradores(STATS stats);
int get_repos_w_bot(STATS stats);
int get_ul_stats(STATS stats);
int get_cl_stats(STATS stats);
int get_rl_stats(STATS stats);


#endif