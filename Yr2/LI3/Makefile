#===================================================================
CC = gcc
CFLAGS = -Wall -Wextra -g
#===================================================================
INCLUDES = -Iinclude
LIBS = -lm
SRC_TEST = $(wildcard src/[!m]*.c)
SRC = $(wildcard src/[!t]*.c)
OBJ = obj
OBJS = $(patsubst src/%.c,$(OBJ)/%.o,$(SRC))
OBJS_TEST = $(patsubst src/%.c,$(OBJ)/%.o,$(SRC_TEST))
#===================================================================
CREATE = mkdir -p
REMOVE = rm -r
OUTPUT = saida
#===================================================================
MAIN = program
TESTS = tests
#===================================================================

.DEFAULT_GOAL = build

build: setup $(MAIN) $(TESTS)

$(MAIN): $(OBJS)
	$(CC) $(INCLUDES) $(CFLAGS) -o $@ $^

$(TESTS): $(OBJS_TEST)
	$(CC) $(INCLUDES) $(CFLAGS) -o $@ $^

$(OBJ)/%.o:	src/%.c
	$(CC) -c $(INCLUDES) $(CFLAGS) -o $@ $^		


.PHONY: setup
setup:
	$(CREATE) $(OUTPUT)

.PHONY: clean
clean:
	$(REMOVE) $(OUTPUT)/
	$(RM) $(OBJ)/*.o
	$(RM) $(MAIN)
	$(RM) $(TESTS)


