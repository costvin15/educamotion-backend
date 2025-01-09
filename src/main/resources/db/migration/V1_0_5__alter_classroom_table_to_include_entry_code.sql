ALTER TABLE classroom ADD COLUMN entry_code VARCHAR(255) DEFAULT '';
ALTER TABLE classroom ADD CONSTRAINT entry_code_unique UNIQUE (entry_code);
