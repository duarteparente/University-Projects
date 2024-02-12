 {- |

 = Introdução

 Esta Tarefa teve como objetivo a determinação do efeito de uma jogada no estado do jogo. Esta tarefa acabou por se revelar a mais trabalhosa, no sentido que foi preciso atentar 
 não só no encunciado, mas também em vários pormenores do jogo real na tentativa de conhecer todas as jogadas e acontecimentos possíveis. 
 Foi também necessária uma remodelação posteriror à entrega da primeira fase, de forma a poder continuar a trabalhar sobre as tarefas da seunda fase. 

 = Objetivos

 Tal como na Tarefa 1, optamos por seguir uma estratégia de separação do trabalho por partes. A primeira dificuldade passou por compreender o novo data type, e todos os novos tipos 
 que apareceram. Para além disso foi necessário criar um novo data Type nosso de forma a facilitar o controlo dos diferentes tipos de acontecimentos que uma jogada pode ter.

 Dado que o resultado final é um State alterado conforme o tipo de jogada, foi prontamente criada uma função que deteta essa mesma ação dentro dos acontecimentos definidos no PlayAction, 
 e foi a partir daí que começou a parte mais trabalhosa da tarefa. Foi preciso definir os vários conportamentos nos Players desde a alteração dos estados, do número de vidas e pontos no 
 Pacman, e para além das alterações próprias da tarefa, optamos por implementar também algumas das alteraçõe gráficas que ocorrem em cada ação, 
 tais como a aparição de uma peça vazia após o Pacman comer por exemplo uma comida pequena.
 
 Sem dúvida que o que nos causou mais dificuldade foi a interação do Pacman com os fantasmas. Acabamos por demorar algum tempo a conseguir completar a nossa função de deteção de forma a 
 detetar tanto estados dos jogadores, como jogadores em si. 

 = Discussão e conclusão
   
 Apesar de ser a mais trabalhosa tarefa da primeira fase, acabou por ser também a que nos deu mais prazer em fazer. Foi uma tarefa feita de passo em passo, e que cada pequena vitória
 nos dava cada vez mais motivação. 

 Mais uma vez conseguimos alcançar o que pretendíamos, e com a ajuda dos testes quer feitos por nós ou os disponibilizados no codeboard, percebemos que realmente não acontecia nada que 
 não fosse suposto. Testamos praticamente todas as situações possiveis de acontecimentos, desde a simples movimentação para um dos lados, até ao diferentes tipos de choques com fantasmas
 ou a passagem pelos túneis, e tudo funcionou como era esperado.

 Para concluir, temos a completa noção de que a forma como estrutuámos o código não foi das mais efecientes, mas a verdade é que ainda não tinhamos grande conhecimento e dominio 
 sobre a linguagem, e achamos mais importante salientar que todos os pontos do enunciado foram cobertos e tal como na tarefa 1 acabamos a tarefa com a sensação de trabalho realizado. 


 -}

module Tarefa2 where    
import Types
import System.Random
import FileUtils
import Data.List
import Tarefa1

-- * Tipos criados de auxílio à Tarefa 2

-- | Tipos de acontecimentos numa Play 
data PlayAction = Eat EatType | Collide | Die | DieG | TeleportL | TeleportR | MoverVazio | DirecaoMuda deriving (Eq,Show)
-- | Tipos de comida
data EatType = Pequena | Grande | Fantasma | Jogador deriving (Eq,Show)

-- * Inferir a ação

-- | Devolve a peça de determinadas coordenadas de um Maze com jogadores
getPiece :: [Player] -> Coords -> Maze -> Piece
getPiece j (x,y) m = getpeca (x,y) (placePlayersOnMap j m)     

-- | Infere a posição final do Player terminada a Play 
moveCoords :: State -> Play -> Coords
moveCoords (State m j l) (Move n o) = let (x,y) = getPlayerCoords (choosePlayer n j)
                                      in if getPlayerOrientation (choosePlayer n j) /= o then (x,y)
                                                else if o == R then (x,y+1) 
                                                            else if o == L then (x,y-1) 
                                                                     else if o == U then (x-1,y) 
                                                                              else (x+1,y)
-- | Infere se uma peça é um Pacman em estado Mega
isPiecePacmanMega :: Piece -> Bool
isPiecePacmanMega (PacPlayer ( Pacman ( PacState (i, c, x, y,z,l) o m Mega   ) )) = True
isPiecePacmanMega _ = False

-- | Infere se uma peça é um Pacman
isPiecePacman :: Piece -> Bool
isPiecePacman (PacPlayer ( Pacman ( PacState (i, c, x, y,z,l) o m Mega   ) )) = True
isPiecePacman _ = False


-- | Devolve a peça que se encontra na posição para a qual o Player se desloca                                                                                
getMovePiece :: Play -> State -> Piece
getMovePiece p@(Move n o) e@(State m j l) = getPiece j (moveCoords e p) m

-- | Dada uma Play e um State infere o tipo de ação
acaoPiece :: Play -> State -> PlayAction
acaoPiece p@(Move n o) e@(State m j l) = if getPlayerOrientation (choosePlayer n j) /= o then DirecaoMuda else infereAcao (choosePlayer n j) (getMovePiece p e) m 

-- | Infere o tipo de ação dada a peça para que o Player se move
infereAcao :: Player -> Piece -> Maze -> PlayAction
infereAcao (Pacman (PacState (id,(x,y),v,o,p,l) tm m pm)) piece (h:hs)
   | y == 0 && o == L = TeleportL
   | y == ((length h) - 1) && o == R  = TeleportR
   | piece == Empty || isPiecePacman piece = MoverVazio 
   | piece == Wall = Collide
   | piece == Food Big = Eat Grande
   | piece == Food Little = Eat Pequena
   | otherwise = if pm == Mega then Eat Fantasma else Die
infereAcao (Ghost (GhoState (a,(x,y),z,t,u,li) q )) piece (h:hs)
   | y == 0 && t == L = TeleportL
   | y == ((length h) - 1) && t == R  = TeleportR
   | isPiecePacmanMega piece = DieG
   | isPiecePacman piece = Eat Jogador
   | piece == Wall = Collide
   | otherwise = MoverVazio

-- * Atualização dos jogadores

-- | ALtera os pontos de um Player conforme a PlayAction
setPlayersPoints :: Player -> PlayAction -> Player
setPlayersPoints (Pacman (PacState (x,y,z,t,h,l) q c d )) (Eat Pequena) = (Pacman (PacState (x,y,z,t,h+1,l) q c d ))
setPlayersPoints (Pacman (PacState (x,y,z,t,h,l) q c d )) (Eat Grande) = (Pacman (PacState (x,y,z,t,h+5,l) q c d )) 
setPlayersPoints (Pacman (PacState (x,y,z,t,h,l) q c d )) (Eat Fantasma) = (Pacman (PacState (x,y,z,t,h+10,l) q c d ))
setPlayersPoints p _ = p 

-- | Altera o número de vidas de um Player conforme a PlayAction
setPlayersLives :: Player -> PlayAction -> Player 
setPlayersLives (Pacman (PacState (x,y,z,t,h,0) q c d )) (Die) = (Pacman (PacState (x,y,z,t,h,0) q c Dying ))
setPlayersLives (Pacman (PacState (x,y,z,t,h,l) q c d )) (Die) = (Pacman (PacState (x,y,z,t,h,l-1) q c d ))
setPlayersLives p _ = p

-- | Altera o estado de um Player conforme a PlayAction
setPlayerState :: Player -> PlayAction -> Player 
setPlayerState (Pacman (PacState (x,y,z,t,h,l) q c d )) (Eat Grande) = (Pacman (PacState (x,y,z,t,h,l) 10 c Mega ))
setPlayerState (Ghost (GhoState (x,(a,b),z,t,h,li) q )) (Eat Grande) = (Ghost (GhoState (x,(a,b),0.5,t,h,li) Dead ))
setPlayerState (Ghost (GhoState (x,(a,b),z,t,h,li) Dead )) (Eat Fantasma) = (Ghost (GhoState (x,(a,b),z,t,h,li) Alive ))
setPlayerState p _ = p

-- | Devolve o numero de vidas de um Player
getPlayerLives :: Player -> Int
getPlayerLives (Pacman (PacState (x,y,z,t,h,l) q c d )) = l
getPlayerLives (Ghost (GhoState (x,y,z,t,h,l) q )) = l

-- | Determina o jogador a aplicar a jogada a partir do ID
choosePlayer :: Int -> [Player] -> Player   
choosePlayer n (x:xs) = if getPlayerID x == n then x else choosePlayer n xs     

-- | Devolve um jogador de uma lista com determinadas coordenadas
choosePlayerWithCoords :: Coords -> [Player] -> Player
choosePlayerWithCoords (x,y) (n:ns) = if getPlayerCoords n == (x,y) then n else choosePlayerWithCoords (x,y) ns 

-- | Remove o Player da lista (Tarefa2)
removeElem :: Play -> [Player] -> [Player] 
removeElem (Move i o) jg =  (\\) jg [(choosePlayerWithCoords (coordenadas (choosePlayer i jg) o) jg)] 

-- | Muda a orientação de um Player
changeOrientation :: Orientation -> Player -> Player 
changeOrientation o p@(Pacman (PacState (x,y,z,t,h,l) q c d )) = if o==t then p else (Pacman (PacState (x,y,z,o,h,l) q c d ))
changeOrientation o g@(Ghost (GhoState (x,(a,b),z,t,h,li) q )) = if o==t then g else (Ghost (GhoState (x,(a,b),z,o,h,li) q )) 

-- | Inverte a orientação de um Ghost
ghoDeadOrientation :: Player -> Player
ghoDeadOrientation g@(Ghost (GhoState (x,(a,b),z,t,h,li) q )) = case t of Null -> g 
                                                                          U -> changeOrientation D g
                                                                          D -> changeOrientation U g
                                                                          R -> changeOrientation L g
                                                                          L -> changeOrientation R g
                                                                                                                 
                                                                                           
-- | Dada uma PlayAction devolve a lista dos jogadores alterada conforme o tipo da mesma
updatePlay :: Play -> Maze -> PlayAction -> [Player] -> [Player] 
updatePlay p l _ [] = []
updatePlay p@(Move i o)  l (Eat Grande) (n:ns) = let (x,y) = getPlayerCoords n
                                                 in if isGhost n then (setPlayerState (ghoDeadOrientation n) (Eat Grande)) : updatePlay p l (Eat Grande) ns
                                                        else if i /= getPlayerID n then n : updatePlay p l (Eat Grande) ns
                                                                 else if o==R then mouth ((setPlayerState (setPlayersPoints (setPlayerCoords n (x,y+1)) (Eat Grande)) (Eat Grande))) : updatePlay p l (Eat Grande) ns
                                                                         else if o==L then mouth ((setPlayerState (setPlayersPoints (setPlayerCoords n (x,y-1)) (Eat Grande)) (Eat Grande))) : updatePlay p l (Eat Grande) ns
                                                                                  else if o==D then mouth ((setPlayerState (setPlayersPoints (setPlayerCoords n (x+1,y)) (Eat Grande)) (Eat Grande))) : updatePlay p l (Eat Grande) ns
                                                                                           else mouth ((setPlayerState (setPlayersPoints (setPlayerCoords n (x-1,y)) (Eat Grande)) (Eat Grande))) : updatePlay p l (Eat Grande) ns   
updatePlay p@(Move i o) l (Eat Pequena) (n:ns) = let (x,y) = getPlayerCoords n 
                                                 in if isGhost n then n : updatePlay p l (Eat Pequena) ns
                                                       else if i /= getPlayerID n then n : updatePlay p l (Eat Pequena) ns
                                                             else if o==R then mouth ((setPlayersPoints (setPlayerCoords n (x,y+1)) (Eat Pequena))) : updatePlay p l (Eat Pequena) ns 
                                                                   else if o==L then mouth ((setPlayersPoints (setPlayerCoords n (x,y-1)) (Eat Pequena))) : updatePlay p l (Eat Pequena) ns 
                                                                          else if o==D then mouth ((setPlayersPoints (setPlayerCoords n (x+1,y)) (Eat Pequena))) : updatePlay p l (Eat Pequena) ns 
                                                                                else mouth ((setPlayersPoints (setPlayerCoords n (x-1,y)) (Eat Pequena))) : updatePlay p l (Eat Pequena) ns   
updatePlay p l (Collide) (n:ns) = (n:ns)
updatePlay p@(Move i o) l (DirecaoMuda) (n:ns) = if i /= getPlayerID n then n : updatePlay p l (DirecaoMuda) ns else (changeOrientation o n) : ns
updatePlay p@(Move i o) l (TeleportL) (n:ns) = let (x,y) = getPlayerCoords n 
                                               in if i /= getPlayerID n then n : updatePlay p l (TeleportL) ns
                                                     else if getPiece (n:ns) (x,((length ((!!) l 0))-1)) l == Empty then (setPlayerCoords n (x,((length ((!!) l 0))-1))) : updatePlay p l (TeleportL) ns
                                                              else if getPacmanMode n == Normal then  (setPlayersLives (setPlayerCoords n (x,((length ((!!) l 0))-1))) (Die)) : updatePlay p l (TeleportL) ns 
                                                                        else (setPlayersPoints (setPlayerCoords n (x,((length ((!!) l 0))-1))) (Eat Fantasma)) : updatePlay p l (TeleportL) ns 
updatePlay p@(Move i o) l (TeleportR) (n:ns) = let (x,y) = getPlayerCoords n 
                                               in if i /= getPlayerID n then n : updatePlay p l (TeleportR) ns
                                                      else if getPiece (n:ns) (x,0) l == Empty then (setPlayerCoords n (x,0)) : updatePlay p l (TeleportR) ns
                                                               else if getPacmanMode n == Normal then  (setPlayersLives (setPlayerCoords n (x,0)) (Die)) : updatePlay p l (TeleportR) ns 
                                                                        else (setPlayersPoints (setPlayerCoords n (x,0)) (Eat Fantasma)) : updatePlay p l (TeleportR) ns                                                                                                                                                                                                           
updatePlay p@(Move i o) l (Die) (n:ns) = let (x,y) = getPlayerCoords n
                                         in if isGhost n then setPlayerCoords n ((div (length l) 2),(div (length ((!!) l 0))2)) : updatePlay p l (Die) ns 
                                                else if i /= getPlayerID n then n : updatePlay p l (Die) ns
                                                          else (setPlayersLives (setPlayerCoords n (((div (length l) 2)+2),(div (length ((!!) l 0))2))) (Die)) : updatePlay p l (Die) ns                                                                  
updatePlay p@(Move i o) l (Eat Fantasma) (n:ns) = let (x,y) = getPlayerCoords (choosePlayer i (n:ns))
                                                  in if i /= getPlayerID n then n : updatePlay p l (Eat Fantasma) ns
                                                        else if o==R && getpeca (x,y+1) l == Food Little then mouth ((setPlayersPoints (setPlayersPoints (setPlayerCoords n (x,y+1)) (Eat Fantasma)) (Eat Pequena))) : updatePlay p l (Eat Fantasma) ns
                                                                 else if o==R && getpeca (x,y+1) l == Food Big then mouth ((setPlayersPoints (setPlayersPoints (setPlayerCoords n (x,y+1)) (Eat Fantasma)) (Eat Grande))) : updatePlay p l (Eat Fantasma) ns
                                                                          else if o==R then mouth ((setPlayersPoints (setPlayerCoords n (x,y+1)) (Eat Fantasma))) : updatePlay p l (Eat Fantasma) ns
                                                                                   else if o==L && getpeca (x,y-1) l == Food Little then mouth ((setPlayersPoints (setPlayersPoints (setPlayerCoords n (x,y-1)) (Eat Fantasma)) (Eat Pequena))) : updatePlay p l (Eat Fantasma) ns
                                                                                            else if o==L && getpeca (x,y-1) l == Food Big then ((setPlayersPoints (setPlayersPoints (setPlayerCoords n (x,y-1)) (Eat Fantasma)) (Eat Grande))) : updatePlay p l (Eat Fantasma) ns
                                                                                                     else if o==L then mouth ((setPlayersPoints (setPlayerCoords n (x,y-1)) (Eat Fantasma))) : updatePlay p l (Eat Fantasma) ns
                                                                                                              else if o==D && getpeca (x+1,y) l == Food Little then mouth ((setPlayersPoints (setPlayersPoints (setPlayerCoords n (x+1,y)) (Eat Fantasma)) (Eat Pequena))) : updatePlay p l (Eat Fantasma) ns
                                                                                                                       else if o==D && getpeca (x+1,y) l == Food Big then mouth ((setPlayersPoints (setPlayersPoints (setPlayerCoords n (x+1,y)) (Eat Fantasma)) (Eat Grande))) : updatePlay p l (Eat Fantasma) ns
                                                                                                                                else if o==D then mouth ((setPlayersPoints (setPlayerCoords n (x+1,y)) (Eat Fantasma))) : updatePlay p l (Eat Fantasma) ns
                                                                                                                                         else if o==U && getpeca (x-1,y) l == Food Little then mouth ((setPlayersPoints (setPlayersPoints (setPlayerCoords n (x-1,y)) (Eat Fantasma)) (Eat Pequena))) : updatePlay p l (Eat Fantasma) ns
                                                                                                                                                  else if o==U && getpeca (x-1,y) l == Food Big then mouth ((setPlayersPoints (setPlayersPoints (setPlayerCoords n (x-1,y)) (Eat Fantasma)) (Eat Grande))) : updatePlay p l (Eat Fantasma) ns
                                                                                                                                                           else mouth ((setPlayersPoints (setPlayerCoords n (x-1,y)) (Eat Fantasma))) : updatePlay p l (Eat Fantasma) ns      
updatePlay p@(Move i o) l (MoverVazio) (n:ns) = let (x,y) = getPlayerCoords n 
                                                in if i /= getPlayerID n then n : updatePlay p l (MoverVazio) ns
                                                        else if o==R then mouth ((setPlayerCoords n (x,y+1))) : updatePlay p l (MoverVazio) ns
                                                                 else if o==L then mouth ((setPlayerCoords n (x,y-1))) : updatePlay p l (MoverVazio) ns 
                                                                          else if o==D then mouth ((setPlayerCoords n (x+1,y))) : updatePlay p l (MoverVazio) ns
                                                                                   else mouth ((setPlayerCoords n (x-1,y))) : updatePlay p l (MoverVazio) ns
updatePlay p@(Move i o) l (DieG) (n:ns) = let j = getPlayerGhostPlay l p (n:ns)
                                              d = getPlayerID j 
                                          in updatePlay (Move d o) l (Eat Fantasma) (n:ns)
updatePlay p@(Move i o) l (Eat Jogador) (n:ns) = let j = getPlayerGhostPlay l p (n:ns)
                                                     d = getPlayerID j 
                                          in updatePlay (Move d o) l (Die) (n:ns)

-- | Devolve o Pacman envolvido na jogada do fantasma
getPlayerGhostPlay :: Maze -> Play -> [Player] -> Player
getPlayerGhostPlay l p@(Move i o) jg = let (x,y) = getPlayerCoords (choosePlayer i jg)
                                       in if o==R then choosePlayerWithCoords (x,y+1) jg
                                              else if o==L then choosePlayerWithCoords (x,y-1) jg
                                                        else if o==U then choosePlayerWithCoords (x-1,y) jg
                                                                 else choosePlayerWithCoords (x+1,y) jg                                                                            

-- | Atualiza o estado do fantasma que é comido
updatePlayFantasmas :: Maze -> Play -> [Player] -> Player
updatePlayFantasmas l p@(Move i o) jg = let (x,y) = getPlayerCoords (choosePlayer i jg)
                                        in if o==R then (setPlayerCoords (setPlayerState (choosePlayerWithCoords (x,y+1) jg) (Eat Fantasma)) ((div (length l) 2),(div (length ((!!) l 0))2)))
                                              else if o==L then (setPlayerCoords (setPlayerState (choosePlayerWithCoords (x,y-1) jg) (Eat Fantasma)) ((div (length l) 2),(div (length ((!!) l 0))2)))
                                                      else if o==D then (setPlayerCoords (setPlayerState (choosePlayerWithCoords (x+1,y) jg) (Eat Fantasma)) ((div (length l) 2),(div (length ((!!) l 0))2)))
                                                               else (setPlayerCoords (setPlayerState (choosePlayerWithCoords (x-1,y) jg) (Eat Fantasma)) ((div (length l) 2),(div (length ((!!) l 0))2)))    

-- * Atualização do labirinto

-- | Dada uma PlayAction altera o Maze conforme o tipo da mesma 
updateMaze :: Player -> PlayAction -> Maze -> Maze 
updateMaze j@(Pacman (PacState (id,(x,y),v,o,z,l) tm m pm)) p maze = case p of Die -> maze
                                                                               DirecaoMuda -> maze
                                                                               Collide -> maze
                                                                               otherwise -> replaceElemInMaze (getPlayerCoords j) (Empty) maze
updateMaze (Ghost (GhoState (a,(x,y),z,t,u,li) q )) p maze = maze


-- * Função final da Tarefa 2

-- | Efetua uma jogada para um determinado jogador        
play :: Play -> State -> State
play p@(Move n o) e@(State m js le) = let j = getPlayerGhostPlay m p js
                                          d = getPlayerID j 
                                      in if (acaoPiece p e) == Eat Fantasma then State (updateMaze (choosePlayer n js) (Eat Fantasma) m) (updatePlay p m (Eat Fantasma) (insereElem (updatePlayFantasmas m p js) (removeElem p js))) le
                                             else if (acaoPiece p e) == DieG then State (updateMaze j (Eat Fantasma) m) (updatePlay p m (Eat Fantasma) (insereElem (updatePlayFantasmas m (Move d o) js) (removeElem (Move d o) js))) le
                                                       else  State (updateMaze (choosePlayer n js) (acaoPiece p e) m) (updatePlay p m (acaoPiece p e) js) le 


-- * Funções de teste


-- | Testa se os fantasmas de uma lista têm o mesmo estado
sameMode :: [Player] -> Bool
sameMode [] = True
sameMode [x] = True
sameMode (x:y:xs) = getGhostMode x == getGhostMode y && sameMode (y:xs)

-- | Teste (movimentação simples para a direita)
moveright :: Play -> State -> Bool
moveright p@(Move n R) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js) 
                                           in getPlayerCoords (choosePlayer n (getPlayersList (play p e))) == (x,y+1) 
-- | Teste (movimentação simples para a esquerda)
moverleft :: Play -> State -> Bool
moverleft p@(Move n L) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js) 
                                           in getPlayerCoords (choosePlayer n (getPlayersList (play p e))) == (x,y-1) 
-- | Teste (movimentação simples para baixo)
moverdown :: Play -> State -> Bool
moverdown p@(Move n D) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js)                                          
                                           in getPlayerCoords (choosePlayer n (getPlayersList (play p e))) == (x+1,y)
-- | Teste (movimentação simples para cima)                                           
moverup :: Play -> State -> Bool
moverup p@(Move n U) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js)
                                         in getPlayerCoords (choosePlayer n (getPlayersList (play p e))) == (x-1,y)
-- | Teste (colisão com parede)
colisao :: Play -> State -> Bool 
colisao p@(Move n o) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js)
                                         in getPlayerCoords (choosePlayer n (getPlayersList (play p e))) == (x,y)
-- | Teste (passar túnel da direita)
tuneldireita :: Play -> State -> Bool 
tuneldireita p@(Move n o) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js)
                                              in getPlayerCoords (choosePlayer n (getPlayersList (play p e))) == (x,0)
-- | Teste (passar tunel da esquerda)
tunelesquerda :: Play -> State -> Bool 
tunelesquerda p@(Move n o) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js)
                                               in getPlayerCoords (choosePlayer n (getPlayersList (play p e))) == (x,((length ((!!) m 0))-1))
-- | Teste (passar tunel da direita e voltar)
tunelvoltar :: Play -> State -> Bool 
tunelvoltar p@(Move n o) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js)
                                             in getPlayerCoords (choosePlayer n (getPlayersList (play p e))) == (x,0) 
-- | Teste (mover para a direita e comer comida pequena)
comerpequena :: Play -> State -> Bool
comerpequena p@(Move n o) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js)
                                                  po = getPlayerPoints (choosePlayer n js)
                                               in (moveright p e) && getPlayerPoints (choosePlayer n (getPlayersList (play p e))) == po+1
-- | Teste (mover para a esquerda e comer comida grande && fantasmas em modo Dead)                                                 
comergrande :: Play -> State -> Bool
comergrande p@(Move n o) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js)
                                                 po = getPlayerPoints (choosePlayer n js)
                                             in (moverleft p e) && (getPlayerPoints (choosePlayer n (getPlayersList (play p e))) == po+5) && (getGhostMode (head (onlyfantasmas (getPlayersList (play p e)))) == Dead) && (sameMode (onlyfantasmas (getPlayersList (play p e))))    
-- | Teste (colisão com fantasma Alive e perder uma vida)
baterfantasma :: Play -> State -> Bool
baterfantasma p@(Move n o) e@(State m js le) = let li = getPlayerLives (choosePlayer n js)
                                               in getPlayerLives (choosePlayer n (getPlayersList (play p e))) == li-1
-- | Teste (ter 0 vidas e colisao com fantasma e passar para modo Dying)
morrer :: Play -> State -> Bool
morrer p@(Move n o) e@(State m js le) = getPacmanMode (choosePlayer n (getPlayersList (play p e))) == Dying
-- | Teste (mover para baixo e comer fantasma && pontuação certa && fantasma renasce na casa em modo Alive)
comerfantasma :: Play -> State -> Bool
comerfantasma p@(Move n o) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js)
                                                   po = getPlayerPoints (choosePlayer n js)
                                               in (moverdown p e) && (getPlayerPoints (choosePlayer n (getPlayersList (play p e))) == po+10) && (getPlayerID (choosePlayerWithCoords (x+1,y) js) == getPlayerID (choosePlayerWithCoords ((div (length m) 2),(div (length ((!!) m 0))2)) (getPlayersList (play p e)))) && (getGhostMode (choosePlayerWithCoords ((div (length m) 2),(div (length ((!!) m 0))2)) (getPlayersList (play p e))) == Alive)
-- | Teste (mover para baixo e comer fantasma em casa com peça Food Little && pontuação certa && fantasma renasce na casa em modo Alive)
comerfantasmaEcomidapequena :: Play -> State -> Bool
comerfantasmaEcomidapequena p@(Move n o) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js)
                                                                 po = getPlayerPoints (choosePlayer n js)
                                                             in (moverdown p e) && (getPlayerPoints (choosePlayer n (getPlayersList (play p e))) == po+11) && (getPlayerID (choosePlayerWithCoords (x+1,y) js) == getPlayerID (choosePlayerWithCoords ((div (length m) 2),(div (length ((!!) m 0))2)) (getPlayersList (play p e)))) && (getGhostMode (choosePlayerWithCoords ((div (length m) 2),(div (length ((!!) m 0))2)) (getPlayersList (play p e))) == Alive)
-- | Teste (mover para baixo e comer fantasma em casa com peça Food Big && pontuação certa && fantasma renasce na casa em modo Alive)
comerfantasmaEcomidagrande :: Play -> State -> Bool
comerfantasmaEcomidagrande p@(Move n o) e@(State m js le) = let (x,y) = getPlayerCoords (choosePlayer n js)
                                                                po = getPlayerPoints (choosePlayer n js)
                                                            in (moverdown p e) && (getPlayerPoints (choosePlayer n (getPlayersList (play p e))) == po+15) && (getPlayerID (choosePlayerWithCoords (x+1,y) js) == getPlayerID (choosePlayerWithCoords ((div (length m) 2),(div (length ((!!) m 0))2)) (getPlayersList (play p e)))) && (getGhostMode (choosePlayerWithCoords ((div (length m) 2),(div (length ((!!) m 0))2)) (getPlayersList (play p e))) == Alive)

