(require 'cider-nrepl.main)


(cider-nrepl.main/init ["refactor-nrepl.middleware/wrap-refactor"
                        "cider.nrepl/cider-middleware"])

