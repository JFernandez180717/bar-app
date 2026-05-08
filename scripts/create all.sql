CREATE TABLE public.categoria
(
    id uuid NOT NULL,
    descripcion character varying(100) NOT NULL,
    estado integer NOT NULL DEFAULT 1,
    fecha_creacion timestamp with time zone NOT NULL DEFAULT NOW(),
    fecha_actualizacion timestamp with time zone,
    usuario_crea character varying(100) NOT NULL,
    usuario_modifica character varying(100),
    CONSTRAINT categoria_pk PRIMARY KEY (id),
    CONSTRAINT chk_estado CHECK (estado in (0, 1)) NOT VALID
);

ALTER TABLE IF EXISTS public.categoria
    OWNER to postgres;

COMMENT ON TABLE public.categoria
    IS 'Categorias de productos';

CREATE TABLE public.tipo_identificacion
(
    tipo character varying(6) NOT NULL,
    descripcion character varying(100) NOT NULL,
    estado integer NOT NULL DEFAULT 1,
    fecha_creacion timestamp with time zone NOT NULL DEFAULT NOW(),
    fecha_actualizacion timestamp with time zone,
    usuario_crea character varying(100) NOT NULL,
    usuario_modifica character varying(100),
    CONSTRAINT tipo_identifcacion_pk PRIMARY KEY (tipo),
    CONSTRAINT chk_estado CHECK (estado in (0, 1)) NOT VALID
);

ALTER TABLE IF EXISTS public.tipo_identificacion
    OWNER to postgres;

COMMENT ON TABLE public.tipo_identificacion
    IS 'Tipos de identificacion';

CREATE TABLE public.tipo_pago
(
    id uuid NOT NULL,
    descripcion character varying(100) NOT NULL,
    es_efectivo integer DEFAULT 0,
    es_transferencia integer DEFAULT 0,
    es_tarjeta_debito integer DEFAULT 0,
    es_tarjeta_credito integer DEFAULT 0,
    estado integer NOT NULL DEFAULT 1,
    fecha_creacion timestamp with time zone NOT NULL DEFAULT NOW(),
    fecha_actualizacion timestamp with time zone,
    usuario_crea character varying(100) NOT NULL,
    usuario_modifica character varying(100),
    CONSTRAINT tipo_pago_pk PRIMARY KEY (id),
    CONSTRAINT chk_estado CHECK (estado in (0, 1)) NOT VALID,
    CONSTRAINT chk_es_efectivo CHECK (es_efectivo in (0, 1)) NOT VALID,
    CONSTRAINT chk_es_tranferencia CHECK (es_transferencia in (0, 1)) NOT VALID,
    CONSTRAINT chk_es_tarjeta_debito CHECK (es_tarjeta_debito in (0, 1)) NOT VALID,
    CONSTRAINT chk_es_tarjeta_credito CHECK (es_tarjeta_credito in (0, 1)) NOT VALID
);

ALTER TABLE IF EXISTS public.tipo_pago
    OWNER to postgres;

COMMENT ON TABLE public.tipo_pago
    IS 'Tipos de pago existentes';

CREATE TABLE public.marca
(
    id uuid NOT NULL,
    descripcion character varying(100) NOT NULL,
    estado integer NOT NULL DEFAULT 1,
    fecha_creacion timestamp with time zone NOT NULL DEFAULT NOW(),
    fecha_actualizacion timestamp with time zone,
    usuario_crea character varying(30) NOT NULL,
    usuario_modifica character varying(30),
    CONSTRAINT marca_pk PRIMARY KEY (id),
    CONSTRAINT chk_estado CHECK (estado in (0, 1)) NOT VALID
);

ALTER TABLE IF EXISTS public.marca
    OWNER to postgres;

CREATE TABLE public.mesa
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    estado integer NOT NULL DEFAULT 1,
    fecha_creacion timestamp with time zone NOT NULL DEFAULT NOW(),
    fecha_actualizacion timestamp with time zone,
    usuario_crea character varying(100) NOT NULL,
    usuario_modifica character varying(100),
    CONSTRAINT mesa_pk PRIMARY KEY (id),
    CONSTRAINT chk_estado CHECK (estado in (0, 1)) NOT VALID
);

ALTER TABLE IF EXISTS public.mesa
    OWNER to postgres;

COMMENT ON TABLE public.mesa
    IS 'Mesas existentes en el bar';

CREATE TABLE public.rol
(
    rol character varying(30) NOT NULL,
    descripcion character varying(60) NOT NULL,
    estado integer NOT NULL DEFAULT 1,
    fecha_creacion timestamp with time zone NOT NULL DEFAULT NOW(),
    fecha_actualizacion timestamp with time zone,
    usuario_crea character varying(100) NOT NULL,
    usuario_modifica character varying(100),
    CONSTRAINT rol_pk PRIMARY KEY (rol),
    CONSTRAINT chk_estado CHECK (estado in (0, 1)) NOT VALID
);

ALTER TABLE IF EXISTS public.rol
    OWNER to postgres;

COMMENT ON TABLE public.rol
    IS 'Roles de la aplicacion';

CREATE TABLE public.proveedor
(
    identificacion character varying(20) NOT NULL,
    tipo_identificacion character varying(4) NOT NULL,
    nombre character varying(250) NOT NULL,
    direccion character varying(200),
    telefono character varying(20),
    estado integer NOT NULL DEFAULT 1,
    fecha_creacion timestamp with time zone NOT NULL DEFAULT NOW(),
    fecha_actualizacion timestamp with time zone,
    usuario_crea character varying(100) NOT NULL,
    usuario_modifica character varying(100),
    CONSTRAINT proveedor_pk PRIMARY KEY (identificacion),
    CONSTRAINT proveedor_ref_tipo_identificacion FOREIGN KEY (tipo_identificacion)
        REFERENCES public.tipo_identificacion (tipo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT chk_estado CHECK (estado in (0, 1)) NOT VALID
);

ALTER TABLE IF EXISTS public.proveedor
    OWNER to postgres;

CREATE TABLE public.usuario
(
    username character varying(30) NOT NULL,
    pass character varying(250) NOT NULL,
    email character varying(100) NOT NULL,
    primer_nombre character varying(40) NOT NULL,
    segundo_nombre character varying(40),
    primer_apellido character varying(40) NOT NULL,
    segundo_apellido character varying(40),
    tipo_identificacion character varying(4) NOT NULL,
    identificacion character varying(20) NOT NULL,
    direccion character varying(250) NOT NULL,
    telefono character varying(20) NOT NULL,
    estado integer NOT NULL DEFAULT 1,
    fecha_creacion timestamp with time zone NOT NULL DEFAULT NOW(),
    fecha_actualizacion timestamp with time zone,
    usuario_crea character varying(30),
    usuario_modifica character varying(30),
    CONSTRAINT usuario_pk PRIMARY KEY (username),
    CONSTRAINT usuario_ref_tipo_identificacion FOREIGN KEY (tipo_identificacion)
        REFERENCES public.tipo_identificacion (tipo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT chk_estado CHECK (estado in (0, 1)) NOT VALID
);

ALTER TABLE IF EXISTS public.usuario
    OWNER to postgres;

CREATE TABLE public.usuario_rol
(
    username character varying(30) NOT NULL,
    rol character varying(30) NOT NULL,
    CONSTRAINT usuario_rol_pk PRIMARY KEY (username, rol),
    CONSTRAINT usuario_rol_ref_usuario FOREIGN KEY (username)
        REFERENCES public.usuario (username) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT usuario_rol_ref_rol FOREIGN KEY (rol)
        REFERENCES public.rol (rol) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

ALTER TABLE IF EXISTS public.usuario_rol
    OWNER to postgres;

CREATE TABLE public.producto
(
    id uuid NOT NULL,
    nombre character varying(50) NOT NULL,
    descripcion character varying(150) NOT NULL,
    id_categoria uuid NOT NULL,
    stock integer NOT NULL DEFAULT 0,
    stock_minimo integer NOT NULL DEFAULT 0,
    precio double precision NOT NULL,
    imagen character varying(500) NOT NULL,
    id_marca uuid NOT NULL,
    destacado integer not null default 0,
    estado integer NOT NULL DEFAULT 1,
    fecha_creacion timestamp with time zone NOT NULL DEFAULT NOW(),
    fecha_actualizacion timestamp with time zone,
    usuario_crea character varying(100) NOT NULL,
    usuario_modifica character varying(100),
    CONSTRAINT producto_pk PRIMARY KEY (id),
    CONSTRAINT producto_ref_categoria FOREIGN KEY (id_categoria)
        REFERENCES public.categoria (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT producto_ref_marca FOREIGN KEY (id_marca)
        REFERENCES public.marca (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
    NOT VALID,
    CONSTRAINT chk_estado CHECK (estado in (0, 1)) NOT VALID
);

ALTER TABLE IF EXISTS public.producto
    OWNER to postgres;

CREATE TABLE public.ingreso_mercancia
(
    id uuid NOT NULL,
    fecha timestamp with time zone NOT NULL,
    usuario_recibe character varying(30) NOT NULL,
    id_proveedor character varying(20),
    estado integer NOT NULL DEFAULT 1,
    fecha_creacion timestamp with time zone NOT NULL DEFAULT NOW(),
    fecha_actualizacion timestamp with time zone,
    usuario_crea character varying(100) NOT NULL,
    usuario_modifica character varying(100),
    CONSTRAINT ingreso_mercancia_pk PRIMARY KEY (id),
    CONSTRAINT ingreso_mercancia_ref_proveedor FOREIGN KEY (id_proveedor)
        REFERENCES public.proveedor (identificacion) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT ingreso_mercancia_ref_usuario FOREIGN KEY (usuario_recibe)
        REFERENCES public.usuario (username) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT chk_estado CHECK (estado in (0, 1)) NOT VALID
);

ALTER TABLE IF EXISTS public.ingreso_mercancia
    OWNER to postgres;

CREATE TABLE public.ingreso_mercancia_det
(
    codigo uuid NOT NULL,
    id_producto uuid NOT NULL,
    cantidad integer NOT NULL,
    precio double precision NOT NULL,
    CONSTRAINT ingreso_mercancia_det_pk PRIMARY KEY (codigo, id_producto),
    CONSTRAINT ingreso_mercancia_det_ref_enc FOREIGN KEY (codigo)
        REFERENCES public.ingreso_mercancia (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT ingreso_mercancia_det_ref_producto FOREIGN KEY (id_producto)
        REFERENCES public.producto (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

ALTER TABLE IF EXISTS public.ingreso_mercancia_det
    OWNER to postgres;

CREATE TABLE public.venta
(
    codigo integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    fecha timestamp with time zone NOT NULL,
    total double precision NOT NULL,
    usuario character varying(30) NOT NULL,
    total_descuento double precision DEFAULT 0,
    id_tipo_pago uuid NOT null,
    estado integer NOT NULL DEFAULT 1,
    fecha_creacion timestamp with time zone NOT NULL DEFAULT NOW(),
    fecha_actualizacion timestamp with time zone,
    usuario_modifica character varying(100),
    CONSTRAINT venta_pk PRIMARY KEY (codigo),
    CONSTRAINT venta_ref_usuario FOREIGN KEY (usuario)
        REFERENCES public.usuario (username) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT venta_ref_tipo_pago FOREIGN KEY (id_tipo_pago)
        REFERENCES public.tipo_pago (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT chk_estado CHECK (estado in (0, 1)) NOT VALID
);

ALTER TABLE IF EXISTS public.venta
    OWNER to postgres;

CREATE TABLE public.venta_detalle
(
    codigo integer NOT NULL,
    id_producto uuid NOT NULL,
    cantidad integer NOT NULL,
    precio_unitario double precision NOT NULL,
    descuento double precision NOT NULL,
    total double precision NOT NULL,
    CONSTRAINT venta_detalle_pk PRIMARY KEY (codigo, id_producto),
    CONSTRAINT venta_det_ref_producto FOREIGN KEY (id_producto)
        REFERENCES public.producto (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT venta_detalle_ref_venta FOREIGN KEY (codigo)
        REFERENCES public.venta (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

ALTER TABLE IF EXISTS public.venta_detalle
    OWNER to postgres;