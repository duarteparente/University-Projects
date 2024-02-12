/**
 * @file auxiliar.c 
 * \brief Project auxiliary functions.
 */ 
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#define __USE_XOPEN
#include <time.h>
#include "../include/auxiliar.h"

/**
 * \brief Swap elements in an integer array
 */
void swap (int v[], int i, int j){
    int tmp;
    tmp = v[i];
    v[i] = v [j];
    v[j] = tmp;
}

/**
 * Bubbledown funtion
 */
void bubbledown(int v[], int N, int i){
    int f = 2*i + 1;
    while(f<N){
        if(f+1 < N && v[f+1] < v[f]) f++;
        if (v[f] > v[i]) break;
        swap(v,i,f);
        i = f; f = 2*i +1;
    }
}

/**
 * Transformation into an heap array
 */
void heapify(int v[], int N){
    int i;
    for(i=(N-2)/2; i>=0; i--)
        bubbledown(v,N,i);
}

/**
 * HeapSort funtion
 */
void heapSort (int v[], int N){
    heapify(v,N);
    while(--N > 0){
        swap(v,0,N);
        bubbledown(v,N,0);
    }
}

/**
 * \brief Binary Search algorithm in a sorted array (increasing order)
 * 
 * @return Boolen value.
 */
int binarySearch(int arr[], int l, int r, int x){
    if (r >= l) {
        int mid = l + (r - l) / 2;
        if (arr[mid] == x)
            return 1;
        if (arr[mid] > x)
            return binarySearch(arr, l, mid - 1, x);
        return binarySearch(arr, mid + 1, r, x);
    }
    return 0;
}

/**
 * \brief Converts a date into the corresponding integer
 * 
 * Example: 2013-08-31 -> 20130831 
 */
int convert_date (char *date){
    struct tm time = {0};
    strptime(date, "%Y-%m-%d %H:%M:%S" , &time);
    int dt = (time.tm_year+1900)*10000 + (time.tm_mon+1)*100 + time.tm_mday;
    return dt;
}

/**
 * \brief Returns the total sum of all ASCII values of a given string (case insensite) 
 */
int soma_ASCII (char *s){
    int sum = 0;
    size_t len = strlen(s);
    for(size_t i=0; i<len; i++)
        sum += tolower((unsigned char) s[i]);
    return sum;
}

/**
 * \brief Simple Linear Search 
 */
int is_elem (int v[], int N, int x){
    int r = 0;
    for(int i=0; i<N; i++) {
        if (v[i]==x) { r=1; break; }
    }
    return r;
}    


/**
 * \brief Clear terminal window 
 */
void clear_window() {
    system("clear");
}

/**
 * \brief Delete query UI output file 
 */
void delete_OutputFile() {
    system("rm saida/ui_output.csv");
}

/**
 * \brief Delete query output file used in tests module 
 */
void delete_TestFiles(){
    system("rm saida/*.txt");
}

/**
 * \brief Delete commands file used in tests module
 */
void delete_commands_file(){
    system("rm entrada/test_commands.txt");
}

/**
 * \brief Delete commands file used in tests module
 */
void execute_progammer_mode(){
    system("./program entrada/test_commands.txt");
}

/**
 * File created when a command isn't valid.
 */
void error_file(char *file){
    FILE *error = fopen(file, "a");
    fprintf(error,"Comando inválido!");
    fclose(error);
}
