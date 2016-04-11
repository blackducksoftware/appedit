CREATE TABLE vuln_nai_audit (
	application_id CHAR(32) NOT NULL,
	request_id CHAR(32) NOT NULL,
	component_id CHAR(32) NOT NULL,
	vulnerability_id CHAR(32) NOT NULL,
	nai_audit_status CHAR(32),
	nai_audit_comment CHAR(512)
);
commit;
