--
-- PostgreSQL database dump
--

-- Dumped from database version 12.1
-- Dumped by pg_dump version 12.1

-- Started on 2020-01-13 21:21:05

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2864 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 547 (class 1247 OID 16482)
-- Name: instructor_category; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.instructor_category AS ENUM (
    'ONE',
    'TWO',
    'THREE',
    'HIGHEST'
);


ALTER TYPE public.instructor_category OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 205 (class 1259 OID 16493)
-- Name: INSTRUCTOR; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."INSTRUCTOR" (
    "ID" bigint NOT NULL,
    "PERSON_ID" bigint NOT NULL,
    "CATEGORY" public.instructor_category NOT NULL
);


ALTER TABLE public."INSTRUCTOR" OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 16491)
-- Name: INSTRUCTOR_ID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."INSTRUCTOR_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."INSTRUCTOR_ID_seq" OWNER TO postgres;

--
-- TOC entry 2865 (class 0 OID 0)
-- Dependencies: 204
-- Name: INSTRUCTOR_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."INSTRUCTOR_ID_seq" OWNED BY public."INSTRUCTOR"."ID";


--
-- TOC entry 203 (class 1259 OID 16475)
-- Name: PERSON; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."PERSON" (
    "ID" bigint NOT NULL,
    "SURNAME" character varying(100) NOT NULL,
    "NAME" character varying(100) NOT NULL,
    "PATRONYMIC" character varying(100),
    "BIRTHDATE" date NOT NULL
);


ALTER TABLE public."PERSON" OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16473)
-- Name: PERSON_ID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."PERSON_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."PERSON_ID_seq" OWNER TO postgres;

--
-- TOC entry 2866 (class 0 OID 0)
-- Dependencies: 202
-- Name: PERSON_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."PERSON_ID_seq" OWNED BY public."PERSON"."ID";


--
-- TOC entry 209 (class 1259 OID 16532)
-- Name: VISIT; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."VISIT" (
    "ID" bigint NOT NULL,
    "CLIENT_ID" bigint NOT NULL,
    "INSTRUCTOR_ID" bigint NOT NULL,
    "WEAPON_ID" bigint NOT NULL,
    "DATETIME_START" timestamp without time zone NOT NULL,
    "DATETIME_END" timestamp without time zone
);


ALTER TABLE public."VISIT" OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 16530)
-- Name: VISIT_ID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."VISIT_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."VISIT_ID_seq" OWNER TO postgres;

--
-- TOC entry 2867 (class 0 OID 0)
-- Dependencies: 208
-- Name: VISIT_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."VISIT_ID_seq" OWNED BY public."VISIT"."ID";


--
-- TOC entry 207 (class 1259 OID 16506)
-- Name: WEAPON; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."WEAPON" (
    "ID" bigint NOT NULL,
    "INSTRUCTOR_ID" bigint NOT NULL,
    "TYPE" character varying(100) NOT NULL,
    "MODEL" character varying(100) NOT NULL,
    "SERIES" character varying(100),
    "NUMBER" integer NOT NULL
);


ALTER TABLE public."WEAPON" OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 16504)
-- Name: WEAPON_ID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."WEAPON_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."WEAPON_ID_seq" OWNER TO postgres;

--
-- TOC entry 2868 (class 0 OID 0)
-- Dependencies: 206
-- Name: WEAPON_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."WEAPON_ID_seq" OWNED BY public."WEAPON"."ID";


--
-- TOC entry 2709 (class 2604 OID 16496)
-- Name: INSTRUCTOR ID; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."INSTRUCTOR" ALTER COLUMN "ID" SET DEFAULT nextval('public."INSTRUCTOR_ID_seq"'::regclass);


--
-- TOC entry 2708 (class 2604 OID 16478)
-- Name: PERSON ID; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PERSON" ALTER COLUMN "ID" SET DEFAULT nextval('public."PERSON_ID_seq"'::regclass);


--
-- TOC entry 2711 (class 2604 OID 16535)
-- Name: VISIT ID; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."VISIT" ALTER COLUMN "ID" SET DEFAULT nextval('public."VISIT_ID_seq"'::regclass);


--
-- TOC entry 2710 (class 2604 OID 16509)
-- Name: WEAPON ID; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."WEAPON" ALTER COLUMN "ID" SET DEFAULT nextval('public."WEAPON_ID_seq"'::regclass);


--
-- TOC entry 2854 (class 0 OID 16493)
-- Dependencies: 205
-- Data for Name: INSTRUCTOR; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."INSTRUCTOR" ("ID", "PERSON_ID", "CATEGORY") FROM stdin;
\.


--
-- TOC entry 2852 (class 0 OID 16475)
-- Dependencies: 203
-- Data for Name: PERSON; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."PERSON" ("ID", "SURNAME", "NAME", "PATRONYMIC", "BIRTHDATE") FROM stdin;
\.


--
-- TOC entry 2858 (class 0 OID 16532)
-- Dependencies: 209
-- Data for Name: VISIT; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."VISIT" ("ID", "CLIENT_ID", "INSTRUCTOR_ID", "WEAPON_ID", "DATETIME_START", "DATETIME_END") FROM stdin;
\.


--
-- TOC entry 2856 (class 0 OID 16506)
-- Dependencies: 207
-- Data for Name: WEAPON; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."WEAPON" ("ID", "INSTRUCTOR_ID", "TYPE", "MODEL", "SERIES", "NUMBER") FROM stdin;
\.


--
-- TOC entry 2869 (class 0 OID 0)
-- Dependencies: 204
-- Name: INSTRUCTOR_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."INSTRUCTOR_ID_seq"', 1, false);


--
-- TOC entry 2870 (class 0 OID 0)
-- Dependencies: 202
-- Name: PERSON_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."PERSON_ID_seq"', 1, false);


--
-- TOC entry 2871 (class 0 OID 0)
-- Dependencies: 208
-- Name: VISIT_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."VISIT_ID_seq"', 1, false);


--
-- TOC entry 2872 (class 0 OID 0)
-- Dependencies: 206
-- Name: WEAPON_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."WEAPON_ID_seq"', 1, false);


--
-- TOC entry 2715 (class 2606 OID 16498)
-- Name: INSTRUCTOR INSTRUCTOR_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."INSTRUCTOR"
    ADD CONSTRAINT "INSTRUCTOR_pkey" PRIMARY KEY ("ID");


--
-- TOC entry 2713 (class 2606 OID 16480)
-- Name: PERSON PERSON_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PERSON"
    ADD CONSTRAINT "PERSON_pkey" PRIMARY KEY ("ID");


--
-- TOC entry 2719 (class 2606 OID 16537)
-- Name: VISIT VISIT_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."VISIT"
    ADD CONSTRAINT "VISIT_pkey" PRIMARY KEY ("ID");


--
-- TOC entry 2717 (class 2606 OID 16511)
-- Name: WEAPON WEAPON_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."WEAPON"
    ADD CONSTRAINT "WEAPON_pkey" PRIMARY KEY ("ID");


--
-- TOC entry 2720 (class 2606 OID 16499)
-- Name: INSTRUCTOR FK_INSTRUCTOR_PERSON_ID; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."INSTRUCTOR"
    ADD CONSTRAINT "FK_INSTRUCTOR_PERSON_ID" FOREIGN KEY ("PERSON_ID") REFERENCES public."PERSON"("ID") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2722 (class 2606 OID 16538)
-- Name: VISIT FK_VISIT_CLIENT_ID; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."VISIT"
    ADD CONSTRAINT "FK_VISIT_CLIENT_ID" FOREIGN KEY ("CLIENT_ID") REFERENCES public."PERSON"("ID") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2723 (class 2606 OID 16543)
-- Name: VISIT FK_VISIT_INSTRUCTOR_ID; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."VISIT"
    ADD CONSTRAINT "FK_VISIT_INSTRUCTOR_ID" FOREIGN KEY ("INSTRUCTOR_ID") REFERENCES public."INSTRUCTOR"("ID") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2724 (class 2606 OID 16548)
-- Name: VISIT FK_VISIT_WEAPON_ID; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."VISIT"
    ADD CONSTRAINT "FK_VISIT_WEAPON_ID" FOREIGN KEY ("WEAPON_ID") REFERENCES public."WEAPON"("ID") ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2721 (class 2606 OID 16512)
-- Name: WEAPON FK_WEAPON_INSTRUCTOR_ID; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."WEAPON"
    ADD CONSTRAINT "FK_WEAPON_INSTRUCTOR_ID" FOREIGN KEY ("INSTRUCTOR_ID") REFERENCES public."INSTRUCTOR"("ID") ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2020-01-13 21:21:05

--
-- PostgreSQL database dump complete
--

