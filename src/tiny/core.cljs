(ns tiny.core
  (:require react-dom
            goog.object
            [tiny.sub :as sub]
            [tiny.api :as tiny-api :refer [e t]]
            [tiny.ex1]))


(js/console.log "============= loading core =============")

(comment
  "GOAL"

  (defc total-contacts [{::sub/keys [total-contact]}]
    (e "p" nil (case total-contact
                 :loading "LOADING"
                 1 "You have 1 contact"
                 (str "You have" total-countact "contacts")))))




(defn fn-cpt [arg]
  (js/console.log "fn-cpt arg type:" arg)
  (e "p" nil (:a arg)))



(goog.object/set fn-cpt "tiny?" true)


(def *stub-store (atom {:description "FROM STORE!"}))

(def store-context (js/React.createContext))
(def Provider (.-Provider store-context))
(def Consumer (.-Consumer store-context))


(tiny.api/defc Plop-into-context []
  (e Consumer nil
     (fn [*store]
       (e "p" nil "Context contained: "
          (-> *store deref :description)))))


(defn app []
  (e Provider #js {:value *stub-store}
     (e "div" #js {:className "Plop"
                   :style #js {:backgroundColor "papayawhip"
                               :margin 10}}
        (e "p" nil "texte 1")
        (e "p" nil "texte 2")
        (t Plop-into-context nil)
        (t tiny.ex1/Plop {:a-prop "PLOUF PROP"})
        (t fn-cpt {:a "b"}))))


(defonce *load-counter (atom 0))
(js/console.log "load-counter:"
                (swap! *load-counter inc))


(defn app-div! [dom-id]
  (or (js/document.getElementById dom-id)
      (doto (js/document.createElement "div")
        (goog.object/set "id" dom-id)
        document.body.firstElementChild.prepend)))


(do (js/console.log "ReactDOM.render")
    (js/ReactDOM.render (app)
                        (app-div! "tiny-demo-app")))
