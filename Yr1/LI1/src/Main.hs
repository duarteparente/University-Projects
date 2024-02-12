module Main where

import Data.Time.Clock.POSIX
import Control.Monad.IO.Class
import UI.NCurses
import Types
import FileUtils
import Tarefa1
import Tarefa2
import Tarefa3
import Tarefa4
import Tarefa5
import Tarefa6

defaultDelayTime = 250 -- 250 ms

loadManager :: Manager
loadManager = ( Manager (loadMaze "maps/1.txt") 0 0 0 0 defaultDelayTime )

-- | Devolve um jogador rodado após pressinar uma tecla
rotatePLayer :: Key -> Player -> Player
rotatePLayer k p = case k of KeyUpArrow -> changeOrientation U p
                             KeyDownArrow -> changeOrientation D p
                             KeyLeftArrow -> changeOrientation L p
                             KeyRightArrow -> changeOrientation R p

updateControlledPlayer :: Key -> Manager -> Manager
updateControlledPlayer k m@(Manager s pid step bf delt del) =
        let jg  = choosePlayer pid (getPlayersList s)
            jg' = rotatePLayer k jg
        in Manager (updatePLayersState m jg') pid step bf delt del 

updateScreen :: Window  -> ColorID -> Manager -> Curses ()
updateScreen w a man =
                  do
                    updateWindow w $ do
                      clear
                      setColor a
                      moveCursor 0 0 
                      drawString $ show (state man)
                    render
     
currentTime :: IO Integer
currentTime = fmap ( round . (* 1000) ) getPOSIXTime

updateTime :: Integer -> Manager -> Manager
updateTime now man = man {delta = (delta man) + (now - before man), before = now}

resetTimer :: Integer -> Manager -> Manager
resetTimer now man = man {delta = 0, before = now}

nextFrame :: Integer -> Manager -> Manager
nextFrame now man = let update = (resetTimer now man)
                    in update { state = (passTime (step man) ((state man))) , step = (step man) +1 }


loop :: Window -> Manager -> Curses ()
loop w man@(Manager s pid step bf delt del ) = do 
  color_schema <- newColorID ColorWhite ColorDefault  10
  now <- liftIO  currentTime
  updateScreen w  color_schema man
  if gameFinished (choosePlayer pid (getPlayersList s)) 
    then return ()
    else if ( delt > del )
             then loop w $ nextFrame now man
             else do
                  ev <- getEvent w $ Just 0
                  case ev of
                        Nothing -> loop w (updateTime now man)
                        Just (EventSpecialKey arrow ) -> loop w $ updateControlledPlayer arrow (updateTime now man)
                        Just ev' ->
                            if (ev' == EventCharacter 'q')
                            then return ()
                                 else loop w (updateTime now man)

main :: IO ()
main =
  runCurses $ do
    setEcho False
    setCursorMode CursorInvisible
    w <- defaultWindow
    loop w loadManager

