-- Script para configurar MySQL para conexiones Docker
-- Ejecutar como root en MySQL

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS educagestor_db;

-- Crear usuario que puede conectarse desde cualquier IP (para Docker)
CREATE USER IF NOT EXISTS 'educagestor_user1'@'%' IDENTIFIED BY 'educagestor_pass';

-- Otorgar privilegios al usuario desde cualquier IP
GRANT ALL PRIVILEGES ON educagestor_db.* TO 'educagestor_user1'@'%';

-- Actualizar privilegios del usuario localhost existente (si existe)
GRANT ALL PRIVILEGES ON educagestor_db.* TO 'educagestor_user1'@'localhost';

-- Aplicar cambios
FLUSH PRIVILEGES;

-- Verificar usuarios creados
SELECT User, Host FROM mysql.user WHERE User = 'educagestor_user1';
