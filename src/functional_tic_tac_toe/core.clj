(ns functional-tic-tac-toe.core
  (:gen-class))

(defn new-game []
  [(into [] (map str (range 1 10))) "X"])

(defn- legal-input? [input]
  (some (partial = input) #{"1" "2" "3" "4" "5" "6" "7" "8" "9"}))

(defn move [[board player :as game] move]
  (if (legal-input? move)
    (let [board-index (- (Integer/parseInt move) 1)]
      (if (= (nth board board-index) move)
        [(assoc board board-index player)
         (if (= player "X") "O" "X")]
        game)) game))

(defn- check-horizontal [board]
  (let [winner (for [x [0 3 6]]
                 (let [first (get board x)]
                   (if (and (= first (get board (+ x 1)))
                            (= first (get board (+ x 2))))
                     first "")))]
    (remove empty? winner)))

(defn- check-vertical [board]
  (let [winner (for [x [0 1 2]]
                 (let [first (get board x)]
                   (if (and (= first (get board (+ x 3)))
                            (= first (get board (+ x 6))))
                     first "")))]
    (remove empty? winner)))

(defn- check-diaganal [board]
  (let [center (get board 4)]
    (cond
     (and (= center (get board 0)) (= center (get board 8))) [center]
     (and (= center (get board 2)) (= center (get board 6))) [center]
     :default nil)))

(defn check-winner [[board _]]
  (first (concat (check-horizontal board)
                 (check-vertical board)
                 (check-diaganal board))))

(defn board-full? [[board player]]
  (= 2 (count (set board))))

(defn game-over? [[board player]]
  (or (not (empty? (check-winner [board player])))
      (board-full? [board player])))

(def top-bottom "\n   |   |   \n")

(defn row-to-string [board row-num]
  (let [[a b c] (take 3 (drop (* (- row-num 1) 3) board))]
    (str " " a " | " b " | " c )))

(def vertical-spacer "\n-----------\n")

(defn render-board [[board player]]
  (str top-bottom (row-to-string board 1) vertical-spacer
       (row-to-string board 2)   vertical-spacer
       (row-to-string board 3) top-bottom))

(defn get-move []
  (do
    (println "Enter # of square to move")
    (read-line)))

(defn play-game [the-game]
  (loop [game the-game]
    (println (render-board game))
    (if (game-over? game)
          (if-let [winner (check-winner game)]
            (println (str "Congrats to " winner))
            (println "tie game"))
          (recur (move game (get-move))))))

(defn -main [& args]
  (play-game (new-game)))
