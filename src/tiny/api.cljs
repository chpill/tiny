(ns tiny.api)
;; (comment
;;   (ns tiny.api
;;     (:require cljsjs.react
;;               tiny.react-checks
;;               goog.object)
;;     (:require-macros [tiny.api :refer [defc]]))

;;   (def runtime-has-symbols? (js* "typeof Symbol === 'function' && Symbol.for;"))

;;   (def REACT_ELEMENT_TYPE (if runtime-has-symbols?
;;                             (.for js/Symbol "react.element")
;;                             0xeac7))

;;   ;; Inspired by rum/core.cljs

;;   (defn build-class [render]
;;     (let [ctor (doto (fn [props]
;;                        (this-as this
;;                          (.call js/React.Component this props)))
;;                  (goog/inherits js/React.Component))
;;           prototype (goog.object/get ctor "prototype")]

;;       (goog.object/set prototype "render" #(this-as this
;;                                              (def plop-instance this)
;;                                              (js/console.log "defined plop-instance")
;;                                              (render this.props)))

;;       (goog.object/set prototype "shouldComponentUpdate"
;;                        (fn [next-props next-state]
;;                          (this-as this
;;                            (let [should-update (not= this.props next-props)]
;;                              (js/console.log "should component update" should-update)
;;                              ;; (js/console.log "this" this)
;;                              ;; (js/console.log "next-props" next-props)
;;                              should-update))))


;;       (goog.object/set ctor "tiny?" true)

;;       ctor))


;;   (comment
;;     (do (js* "debugger")
;;         (.setState plop-instance (fn [& args]
;;                                    (js/console.log "setState fn args:" args)
;;                                    ;; NOTE: new state attributes are assigned onto
;;                                    ;; a new js object, just like props...
;;                                    #js {:a-state 3})))

;;     )



;;   ;; TODO investigate: would this work? constant are better for dead code elimination
;;   ;; see HTTP://gist.github.com/mfikes/93cb77921ee35c2328b92af441ee5393
;;   (defonce ^:const dev? js/goog.DEBUG)


;;   ;; TODO add dev validation for key and ref?
;;   (defn t [type config & children]
;;     ;; Meta would not work, the type must be a function or an instance of Component
;;     (cond-> #js {:$$typeof REACT_ELEMENT_TYPE
;;                  :type type
;;                  :key (:key config)
;;                  :ref (:ref config)
;;                  :props (assoc config :children children),

;;                  ;; does this work?
;;                  :_owner React.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED.ReactCurrentOwner.current}
;;       ;; add similar dev props
;;       (identical? js/goog.DEBUG true)
;;       ;; TODO: what can we do for those dev props?
;;       (tiny.react-checks/add-dev-props nil ;; self
;;                                        nil ;; source
;;                                        )))


;;   (def e js/React.createElement))
