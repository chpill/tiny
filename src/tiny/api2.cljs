(ns tiny.api2
  (:require-macros [tiny.api2 :refer [e defc]])
  (:require react))

(def react-create-element react/createElement)



(defc plop [{:keys [text]}]
  (e [:div {:style {:background-color "red"
                    :height "100px"
                    :width  "800px"}}
      [:p text]]))

(defc plouf [n]
  (let [[counter set-counter] [n js/console.log]
        #_(react/useState n) ]
    (e [:div {:key n}
        [:p (str "My argument is: " counter)]
        [:button {:on-click #(set-counter (inc counter))}
         "INCREMENT"]])))

(defc app []
  (let [[counter, set-counter] (react/useState 42)]
    (e [:div [:p "2 children follow"]
        [:div [:p "base prop for last child:" counter]
         [:button {:on-click #(set-counter (inc counter))}
          "INCREMENT base prop"]]
        (plop {:text "lalala"})
        (plouf counter)])))

























(comment

  (macroexpand-1 '(e [plop {:text "lalala" :lolo {:lilil [:a :b :c]}}]))

  (macroexpand-1 '(e [:div {:style {:background-color "red"}}]))

  (macroexpand-1 '(e (let [color "red"]
                       [:div {:style {:background-color red}}])))


  (macroexpand-1 '(defc plop [a]
                    [:div {:style {:background-color "red"}}]))

  (clojure.core/defn plop [props__27682__auto__]
    (clojure.core/let [[a] (.-args props__27682__auto__)]
      (tiny.api2/e [:div {:style {:background-color "red"}}])))









  (macroexpand-1 '(e [plop {:plop {:background-color "red"}}]))
  (tiny.api2/react-create-element plop
                                  (js* "{'plop':~{}}"
                                       (js* "{'background-color':~{}}" "red")))


  (macroexpand-1 '(e [:> plop {:plop {:background-color "red"}}]))

  (tiny.api2/react-create-element plop
                                  (js* "{'plop':~{}}"
                                       (js* "{'background-color':~{}}" "red")))


  )
