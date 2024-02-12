#ifndef AUXILIAR_H__
#define AUXILIAR_H__


void swap (int v[], int i, int j);
void heapify(int v[], int N);
void bubbledown(int v[], int N, int i);
void heapSort (int v[], int N);

int binarySearch(int arr[], int l, int r, int x);
int is_elem (int v[], int N, int x);

int convert_date (char *date);
int soma_ASCII (char *s);


void clear_window();
void delete_TestFiles();
void delete_OutputFile();
void delete_commands_file();
void execute_progammer_mode();

void error_file(char *file);


#endif