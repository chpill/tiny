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



















(comment

  (macroexpand-1 '(t plop {:toto "lala"}))


  )
