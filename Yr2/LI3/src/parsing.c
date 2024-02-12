#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "../include/user.h"
#include "../include/commit.h"
#include "../include/repo.h"
#include "../include/validation.h"
#include "../include/queries.h"
#include "../include/parsing.h"


/**
 * \brief Parses and apply first validation method to the file containing users information.
 * 
 * Creates a catalogue with all valid users stored.
 */
USERS read_users(STATS stats){
    char *line = NULL;
    size_t len = 0;
    USERS users = create_users();
    FILE *users_file = fopen("entrada/users.csv", "r");
    getline(&line, &len, users_file);
    while(getline(&line, &len, users_file) != -1){
        USER user = create_user(line);
        if (is_valid_user(user)) {
            add_user(user, users);
            update_users_lenght(stats);
            update_counters(stats, get_user_id(user), get_type(user));
        }    
    }
    heapSort_USERS(users,get_lenght_users(users));
    ordena_bot_id(stats);
    free(line);
    fclose(users_file);
    return users;   
}


/**
 * \brief Parses the file containing commits information.
 * 
 * Returns a catalogue with all commits stored, and fills statistic's struct as the file is being parsed.
 */
COMMITS read_commits(STATS stats, USERS users, REPOS repos_1valid){
    char *line = NULL;
    size_t len = 0;
    COMMITS commits = create_commits();
    FILE *commits_file = fopen("entrada/commits.csv", "r");
    getline(&line, &len, commits_file);
    while(getline(&line, &len, commits_file) != -1){
        COMMIT commit = create_commit(line);
        if (is_valid_commit(commit) && existing_user(get_commiter_id(commit), get_author_id(commit), users) && existing_repo(get_repo_id(commit), repos_1valid)){
            add_commit(commit, commits);
            update_commits_STATS(get_author_id(commit), get_commiter_id(commit), get_repo_id(commit), stats);
            update_commits_lenght(stats);
        }
    }
    free(line);
    fclose(commits_file);
    heapSort_COMMITS(commits, get_lenght_commits(commits));
    return commits;
}


/**
 * \brief Parses and apply first validation method to the file containing repositories information.
 * 
 * Creates a catalogue with all valid repositories stored.
 */
REPOS validate_fields_repos(){
    char *line = NULL;
    size_t len = 0;
    REPOS repos = create_repos();
    FILE *repos_file = fopen("entrada/repos.csv", "r");
    getline(&line, &len, repos_file);
    while(getline(&line, &len, repos_file) != -1){
        REPO repo = create_repo(line);
        if (is_valid_repo(repo)) add_repo(repo, repos);
    }
    free(line);
    heapSort_REPOS(repos,get_lenght_repos(repos));
    fclose(repos_file);
    return repos;
}

/**
 * \brief Parses the file containing repositories information.
 * 
 * Returns a catalogue with all repositories stored, and fills statistic's struct as the file is being parsed.
 */
REPOS read_repos(STATS stats, REPOS repos_valid, USERS users, COMMITS commits){
    REPOS repos = create_repos();
    for(int i=0; i<get_lenght_repos(repos_valid); i++){
        if(existing_elems(get_repos_owner_id(repos_valid,i), get_repos_id(repos_valid,i), users, commits)){
            update_repos_lenght(stats);
            move_repo(i,repos_valid,repos);
        }
    }
    return repos;
}

/**
 * \brief Main function for parsing the files.
 *
 * This function handles the parsing of the 3 files as well as calling the queries execution. 
 */
void parsing(char *commands){
    STATS stats = create_STATS();
    USERS users = read_users(stats);
    REPOS repos_valid = validate_fields_repos();
    COMMITS commits = read_commits(stats,users,repos_valid);
    REPOS repos = read_repos(stats,repos_valid,users,commits);
    queries(stats, users, commits, repos, commands);
    destroy_USERS(users);
    destroy_COMMITS(commits);
    destroy_REPOS(repos);
    destroy_STATS(stats);
}


