/**
 * @file stats.c 
 * \brief Holding statistics.
 */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "../include/stats.h"
#include "../include/auxiliar.h"


struct statistics {
    int bot_count;
    int user_count;
    int org_count;
    int *bot_id;
    int lenght_bot_id;
    int size_bot_id;
    int repos_w_bots;
    int users_lenght;
    int commits_lenght;
    int repos_lenght;
    int colaboradores;
    int prev_repo_id;
    int prev_author_id;
    int prev_commiter_id;
};

/**
 * Creates a new instance of a Statistics struct.
 */
STATS create_STATS() {
    STATS stats = calloc(1, sizeof(*stats));
    stats->size_bot_id = 2;
    stats->bot_id = calloc(stats->size_bot_id, sizeof(int));
    return stats;
}

/**
 * Frees all memory allocated in the statistics struct.
 */
void destroy_STATS (STATS stats){
    free(stats->bot_id);
    free(stats);
}

/**
 * Sorts 'bot_id' array in ascending order.
 */
void ordena_bot_id(STATS stats){
    heapSort(stats->bot_id, stats->lenght_bot_id);
}

/**
 * Adds a new id to the 'bot_id' array in the statistics struct.
 */
void add_bot_id (int bot_id, STATS stats){
    if(stats->size_bot_id == stats->lenght_bot_id){
        stats->size_bot_id*=2;
        stats->bot_id = realloc(stats->bot_id, stats->size_bot_id*sizeof(int));
    }
    stats->bot_id[stats->lenght_bot_id] = bot_id;
    stats->lenght_bot_id++;
}

/**
 * Checks if a contributor in a repository is a Bot
 */
void is_elem_bot_id (int author_id, int commiter_id, STATS stats){
    int r1 = binarySearch(stats->bot_id, 0, stats->lenght_bot_id, author_id);
    int r2 = binarySearch(stats->bot_id, 0, stats->lenght_bot_id, commiter_id);
    if (author_id == commiter_id) stats->repos_w_bots += r1;
    else stats->repos_w_bots += (r1 + r2);
}

/**
 * Checks the number os contributers and updates its counter in accordance.
 */
void update_commits_STATS(int author_id, int commiter_id, int repo_id, STATS stats){
    if(!stats->commits_lenght){
        if(author_id == commiter_id)  stats->colaboradores++;
        else stats->colaboradores+=2;
        stats->prev_repo_id = repo_id;
        stats->prev_author_id = author_id;
        stats->prev_commiter_id = commiter_id;
        is_elem_bot_id(author_id, commiter_id, stats);
    }
    else {
        if(repo_id != stats->prev_repo_id) is_elem_bot_id(author_id, commiter_id, stats);
        if(stats->prev_commiter_id != commiter_id)  stats->colaboradores++;
        stats->prev_author_id = author_id;
        stats->prev_commiter_id = commiter_id;
    }
} 

/**
 * Returns the parameter 'bot_count' of the statistics struct.
 */
int get_bot_count(STATS stats){
    return stats->bot_count;
}

/**
 * Returns the parameter 'org_count' of the statistics struct.
 */
int get_org_count(STATS stats){
    return stats->org_count;
}

/**
 * Returns the parameter 'user_count' of the statistics struct.
 */
int get_user_count(STATS stats){
    return stats->user_count;
}

/**
 * Returns the parameter 'colaboradores' of the statistics struct.
 */
int get_colaboradores(STATS stats){
    return stats->colaboradores;
}

/**
 * Returns the parameter 'repos_w_bots' of the statistics struct.
 */
int get_repos_w_bot(STATS stats){
    return stats->repos_w_bots;
}

/**
 * Returns the parameter 'users_lenght' of the statistics struct.
 */
int get_ul_stats(STATS stats){
    return stats->users_lenght;
}

/**
 * Returns the parameter 'commits_length' of the statistics struct.
 */
int get_cl_stats(STATS stats){
    return stats->commits_lenght;
}

/**
 * Returns the parameter 'repos_lenght' of the statistics struct.
 */
int get_rl_stats(STATS stats){
    return stats->repos_lenght;
}

/**
 * Checks the type of an user and updates the respective counter.
 */
void update_counters(STATS stats, int id, char *type){
    if (strcmp(type,"User") == 0) stats->user_count++;
    else if (strcmp(type,"Organization") == 0) stats->org_count++;
    else { 
        stats->bot_count++;
        add_bot_id(id,stats);
    }
}

/**
 * Increments the counter 'users_lenght'.
 */
void update_users_lenght(STATS stats){
    stats->users_lenght++;
}

/**
 * Increments the counter 'commits_lenght'.
 */
void update_commits_lenght(STATS stats){
    stats->commits_lenght++;
}

/**
 * Increments the counter 'repos_lenght'.
 */
void update_repos_lenght(STATS stats){
    stats->repos_lenght++;
}

