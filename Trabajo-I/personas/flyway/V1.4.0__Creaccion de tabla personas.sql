CREATE TABLE PERSONAS(
                         DNI VARCHAR(10),
                         NOMBRE VARCHAR(100),
                         FECHA_NACIMIENTO DATE,
                         TELEFONO VARCHAR(20),
                         CONSTRAINT "PK_PERSONAS"                    PRIMARY KEY (DNI),
                         CONSTRAINT "NN_PERSONAS.DNI"                CHECK (DNI IS NOT NULL ),
                         CONSTRAINT "NN_PERSONAS.NOMBRE"             CHECK (NOMBRE IS NOT NULL ),
                         CONSTRAINT "NN_PERSONAS.FECHA_NACIMIENTO"   CHECK (FECHA_NACIMIENTO IS NOT NULL ),
                         CONSTRAINT "NN_PERSONAS.TELEFONO"           CHECK (TELEFONO IS NOT NULL ),
                         CONSTRAINT "CH_FECHA_NACIMIENTO"            CHECK (FECHA_NACIMIENTO <= CURRENT_DATE)
);