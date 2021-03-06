(ns concurrent-graph.core-test
  (:require [clojure.test :refer :all]
            [concurrent-graph.core :refer :all]))


(deftest create-concurrent-graph
  (testing "constructs a new concurrent-graph"
    (is (true? (= (:graph (concurrent-graph)) {})))
    (is (true? (= (:graph (concurrent-graph)) (:graph (concurrent-graph)))))))

(deftest adds-vertex-to-graph
  (testing "adds vertex to graph"
    (let [graph (concurrent-graph)]
      (is (true? (= (add-vertex graph :A) {:A #{}})))
      (is (true? (= (:graph graph) {:A #{}}))))))

(deftest adds-edge-to-graph
  (testing "adds edge to graph"
    (let [graph (concurrent-graph)]
      (add-vertex graph :A)
      (add-vertex graph :B)
      (is (true? (= (add-edge graph :A :B) {:A #{:B} :B #{:A}})))
      (is (true? (= (:graph graph) {:A #{:B} :B #{:A}}))))))

(deftest seqable-graph
  (testing "graph should be seqable"
    (let [graph (concurrent-graph)]
      (add-vertex graph :A)
      (add-vertex graph :B)
      (add-edge graph :A :B)
      (is (true? (= (seq graph) '([:A #{:B}] [:B #{:A}])))))))

(deftest counted-graph
  (testing "graph should be counted"
    (let [graph (concurrent-graph)]
      (add-vertex graph :A)
      (add-vertex graph :B)
      (add-vertex graph :C)
      (is (true? (= (count graph) 3))))))

(deftest indexed-graph
  (testing "graph should be indexed"
    (let [graph (concurrent-graph)]
      (add-vertex graph :A)
      (add-vertex graph :B)
      (add-vertex graph :C)
      (add-edge graph :C :A)
      (add-edge graph :C :B)
      (is (true? (= (nth graph 2) [:C #{:A :B}]))))))
