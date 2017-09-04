CREATE TABLE UrlList (
    rowId   INTEGER GENERATED ALWAYS AS IDENTITY,
    url     VARCHAR(128)
);

CREATE TABLE WordList (
    rowId   INTEGER GENERATED ALWAYS AS IDENTITY,
    word    VARCHAR(64)    
);

CREATE TABLE WordLocation (
    urlId       INTEGER,
    wordId      INTEGER,
    location    INTEGER
);

CREATE TABLE PageLink (
    rowId   INTEGER GENERATED ALWAYS AS IDENTITY,
    fromId  INTEGER,
    toId    INTEGER
);

CREATE TABLE LinkWord (
    wordId  INTEGER,
    linkId  INTEGER
);

CREATE TABLE PageRank (
    urlId   INTEGER,
    score   DOUBLE
);

CREATE TABLE HiddenNode(
    rowId   INTEGER
);

CREATE TABLE WordHidden(
    fromId      INTEGER,
    toId        INTEGER,
    strength    DOUBLE
);

CREATE TABLE UrlHidden(
    fromId      INTEGER,
    toId        INTEGER,
    strength    DOUBLE
);