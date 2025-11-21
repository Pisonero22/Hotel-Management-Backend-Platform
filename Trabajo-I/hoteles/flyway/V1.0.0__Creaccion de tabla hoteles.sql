CREATE TABLE HOTELES (
                         ID VARCHAR(5),
                         NOMBRE VARCHAR(50),
                         LOCALIZACION VARCHAR(100),
                         ESTRELLAS NUMERIC(5),
                         PRECIO_NOCHE NUMERIC(5,2),
                         CONSTRAINT "PK_HOTELES"                 PRIMARY KEY (ID),
                         CONSTRAINT "NN_HOTELES.NOMBRE"          CHECK (NOMBRE IS NOT NULL ),
                         CONSTRAINT "NN_HOTELES.LOCALIZACION"    CHECK (LOCALIZACION IS NOT NULL ),
                         CONSTRAINT "NN_HOTELES.ESTRELLAS"       CHECK (ESTRELLAS IS NOT NULL ),
                         CONSTRAINT "NN_PRECIO_NOCHE"            CHECK (PRECIO_NOCHE IS NOT NULL ),
                         CONSTRAINT "CH_HOTELES.ESTRELLAS"       CHECK (ESTRELLAS BETWEEN 1 AND 5)
);