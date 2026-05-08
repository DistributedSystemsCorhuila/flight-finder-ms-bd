# Docker Compose Quick Commands

## Development Environment

### Start Database
```bash
docker-compose -f docker-compose.db.dev.yml --env-file .env.db.dev --env-file .env.dev up -d
```

### Start Application
```bash
docker-compose -f docker-compose.app.dev.yml --env-file .env.app.dev --env-file .env.dev up -d
```

### Stop Database
```bash
docker-compose -f docker-compose.db.dev.yml --env-file .env.db.dev down
```

### Stop Application
```bash
docker-compose -f docker-compose.app.dev.yml --env-file .env.app.dev down
```

### Database Logs
```bash
docker-compose -f docker-compose.db.dev.yml --env-file .env.db.dev logs -f
```

### Application Logs
```bash
docker-compose -f docker-compose.app.dev.yml --env-file .env.app.dev logs -f
```

### Rebuild Application
```bash
docker-compose -f docker-compose.app.dev.yml --env-file .env.app.dev --env-file .env.dev up -d --build
```

### Remove database volumes (clean start)
```bash
docker-compose -f docker-compose.db.dev.yml --env-file .env.db.dev down -v
```

---

## Staging Environment

### Start Database
```bash
docker-compose -f docker-compose.db.qa.yml --env-file .env.db.qa --env-file .env.qa up -d
```

### Start Application
```bash
docker-compose -f docker-compose.app.qa.yml --env-file .env.app.qa --env-file .env.qa up -d
```

### Stop Database
```bash
docker-compose -f docker-compose.db.qa.yml --env-file .env.db.qa down
```

### Stop Application
```bash
docker-compose -f docker-compose.app.qa.yml --env-file .env.app.qa down
```

### Database Logs
```bash
docker-compose -f docker-compose.db.qa.yml --env-file .env.db.qa logs -f
```

### Application Logs
```bash
docker-compose -f docker-compose.app.qa.yml --env-file .env.app.qa logs -f
```

---

## Production Environment

### Start Database
```bash
docker-compose -f docker-compose.db.prod.yml --env-file .env.db.prod --env-file .env.prod up -d
```

### Start Application
```bash
docker-compose -f docker-compose.app.prod.yml --env-file .env.app.prod --env-file .env.prod up -d
```

### Stop Database
```bash
docker-compose -f docker-compose.db.prod.yml --env-file .env.db.prod down
```

### Stop Application
```bash
docker-compose -f docker-compose.app.prod.yml --env-file .env.app.prod down
```

### Database Logs
```bash
docker-compose -f docker-compose.db.prod.yml --env-file .env.db.prod logs -f
```

### Application Logs
```bash
docker-compose -f docker-compose.app.prod.yml --env-file .env.app.prod logs -f
```

---

## General Commands

### List all running containers
```bash
docker ps
```

### Access PostgreSQL Database
```bash
# Development
docker-compose -f docker-compose.db.dev.yml --env-file .env.db.dev exec postgres psql -U postgres -d flight_finder_db

# Staging
docker-compose -f docker-compose.db.qa.yml --env-file .env.db.qa exec postgres psql -U postgres_staging -d flight_finder_db_staging

# Production
docker-compose -f docker-compose.db.prod.yml --env-file .env.db.prod exec postgres psql -U postgres_prod -d flight_finder_db_prod
```

### View all containers status
```bash
# Development
docker-compose -f docker-compose.db.dev.yml --env-file .env.db.dev ps
docker-compose -f docker-compose.app.dev.yml --env-file .env.app.dev ps

# Staging
docker-compose -f docker-compose.db.qa.yml --env-file .env.db.qa ps
docker-compose -f docker-compose.app.qa.yml --env-file .env.app.qa ps

# Production
docker-compose -f docker-compose.db.prod.yml --env-file .env.db.prod ps
docker-compose -f docker-compose.app.prod.yml --env-file .env.app.prod ps
```

### Remove all stopped containers
```bash
docker container prune
```

### Remove unused volumes
```bash
docker volume prune
```

### View container stats
```bash
docker stats
```

### Stop All Services
```bash
# Development
docker-compose -f docker-compose.db.dev.yml --env-file .env.db.dev down
docker-compose -f docker-compose.app.dev.yml --env-file .env.app.dev down

# Staging
docker-compose -f docker-compose.db.qa.yml --env-file .env.db.qa down
docker-compose -f docker-compose.app.qa.yml --env-file .env.app.qa down

# Production
docker-compose -f docker-compose.db.prod.yml --env-file .env.db.prod down
docker-compose -f docker-compose.app.prod.yml --env-file .env.app.prod down
```

