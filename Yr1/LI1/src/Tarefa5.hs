{- |

 = Introdução
 
 Esta tarefa teve como objetivo a criação de jogadas para os fantasmas. Foi uma tarefa desafiante, pelo que o mais 
 importante a ter em conta foi quais são as melhores reações dos fantasmas ao pacman e criar uma estratégia para eles 
 executarem.
 
 = Objetivos
 
 Compreender o objetivo da tarefa não foi muito complicado. Todos os data types que foram necessários para a completar já 
 tinham sido anteriormente definidos em tarefas anteriores, aos quais já estávamos portanto habituados a utilizar 
 (principalmente desde a tarefa 2).
 
 Sabendo que todas as jogadas dos fantasmas em modo Alive são reações ao pacman, tivemos que criar uma grande quantidade 
 de funções que comparam coordenadas de jogadores e a localização deles no labirinto. O nosso objetivo foi fazer os
 fanstamas perseguir constantemente o pacman pelo melhor caminho possível. O mais desafiante nesse aspecto, foi fazer os 
 fantasmas reagir ao tunel no caso em que passar pelo mesmo seja a melhor jogada para apanhar o pacman. Para tal, foi 
 necessário ter em conta uma grande quantidade de situações diferentes em conta, o que tornou o código desta tarefa mais 
 complexo.
 
 No modo Dead, os fantasmas não são muito reactivos, visto que apenas seguem um caminho em linha reta até se encontrarem 
 com uma parede, o que os faz virar para a sua direita e continuar o caminho em linha reta. Visto que é uma movimentação 
 bastante simples, foi muito provavelmente a parte mais fácil de codificar nesta tarefa.
 
 Para inverter a direção dos fantasmas quando eles entram em modo Dead, fizemos umas alterações na tarefa 2. Foi 
 adicionada uma função auxiliar que inverte a direção de um fantasma, tendo usado a mesma no caso em que o pacman come 
 uma Comida Grande.
 
 = Discussão e conclusão
 
 Embora tenha tido alguns desafios, esta tarefa deixou-nos bastante entusiasmados, principalmente tendo em conta que foi 
 ela que tornou o nosso jogo no seu estado final, onde todos os elementos estão funcionais.
 
 Apesar disso, alguns aspectos desta tarefa não ficaram exatamente como nós gostaríamos que tivessem ficado, tendo sido o 
 principal motivo o curto tempo que conseguimos dedicar à mesma. Um exemplo é a codificação desta fase do trabalho, que 
 poderia provavelmente estar mais compacta e eficaz.
 
 Para concluir, embora não esteja perfeita, esta tarefa cumpriu o seu objetivo de criar jogadas para os fantasmas, o que 
 já foi um motivo de contentamento.
 
 -}


module Tarefa5 where 
import Types
import Tarefa2
import Data.List

-- * Funções gerais da Tarefa 5

-- | Lista das coordenadas de lista de jogadores
pCoords :: [Player] -> [Coords]
pCoords [] = []
pCoords (h:hs)  = (getPlayerCoords h) : pCoords hs


-- | Distancia entre dois players em coordenadas
difCoordsI :: Player -> Player -> Coords
difCoordsI j p | (fst (getPlayerCoords j)) >= (fst (getPlayerCoords p)) && (snd (getPlayerCoords j)) >= (snd (getPlayerCoords p)) = ((fst (getPlayerCoords j)) - (fst (getPlayerCoords p)) , (snd (getPlayerCoords j)) - (snd (getPlayerCoords p)))
               | (fst (getPlayerCoords j)) <= (fst (getPlayerCoords p)) && (snd (getPlayerCoords j)) >= (snd (getPlayerCoords p)) = ((fst (getPlayerCoords p)) - (fst (getPlayerCoords j)) , (snd (getPlayerCoords j)) - (snd (getPlayerCoords p)))
               | (fst (getPlayerCoords j)) >= (fst (getPlayerCoords p)) && (snd (getPlayerCoords j)) <= (snd (getPlayerCoords p)) = ((fst (getPlayerCoords j)) - (fst (getPlayerCoords p)) , (snd (getPlayerCoords p)) - (snd (getPlayerCoords j)))
               | (fst (getPlayerCoords j)) <= (fst (getPlayerCoords p)) && (snd (getPlayerCoords j)) <= (snd (getPlayerCoords p)) = ((fst (getPlayerCoords p)) - (fst (getPlayerCoords j)) , (snd (getPlayerCoords p)) - (snd (getPlayerCoords j)))
              

-- | Distancia entre um fantasma e as coordenadas para onde ele vai para apanhar o pacman
difCoords :: Player -> Player -> Coords
difCoords j p | getPlayerOrientation j == U = (((fst(difCoordsI j p)) - 1) , (snd (difCoordsI j p)))
              | getPlayerOrientation j == D = (((fst(difCoordsI j p)) + 1) , (snd (difCoordsI j p)))
              | getPlayerOrientation j == L = ((fst(difCoordsI j p)) , ((snd (difCoordsI j p)) - 1))
              | getPlayerOrientation j == R = ((fst(difCoordsI j p)) , ((snd (difCoordsI j p)) + 1))
              

-- | Distancia em coordenadas entre uma coordenade e um player
difCoordsC :: Coords -> Coords -> Coords
difCoordsC (x,y) (n,z) | x >= n && y >= z = ((x-n),(y-z))
                       | x >= n && y <= z = ((x-n),(z-y))
                       | x <= n && y <= z = ((n-x),(z-y))
                       | x <= n && y >= z = ((n-x),(y-z))
                       
                       
-- | Distancia horizontal entre uma coordenada e um player (para usar com o tunel)
difCoordsHT :: Coords -> Player -> Maybe Int
difCoordsHT (x,y) p | (snd (getPlayerCoords p)) >= y = Just ((snd (getPlayerCoords p)) - y)
                    | (snd (getPlayerCoords p)) <= y = Just (y - (snd (getPlayerCoords p)))
                    | otherwise = Nothing
                       

-- * Funções de tracking (Onde se encontra um elemento em relação a um jogador no labirinto)


-- | Mostra se o tunel estã à direita ou à esquerda de um jogador
whereTunH :: Player -> Player -> Maze -> Orientation
whereTunH j p m | (snd (pertoDoTunel j p m)) >= (snd (getPlayerCoords p)) = R
                | (snd (pertoDoTunel j p m)) <= (snd (getPlayerCoords p)) = L


-- | Mostra se o tunel está acima ou abaixo de um jogador
whereTunV :: Player -> Player -> Maze -> Orientation
whereTunV j p m | (fst (pertoDoTunel j p m)) > (fst (getPlayerCoords p)) = D
                | (fst (pertoDoTunel j p m)) < (fst (getPlayerCoords p)) = U
                | otherwise = whereTunH j p m


-- | Dá as coordenadas do tunel no qual o fantasma tem que entrar no caso de ser a melhor jogada para apanhar o pacman
pertoDoTunel :: Player -> Player -> Maze -> Coords
pertoDoTunel j p m@(h:hs) | difCoordsHT (1,0) p < difCoordsHT (getPlayerCoords j) p && difCoordsHT (1,((length h) - 1)) j < difCoordsHT (getPlayerCoords j) p = ((div (length m) 2),-1)
                          | difCoordsHT (1,0) j < difCoordsHT (getPlayerCoords j) p && difCoordsHT (1,((length h) - 1)) p < difCoordsHT (getPlayerCoords j) p = ((div (length m) 2),((length h) - 1))
                          | otherwise = getPlayerCoords j


-- | Mostra se um player está à direita ou à esquerda de outro jogador
wherePacH :: Player -> Player -> Orientation
wherePacH j p | (snd (getPlayerCoords j)) >= (snd (getPlayerCoords p)) = R
              | (snd (getPlayerCoords j)) <= (snd (getPlayerCoords p)) = L


-- | Mostra se um player está acima ou abaixo de outro jogador
wherePacV :: Player -> Player -> Orientation
wherePacV j p | (fst (getPlayerCoords j)) >= (fst (getPlayerCoords p)) = D
              | (fst (getPlayerCoords j)) <= (fst (getPlayerCoords p)) = U


-- * Funções de decisão de jogadas


-- | Decide a jogada para um fantasma apanhar o pacman.
basicGPlayA :: Player -> Player -> Maze -> Play
basicGPlayA j p m@(h:hs) | difCoordsHT (1,0) p < difCoordsHT (getPlayerCoords j) p && difCoordsHT (1,((length h) - 1)) j < difCoordsHT (getPlayerCoords j) p && whereTunV j p m == D = Move (getPlayerID p) D
                         | difCoordsHT (1,0) p < difCoordsHT (getPlayerCoords j) p && difCoordsHT (1,((length h) - 1)) j < difCoordsHT (getPlayerCoords j) p && whereTunV j p m == U = Move (getPlayerID p) U
                         | difCoordsHT (1,0) j < difCoordsHT (getPlayerCoords j) p && difCoordsHT (1,((length h) - 1)) p < difCoordsHT (getPlayerCoords j) p && whereTunV j p m == D = Move (getPlayerID p) D
                         | difCoordsHT (1,0) j < difCoordsHT (getPlayerCoords j) p && difCoordsHT (1,((length h) - 1)) p < difCoordsHT (getPlayerCoords j) p && whereTunV j p m == U = Move (getPlayerID p) U
                         | difCoordsHT (1,0) p < difCoordsHT (getPlayerCoords j) p && difCoordsHT (1,((length h) - 1)) j < difCoordsHT (getPlayerCoords j) p && whereTunH j p m == R = Move (getPlayerID p) R
                         | difCoordsHT (1,0) p < difCoordsHT (getPlayerCoords j) p && difCoordsHT (1,((length h) - 1)) j < difCoordsHT (getPlayerCoords j) p && whereTunH j p m == L = Move (getPlayerID p) L
                         | difCoordsHT (1,0) j < difCoordsHT (getPlayerCoords j) p && difCoordsHT (1,((length h) - 1)) p < difCoordsHT (getPlayerCoords j) p && whereTunH j p m == R = Move (getPlayerID p) R
                         | difCoordsHT (1,0) j < difCoordsHT (getPlayerCoords j) p && difCoordsHT (1,((length h) - 1)) p < difCoordsHT (getPlayerCoords j) p && whereTunH j p m == L = Move (getPlayerID p) L
                         | wherePacV j p == D && fst (difCoords j p) >= snd (difCoords j p) = Move (getPlayerID p) D
                         | wherePacV j p == U && fst (difCoords j p) >= snd (difCoords j p) = Move (getPlayerID p) U
                         | wherePacH j p == L && fst (difCoords j p) <= snd (difCoords j p) = Move (getPlayerID p) L
                         | wherePacH j p == R && fst (difCoords j p) <= snd (difCoords j p) = Move (getPlayerID p) R


-- | Decide a jogada para um fantasma apanhar o pacman no caso de ele se encontrar com uma parede.
collideGPlayA :: Player -> Player -> Play
collideGPlayA j p | getPlayerOrientation p == R && wherePacV j p == D = Move (getPlayerID p) D
                  | getPlayerOrientation p == R && wherePacV j p == U = Move (getPlayerID p) U
                  | getPlayerOrientation p == L && wherePacV j p == D = Move (getPlayerID p) D
                  | getPlayerOrientation p == L && wherePacV j p == U = Move (getPlayerID p) U
                  | getPlayerOrientation p == D && wherePacH j p == R = Move (getPlayerID p) R
                  | getPlayerOrientation p == U && wherePacH j p == R = Move (getPlayerID p) R
                  | otherwise = Move (getPlayerID p) L


-- | Decide a jogada final para o fantasma apanhar o pacman            
decideGPlayA :: Player -> Player -> State -> Play
decideGPlayA j p e@(State m js l) | (getPlayerCoords p) `elem` (delete (getPlayerCoords p) (pCoords (onlyfantasmas js))) && getPiece js ((fst(getPlayerCoords p) - 1),(snd(getPlayerCoords p))) m /= Wall = Move (getPlayerID p) U
                                  | (getPlayerCoords p) `elem` (delete (getPlayerCoords p) (pCoords (onlyfantasmas js))) && getPiece js ((fst(getPlayerCoords p)),(snd(getPlayerCoords p) - 1)) m /= Wall = Move (getPlayerID p) L
                                  | let x = ((div (length m) 2),(div (length ((!!) m 0)) 2)) in getPlayerCoords p == x || getPlayerCoords p == ((fst x)-1,snd x) || getPlayerCoords p == ((fst x)-2,snd x) = Move (getPlayerID p) U
                                  | acaoPiece (basicGPlayA j p m) e == Collide = collideGPlayA j p
                                  | getPlayerOrientation p == D && wherePacV j p == D && getPiece js ((fst(getPlayerCoords p)),(snd(getPlayerCoords p) + 1)) m == Wall = Move (getPlayerID p) D
                                  | getPlayerOrientation p == U && wherePacV j p == U && getPiece js ((fst(getPlayerCoords p)),(snd(getPlayerCoords p) + 1)) m == Wall = Move (getPlayerID p) U
                                  | getPlayerOrientation p == D && wherePacV j p == D && getPiece js ((fst(getPlayerCoords p)),(snd(getPlayerCoords p) - 1)) m == Wall = Move (getPlayerID p) D
                                  | getPlayerOrientation p == U && wherePacV j p == U && getPiece js ((fst(getPlayerCoords p)),(snd(getPlayerCoords p) - 1)) m == Wall = Move (getPlayerID p) U
                                  | getPlayerOrientation p == R && wherePacH j p == R && getPiece js ((fst(getPlayerCoords p) - 1),(snd(getPlayerCoords p))) m == Wall = Move (getPlayerID p) R
                                  | getPlayerOrientation p == R && wherePacH j p == R && getPiece js ((fst(getPlayerCoords p) + 1),(snd(getPlayerCoords p))) m == Wall = Move (getPlayerID p) R
                                  | getPlayerOrientation p == L && wherePacH j p == L && getPiece js ((fst(getPlayerCoords p) - 1),(snd(getPlayerCoords p))) m == Wall = Move (getPlayerID p) L
                                  | getPlayerOrientation p == L && wherePacH j p == L && getPiece js ((fst(getPlayerCoords p) + 1),(snd(getPlayerCoords p))) m == Wall = Move (getPlayerID p) L
                                  | otherwise = basicGPlayA j p m


-- | Jogada do fantasma para apanhar o pacman
chaseMode :: State -> Int -> Play
chaseMode e@(State m j l) n = decideGPlayA (head (onlyPacman j)) (choosePlayer n j) e


-- | Jogada basica do fantasma quando ele está em modo Dead
basicGPlayD :: Player -> Play
basicGPlayD j = Move (getPlayerID j) (getPlayerOrientation j)


-- | Jogada do fantasma em modo Dead quando ele se encontra com uma parede
collideGPlayD :: Player -> Play
collideGPlayD p | getPlayerOrientation p == D = Move (getPlayerID p) L
                | getPlayerOrientation p == U = Move (getPlayerID p) R
                | getPlayerOrientation p == R = Move (getPlayerID p) D
                | getPlayerOrientation p == L = Move (getPlayerID p) U


-- | Decide a jogada final para o fantasma em modo Dead
decideGPlayD :: Player -> State -> Play
decideGPlayD j e | acaoPiece (basicGPlayD j) e == Collide = collideGPlayD j
                 | otherwise = basicGPlayD j


-- | Jogada do fantasma em modo Dead
scatterMode :: State -> Int -> Play
scatterMode e@(State m j l) n = decideGPlayD (choosePlayer n j) e


-- | Cria uma lista de jogadas, uma para cada fantasma
playToList :: [Player] -> State -> [Play]
playToList [] _ = []
playToList (h:hs) e | isGhostDead h = (scatterMode e (getPlayerID h)) : playToList hs e
                    | otherwise = (chaseMode e (getPlayerID h)) : playToList hs e

-- * Função final da Tarefa 5

-- | Devolve um conjunto de jogadas, uma para cada fantasma
ghostPlay :: State -> [Play]
ghostPlay e@(State m j l) = playToList (onlyfantasmas j) e
