CREATE TABLE RESERVAS(
                         ID VARCHAR(5),
                         DNI VARCHAR(10),
                         ID_HOTEL VARCHAR(5),
                         TITULAR_DNI VARCHAR(100),
                         FECHA_ENTRADA DATE,
                         FECHA_SALIDA DATE,
                         PRECIO NUMERIC(6,2),

                         CONSTRAINT "PK_RESERVAS"                    PRIMARY KEY (ID),
                         CONSTRAINT "FK_RESERVAS_HOTELES"            FOREIGN KEY (ID_HOTEL) REFERENCES HOTELES(ID),
                         CONSTRAINT "FK_RESERVAS_PERSONAS"           FOREIGN KEY (DNI)  REFERENCES PERSONAS(DNI),
                         CONSTRAINT "FK_RESERVAS_TITULAR"            FOREIGN KEY (TITULAR_DNI) REFERENCES PERSONAS(DNI),
                         CONSTRAINT "NN_RESERVAS_PERSONAS_DNI"       CHECK(DNI IS NOT NULL),
                         CONSTRAINT "NN_RESERVAS_HOTELES_ID"         CHECK(ID IS NOT NULL),
                         CONSTRAINT "NN_RESERVAS_TITULAR"            CHECK(TITULAR_DNI IS NOT NULL),
                         CONSTRAINT "NN_RESERVAS_FECHA_ENTRADA"      CHECK(FECHA_ENTRADA IS NOT NULL),
                         CONSTRAINT "NN_RESERVAS_FECHA_SALIDA"       CHECK(FECHA_SALIDA IS NOT NULL),
                         CONSTRAINT "CHK_FECHA_ENTRADA_ANTES_SALIDA" CHECK(FECHA_ENTRADA <= FECHA_SALIDA),
                         CONSTRAINT "NN_RESERVAS_PRECIO"             CHECK (PRECIO IS NOT NULL)
);
