import sys
import re
import ply.yacc as yacc
from conversor_lex import tokens


def print_indent(ind):
    return " " * ind


def p_pug(p):
    'pug : content'
    p[1] = p[1][:-1]
    p[0] = p[1]


def p_content(p):
    '''content : content elem
                | '''
    if len(p) == 1:
        p[0] = ""
    else:
        p[0] = p[1] + p[2]
    

def p_elem(p):
    '''elem : TAG attributes text INDENT content DEDENT
            | TAG attributes text INDENT content
            | TAG attributes text '''
    parts = p[1].split(':', 1)
    if len(p) == 4:
        p[0]  = f"{print_indent(int(parts[0]))}<{parts[1]}{p[2]}>"
        if parts[1] == 'script':
            p[0] += '\n' + f"{print_indent(int(parts[0])+4)}"
        p[0] += f"{p[3]}"
        if p[0][-1] == '\n':
            p[0] = p[0][:-1]
        p[0] += f"</{parts[1]}>\n"
    else:
        p[0] = f"{print_indent(int(parts[0]))}<{parts[1]}{p[2]}>{p[3]}\n{p[5]}{print_indent(int(parts[0]))}</{parts[1]}>\n"


def p_elem_doctype(p):
    'elem : DOCTYPE'
    p[0] = f"<!DOCTYPE {p[1]}>\n\n"


def p_elem_comment(p):
    'elem : COMMENT'
    parts = p[1].split(':', 1)
    p[0] = f"{print_indent(int(parts[0]))}<!--{parts[1]}-->\n"


def p_elem_self_closing(p):
    '''elem : SELF_CLOSING attributes '''
    parts = p[1].split(':', 1)
    p[0] = f"{print_indent(int(parts[0]))}<{parts[1]}{p[2]} \>\n"


def p_attributes_attributes(p):
    '''attributes : ATTRIBUTES
                  | '''
    if len(p) == 1:
        p[0] = ""
    else:
        p[1] = p[1].replace("\n", "")
        p[1] = p[1].replace(" ", "")
        p[0] = " " + p[1]


def p_attributes_literal(p):
    '''attributes : class_b ID
                  | class_b'''
    if len(p) == 2:
        p[0] = f' class="{p[1][:-1]}"'
    else:
        p[0] = f' class="{p[1][:-1]}" id="{p[2]}"'


def p_attributes_id(p):
    '''attributes : ID
                  | ID class_b'''
    if len(p) == 2:
        p[0] = f' id="{p[1]}"'
    else:
        p[0] = f' class="{p[2][:-1]}" id="{p[1]}"'


def p_class_b(p):
    '''class_b : CLASS_B class_b
               | '''
    if len(p) == 1:
        p[0] = ""
    else:
        p[0] = f"{p[1]} " + p[2]
    
    
def p_text(p):
    '''text : TEXT text
            | '''
    if len(p) == 1:
        p[0] = ""
    else:
        p[0] = p[1] + p[2]


def p_error(p):
    print("Syntax error in input: ", p)

parser = yacc.yacc()

if len(sys.argv) != 2:
    print('Wrong arguments')
else:
    with open(sys.argv[1]) as f:
        content = f.read()
    m = re.sub('([\w\-\_]+)\.(\w+)', r'\1.html', sys.argv[1])
    with open(m, 'w') as f:
        f.write(parser.parse(content))