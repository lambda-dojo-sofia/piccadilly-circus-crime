(ns piccadilly-circus-clj.core
  (:require [clojure.string :as str])
  (:gen-class))

(def dataset (-> "resources/input.txt"
                 slurp
                 (str/split #"\n")))

(defn str->interval [s]
  (->> (str/split s #" ")
       (map #(Integer. %))))

(defn update-interval [count interval hour]
  (let [[start end] interval]
    (if (and (>= hour start)
             (<= hour end))
      (inc count)
      count)))

(defn number-of-people-in-hour [visitors-diary hour]
  (reduce #(update-interval %1 %2 hour) 0 (map str->interval visitors-diary)))

(defn find-min-max [report]
  (str (->> report vals (apply min))
       " "
       (->> report vals (apply max))))

(defn min-max-attendance [data]
  (let [[start end] (-> data
                        first
                        str->interval )
        visitors    (drop 2 data)
        interval-hours (range start (inc end))
        interval-report (-> interval-hours
                            (zipmap (map #(number-of-people-in-hour visitors %) interval-hours)))]
    (find-min-max interval-report)))

(defn -main []
  (-> dataset
      min-max-attendance
      prn))
