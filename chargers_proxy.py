
import json
import os
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from urllib.error import HTTPError, URLError
from urllib.parse import quote, urlsplit
from urllib.request import Request, urlopen

# La clave se introduce en PowerShell y no se guarda en este archivo.
API_KEY = os.environ.get("QUACKMOVE_MAPS_DEMO_KEY")

if not API_KEY:
    raise SystemExit("Falta configurar la clave Demo de Google Maps.")


class QuackMoveHandler(BaseHTTPRequestHandler):

    def do_GET(self):
        path = urlsplit(self.path).path

        if path == "/chargers":
            self.get_chargers()
        elif path == "/route":
            self.get_route()
        elif path == "/map":
            self.get_map()
        else:
            self.respond(404, {"error": "Ruta no encontrada"})

    # Enviar solicitudes POST a los servicios de Google.
    def google_request(self, url, payload, field_mask):
        body = json.dumps(payload, ensure_ascii=False).encode("utf-8")

        request = Request(
            url,
            data=body,
            headers={
                "X-Goog-Api-Key": API_KEY,
                "X-Goog-FieldMask": field_mask,
                "Content-Type": "application/json; charset=utf-8"
            },
            method="POST"
        )

        try:
            with urlopen(request, timeout=25) as response:
                return json.load(response)
        except HTTPError as error:
            self.respond(502, {
                "error": f"Google devolvió HTTP {error.code}"
            })
        except (URLError, TimeoutError):
            self.respond(503, {
                "error": "No se pudo conectar con Google"
            })

        return None

    # Mantener la búsqueda de estaciones que ya funciona.
    def get_chargers(self):
        payload = {
            "textQuery": (
                "estaciones de carga de vehículos eléctricos "
                "en Ciudad de Guatemala"
            ),
            "languageCode": "es",
            "pageSize": 5
        }

        result = self.google_request(
            "https://places.googleapis.com/v1/places:searchText",
            payload,
            "places.displayName,places.formattedAddress"
        )

        if result is not None:
            self.respond(200, result)

    # Mantener la ruta de prueba Guatemala -> Flores.
    def get_route(self):
        payload = {
            "origin": {
                "location": {
                    "latLng": {
                        "latitude": 14.6349,
                        "longitude": -90.5069
                    }
                }
            },
            "destination": {
                "location": {
                    "latLng": {
                        "latitude": 16.9299,
                        "longitude": -89.8915
                    }
                }
            },
            "travelMode": "DRIVE",
            "routingPreference": "TRAFFIC_UNAWARE",
            "computeAlternativeRoutes": False,
            "units": "METRIC"
        }

        result = self.google_request(
            "https://routes.googleapis.com/directions/v2:computeRoutes",
            payload,
            "routes.distanceMeters,routes.duration"
        )

        if result is None:
            return

        routes = result.get("routes", [])
        if not routes:
            self.respond(404, {
                "error": "Google no encontró una ruta en automóvil"
            })
            return

        route = routes[0]
        distance_meters = route["distanceMeters"]
        duration_seconds = float(route["duration"].removesuffix("s"))

        self.respond(200, {
            "origin": "Ciudad de Guatemala",
            "destination": "Flores, Petén",
            "distanceKm": round(distance_meters / 1000, 1),
            "drivingMinutes": round(duration_seconds / 60),
            "note": (
                "Estimación sin tráfico en tiempo real. "
                "No incluye paradas de recarga."
            )
        })

    # NUEVO: mostrar un mapa interactivo de Google con un marcador.
    def get_map(self):
        html = """<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mapa QuackMove</title>
    <style>
        html, body, #map {
            width: 100%;
            height: 100%;
            margin: 0;
            padding: 0;
        }
        #status {
            position: absolute;
            top: 12px;
            left: 12px;
            z-index: 5;
            padding: 10px 14px;
            border-radius: 10px;
            background: white;
            color: #182335;
            font: 13px Arial, sans-serif;
            box-shadow: 0 2px 8px rgba(0, 0, 0, .15);
        }
    </style>
    <script>
        window.initMap = async function () {
            const status = document.getElementById("status");
            try {
                const point = { lat: 14.6349, lng: -90.5069 };
                const map = new google.maps.Map(
                    document.getElementById("map"),
                    {
                        center: point,
                        zoom: 13,
                        mapId: "DEMO_MAP_ID",
                        mapTypeControl: false
                    }
                );

                const { AdvancedMarkerElement } =
                    await google.maps.importLibrary("marker");

                new AdvancedMarkerElement({
                    map: map,
                    position: point,
                    title: "Ciudad de Guatemala"
                });

                status.textContent = "Ciudad de Guatemala - punto de prueba";
            } catch (error) {
                console.error(error);
                status.textContent = "No se pudo cargar el mapa";
            }
        };

        window.gm_authFailure = function () {
            document.getElementById("status").textContent =
                "Google Maps rechazó la clave API";
        };
    </script>
</head>
<body>
    <div id="status">Cargando Google Maps...</div>
    <div id="map"></div>
    <script
        src="https://maps.googleapis.com/maps/api/js?key=__API_KEY__&callback=initMap&v=weekly&libraries=marker"
        async defer
        onerror="document.getElementById('status').textContent='No se pudo descargar Google Maps'">
    </script>
</body>
</html>"""

        # Solo se usa esta clave en el prototipo de demostración.
        html = html.replace("__API_KEY__", quote(API_KEY, safe=""))
        data = html.encode("utf-8")

        self.send_response(200)
        self.send_header("Content-Type", "text/html; charset=utf-8")
        self.send_header("Content-Length", str(len(data)))
        self.send_header("Cache-Control", "no-store")
        self.end_headers()
        self.wfile.write(data)

    # Enviar respuestas JSON a la aplicación Android.
    def respond(self, status, payload):
        data = json.dumps(payload, ensure_ascii=False).encode("utf-8")

        self.send_response(status)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(data)))
        self.send_header("Cache-Control", "no-store")
        self.end_headers()
        self.wfile.write(data)


if __name__ == "__main__":
    print("Servidor de QuackMove iniciado")
    print("Cargadores: http://127.0.0.1:8765/chargers")
    print("Viajes: http://127.0.0.1:8765/route")
    print("Mapa: http://127.0.0.1:8765/map")
    print("Presiona Ctrl+C para detenerlo")

    ThreadingHTTPServer(( "127.0.0.1", 8765), QuackMoveHandler).serve_forever()
