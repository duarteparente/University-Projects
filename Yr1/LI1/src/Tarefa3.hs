{- |
 
 = Introdução
 
 Esta tarefa tem como objetivo a conversão de um labirinto numa sequência de instruções, que de forma mais compacta,
 recriam o mesmo labirinto. Foi uma tarefa que começou por ser relativamente simples, principalmente comparando com as
 tarefas anteriores.
 
 = Objetivos
 
 Como a tarefa 3 se revelou uma tarefa simples de começar, resolvemos deixar a primeira parte da mesma com apenas um dos
 membros do grupo, enquanto o outro completava e corrigia certos lapsos de tarefas anteriores, aproveitando o tempo da
 melhor forma possível.
 
 Antes de ter o Repeat em conta, a conversão de um labirinto em instruções foi rápida e eficaz, visto que foi apenas
 necessário usar algumas das funções mais básicas de Haskell e o raciocínio necessário para cumprir tal objetivo não foi
 muito exigente.
 
 O mais complicado nesta fase do trabalho, foi de longe a implementação do Repeat na lista de instruções, que exigiu o
 nosso trabalho em equipa para, depois de algumas ideias e tentativas, chegarmos à solução final. Foi necessário criar uma
 função (countRepeat) com um contador que testava se um elemento era igual ao elemento da cabeça de uma lista, sendo que se
 não fosse o caso, fazia o mesmo com a cauda da lista e adicionava um ao contador. Essa função permitiu-nos avançar e
 finalmente implementar o Repeat na lista de instruções inicial
 
  = Discussão e conclusão
  
  Embora tenha sido provavelmente a tarefa mais simples da primeira fase, a tarefa 3 revelou-se mais difícil do que aquilo
  que esperavamos. Sendo que quando a começamos, já não tinhamos muito tempo até à deadline de entrega, tememos que essa
  inesperada dificuldade não nos fosse possibilitar terminar a tarefa. Por esse motivo, acabar esta tarefa foi bastante
  satisfatório.
  
  Para concluir, acreditamos ter feito o melhor que estava ao nosso alcance nesta tarefa. O código também ficou bem
  organizado e compacto.
 
 -}
 

module Tarefa3 where     
import Types
import FileUtils
import Data.List

-- * Conversão em Instruction

-- | Inicia a transformação de um Maze em [Instruction]
toInstruct :: [[Piece]] -> [(Int, Piece)]
toInstruct [] = []
toInstruct (h:t) = (((length h), (head h)) : toInstruct t)

-- | Finaliza a transformação de um Maze em [Instruction] 
converteToInstruct :: [(Int, Piece)] -> Instruction
converteToInstruct l = Instruct l

-- | Transforma um Corredor em [Instruction]
corridorInstruction :: Corridor -> Instruction
corridorInstruction c = converteToInstruct (toInstruct (group c))

-- | Transforma um Maze em [Instruction] (sem Repeat)
mazeInstructions :: Maze -> Instructions
mazeInstructions [] = []
mazeInstructions (h:hs) = corridorInstruction h : mazeInstructions hs 

-- * Implementação do Repeat

-- | Testa se duas Instruction são iguais
equalInstruct :: Instruction -> Instruction -> Bool
equalInstruct (Instruct l) (Instruct x) = x == l

-- | Transforma uma Instruction repetida em Repeat
countRepeat :: Instruction -> Instructions -> Int -> Instruction
countRepeat l [] _ = l
countRepeat l (h:hs) x = if equalInstruct l h then Repeat x
                              else countRepeat l hs (x+1)

-- | Devolve [Instruction] com Repeat (ao contrário)
reverseRepeat :: Instructions -> Instructions
reverseRepeat [] = []
reverseRepeat l = countRepeat (last l) (init l) 0 : reverseRepeat (init l)

-- | Devolve [Instruction] final pela ordem corrreta
repete :: Instructions -> Instructions
repete l = reverse (reverseRepeat l)

-- * Função final da Tarefa 3

-- | Devolve um Maze compactado
compactMaze :: Maze -> Instructions
compactMaze l = repete (mazeInstructions l)  



