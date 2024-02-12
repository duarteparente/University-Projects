#ifndef CAT_USERS_H__
#define CAT_USERS_H__

#include "user.h"

/**
 * \brief Structure used to store a catalogue of Users.
 */
typedef struct vetor_users * USERS;

USERS create_users();
void add_user(USER user, USERS users);
void destroy_USERS(USERS users);
void heapSort_USERS (USERS users, int N);
void sort_USER_ids (USERS users);
int posBinarySearch_Users(USERS users, int l, int r, int x);

int get_lenght_users(USERS users);
int get_followers_users(USERS users, int i);
int *get_users_followersList (USERS users, int i);
int get_following_users(USERS users, int i);
int *get_users_followingList (USERS users, int i);
char *get_login_users (USERS users, int i);


#endif