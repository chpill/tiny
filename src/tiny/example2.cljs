(ns tiny.example2
  (:require [tiny.api2 :refer [c e]]
            react))



(defn plop [{:keys [text]}]
  (e [:div {:style {:background-color "papayawhip"
                    ;; :height "100px"
                    :width  "800px"}}
      (for [x (range 4)]
        (identity
         (e [:p {:key x
                 :style {:text-align "center"
                         :color "green"}}
             text])))]))


;; (js/console.log (c plop {:text "lala"}))

(defn with-raw-react-element-clone []
  (let [e1 (e [:p {:key "original"}
               "ORIGINAL OR CLONE?"])
        e2 (react/cloneElement e1 #js {:key "clone"})]

    (e #js [e1 e2])))

(defn with-custom-react-element-clone []
  (let [t1 (c plop {:key "t-original" :text "lala"})
        t2 (react/cloneElement t1 #js {:key "t-clone"})]
    #js [t1 t2]))


(comment
  (let [t1 (c plop {:key "t-original" :text "lala"})]
    (react/cloneElement t1 #js {:key "t-clone"})))




(defn plouf
  "Example creating initial state from a prop"
  [{:keys [n]}]
  (let [[counter set-counter]
        ;;[n js/console.log]
        (react/useState n)
        ]
    (e [:div {:key n}
        [:p (str "My argument is: " counter)]
        [:button {:on-click #(set-counter (inc counter))}
         "increment leaf internal state"]])))

(macroexpand-1 '(e [:> tiny.guard/warn-on-clone "plop"]))

(macroexpand-1 '(e [:div]))

(defn app []
  (let [[counter set-counter] (react/useState 42)
        [trigger-error? trigger-error!] (react/useState false)]
    (tiny.guard/warn-on-clone
     (e [:div {:style {:display "flex"
                       :flex-direction "column"
                       :align-items "center"}}
         [:p "2 children follow"]

         (c with-raw-react-element-clone)

         [:div [:p "trigger error: " (if trigger-error? "YES" "NO")]
          [:button {:on-click #(trigger-error! true)}
           "TRIGGER ERROR"]]

         ;; This voluntarily triggers an error, by "react cloning" one our custom react component
         (when trigger-error?
           (c with-custom-react-element-clone))

         (c plop {:text "lalala"})

         [:div [:p "base prop for last child:" counter]
          [:button {:on-click #(set-counter (inc counter))}
           "increment base prop"]]

         (c plouf {:key counter
                   :n counter})]))))




(defn make-app [] (c app))
