CREATE TABLE kv (user varchar, key varchar, value varchar, PRIMARY KEY(user, key));
CREATE TABLE clicks (user_id text, event_time timestamp, PRIMARY KEY(user_id, event_time));