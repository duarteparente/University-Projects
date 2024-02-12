/**
 * @file repo.c 
 * \brief Information about a single repository.
 */ 
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "../include/repo.h"
#include "../include/validation.h"

struct one_repo {
    int id;
    int owner_id;
    char *full_name;
    char *license;
    char *has_wiki;
    char *description;
    char *language;
    char *default_branch;
    char *created_at;
    char *updated_at;
    int forks_count;
    int open_issues;
    int stargazers_count;
    int size;
};

/**
 * Creates a new instance of a Repository.
 */
REPO create_repo (char *info){
    REPO r = (REPO) malloc (sizeof(*r));;
    r->id = is_empty (strsep (&info, ";"));
    r->owner_id = is_empty (strsep (&info, ";"));
    r->full_name = strdup (strsep (&info, ";"));
    r->license = strdup (strsep (&info, ";"));
    r->has_wiki = strdup (strsep (&info, ";"));
    r->description = strdup (strsep (&info, ";"));
    r->language = strdup (strsep (&info, ";"));
    r->default_branch = strdup (strsep (&info, ";"));
    r->created_at = strdup (strsep (&info, ";"));
    r->updated_at = strdup (strsep (&info, ";"));
    r->forks_count = is_empty (strsep (&info, ";"));
    r->open_issues = is_empty (strsep (&info, ";"));
    r->stargazers_count = is_empty (strsep (&info, ";"));
    r->size = is_empty (strsep (&info, ";"));
    return r;
};

/**
 * Frees all memory allocated to store the information of a single repository.
 */
void destroy_REPO(REPO repo){
    free(repo->full_name) ;
    free(repo->license);
    free(repo->has_wiki);
    free(repo->description); 
    free(repo->language);
    free(repo->default_branch); 
    free(repo->created_at);
    free(repo->updated_at); 
    free(repo);
}

/**
 * Returns the parameter 'id' of a repository.
 */
int get_id_REPO(REPO repo){
    return repo->id;
}

/**
 * Returns the parameter 'owner_id' of a repository.
 */
int get_owner_id(REPO repo){
    return repo->owner_id;
}

/**
 * Returns a copy of the parameter 'full_name' of a repository
 */
char *get_full_name(REPO repo){
    return strdup(repo->full_name);
}

/**
 * Returns a copy of the parameter 'license' of a repository
 */
char *get_license(REPO repo){
    return strdup(repo->license);
}

/**
 * Returns a copy of the parameter 'has_wiki' of a repository
 */
char *get_has_wiki(REPO repo){
    return strdup(repo->has_wiki);
}

/**
 * Returns a copy of the parameter 'description' of a repository
 */
char *get_description(REPO repo){
    return strdup(repo->description);
}

/**
 * Returns a copy of the parameter 'language' of a repository
 */
char *get_language(REPO repo){
    return strdup(repo->language);
}

/**
 * Returns a copy of the parameter 'default_branch' of a repository
 */
char *get_default_branch(REPO repo){
    return strdup(repo->default_branch);
}

/**
 * Returns a copy of the parameter 'created_at' of a repository
 */
char *get_created_at_REPO(REPO repo){
    return strdup(repo->created_at);
}

/**
 * Returns a copy of the parameter 'updated_at' of a repository
 */
char *get_updated_at_REPO(REPO repo){
    return strdup(repo->updated_at);
}

/**
 * Returns the parameter 'forks_count' of a repository.
 */
int get_forks_count(REPO repo){
    return repo->forks_count;
}

/**
 * Returns the parameter 'open_issues' of a repository.
 */
int get_open_issues(REPO repo){
    return repo->open_issues;
}

/**
 * Returns the parameter 'stargazers_count' of a repository.
 */
int get_stargazers_count(REPO repo){
    return repo->stargazers_count;
}

/**
 * Returns the parameter 'size' of a repository.
 */
int get_size(REPO repo){
    return repo->size;
}

