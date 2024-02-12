/**
 * @file cat_users.c 
 * \brief Users catalogue.
 */ 
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "../include/cat_users.h"


struct vetor_users {
    USER *user;   /**< Users Array */
    int lenght;
    int size;
};

/**
 * Creates a new instance of a Users catalogue.
 */
USERS create_users(){
    USERS users = calloc(1, sizeof(*users));
    users->size = 5000;
    users->user = calloc(users->size, sizeof(users->user));
    return users;
}

/**
 * Stores a new user in the catalogue and stores its id.
 */
void add_user(USER user, USERS users){
    if(users->size == users->lenght){
        users->size*=2;
        users->user = realloc(users->user, users->size*sizeof(users->user));
    }
    users->user[users->lenght++] = user;
}

/**
 * Frees all memory allocated in the users catalogue.
 */
void destroy_USERS(USERS users){
    for(int i=0; i<users->lenght; i++){
        destroy_USER(users->user[i]);
    }  
    free(users);
}

/**
 * Returns the number of users stored in the catalogue.
 */
int get_lenght_users(USERS users){
    return users->lenght;
}

/**
 * Returns an array with the follower's ids of an user.
 */
int *get_users_followersList (USERS users, int i){
    return get_follower_list(users->user[i]);
}

/**
 * Returns the parameter 'followers' of an user.
 */
int get_followers_users(USERS users, int i){
    return get_followers(users->user[i]);
}

/**
 * Returns an array with the following's ids of an user.
 */
int *get_users_followingList (USERS users, int i){
    return get_following_list(users->user[i]);
}

/**
 * Returns the parameter 'following' of an user.
 */
int get_following_users(USERS users, int i){
    return get_following(users->user[i]);
}

/**
 * Returns a copy of the parameter 'login' of an user.
 */
char *get_login_users (USERS users, int i){
    return get_login(users->user[i]);
}

/**
 * Swap the position of two users in the catalogue.
 */
void swap_USERS (USERS users, int i, int j){
    USER temp = users->user[i];
    users->user[i] = users->user[j];
    users->user[j] = temp;
}

/**
 * HeapSort's bubbleDown function.
 * 
 * This function sorts the users catalogue in descending order by the parameter 'id'. 
 */
void bubbleDown_USERS(USERS users, int N, int i){
    int f = 2*i + 1;
    while(f<N){
        if (f+1 < N && get_user_id(users->user[f+1]) < get_user_id(users->user[f])) f++; // right descendent < left descendent
        if (get_user_id(users->user[f]) > get_user_id(users->user[i])) break;
        swap_USERS (users,i,f);
        i=f; f = 2*i + 1; 
    }
}

/**
 * Converts the users catalogue into an Heap catalogue. 
 */
void heapify_USERS (USERS users, int N){
    int i;
    for(i = (N-2)/2; i>=0; i--) bubbleDown_USERS(users,N,i);
}

/**
 * Sorts a Heap users catalogue in descending order, by 'id'.
 */
void heapSort_USERS (USERS users, int N){
    heapify_USERS(users,N);
    while(--N > 0){
        swap_USERS (users,0,N);
        bubbleDown_USERS(users,N,0);
    }
}


/**
 * BinarySearch for a user in a users catalogue, by the parameter 'id'.
 * 
 * @return The index where the user is stored.
 */
int posBinarySearch_Users(USERS users, int l, int r, int x){
    if (r >= l) {
        int mid = l + (r - l) / 2;
        if (get_user_id(users->user[mid]) == x)
            return mid;
        if (get_user_id(users->user[mid]) < x)
            return posBinarySearch_Users(users, l, mid - 1, x);
        return posBinarySearch_Users(users, mid + 1, r, x);
    }
    return -1;
}
