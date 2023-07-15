CREATE TABLE oauth_clients (
  client_                   VARCHAR(256) UNIQUE PRIMARY KEY,
  client_secret             VARCHAR(256) NOT NULL,
  resources                 VARCHAR(256),
  scope                     VARCHAR(256) NOT NULL,
  grant_types               VARCHAR(256) NOT NULL,
  redirect_uris             VARCHAR(256),
  authorities               VARCHAR(256),
  access_token_validity     INTEGER,
  refresh_token_validity    INTEGER,
  additional_information    VARCHAR(4000),
  auto_approve              VARCHAR(256)
);