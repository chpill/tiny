(ns tiny.ex1
  (:require [tiny.api :refer [e]]))


(tiny.api/defc Plop [{:keys [a-prop] :as props}]
  (e "p" nil "PLOP! " a-prop))




;; (js/console.log (js* "debugger; ~{}" 300))

;; (js/alert "plop")
