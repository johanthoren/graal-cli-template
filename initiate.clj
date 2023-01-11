#!/usr/bin/env bb
(require '[babashka.fs :as fs]
         '[clojure.string :as str])

(def group-id (first *command-line-args*))
(def tld (first (str/split group-id #"\.")))
(def domain (second (str/split group-id #"\.")))
(def group-id-dir (str tld "/" domain))
(def artifact (second *command-line-args*))
(def artifact-name (str/replace artifact #"-" "_"))
(def old-proj (slurp "project.clj"))

(defn reset-version-counter [s]
  (str/replace-first s #"\"\d+\.\d+\.\d+.*\"" "\"0.1.0-SNAPSHOT\""))

(def new-proj
  (-> old-proj
      reset-version-counter
      (str/replace #"xyz.thoren/graal-cli-template" (str group-id "/" artifact))
      (str/replace #"johanthoren/graal-cli-template" (str "johanthoren/" artifact))))

(spit "project.clj" new-proj)

(fs/move (str (fs/cwd) "/src/xyz/thoren/graal_cli_template.clj")
         (str (fs/cwd) "/src/xyz/thoren/" artifact-name ".clj"))

(when-not (= "thoren" domain)
  (fs/move (str (fs/cwd) "/src/xyz/thoren")
           (str (fs/cwd) "/src/xyz/" domain)))

(when-not (= "xyz" tld)
  (fs/move (str (fs/cwd) "/src/xyz")
           (str (fs/cwd) "/src/" tld)))

(fs/move (str (fs/cwd) "/test/xyz/thoren/graal_cli_template_test.clj")
         (str (fs/cwd) "/test/xyz/thoren/" artifact-name "_test.clj"))

(fs/move (str (fs/cwd) "/test/xyz/thoren/graal_cli_template_test.bats")
         (str (fs/cwd) "/test/xyz/thoren/" artifact-name "_test.bats"))

(when-not (= "thoren" domain)
  (fs/move (str (fs/cwd) "/test/xyz/thoren")
           (str (fs/cwd) "/test/xyz/" domain)))

(when-not (= "xyz" tld)
  (fs/move (str (fs/cwd) "/test/xyz")
           (str (fs/cwd) "/test/" tld)))

(defn files [d]
  (remove fs/directory? (fs/glob "." (str d "/**") {:hidden true})))

(def files-to-edit (flatten (map files ["test" "src" ".github"])))

(doseq [f files-to-edit]
  (as-> (str f) <>
        (slurp <>)
        (str/replace <> #"xyz\.thoren" group-id)
        (str/replace <> #"graal-cli-template" artifact)
        (str/replace <> #"xyz/thoren" group-id-dir)
        (spit (str f) <>)))

(println "Remember to add the following GitHub secrets to your repository:")
(println "SIGN_KEY")
(println "SIGN_KEY_PASSPHRASE")
(println "SIGN_KEY_FINGERPRINT")
