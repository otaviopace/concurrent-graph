(ns concurrent-graph.core
  (:import [clojure.lang Counted Indexed ILookup Seqable]))

(defprotocol Graph
  (add-vertex [this vertex])
  (add-edge [this v1 v2]))

(deftype ConcurrentGraph [graph]
  Seqable
  (seq [_] (seq @graph))

  Counted
  (count [_] (count @graph))

  Indexed
  (nth [_ i]
    (nth (seq @graph) i))

  Graph
  (add-vertex [_ vertex]
    (swap! graph assoc vertex #{}))
  (add-edge [_ v1 v2]
    (swap! graph #(update % v1 conj v2))
    (swap! graph #(update % v2 conj v1)))

  ILookup
  (valAt [this k]
    (.valAt this k nil))
  (valAt [_ k not-found]
    (if (= k :graph)
      @graph
      (if (nil? not-found)
        (ex-info "The only key ConcurrentGraph has is :graph" {})
        not-found))))

(defn concurrent-graph
  ([]
   (concurrent-graph {}))
  ([graph]
    (->ConcurrentGraph (atom graph))))
