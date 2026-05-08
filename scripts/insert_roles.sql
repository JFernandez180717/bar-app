INSERT INTO public.rol(rol, descripcion, estado, fecha_creacion, fecha_actualizacion, usuario_crea, usuario_modifica)
VALUES ('SUPER', 'Super usuario', 0, NOW(), NULL, 'superuser@pruebas.com', NULL);
INSERT INTO public.rol(rol, descripcion, estado, fecha_creacion, fecha_actualizacion, usuario_crea, usuario_modifica)
VALUES ('ADMIN', 'Administrador', 1, NOW(), NULL, 'superuser@pruebas.com', NULL);
INSERT INTO public.rol(rol, descripcion, estado, fecha_creacion, fecha_actualizacion, usuario_crea, usuario_modifica)
VALUES ('USER', 'Usuario', 1, NOW(), NULL, 'superuser@pruebas.com', NULL);
commit;