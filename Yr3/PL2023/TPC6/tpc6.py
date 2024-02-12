import ply.lex as lex

tokens = (
    'MULTILINE_COMMENT',
    'INLINE_COMMENT',
    'SEMICOLON',
    'COMMA',
    'INT',
    'NUMBER',
    'FUNCTION',
    'PROGRAM',
    'IF',
    'WHILE',
    'FOR',
    'PRINT',
    'IN',
    'ID',
    'OPEN_BRACE',
    'CLOSE_BRACE',
    'OPEN_BRACKET',
    'CLOSE_BRACKET',
    'OPEN_PARENTHESIS',
    'CLOSE_PARENTHESIS',      
    'ASSIGN',
    'RANGE',
    'MINUS',
    'PLUS',
    'MULTIPLICATION',
    'LT',
    'GT'
)

t_MULTILINE_COMMENT = r'\/\*[\s\S]*\*\/'
t_INLINE_COMMENT = r'(--|\/\/).*'
t_SEMICOLON = r';'
t_COMMA = r'\,'
t_OPEN_BRACE = r'{'
t_CLOSE_BRACE = r'}'
t_OPEN_BRACKET = r'\['
t_CLOSE_BRACKET = r'\]'
t_OPEN_PARENTHESIS = r'\('
t_CLOSE_PARENTHESIS = r'\)'
t_NUMBER = r'\d+'
t_MINUS = r'-'
t_PLUS = r'\+'
t_MULTIPLICATION = r'\*'
t_LT = r'\<'
t_GT = r'\>'
t_ASSIGN = r'\='

def t_FUNCTION(t):
    r'function'
    return t

def t_PROGRAM(t):
    r'program'
    return t

def t_INT(t):
    r'int'
    return t

def t_IF(t):
    r'if'
    return t

def t_WHILE(t):
    r'while'
    return t

def t_FOR(t):
    r'for'
    return t

def t_IN(t):
    r'in'
    return t

def t_PRINT(t):
    r'print'
    return t

def t_RANGE(t):
    r'\[\d+..\d+\]'
    return t

def t_ID(t):
    r'[aA-zZ_]+'
    return t
    
t_ignore = ' \t\n'

def t_error(t):
    print(f'Caracter ilegal: {t.value}')
    t.lexer.skip(1)

lexer = lex.lex()

print('/*  Exemplo 1  */\n')

lexer.input(open("data1.txt").read())
while tok := lexer.token():
    print(tok)
        
print('\n/*  Exemplo 2  */\n')

lexer.input(open("data2.txt").read())
while tok := lexer.token():
    print(tok)