 {- |

 = Introdução

 A Tarefa 6 implicava a criação de um bot que jogasse Pacman. Depois de feitas as movimentações dos fantasmas foi fácil notar a semelhança quer em estratégias de execução quer em estratégias de pensamento para produção das jogadas do bot o que
 facilitou imenso a concretização da tarefa, sendo esta a última e estando o prazo de entrega a aproximar-se.


 = Objetivos

 O objetivo primário desta tarefa passava, sem sombra de dúvida, por fazer o melhor bot possível. Para isso foi necessário definir estratégias antes de passar à escrita do código em si,
 e após um período de deliberação e diversas simulações em papel acabamos por finalmente nos decidir pela qual achamos a melhor estratégia.

 Em vez de o nosso bot priorizar a busca por comida grande, achamos melhor priorizar a sobrevivência, sendo que ,eventualmente, estando o Pacman vivo acabaria por mais cedo ou mais tarde encontrar-se perante uma comida grande
 e a partir daí começar a busca pelos fantasmas. 
 Para facilitar a escrita do código acabamos por aderir à estratégia da Tarefa 5 e aqui também definimos dois tipos de funções de jogadas, particularmente a normalPlay e a megaPlay para o a fugir e a perseguir os fantasmas, respetivamente.

 A estratégia usada para o bot fugir consiste no cálculo das distâncias deste a todos os fantasmas, e a partir daí começar a fuga ao que se encontra mais próximo, movimentando-se na direção contrária do mesmo. 
 Já em relação às jogadas de perseguição aos fantasmas optamos por novamente calcular qual o fantasma mais próximo e fazer então jogar o bot na direção do mesmo, visto que estando o Pacman em modo Mega, os fantasmas movem-se com o metade da velocidade.   
 Tivemos ainda em atenção os casos em que o bot e o fantasma se encontram na mesma linha ou coluna, e verificando também outros detalhes fulcrais nessas situações tais como verificar quem é que se encontra à esquerda de quem, em cima de quem, etc. 
 Para além disso, atentamos ao choque contra uma parede, fazendo então o bot rodar no sentido dos ponteiros do relógio.

  
 = Discussão e conclusão

 Esta foi a única tarefa que acabamos por não testar graficamente, por isso é nos dificil avaliar se o comportamento da mesma vai de encontro ao que era estipulado por nós. Consideramos os testes de funcionamento do código uma parte fulcral do 
 projeto e desde o inicio nos acompanharam e nos permitiram encontrar erros que de outra forma seria muito dificil de encontar. Sendo assim apenas nos pudemos guiar por testes feitos à mão e constantes simulações de jogadas. 

 Para concluir, apesar da falta de testes em concreto, acreditamos ter sido minimamente bem sucedidos naquilo que pretendíamos alcançar. Temos a plena consciência de que o nosso bot não está a trabalhar no seu máximo potencial, e de que haveriam ainda 
 variadas hipóteses a serem implementadas, mas ainda assim conseguimos notar vários aspetos positivos.

 Com o fim desta Tarefa termina também o projeto. Sem dúvida que de todo o semestre foi o que nos deu mais trabalho e dores de cabeça, mas foi também o que mais prazer nos deu a fazer.
 Acabamos este longo trajeto com uma sensação de realização por completo, e sem sombra de dúvida, que retiramos diversos ensinamentos não só em termos de programação, mas também entre outros aspetos como os métodos de trabalho corretos e em particular em equipa,
 que levaremos connosco nas nossas vidas.

 -}
 
module Tarefa6 where 

import Types
import Tarefa2
import Tarefa5

-- * Funções gerais da Tarefa 6

-- | Player na metade superior do mapa 
metadeA :: Maze -> Player -> Bool
metadeA m p = let c = getPlayerCoords p
                  a = length m                   
              in (div a 2) <= fst c    

-- | Player na metade mais à direita do mapa
metadeL :: Maze -> Player -> Bool
metadeL m p = let c = getPlayerCoords p
                  l = length (head m)
              in (div l 2) <= snd c   

-- | Estuda a existência de fantasmas na mesma coluna que o Pacman
mesmaColuna :: Player -> [Player] -> Bool
mesmaColuna j [] = False  
mesmaColuna j l  = let c = getPlayerCoords j 
                       (x:xs) = onlyfantasmas l 
                   in snd c == snd (getPlayerCoords x) || mesmaColuna j xs 

-- | Estuda a existência de fantasmas na mesma linha que o Pacman
mesmaLinha :: Player -> [Player] -> Bool
mesmaLinha j [] = False
mesmaLinha j l  = let c = getPlayerCoords j 
                      (x:xs) = onlyfantasmas l 
                   in fst c == fst (getPlayerCoords x) || mesmaColuna j xs

-- | Estuda a existência de fantasmas na mesma linha e à esquerda do Pacman
fantasmaEsquerda :: Player -> [Player] -> Bool
fantasmaEsquerda j [] = False
fantasmaEsquerda j l = let c = getPlayerCoords j
                           (x:xs) = onlyfantasmas l
                       in (mesmaLinha j l && snd (getPlayerCoords x) < snd c) || fantasmaEsquerda j xs

-- | Estuda a existência de fantasmas na mesma linha e à direita do Pacman
fantasmaDireita :: Player -> [Player] -> Bool
fantasmaDireita j [] = False
fantasmaDireita j l = let c = getPlayerCoords j
                          (x:xs) = onlyfantasmas l
                      in (mesmaLinha j l && snd (getPlayerCoords x) > snd c) || fantasmaDireita j xs

-- | Estuda a existência de fantasmas na mesma coluna e acima do Pacman
fantasmaCima :: Player -> [Player] -> Bool
fantasmaCima j [] = False
fantasmaCima j l = let c = getPlayerCoords j
                       (x:xs) = onlyfantasmas l
                   in (mesmaColuna j l && fst (getPlayerCoords x) < fst c) || fantasmaCima j xs

-- | Estuda a existência de fantasmas na mesma linha e abaixo do Pacman
fantasmaBaixo :: Player -> [Player] -> Bool
fantasmaBaixo j [] = False
fantasmaBaixo j l = let c = getPlayerCoords j
                        (x:xs) = onlyfantasmas l
                    in (mesmaColuna j l && fst (getPlayerCoords x) > snd c) || fantasmaBaixo j xs                    

-- | Deteta qual o fantasma mais próximo do Pacman
ghostCloser :: Player -> [Player] -> Player
ghostCloser j l = let (x:xs) = onlyfantasmas l 
                  in chooseAC xs x
           where chooseAC [] n = n
                 chooseAC (x:xs) n = if (difCoords j x) < (difCoords j n) then chooseAC xs x else chooseAC xs n  

-- | Muda a orientação do Pacman no sentido dos ponteiros do relógio
playDireita :: Int -> State -> Maybe Play
playDireita n (State m js le) = let j = choosePlayer n js
                                    o = getPlayerOrientation j
                                in case o of U -> Just (Move n R)
                                             R -> Just (Move n D)
                                             D -> Just (Move n L)
                                             otherwise -> Just (Move n U)

-- * Pacman em modo Normal

-- | Jogadas para o Pacman em modo Normal
escapeMove :: Int -> State -> Maybe Play 
escapeMove n (State m js le) = let j = choosePlayer n js
                                   o = getPlayerOrientation (ghostCloser j js)
                               in if mesmaLinha j js && metadeA m j then Just (Move n D)
                                      else if mesmaLinha j js then Just (Move n U)
                                               else if mesmaColuna j js && metadeL m j then Just (Move n L)
                                                       else if mesmaColuna j js then Just (Move n R) 
                                                                else if o==R then Just (Move n L)
                                                                        else if o==L then Just (Move n R)
                                                                                else if o==U then Just (Move n D)
                                                                                        else Just (Move n U) 

-- | Conjunto de jogadas para Pacman em modo normal
normalPlay :: Int -> State -> Maybe Play
normalPlay n s@(State m js le) = let o = getPlayerOrientation (choosePlayer n js)
                                 in if acaoPiece (Move n o) s == Collide then playDireita n s else escapeMove n s                     

-- * Pacman em modo Mega

-- | Jogadas para o Pacman em modo Mega 
chaseMove :: Int -> State -> Maybe Play
chaseMove n (State m js le) = let j = choosePlayer n js
                                  l = onlyfantasmas js
                                  o = getPlayerOrientation (ghostCloser j js)
                              in if fantasmaEsquerda j js then Just (Move n L)
                                      else if fantasmaDireita j js then Just (Move n D)
                                                else if fantasmaCima j js then Just (Move n U)
                                                           else if fantasmaBaixo j js then Just (Move n D)
                                                                     else Just (Move n o)

-- | Conjunto de jogadas para o Pacman em modo Mega
megaPlay :: Int -> State -> Maybe Play
megaPlay n s@(State m js le) = let o = getPlayerOrientation (choosePlayer n js)
                               in if acaoPiece (Move n o) s == Collide then playDireita n s else chaseMove n s  


-- * Função final da Tarefa 6

-- | Devolve uma possivel jogada a realizar pelo robô
bot :: Int -> State -> Maybe Play
bot n s@(State m js le) = let j = choosePlayer n js
                          in if getPacmanMode j == Dying then Nothing
                                  else if getPacmanMode j == Normal then normalPlay n s
                                            else megaPlay n s
