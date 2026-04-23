# flight-finder-ms-bd
Servicio de persistencia y configuracion de base de datos.

## Docker por ambientes
El proyecto usa un solo `docker-compose.yml` y variables por entorno en archivos `.env`.

### Ambientes disponibles
- Develop: `.env.develop` (app `8080`, db `5432`)
- QA: `.env.qa` (app `8081`, db `5433`)
- Main: `.env.main` (app `8082`, db `5434`)

### Levantar ambiente
```powershell
docker compose --env-file .env.develop -p flight-finder-develop up -d --build
docker compose --env-file .env.qa -p flight-finder-qa up -d --build
docker compose --env-file .env.main -p flight-finder-main up -d --build
```

### Bajar ambiente
```powershell
docker compose --env-file .env.develop -p flight-finder-develop down
docker compose --env-file .env.qa -p flight-finder-qa down
docker compose --env-file .env.main -p flight-finder-main down
```
