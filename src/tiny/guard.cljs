(ns tiny.guard
  (:require react))


;; We cannot use create-react-class, because it only allows to define instance
;; methods. getDerivedStateFromError is a class (static) method
(def CloneWarnComponent
  (let [ctor (doto (fn [props]
                     (this-as this
                       (.call react/Component this props)))
               (goog/inherits react/Component))
        prototype (goog.object/get ctor "prototype")]

    (goog.object/set prototype "render" (fn guard-render []
                                          (do (js/console.log "GUARD RENDER")
                                              (this-as this
                                                (if (and this.state
                                                         this.state.error)
                                                  (react/createElement "h1" #js {:text-align "center"} "ERROR")
                                                  this.props.children)))))

    (goog.object/set prototype "componentDidCatch"
                     (fn [error info]
                       (this-as this
                         (js/console.log "GUARD CAUGHT:")
                         (js/console.log "message" (.-message error))
                         (js/console.log error)
                         (js/console.log info))))


    (goog.object/set ctor "getDerivedStateFromError"
                     (fn [error]

                       (js/console.log "GUARD getDerivedStateFromError")
                       ;;(js/console.log error)
                       #js {:error error}))

    (goog.object/set ctor "displayName" "tiny guard component")


    ctor))



(defn warn-on-clone [child]
  (react/createElement CloneWarnComponent #js {} child))
