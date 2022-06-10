(ns xyz.thoren.graal-cli-template
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as str]
            [trptcolin.versioneer.core :refer [get-version]])
  (:gen-class))

(def version-number
  "The version number as defined in project.clj."
  ;; Note that this is evaluated at build time by native-image.
  (get-version "xyz.thoren" "graal-cli-template"))
;;
;; Beginning of command line parsing.

(defn cli-options []
  [["-h"
    "--help"
    "Print this help message."
    :default false]
   ["-v"
    "--version"
    "Print the current version number."
    :default false]])

(defn usage
  "Print a brief description and a short list of available options."
  [options-summary]
  (str/join
   \newline
   ["graal-cli-template: FIXME: Description"
    ""
    (str "Version: " version-number)
    ""
    "Usage: graal-cli-template FIXME: Usage"
    ""
    "Options:"
    options-summary
    ""
    "Examples:"
    ""
    "Command: $ FIXME: Example"
    "Result:  FIXME: Result"]))

(def exit-messages
  "Exit messages used by `exit`."
  {:64 "ERROR: Placeholder error message."})

(defn validate-args
  [args]
  (let [{:keys [options arguments errors summary]}
        (parse-opts args (cli-options))]
    (cond
      (:help options) ; help => exit OK with usage summary
      {:exit-message (usage summary) :exit-code 0}
      ;;
      (:version options) ; version => exit OK with version number
      {:exit-message version-number :exit-code 0}
      ;;
      errors ; errors => exit with description of errors
      {:exit-message (str/join \newline errors) :exit-code 1}
      ;;
      :else
      {:options options, :arguments arguments})))

;; End of command line parsing.

(defn exit
  "Print a `message` and exit the program with the given `status` code.
  See also [[exit-messages]]."
  [status message]
  (println message)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options arguments exit-message exit-code]}
        (validate-args args)]
    (when exit-message
      (exit exit-code exit-message))
    (println "Hello World.")
    (println "Options:" options)
    (println "Argumens:" arguments))
  (System/exit 0))
