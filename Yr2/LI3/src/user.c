/**
 * @file user.c 
 * \brief Information about a single user.
 */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "../include/user.h"
#include "../include/validation.h"

struct one_user {
    int id;
    char *login;
    char *type;
    char *created_at;
    int followers;
    char *follower_list;
    int following;
    char *following_list;
    int public_gists;
    int public_repos;
};


/**
 * Creates a new instance of a User.
 */
USER create_user (char *info){
    USER u = (USER) malloc (sizeof(*u));
    u->id = is_empty (strsep (&info, ";"));
    u->login = strdup (strsep (&info, ";"));
    u->type = strdup (strsep (&info, ";"));
    u->created_at = strdup (strsep (&info, ";"));
    u->followers = is_empty (strsep (&info, ";"));
    u->follower_list = strdup (strsep (&info, ";"));
    u->following = is_empty (strsep (&info, ";"));
    u->following_list = strdup (strsep (&info, ";"));
    u->public_gists = is_empty (strsep (&info, ";"));
    u->public_repos = is_empty (strsep (&info, ";"));
    return u;
};

/**
 * Frees all memory allocated to store the information of a single user.
 */
void destroy_USER(USER user){
    free(user->login);
    free(user->type);
    free(user->created_at);
    free(user->follower_list);
    free(user->following_list);
    free(user);
}

/**
 * Returns a copy of the parameter 'type' of an user.
 */
char *get_type(USER user){
    return strdup(user->type);
}

/**
 * Returns a copy of the parameter 'login' of an user.
 */
char *get_login(USER user){
    return strdup(user->login);
}

/**
 * Returns a copy of the parameter 'created_at' of an user.
 */
char *get_created_at(USER user){
    return strdup(user->created_at);
}

/**
 * Returns the parameter 'id' of an user.
 */
int get_user_id(USER user){
    return user->id;
}

/**
 * Returns the parameter 'public_gists' of an user.
 */
int get_public_gists(USER user){
    return user->public_gists;
}

/**
 * Returns the parameter 'public_repos' of an user.
 */
int get_public_repos(USER user){
    return user->public_repos;
}

/**
 * Returns the parameter 'followers' of an user.
 */
int get_followers(USER user){
    return user->followers;
}

/**
 * Returns the parameter 'following' of an user.
 */
int get_following(USER user){
    return user->following;
}

/**
 * Returns a copy of the parameter 'follower_list' of an user.
 */
char *get_String_follower_list(USER user){
    return strdup(user->follower_list);
}

/**
 * Returns an array with the follower's ids of an user.
 */
int *get_follower_list(USER user){
    int i = 0;
    int *followers = calloc(user->followers, sizeof(int));
    char *str = strdup(user->follower_list);
    while(i < user->followers){
        strsep(&str, "[] ,");
        followers[i] = atoi(strsep(&str, "[] ,"));
        i++;
    }
    return followers;
}

/**
 * Returns a copy of the parameter 'following_list' of an user.
 */
char *get_String_following_list(USER user){
    return strdup(user->following_list);
}

/**
 * Returns an array with the following's ids of an user.
 */
int *get_following_list(USER user){
    int i = 0;
    int *following = calloc(user->following, sizeof(int));
    char *str = strdup(user->following_list);
    while(i < user->following){
        strsep(&str, "[] ,");
        following[i] = atoi(strsep(&str, "[] ,"));
        i++;
    }
    return following;
}
