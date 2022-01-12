-- Treedemo DB
CREATE DATABASE IF NOT EXISTS treedb;
USE treedb;

CREATE TABLE IF NOT EXISTS t_tree (
  id int(11) NOT NULL AUTO_INCREMENT,
  treename varchar(50) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS t_node (
  id int(11) NOT NULL,
  tree_id int(11) NOT NULL,
  nodename varchar(50) NOT NULL,
  PRIMARY KEY (id,tree_id),
  KEY idxnode_tree_id (tree_id),
  CONSTRAINT fknode_tree_id FOREIGN KEY (tree_id) REFERENCES t_tree (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS t_relation (
  parent_id int(11) NOT NULL,
  child_id int(11) NOT NULL,
  tree_id int(11) NOT NULL,
  treelevel int(11) NOT NULL,
  PRIMARY KEY (tree_id,parent_id,child_id),
  KEY idxrelation_tree_id (tree_id),
  CONSTRAINT fkrelation_tree_id FOREIGN KEY (tree_id) REFERENCES t_tree (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE VIEW IF NOT EXISTS v_tree_cntNodes AS 
	select t.id AS id,t.treename AS name,count(n.id) AS cntnodes 
	from (t_tree t join t_node n) 
	where (t.id = n.tree_id) 
	group by t.id,t.treename;
	
CREATE VIEW IF NOT EXISTS v_tree_maxlevel AS 
	select t.id AS id,t.treename AS name,max(r.treelevel) AS maxlevel 
	from (t_tree t join t_relation r) 
	where (t.id = r.tree_id) 
	group by t.id,t.treename;	
	
CREATE VIEW IF NOT EXISTS v_tree_info AS 
	select vn.id AS id,vn.name AS name,vn.cntnodes AS cntNodes,vl.maxlevel AS maxLevel 
	from (v_tree_cntNodes vn join v_tree_maxlevel vl) 
	where (vn.id = vl.id);	


