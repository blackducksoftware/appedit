CREATE TABLE vuln_nai_audit (
	application_id VARCHAR(32) NOT NULL,
	request_id VARCHAR(32) NOT NULL,
	component_id VARCHAR(32) NOT NULL,
	vulnerability_id VARCHAR(32) NOT NULL,
	nai_audit_status VARCHAR(32),
	nai_audit_comment VARCHAR(512)
);
commit;
