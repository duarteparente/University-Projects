import re
import ply.lex as lex

# List of token names
tokens = (
    'TAG',
    'INDENT',
    'DEDENT',
    'COMMENT',
    'TEXT',
    'ATTRIBUTES',
    'CLASS_B',
    "ID", 
    'DOCTYPE',
    'SELF_CLOSING',
    'COMMENT_PUG',
    'VAR',
    'LIST',
    'CODE',
    'CONDITION',
    'COND_CONTENT',
    'BLOCK'
)

# List of state names
states = (
    ('code', 'exclusive'),
    ('if', 'exclusive'),
    ('unless', 'exclusive'),
    ('else', 'exclusive'),
    ('block', 'exclusive'),
)

# List of Pug reserved keywords
reserved = ['if', 'else', 'unless']

# HTML special characters
reserved_char = {
    '"' : '&quot;',
    "'" : '&apos;',
    '&' : '&amp;',
    '<' : '&lt;',
    '>' : '&gt;'
}

# List of HTML closing tags
self_closing = ['area', 'base', 'br', 'col', 'embed', 'hr', 'img', 'input', 'link', 'meta', 'param', 'source', 'track', 'wbr']

cond_flag = False    # Conditional Flow Controller
line_block = 0       # Block Line Controller
indent_level = 0     # Indent Level Controller


def t_CODE(t):
    r'-[ \n]*var[ ]*'
    t.lexer.begin('code')
    pass

# ==================  CODE =======================

def t_code_LIST(t):
    r'\w+[ \n]*\=[ \n]*\[[^\]]+\]'
    m = re.match(r'(\w+)[ \n]*\=[ \n]*\[([^\]]+)\]', t.value)
    values = m.group(2).split(', ')
    res = []
    for i in values:
        if i[0] in ['"',"'"]:
            i = i[1:-1]
        res.append(i)
    t.lexer.variables[m.group(1)] = res
    t.lexer.begin('INITIAL')
    pass


def t_code_VAR(t):
    r'\w+[ \n]*\=[ \n]*[^\n]+'
    m = re.match(r'(\w+)[ \n]*\=[ \n]*([^\n]+)', t.value)
    value = m.group(2)
    if value[0] in ['"', "'"]:
        value = value[1:-1]
    elif m.group(2).isnumeric():
        value = int(value)
    t.lexer.variables[m.group(1)] = value
    t.lexer.begin('INITIAL')
    pass

# ================================================

def t_DOCTYPE(t):
    r'doctype[ ]*[^\n]+'
    t.value = re.sub(r'doctype[ ]*', '', t.value)
    return t


def t_ID(t):
    r'\#\w+-*\w*'
    t.value = t.value[1:]
    return t


def t_ATTRIBUTES(t):
    r'\([^)]+\)'
    t.value = t.value.replace(',', '')
    t.value = t.value[1:-1]
    return t


def t_CLASS_B(t):
    r"\.[a-zA-Z0-9_-]+"
    t.value = t.value[1:]
    return t
    

def t_TAG_ATTRIBUTION(t):
    r'!?=[ ]*[^\n]+'
    m = re.search('!?=[ ]*([^\n]+)', t.value)
    t.type = 'TEXT'
    if m.group(1)[0] in ['"', "'"]:
        pin = t.value
        t.value = m.group(1)[1:-1]
        if pin[0] != '!':
            t.value = m.group(1)[1:-1]
            l = re.findall('(\'|"|&|<|>)', t.value)
            for i in l:
                t.value = t.value.replace(i,reserved_char[i])
    else:
        t.value = ""
        variable = m.group(1).split('[')
        if variable[0] in t.lexer.variables:
            pin = t.lexer.variables[variable[0]]
            if len(variable) != 1:
                t.value = str(pin[int(variable[1][0])])
            elif type(pin) == list:
                for i in range(0,len(pin)-1):
                    t.value += str(pin[i]) + ','
                t.value += str(pin[len(pin)-1])
            else:
                t.value = str(pin)
    return t


def t_TAG(t):
    r'[a-z][\w\-]*'
    global cond_flag
    if t.value in reserved:
        if t.value == 'if' or t.value == 'unless':
            cond_flag = False
        t.lexer.begin(t.value)
        pass
    else:
        if t.value in self_closing:
            t.type = 'SELF_CLOSING' 
        t.value = str(indent_level) + ":" + t.value
        return t

# ==================   IF  =======================

def t_if_CONDITION(t):
    r'[ ]+[^\n]+\n[ ]*'
    global cond_flag
    if cond_flag != True:
        t.value = t.value.replace("\n","")
        t.value = t.value.replace(" ","")
        holder = t.value
        index = 0
        if '[' in t.value:
            holder, index = t.value.split('[')
            index = int(index[:-1])
        if holder in t.lexer.variables:
            if type(t.lexer.variables[holder]) == list:
                pin = t.lexer.variables[holder][index]
            else:
                pin = t.lexer.variables[holder]
            if pin != 'false':
                cond_flag = True
                t.lexer.begin('INITIAL')
        pass
    else:
        pass


def t_if_COND_CONTENT(t):
    r'[^\n]+'
    t.lexer.begin("INITIAL")
    pass

# ================================================

# ==================  ELSE =======================

def t_else_CONDITION(t):
    r'\sif'
    t.lexer.begin('if')
    pass


def t_else_final(t):
    r'\n[ ]*'
    global cond_flag
    if cond_flag != True:
        t.lexer.begin('INITIAL')
    pass


def t_else_COND_CONTENT(t):
    r'[^\n]+'
    t.lexer.begin("INITIAL")
    pass

# ================================================

# ================== UNLESS ======================

def t_unless_CONDITION(t):
    r'[ ]+[^\n]+\n[ ]*'
    global cond_flag
    if cond_flag != True:
        t.value = t.value.replace("\n","")
        t.value = t.value.replace(" ","")
        holder = t.value
        index = 0
        if '[' in t.value:
            holder, index = t.value.split('[')
            index = int(index[:-1])
        if holder in t.lexer.variables: 
            if type(t.lexer.variables[holder]) == list:
                pin = t.lexer.variables[holder][index]
            else:
                pin = t.lexer.variables[holder]
            if pin != 'true':
                cond_flag = True
                t.lexer.begin('INITIAL')
        else:
            cond_flag = True
            t.lexer.begin('INITIAL')
        pass
    else:
        pass


def t_unless_COND_CONTENT(t):
    r'[^\n]+'
    t.lexer.begin("INITIAL")
    pass

# ================================================

def t_BLOCK(t):
    r'\.\n'
    t.lexer.begin('block')
    pass

# ================== BLOCK ======================

def t_block_newline(t):
    r'[ \t]+'
    global line_block
    global indent_level
    t.lexer.lineno += 1
    i = len(t.value)
    if i >= indent_level + 4:
        if line_block != 0:
            t.type = 'TEXT'
            return t
        line_block += 1
    elif i == indent_level:
        line_block = 0
        t.lexer.begin('INITIAL')
        pass
    elif i < indent_level:
        line_block = 0
        t.lexer.begin('INITIAL')
        t.type = 'DEDENT'
        indent_level = i
        return t 
    

def t_block_content(t):
    r'[^\n]+\n?'
    t.type = 'TEXT'
    if '#{' in t.value:
        m = re.findall(r'\\?#\{[^\}]+\}', t.value)
        for i in m:
            if '\\' not in i:
                keeper = ""
                holder = i[2:-1]
                variable = holder.split('[')
                if variable[0] in t.lexer.variables:
                    pin = t.lexer.variables[variable[0]]
                    if len(variable) != 1:
                        keeper = str(pin[int(variable[1][0])])
                    elif type(pin) == list:
                        for j in range(0,len(pin)-1):
                            keeper += str(pin[j]) + ','
                        keeper += str(pin[len(pin)-1])
                    elif holder[0] in ['\'','"']:
                        keeper = holder[1:-1]
                    else:
                        keeper = str(pin)
                    t.value = t.value.replace(i, keeper)
            else:
                holder = i.replace('\\', '')
                t.value = t.value.replace(i, holder)
    return t

# ================================================

def t_newline(t):
    r'\n[ \t]*'
    global indent_level
    t.lexer.lineno += 1
    i = len(t.value) - 1
    if(t.value[-1] != '\n') and (i != indent_level):
        if i > indent_level:
            t.type = 'INDENT'
            t.value = i
            indent_level = i
        elif i < indent_level:
            t.type = 'DEDENT'
            t.value = i
            indent_level = i
            t.lexer.begin('INITIAL')
        return t
    else:
        pass

def t_COMMENT_PUG(t):
    r'//-.*'
    pass


def t_COMMENT(t):
    r'//.*'
    t.value = t.value[2:]
    t.value = str(indent_level) + ":" + t.value
    return t


def t_TEXT(t):
    r'[^\n]+'
    t.value = t.value[1:]
    if '#{' in t.value:
        m = re.findall(r'\\?#\{[^\}]+\}', t.value)
        for i in m:
            if '\\' not in i:
                keeper = ""
                holder = i[2:-1]
                variable = holder.split('[')
                if variable[0] in t.lexer.variables:
                    pin = t.lexer.variables[variable[0]]
                    if len(variable) != 1:
                        keeper = str(pin[int(variable[1][0])])
                    elif type(pin) == list:
                        for j in range(0,len(pin)-1):
                            keeper += str(pin[j]) + ','
                        keeper += str(pin[len(pin)-1])
                    elif holder[0] in ['\'','"']:
                        keeper = holder[1:-1]
                    else:
                        keeper = str(pin)
                    t.value = t.value.replace(i, keeper)
            else:
                holder = i.replace('\\', '')
                t.value = t.value.replace(i, holder)
    return t


# Error handling rule
def t_ANY_error(t):
    print(f"Invalid token: {t.value[0]}")
    t.lexer.skip(1)

# Building the lexer
lexer = lex.lex()
lexer.variables = {}   # Variables and respective value storaged
