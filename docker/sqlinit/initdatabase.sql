-- Treedemo DB
CREATE DATABASE IF NOT EXISTS treedb;
USE treedb;

CREATE TABLE IF NOT EXISTS t_tree (
  id int(11) NOT NULL AUTO_INCREMENT,
  treename varchar(50) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS t_node (
  id int(11) NOT NULL,
  tree_id int(11) NOT NULL,
  nodename varchar(50) NOT NULL,
  PRIMARY KEY (id, tree_id),
  KEY idxnode_tree_id(tree_id),
  CONSTRAINT fknode_tree_id FOREIGN KEY(tree_id) REFERENCES t_tree(id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS t_relation (
  parent_id int(11) NOT NULL,
  child_id int(11) NOT NULL,
  tree_id int(11) NOT NULL,
  treelevel int(11) NOT NULL,
  PRIMARY KEY (tree_id, parent_id, child_id),
  KEY idxrelation_child_id(child_id),
  KEY idxrelation_tree_id(tree_id),
  CONSTRAINT fkrelation_parent_id FOREIGN KEY(parent_id) REFERENCES t_node(id),
  CONSTRAINT fkrelation_child_id FOREIGN KEY(child_id) REFERENCES t_node(id),
  CONSTRAINT fkrelation_tree_id FOREIGN KEY(tree_id) REFERENCES t_tree(id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


