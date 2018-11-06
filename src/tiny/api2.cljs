(ns tiny.api2
  (:require-macros [tiny.api2 :refer [e t]])
  (:require react
            tiny.react-checks
            tiny.guard))

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
(defn internal-t
  "** WARNING: VERY EXPERIMENTAL **

  Trimmed down version of react.createElement that plays nice with cljs
  datastructures as props.

  This voluntarily does not support children. If you want to pass other
  components through here, you may pass them inside the config map and use the
  render props pattern."
  [type config source-infos]
  (cond-> #js {:$$typeof REACT_ELEMENT_TYPE
               :type type
               :key (:key config)
               :ref (:ref config)
               :props config,

               ;; does this work?
               :_owner react/__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED.ReactCurrentOwner.current}

    (identical? js/goog.DEBUG true)
    (tiny.react-checks/add-dev-props
     ;; TODO check if `this` is what react expects?. What do we get in the
     ;; devtools with this?
     (doto (this-as self
             (js/console.log "this?" self)
             self) js/console.log)
     (doto source-infos js/console.log))))


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


;; (js/console.log (t plop {:text "lala"}))

(defn with-raw-react-element-clone []
  (let [e1 (e [:p {:key "original"}
               "ORIGINAL OR CLONE?"])
        e2 (react/cloneElement e1 #js {:key "clone"})]

    (e #js [e1 e2])))

(defn with-custom-react-element-clone []
  (let [t1 (t plop {:key "t-original" :text "lala"})
        t2 (react/cloneElement t1 #js {:key "t-clone"})]
    #js [t1 t2]))


(comment
  (let [t1 (t plop {:key "t-original" :text "lala"})]
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
         "INCREMENT PLOP??"]])))

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

         (t with-raw-react-element-clone)

         [:div [:p "trigger error: " (if trigger-error? "YES" "NO")]
          [:button {:on-click #(trigger-error! true)}
           "TRIGGER ERROR"]]

         ;; This voluntarily triggers an error, by "react cloning" one our custom react component
         (when trigger-error?
           (t with-custom-react-element-clone))

         (t plop {:text "lalala"})

         [:div [:p "base prop for last child:" counter]
          [:button {:on-click #(set-counter (inc counter))}
           "INCREMENT base prop"]]

         (t plouf {:key counter
                   :n counter})]))))




















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
