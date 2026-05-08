INSERT INTO public.tipo_identificacion(tipo, descripcion, estado, fecha_creacion, fecha_actualizacion, usuario_crea, usuario_modifica)
VALUES ('CC', 'Cedula de ciudadanía', 1, NOW(), NULL, 'superuser@pruebas.com', NULL);
commit;