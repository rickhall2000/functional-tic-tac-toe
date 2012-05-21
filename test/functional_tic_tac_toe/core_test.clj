(ns functional-tic-tac-toe.core-test
  (:use clojure.test
        functional-tic-tac-toe.core))

(def initial-state [["1" "2" "3" "4" "5" "6" "7" "8" "9"] "X"])

(def full-board ["X" "X" "O" "O" "O" "X" "X" "O" "X"])

(def empty-board "\n   |   |   \n 1 | 2 | 3\n-----------\n 4 | 5 | 6\n-----------\n 7 | 8 | 9\n   |   |   \n")

(deftest new-game-test
  (testing "New game should return the empty board"
    (is (= initial-state (new-game)))))

(deftest move-test
  (testing "making a move should update the board and switch the player"
    (is (=  [ ["1" "2" "3" "4" "X" "6" "7" "8" "9"] "O"] (move initial-state "5") ) )))

(deftest multi-move-test
  (testing "Making sure a slew of moves gets the right result"
    (is (=  [["O" "X" "X" "4" "X" "6" "O" "8" "9"] "O" ]
            (-> initial-state
                (move "5") (move "1")
                (move "3") (move "7")
                (move "2"))))))

(deftest check-winner-test
  (testing "Making sure the game knows when someone has won"
    (is (= "X" (check-winner [["X" "X" "X" "4" "5" "6" "7" "8" "9"] "X"])))
    (is (= "O" (check-winner [["O" "2" "3" "O" "5" "6" "O" "8" "9"] "X"])))
    (is (= "X" (check-winner [["1" "2" "X" "O" "X" "6" "X" "8" "9"] "X"])))
    (is (= nil (check-winner [["1" "2" "O" "O" "X" "6" "X" "8" "9"] "X"])))
    (is (= "O" (check-winner [["O" "2" "3" "4" "O" "6" "7" "8" "O"] "X"])))))

(deftest game-over-test
  (testing "Does the game know when it is over?"
    (is (= true (game-over? [["X" "X" "X" "4" "5" "6" "7" "8" "9"] "X"])))
    (is (= false (game-over? [["1" "X" "O" "O" "O" "X" "X" "O" "X"] "X"])))
    (is (= true (game-over? [full-board "X"])))))

(deftest render-board-test
  (testing "This will test that the board is displaying correctly"
    (is (= empty-board (render-board initial-state)))
    (is (= 5 (count
              (filter (partial = \X)
                      (seq (render-board [full-board "X"]))))))))

(deftest invaolid-move-test
  (testing "Calling move with an invalid command should return the original"
    (is (= initial-state (move initial-state "Q")))))

(deftest duplicate-move-test
  (testing "Calling move on the same square twice shouldn't change the board a second time"
    (is (= (first (move initial-state "5")) (first (move (move initial-state "5") "5"))))))
