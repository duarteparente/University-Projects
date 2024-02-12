 {- |

 = Introdução

 Esta tarefa teve como objetivo a implementação de um mecanismo de geração de labirintos pseudo-aleatórios válidos.  

 = Objetivos

 Sem termos qualquer experiência de programação, de inicio foi extremamente complicado entrar no ritmo de trabalho, mas à medida que as aulas iam passado era notório
 que cada vez conseguiamos avançar mais na tarefa. 

 O planeamneto desta tarefa consistiu na separação do trabalho em partes cruciais. Por exemplo, primeiro foram tratados os limites do labirinto, depois seguiu-se a implementação
 dos tunéis e por último, que foi também onde tivemos mais dificuldade, a implementação da casa dos fantasmas.

 As estratégias para ambas as partes foram similares. Sendo um labirinto nada mais que uma lista de listas de peças, o nosso foco sempre passou por trabalhar sempre que possivel
 num sistema de posições dentro da lista. 

 = Discussão e conclusão

 Apesar da dificuldade inicial, muito devida à nossa inexperiência na área, achamos ter terminado com sucesso a realização da Tarefa 1.
 Através de ínumeros testes com diferentes medidas de comprimento e largura conseguimos sempre visualizar um labirinto válido, com particular atenção para a casa dos fantasmas 
 sempre no sitio certo, tal como os túneis.

 Para concluir, achamos a tarefa um agradável desafio para introdução do projeto. E o bom resultado alcançado no final da tarefa acabou por nos transmitir confiança para as 
 tarefas seguintes. Foi também importante estarmos em contacto com certas funções que apareceram no módulo Generator para também começar a tentar
 perceber o funcionamento de algo que consideramos avançado tendo em conta o estado da aprendizagem em que nos encontrávamos.   
  

 -}

module Tarefa1 where  
import Generator
import System.Random
import Types

-- * Funções gerais da Tarefa 1

-- | Cria um corredor com determinado numero de peças Wall 
donaldTrump :: Int -> Corridor
donaldTrump 0 = []
donaldTrump x = Wall : donaldTrump (x-1)

-- | Dado um Maze devolve-o com limitado inferiormente por peças Wall
fronteiraMexicana :: Maze -> Corridor -> Maze
fronteiraMexicana [] c = [c]
fronteiraMexicana (x:xs) c = x : (fronteiraMexicana xs c)

-- | Gera um maze limitado inferior e superiormente por peças Wall
geraMazeAmericano :: Int -> Int -> Int -> Maze
geraMazeAmericano l a s = fronteiraMexicana ((donaldTrump l) : (geraMaze l (a-2) s)) (donaldTrump l)


-- | Casa dos fantasmas construida manualmente, com peças Empty à volta
bobConstrutor :: Int -> Maze
bobConstrutor i 
  | even i     = [[Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty],
                  [Empty,Wall,Wall,Wall,Empty,Empty,Wall,Wall,Wall,Empty],
                  [Empty,Wall,Empty,Empty,Empty,Empty,Empty,Empty,Wall,Empty],
                  [Empty,Wall,Wall,Wall,Wall,Wall,Wall,Wall,Wall,Empty],
                  [Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty]]                                                   
  | otherwise  = [[Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty],
                  [Empty,Wall,Wall,Wall,Empty,Empty,Empty,Wall,Wall,Wall,Empty],
                  [Empty,Wall,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Wall,Empty],
                  [Empty,Wall,Wall,Wall,Wall,Wall,Wall,Wall,Wall,Wall,Empty],
                  [Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty]]  


-- | Definição recursiva da função (!!) já definida no Prelude 
exclamacao :: [[Piece]] -> Int -> [Piece]
exclamacao [] _ = [Wall]
exclamacao (x:xs) 0 = x
exclamacao (x:xs) n = exclamacao xs (n-1)



-- | Testa a paridade da medida da altura de um Maze
ifEvenA :: Maze -> Bool
ifEvenA l = even $ length l 
-- | Testa a paridade da medida da largura de um Maze 
ifEvenC :: Maze -> Bool
ifEvenC l = even $ length $ exclamacao l 0 


-- | Dado um Maze devolve o corredor central quando este tem altura impar ou os dois centrais quando a altura é par
getmiddleCorridor :: Maze -> [Corridor] 
getmiddleCorridor l = if ifEvenA l then [exclamacao l ((div (length l) 2)-1), exclamacao l (div (length l) 2)]
                                   else [exclamacao l (div (length l) 2)]



-- | Dado um Maze devolve-o limitado à esquerda por peças Wall
replace :: Maze -> Maze
replace [] = []
replace (x:xs) = (Wall : (drop 1 x)) : replace xs 
-- | Dado um Maze devolve-o limitado à direita por peças Wall
replacelast :: Maze -> Maze
replacelast [] = []
replacelast (x:xs) = replace (((init x) ++ [Wall]) : replacelast xs) 

-- | Gera um Maze totalmente rodeado por peças Wall
mazecomparedes :: Int -> Int -> Int -> Maze  
mazecomparedes l a s = replacelast (geraMazeAmericano l a s)

 
-- * Implementação dos túneis


-- | Devolve o corredor/corredores centrais de um Maze com um/dois tuneis no final
tunellast :: Maze -> [Corridor] 
tunellast l = if ifEvenA l then [init (exclamacao l ((div (length l) 2)-1)) ++ [Empty], init (exclamacao l (div (length l) 2)) ++ [Empty]] 
                           else [init (exclamacao l (div (length l) 2)) ++ [Empty]] 
-- | Devolve o corredor/corredores centrais de um Maze com um/dois tuneis no inicio
tunelbegin :: Maze -> [Corridor]
tunelbegin l = if ifEvenA l then [[Empty] ++ tail (exclamacao l ((div (length l) 2)-1)), [Empty]++ tail (exclamacao l (div (length l) 2))]
                            else [[Empty] ++ tail (exclamacao l (div (length l) 2))] 



-- | Devolve um Maze (par) com dois tuneis no fim 
mazecomtunelpar :: Maze -> Maze  
mazecomtunelpar l = (take ((div (length l) 2)-1) l) ++ (tunellast l) ++ (drop ((div (length l) 2)+1) l)
-- | Devolve um Maze (par) com dois tuneis no inicio
mazecomtunelcompletopar :: Maze -> Maze 
mazecomtunelcompletopar l = (take ((div (length l) 2)-1) l) ++ (tunelbegin l) ++ (drop ((div (length l) 2)+1) l)
-- | Gera um Maze (par) rodeado por Wall com tuneis nos dois lados 
mazequasecompletopar :: Int -> Int -> Int -> Maze
mazequasecompletopar l a s = mazecomtunelcompletopar (mazecomtunelpar (mazecomparedes l a s))


-- | Devolve um Maze (impar) com dois tuneis no fim
mazecomtunelimpar :: Maze -> Maze  
mazecomtunelimpar l = (take (div (length l) 2) l) ++ (tunellast l) ++ (drop ((div (length l) 2)+1) l)
-- | Devolve um Maze (impar) com dois tuneis no inicio
mazecomtunelcompletoimpar :: Maze -> Maze 
mazecomtunelcompletoimpar l = (take (div (length l) 2) l) ++ (tunelbegin l) ++ (drop ((div (length l) 2)+1) l)
-- | Gera um Maze (impar) rodeado por Wall com tuneis nos dois lados
mazequasecompletoimpar :: Int -> Int -> Int -> Maze
mazequasecompletoimpar l a s = mazecomtunelcompletoimpar (mazecomtunelimpar (mazecomparedes l a s)) 


-- | Gera um Maze (par ou impar) rodeado por Wall com tuneis nos dois lados
typemaze :: Int -> Int -> Int -> Maze
typemaze l a s = if (even a) then (mazequasecompletopar l a s) else (mazequasecompletoimpar l a s)


-- * Implementação da casa dos fantasmas  
 

-- | Devolve os 5 corredores centrais de um labirinto (par ou impar)
get5middlecorridor :: Maze -> [Corridor]  
get5middlecorridor l = if ifEvenA l then [exclamacao l ((div (length l) 2)-3), exclamacao l ((div (length l) 2)-2), 
                                          exclamacao l ((div (length l) 2)-1), exclamacao l (div (length l) 2), 
                                          exclamacao l ((div (length l) 2)+1)]
                                    else [exclamacao l ((div (length l) 2)-2), exclamacao l ((div (length l) 2)-1), 
                                          exclamacao l (div (length l) 2), exclamacao l ((div (length l) 2)+1),
                                          exclamacao l ((div (length l) 2)+2)]

-- | Substitui os 5 corredores centrais de um Maze    
replaceinMaze :: Maze -> Maze -> Maze
replaceinMaze _ [] = []
replaceinMaze [] _ = []
replaceinMaze (h:t) (h':t')= if ifEvenC (h:t) then ((take ((div (length h) 2)-5) h) ++ h'++ (drop ((div (length h) 2)+5) h)) : replaceinMaze t t'
                                              else ((take ((div (length h) 2)-5) h) ++ h'++ (drop ((div (length h) 2)+6) h)) : replaceinMaze t t'

-- | Implementa a casa dos fantasmas no labirinto 
middle3Corridors :: Maze -> Maze
middle3Corridors l = if ifEvenC l then replaceinMaze (get5middlecorridor l) (bobConstrutor 2)
                                  else replaceinMaze (get5middlecorridor l) (bobConstrutor 1)   

-- | Devolve um Maze (par ou impar) com a casa dos fantasmas no meio 
mazecompleto :: Maze -> Maze 
mazecompleto l = if ifEvenA l then (take ((div (length l) 2)-3) l) ++ (middle3Corridors l) ++ (drop ((div (length l) 2)+2) l)
                              else (take ((div (length l) 2)-2) l) ++ (middle3Corridors l) ++ (drop ((div (length l) 2)+3) l)


-- * Função final da Tarefa 1

-- | Gera um Maze pseudo-aleatório com as dimensões desejadas
generateMaze :: Int -> Int -> Int -> Maze
generateMaze x y z = mazecompleto $ typemaze x y z    



-- * Funções de teste


-- | Teste altura
alturacerta :: Int -> Int -> Int -> Bool
alturacerta l a s = length (generateMaze l a s) == a 
-- | Teste largura
larguracorreta :: Int -> Int -> Int -> Bool
larguracorreta l a s = (length $ exclamacao (generateMaze l a s) 0) == l                                
-- | Teste (Maze limitado superiormente por Wall)
onlyWalltop :: Int -> Int -> Int -> Bool
onlyWalltop l a s =  exclamacao (generateMaze l a s) 0 == (replicate l Wall) 
-- | Teste (Maze limitado inferiormente po Wall)
onlyWallbottom :: Int -> Int -> Int -> Bool
onlyWallbottom l a s = exclamacao (generateMaze l a s) (a-1) == (replicate l Wall) 
-- | Teste (Maze de altura par && tuneis no meio)
tunnelpar :: Int -> Int -> Int -> Bool
tunnelpar l a s = getpeca (((div (length (generateMaze l a s)) 2)-1),0) (generateMaze l a s) == Empty && getpeca (((div (length (generateMaze l a s)) 2)-1),((length $ exclamacao (generateMaze l a s) 0)-1)) (generateMaze l a s) == Empty && getpeca ((div (length (generateMaze l a s)) 2),0) (generateMaze l a s) == Empty && getpeca ((div (length (generateMaze l a s)) 2),((length $ exclamacao (generateMaze l a s) 0)-1)) (generateMaze l a s) == Empty 
-- | Teste (Maze de altura impar && tunel no meio)
tunnelimpar :: Int -> Int -> Int -> Bool
tunnelimpar l a s = getpeca ((div (length (generateMaze l a s)) 2),0) (generateMaze l a s) == Empty && getpeca ((div (length (generateMaze l a s)) 2),((length $ exclamacao (generateMaze l a s) 0)-1)) (generateMaze l a s) == Empty
