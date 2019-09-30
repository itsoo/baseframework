/** DEMO */
ALTER TABLE CFC_RPT.JV_BASEFRAMEWORK_DEMO
 DROP PRIMARY KEY CASCADE;

DROP TABLE CFC_RPT.JV_BASEFRAMEWORK_DEMO CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.JV_BASEFRAMEWORK_DEMO
(
  ID       NUMBER(16)                           NOT NULL,
  NAME     VARCHAR2(20 BYTE),
  ADDRESS  VARCHAR2(200 BYTE)
)
TABLESPACE CFC_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;

/** SEs */
DROP TABLE CFC_RPT.SE_EMAIL_CODE CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.SE_EMAIL_CODE
(
  USERNAME     VARCHAR2(300 BYTE)               NOT NULL,
  UUID         VARCHAR2(300 BYTE)               NOT NULL,
  CREATE_TIME  DATE                             DEFAULT SYSDATE
)
TABLESPACE CFC_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;

DROP TABLE CFC_RPT.SE_LOG_INFO CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.SE_LOG_INFO
(
  USER_ID      NUMBER(8)                        DEFAULT 0,
  USERNAME     VARCHAR2(300 BYTE),
  NAME         VARCHAR2(300 BYTE),
  PROJECT_URL  VARCHAR2(1024 BYTE),
  INSERT_TIME  DATE                             DEFAULT SYSDATE
)
TABLESPACE CFC_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;

ALTER TABLE CFC_RPT.SE_PERMISSION
 DROP PRIMARY KEY CASCADE;

DROP TABLE CFC_RPT.SE_PERMISSION CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.SE_PERMISSION
(
  ID           NUMBER(8)                        NOT NULL,
  NAME         VARCHAR2(256 BYTE),
  PROJECT_ID   NUMBER(8),
  PARENT_ID    NUMBER(8),
  URL          VARCHAR2(200 BYTE),
  PERMISSIONS  VARCHAR2(500 BYTE),
  TYPE         CHAR(1 BYTE),
  ICON         VARCHAR2(128 BYTE),
  ORDER_NUM    NUMBER(8)
)
TABLESPACE TBS_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;

ALTER TABLE CFC_RPT.SE_PERMISSION ADD (
  PRIMARY KEY
  (ID)
  USING INDEX
    TABLESPACE TBS_DM
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                NEXT             1M
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
                BUFFER_POOL      DEFAULT
               )
  ENABLE VALIDATE);

ALTER TABLE CFC_RPT.SE_PROJECT
 DROP PRIMARY KEY CASCADE;

DROP TABLE CFC_RPT.SE_PROJECT CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.SE_PROJECT
(
  ID           NUMBER(8)                        NOT NULL,
  NAME         VARCHAR2(64 BYTE),
  CREATE_TIME  DATE                             DEFAULT sysdate,
  CODE         VARCHAR2(32 BYTE)
)
TABLESPACE CFC_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;

ALTER TABLE CFC_RPT.SE_PROJECT ADD (
  PRIMARY KEY
  (ID)
  USING INDEX
    TABLESPACE CFC_DM
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                NEXT             1M
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
                BUFFER_POOL      DEFAULT
               )
  ENABLE VALIDATE,
  UNIQUE (CODE)
  USING INDEX
    TABLESPACE CFC_DM
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                NEXT             1M
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
                BUFFER_POOL      DEFAULT
               )
  ENABLE VALIDATE);
ALTER TABLE CFC_RPT.SE_ROLE
 DROP PRIMARY KEY CASCADE;

DROP TABLE CFC_RPT.SE_ROLE CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.SE_ROLE
(
  ID           NUMBER(8)                        NOT NULL,
  NAME         VARCHAR2(128 BYTE),
  STATUS       VARCHAR2(16 BYTE)                DEFAULT 'active',
  REMARK       VARCHAR2(64 BYTE),
  CREATE_TIME  DATE                             DEFAULT sysdate,
  PROJECT_ID   NUMBER(8),
  IS_ADMIN     CHAR(1 BYTE)
)
TABLESPACE TBS_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;

CREATE UNIQUE INDEX CFC_RPT.SE_ROLE_U1 ON CFC_RPT.SE_ROLE
(PROJECT_ID, NAME)
LOGGING
TABLESPACE TBS_DM
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           );

ALTER TABLE CFC_RPT.SE_ROLE ADD (
  PRIMARY KEY
  (ID)
  USING INDEX
    TABLESPACE TBS_DM
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                NEXT             1M
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
                BUFFER_POOL      DEFAULT
               )
  ENABLE VALIDATE,
  CONSTRAINT SE_ROLE_U1
  UNIQUE (PROJECT_ID, NAME)
  USING INDEX CFC_RPT.SE_ROLE_U1
  ENABLE VALIDATE);
ALTER TABLE CFC_RPT.SE_ROLE_PERMISSION
 DROP PRIMARY KEY CASCADE;

DROP TABLE CFC_RPT.SE_ROLE_PERMISSION CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.SE_ROLE_PERMISSION
(
  ID             NUMBER(16)                     NOT NULL,
  ROLE_ID        NUMBER(8),
  PERMISSION_ID  NUMBER(8),
  CREATE_TIME    DATE                           DEFAULT sysdate
)
TABLESPACE TBS_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;

ALTER TABLE CFC_RPT.SE_ROLE_PERMISSION ADD (
  PRIMARY KEY
  (ID)
  USING INDEX
    TABLESPACE TBS_DM
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          128K
                NEXT             1M
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
                BUFFER_POOL      DEFAULT
               )
  ENABLE VALIDATE);
DROP TABLE CFC_RPT.SE_T_MSG_USER_CONFIG CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.SE_T_MSG_USER_CONFIG
(
  MSGCODE      VARCHAR2(20 BYTE),
  MSGNAME      VARCHAR2(100 BYTE),
  DEPT         VARCHAR2(500 BYTE),
  USERNO       VARCHAR2(32 BYTE),
  USERNAME     VARCHAR2(128 BYTE),
  MOBILE       VARCHAR2(32 BYTE),
  MAIL         VARCHAR2(64 BYTE),
  IS_VALID     CHAR(1 BYTE),
  UPDATE_TIME  DATE
)
TABLESPACE CFC_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;

CREATE INDEX CFC_RPT.INDEX_MSGCODE_USERNO ON CFC_RPT.SE_T_MSG_USER_CONFIG
(MSGCODE, USERNO)
LOGGING
TABLESPACE CFC_DM
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           );
DROP TABLE CFC_RPT.SE_USER CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.SE_USER
(
  ID             NUMBER(16)                     NOT NULL,
  SNCFC_NO       VARCHAR2(16 BYTE),
  PASSWORD       VARCHAR2(64 BYTE),
  NAME           VARCHAR2(16 BYTE),
  STATUS         VARCHAR2(16 BYTE),
  CREATE_TIME    DATE,
  PHONE_NO       NUMBER(11),
  EMAIL          VARCHAR2(64 BYTE),
  GROUP_ID       NUMBER(8),
  DELETE_STATUS  VARCHAR2(16 BYTE)              DEFAULT 'normal',
  SUNING_NO      VARCHAR2(16 BYTE),
  LEADER         NUMBER(1)
)
TABLESPACE CFC_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;
ALTER TABLE CFC_RPT.SE_USER_GROUP
 DROP PRIMARY KEY CASCADE;

DROP TABLE CFC_RPT.SE_USER_GROUP CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.SE_USER_GROUP
(
  ID         NUMBER(8)                          NOT NULL,
  NAME       VARCHAR2(32 BYTE),
  PARENT_ID  NUMBER(8),
  CLASS      NUMBER(2)                          DEFAULT 0
)
TABLESPACE TBS_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;

ALTER TABLE CFC_RPT.SE_USER_GROUP ADD (
  PRIMARY KEY
  (ID)
  USING INDEX
    TABLESPACE TBS_DM
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                NEXT             1M
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
                BUFFER_POOL      DEFAULT
               )
  ENABLE VALIDATE);
ALTER TABLE CFC_RPT.SE_USER_POSITION
 DROP PRIMARY KEY CASCADE;

DROP TABLE CFC_RPT.SE_USER_POSITION CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.SE_USER_POSITION
(
  ID           NUMBER(8),
  NAME         VARCHAR2(300 BYTE)               NOT NULL,
  GROUP_ID     NUMBER(8)                        NOT NULL,
  USER_ID      NUMBER(8)                        NOT NULL,
  IS_LEADER    NUMBER(2)                        NOT NULL,
  PLURALISTIC  NUMBER(2)                        NOT NULL,
  CREATE_TIME  DATE                             DEFAULT SYSDATE
)
TABLESPACE CFC_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;

ALTER TABLE CFC_RPT.SE_USER_POSITION ADD (
  PRIMARY KEY
  (ID)
  USING INDEX
    TABLESPACE CFC_DM
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                NEXT             1M
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
                BUFFER_POOL      DEFAULT
               )
  ENABLE VALIDATE);
ALTER TABLE CFC_RPT.SE_USER_ROLE
 DROP PRIMARY KEY CASCADE;

DROP TABLE CFC_RPT.SE_USER_ROLE CASCADE CONSTRAINTS;

CREATE TABLE CFC_RPT.SE_USER_ROLE
(
  ID           NUMBER(16)                       NOT NULL,
  USER_ID      NUMBER(8),
  ROLE_ID      NUMBER(8),
  CREATE_TIME  DATE                             DEFAULT sysdate
)
TABLESPACE TBS_DM
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          192K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
MONITORING;

ALTER TABLE CFC_RPT.SE_USER_ROLE ADD (
  PRIMARY KEY
  (ID)
  USING INDEX
    TABLESPACE TBS_DM
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          192K
                NEXT             1M
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
                BUFFER_POOL      DEFAULT
               )
  ENABLE VALIDATE);
