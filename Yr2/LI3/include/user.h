#ifndef USER_H__
#define USER_H__

/**
 * \brief Structure used to store the information of one single User.
 */
typedef struct one_user * USER;

USER create_user (char *info);
void destroy_USER(USER user);

char *get_type(USER user);
char *get_login(USER user);
char *get_created_at(USER user);
int get_user_id(USER user);
int get_public_gists(USER user);
int get_public_repos(USER user);
int get_followers(USER user);
int get_following(USER user);
char *get_String_follower_list(USER user);
int *get_follower_list(USER user);
char *get_String_following_list(USER user);
int *get_following_list(USER user);

#endif 
