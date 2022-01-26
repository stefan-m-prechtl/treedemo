-- Treedemo DB
CREATE DATABASE IF NOT EXISTS treedb
  CHARACTER SET 'utf8' 
  COLLATE 'utf8_general_ci';

USE treedb;

-- Sequence als ID-Generator
--CREATE SEQUENCE treedb.seq_tree_id;

CREATE TABLE IF NOT EXISTS treedb.t_tree (
  id int(11) NOT NULL AUTO_INCREMENT,
  treename varchar(50) NOT NULL,
  PRIMARY KEY (id)
) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';


CREATE TABLE IF NOT EXISTS treedb.t_node (
  id int(11) NOT NULL,
  tree_id int(11) NOT NULL,
  nodename varchar(50) NOT NULL,
  PRIMARY KEY (id, tree_id),
  KEY idxnode_tree_id(tree_id),
  CONSTRAINT fk_tree foreign key(tree_id) REFERENCES treedb.t_tree(id)
) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';


CREATE TABLE IF NOT EXISTS treedb.t_relation (
  parent_id int(11) NOT NULL,
  child_id int(11) NOT NULL,
  tree_id int(11) NOT NULL,
  treelevel int(11) NOT NULL,
  PRIMARY KEY (tree_id,parent_id,child_id),
  KEY idxrelation_tree_id (tree_id),
  CONSTRAINT fkrelation_tree_id FOREIGN KEY (tree_id) REFERENCES treedb.t_tree (id)
) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

CREATE TABLE IF NOT EXISTS treedb.t_closure (
  ancestor_id int(11) NOT NULL,
  descendant_id int(11) NOT NULL,
  tree_id int(11) NOT NULL,
  depth int(11) NOT NULL,
  PRIMARY KEY (tree_id,ancestor_id,descendant_id),
  KEY idxrelation_tree_id (tree_id),
  CONSTRAINT fkclosure_tree_id FOREIGN KEY (tree_id) REFERENCES treedb.t_tree (id)
) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';


CREATE TABLE IF NOT EXISTS treedb.t_project (
  id int(11) NOT NULL AUTO_INCREMENT,
  projectname varchar(50) NOT NULL,
  PRIMARY KEY (id)
) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';


CREATE VIEW IF NOT EXISTS treedb.v_tree_cntNodes AS 
	select t.id AS id,t.treename AS name,count(n.id) AS cntnodes 
	from (t_tree t join t_node n) 
	where (t.id = n.tree_id) 
	group by t.id,t.treename;
	

CREATE VIEW IF NOT EXISTS treedb.v_tree_maxlevel AS 
	select t.id AS id,t.treename AS name,max(r.treelevel) AS maxlevel 
	from (t_tree t join t_relation r) 
	where (t.id = r.tree_id) 
	group by t.id,t.treename;	
	
CREATE VIEW IF NOT EXISTS treedb.v_tree_info AS 
	select vn.id AS id,vn.name AS name,vn.cntnodes AS cntNodes,vl.maxlevel AS maxLevel 
	from (v_tree_cntNodes vn join v_tree_maxlevel vl) 
	where (vn.id = vl.id);	


