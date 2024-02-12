 {- |

 = Introdução 

 A Tarefa 4 tem como objetivo reagir à passagem do tempo. Com uma primeira parte direcionada apenas para a transformação do jogo estático que possuíamos no final da 
 Tarefa 2, foi necessária a interação com a biblioteca NCurses que permite a alteração do estado do jogo através de inputs do próprio utilizador. Já a segunda parte 
 focava-se em determinados aspetos na movimentação de todos os jogadores de forma a tornar o jogo o mais póximo possível do original.

 = Objetivos

 Apesar de dividida em duas partes, a dificuldade de ambas em nada se assemelha e o comparativo de tempo gasto entre as duas é a prova disso mesmo. O verdadeiro entrave
 desta tarefa foi conseguir perceber a primeira parte, visto que sentimos uma dificuldade enorme em compreender tudo o que o controlo da passagem do tempo envolvia. Só conseguimos
 progredir verdadeiramente na tarefa quando os docentes ajudaram na definição das funções do módulo Main, mas a partir daí tudo o resto se tornou mais claro.

 Na segunda parte da tarefa, na parte ligada à movimentação pouco foi feito na prórpria Tarefa 4. Determinados aspetos, quer pedidos no enunciado quer alguns adicionados à nossa conta
 foram maioritariamente acrescentados à Tarefa 2, visto ser esta a tarefa que controla todas as envolvências de uma jogada.
 Primeiramente foi necessário a reformulação de algumas funções definidas anteriormente de forma a suportar jogadas dos fantasmas, necessárias para esta tarefa e para a Tarefa 5.
 Depois preocupamo-nos em garantir que existe a abertura e fecho da boca do Pacman a cada movimentação do mesmo, e só depois nos desbroçados nas velocidades.
 Essa parte não foi clara logo de seguida, mas assim que conseguimos perceber a relação entre o step e o tempo, tornou-se bastante fácil relacioná-lo com a velocidade que os players 
 devem assumir. Tendo uma atenção particular ao TimeMega (que definimos como 10 segundos) fazendo-o diminuir a cada segundo, e garantindo que os modos dos players voltam ao normal quando este
 chega a 0.  
 No nosso jogo como não implementámos um sistema de níveis só existem dois valores de velocidade. 

 Para além do que estava estipulado no enunciado acrescentamos ainda uma funcionalidade no módulo Main que termina o jogo quando o Pacman fica sem vidas (modo Dying), ou seja, o utilizador 
 perde o jogo. Tomamos ainda a liberdade de dar um reset nas posições dos players quando o Pacman morre, tal como no jogo original.
 É importante também salientar que depois de concluída a Tarefa 5 as jogadas para os fantasmas foram substituìdas pelas jogadas provisórias que tinhamos nesta mesma tarefa, tornando assim o jogo mais
 apelativo e desafiante.        

 = Discussão e conclusão

 Para concluir, esta foi a única tarefa que não conseguimos fazer tudo o que pretendíamos. O plano inicial para a tarefa continha a criação de um sistema de níveis, mas a não utilização do biblioteca
 Gloss acabou por nos fazer adiar a ideia, até acabarmos mesmo por não o fazer. Contudo, não deixamos de ficar satisfeitos com o resultado alcançado, já que para além de cobrirmos todo o enunciado, fomos 
 capazes de adicionar outras funcionalidades que ajudaram a melhorar o jogo.


 -}

module Tarefa4 where 

import System.Random
import Types
import Tarefa2
import Tarefa5

-- * Funções relativas ao timeMega

-- | Devolve um Player convertido ao estado normal 
setPlayerNormal :: Player -> Player
setPlayerNormal (Ghost (GhoState (x,(a,b),z,t,h,li) Dead )) = (Ghost (GhoState (x,(a,b),z,t,h,li) Alive ))
setPlayerNormal (Pacman (PacState (x,y,z,t,h,l) q c Mega )) = (Pacman (PacState (x,y,z,t,h,l) q c Normal )) 

-- | Atualiza uma lista de jogadores quando o timeMega acaba
timeMegaZero :: [Player] -> [Player]
timeMegaZero [] = []
timeMegaZero (x:xs) | isGhost x && isGhostDead x = setPlayerNormal x : timeMegaZero xs
                    | not (isGhost x) && isPacmanMega x = setPlayerNormal x : timeMegaZero xs
                    | otherwise = x: timeMegaZero xs

-- | Diminui o timeMega do Player
subtrai :: Player -> Player
subtrai p@(Pacman (PacState (x,y,z,t,h,l) q c Mega )) = (Pacman (PacState (x,y,z,t,h,l) (q-1) c Mega ))
                                                        
-- | Devolve o state atualizado conforme após um determinado número de iterações
mega :: Int -> Player -> State -> State 
mega i p@(Pacman (PacState (x,y,z,t,h,l) q c Mega )) s@(State m js le) = if q <= 0 then State m (timeMegaZero js) le 
                                                                             else if mod i 4 ==0 then State m (insereElem (subtrai p) (removePLayer x js)) le
                                                                                       else s

-- * Função que faz jogar todos os jogadores

-- | Devolve o state atualizado após a jogada de todos os jogadores
normalPlay :: [Player] -> State -> Int -> State
normalPlay [] state i = state
normalPlay (x:xs) state i | isPacmanMega x = normalPlay xs (mega i x (play (Move (getPlayerID x) (getPlayerOrientation x)) state)) i
                          | isGhostDead x && mod i 2 /=0 = normalPlay xs state i
                          | isGhost x = normalPlay xs (play (choosePlay (getPlayerID x) (ghostPlay state)) state) i 
                          | otherwise = normalPlay xs (play (Move (getPlayerID x) (getPlayerOrientation x)) state) i

-- * Função final da Tarefa 4

-- | Altera o estado do jogo em uma iteração
passTime :: Int  -> State -> State
passTime x s = let jg = getPlayersList s
               in normalPlay jg s x





