Instrucciones rápidas

- El frontend ya está dentro de `frontend/index.html` y el contenedor estático `frontend` sirve la página mediante nginx.
- Para evitar problemas de CORS la recomendación es acceder al frontend a través de Traefik (mismo origen que las APIs). Tras actualizar puertos Traefik está en `http://localhost:8081` por defecto en este repo.
- Abra el frontend en: `http://localhost:8081/frontend/index.html`

Comportamiento por defecto
- El campo "Gateway base" en la UI se inicializa con el origen de la página (`window.location.origin`). Si accede por Traefik en `http://localhost:8081` las peticiones apuntarán automáticamente a `http://localhost:8081/employee`, `http://localhost:8081/access`, etc.


