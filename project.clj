(defproject xyz.thoren/graal-cli-template "0.1.3"
  :description "FIXME: Description"
  :url "https://github.com/johanthoren/graal-cli-template"
  :license {:name "ISC"
            :url "https://choosealicense.com/licenses/isc"
            :comment "ISC License"
            :year 2022
            :key "isc"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [com.github.clj-easy/graal-build-time "0.1.4"]
                 [org.clojure/tools.cli "1.0.206"]
                 [trptcolin/versioneer "0.2.0"]]
  :plugins [[lein-kibit "0.1.8"]
            [jonase/eastwood "1.2.3"]
            [io.taylorwood/lein-native-image "0.3.1"]]
  :native-image {:name "graal-cli-template"                 ;; name of output image, optional
                 :opts ["--verbose"           ;; pass-thru args to GraalVM native-image, optional
                        "--no-fallback"
                        "--report-unsupported-elements-at-runtime"
                        "-H:+ReportExceptionStackTraces"
                        "--link-at-build-time"]}
  :main xyz.thoren.graal-cli-template
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :aliases
  {"lint"
   ["do" ["kibit"] ["eastwood"]]
   "make-uberjars"
   ["do" ["test"] ["clean"] ["uberjar"]]}
  :release-tasks [["lint"]
                  ["test"]
                  ["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]])
