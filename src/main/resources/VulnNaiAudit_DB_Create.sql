CREATE TABLE vuln_nai_audit (
	application_id VARCHAR(32) NOT NULL,
	request_id VARCHAR(32) NOT NULL,
	component_id VARCHAR(32) NOT NULL,
	vulnerability_id VARCHAR(32) NOT NULL,
	nai_audit_status VARCHAR(32) NOT NULL,
	nai_audit_comment VARCHAR(512) NOT NULL,
	CONSTRAINT unique_key UNIQUE ( application_id, request_id, component_id, vulnerability_id )
);

CREATE TABLE vuln_nai_audit_change_history (
	change_time timestamp with time zone NOT NULL,
	application_id VARCHAR(32) NOT NULL,
	request_id VARCHAR(32) NOT NULL,
	component_id VARCHAR(32) NOT NULL,
	vulnerability_id VARCHAR(32) NOT NULL,
	cc_user_name VARCHAR(100) NOT NULL,
	old_nai_audit_status VARCHAR(32) NOT NULL,
	old_nai_audit_comment VARCHAR(512) NOT NULL,
	new_nai_audit_status VARCHAR(32) NOT NULL,
	new_nai_audit_comment VARCHAR(512) NOT NULL
);


