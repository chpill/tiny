(ns tiny.api2
  (:require-macros [tiny.api2 :refer [e]])
  (:require react
            [tiny.react-checks]))

(def react-create-element react/createElement)

(def runtime-has-symbols? (js* "typeof Symbol === 'function' && Symbol.for;"))

(def REACT_ELEMENT_TYPE (if runtime-has-symbols?
                          (.for js/Symbol "react.element")
                          0xeac7))

;; TODO investigate: would this work? constant are better for dead code elimination
;; see HTTP://gist.github.com/mfikes/93cb77921ee35c2328b92af441ee5393
(defonce ^:const dev? js/goog.DEBUG)


;; TODO add dev validation for config, key and ref?
;; TODO2 macro to do some of this at compile time?
(defn t
  ([type] (t type {}))
  ([type config & children]
   ;; Meta would not work, the type must be a function or an instance of Component
   (cond-> #js {:$$typeof REACT_ELEMENT_TYPE
                :type type
                :key (:key config)
                :ref (:ref config)
                :props (if (empty? children)
                         config
                         (assoc config :children children)),

                ;; does this work?
                :_owner react/__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED.ReactCurrentOwner.current}

     ;; add similar dev props
     (identical? js/goog.DEBUG true)
     ;; TODO: what can we do for those dev props?
     (tiny.react-checks/add-dev-props nil ;; self
                                      nil ;; source
                                      ))))


(defn plop [{:keys [text]}]
  (e [:div {:style {:background-color "red"
                    :height "100px"
                    :width  "800px"}}
      [:p text]]))

(defn plouf [{:keys [n]}]
  (let [[counter set-counter]
        ;;[n js/console.log]
        (react/useState n)
        ]
    (e [:div {:key n}
        [:p (str "My argument is: " counter)]
        [:button {:on-click #(set-counter (inc counter))}
         "INCREMENT"]])))

(defn app []
  (let [[counter, set-counter] (react/useState 42)]
    (e [:div [:p "2 children follow"]
        [:div [:p "base prop for last child:" counter]
         [:button {:on-click #(set-counter (inc counter))}
          "INCREMENT base prop"]]

        (t plop {:text "lalala"})
        (t plouf {:key counter
                  :n counter})])))

























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
