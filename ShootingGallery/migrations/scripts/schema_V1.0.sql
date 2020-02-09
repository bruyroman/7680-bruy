CREATE SCHEMA public;

CREATE TYPE public.instructor_category AS ENUM
    ('ONE', 'TWO', 'THREE', 'HIGHEST');

CREATE TABLE public."PERSON"
(
    "ID"            bigserial               NOT NULL,
    "SURNAME"       character varying(100)  NOT NULL,
    "NAME"          character varying(100)  NOT NULL,
    "PATRONYMIC"    character varying(100),
    "BIRTHDATE"     date                    NOT NULL,

    CONSTRAINT "PERSON_pkey" PRIMARY KEY ("ID")
)

CREATE TABLE public."INSTRUCTOR"
(
    "ID"        bigserial           NOT NULL,
    "PERSON_ID" bigint              NOT NULL,
    "CATEGORY"  instructor_category NOT NULL,

    CONSTRAINT "INSTRUCTOR_pkey" PRIMARY KEY ("ID"),

    CONSTRAINT "FK_INSTRUCTOR_PERSON_ID" FOREIGN KEY ("PERSON_ID")
        REFERENCES public."PERSON" ("ID") MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

CREATE TABLE public."WEAPON"
(
    "ID"            bigserial               NOT NULL,
    "INSTRUCTOR_ID" bigint                  NOT NULL,
    "TYPE"          character varying(100)  NOT NULL,
    "MODEL"         character varying(100)  NOT NULL,
    "SERIES"        character varying(100),
    "NUMBER"        integer                 NOT NULL,

    CONSTRAINT "WEAPON_pkey"             PRIMARY KEY ("ID"),

    CONSTRAINT "FK_WEAPON_INSTRUCTOR_ID" FOREIGN KEY ("INSTRUCTOR_ID")
        REFERENCES public."INSTRUCTOR" ("ID") MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

CREATE TABLE public."VISIT"
(
    "ID" bigserial          NOT NULL,
    "CLIENT_ID" bigint      NOT NULL,
    "INSTRUCTOR_ID" bigint  NOT NULL,
    "WEAPON_ID" bigint      NOT NULL,
    "DATETIME_START" timestamp without time zone NOT NULL,
    "DATETIME_END" timestamp without time zone,

    CONSTRAINT "VISIT_pkey" PRIMARY KEY ("ID"),

    CONSTRAINT "FK_VISIT_CLIENT_ID" FOREIGN KEY ("CLIENT_ID")
        REFERENCES public."PERSON" ("ID") MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT "FK_VISIT_INSTRUCTOR_ID" FOREIGN KEY ("INSTRUCTOR_ID")
        REFERENCES public."INSTRUCTOR" ("ID") MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT "FK_VISIT_WEAPON_ID" FOREIGN KEY ("WEAPON_ID")
        REFERENCES public."WEAPON" ("ID") MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)