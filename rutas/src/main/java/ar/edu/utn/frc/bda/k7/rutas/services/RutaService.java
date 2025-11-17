package ar.edu.utn.frc.bda.k7.rutas.services;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.GeoApiClient;
import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.GeoapiDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.RutaRequestDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos.CalcularDefRequestDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos.CalcularDefResponseDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos.EstimarCostoRequestDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos.ParadaEnDepositoDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos.RutaCalculadaDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos.TramoCalcularRutaDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos.TramoDTO;
import ar.edu.utn.frc.bda.k7.rutas.dtos.CamionDTO;
import ar.edu.utn.frc.bda.k7.rutas.dtos.DepositoDTO;
import ar.edu.utn.frc.bda.k7.rutas.dtos.UbicacionDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.TarifaCombustible;
import ar.edu.utn.frc.bda.k7.rutas.entities.TarifaVolumen;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor // Lombok inyectará todos los services
public class RutaService {

    // Services
    private final UbicacionService ubicacionService;
    private final DepositoService depositoService;
    private final GeoApiClient geoApiClient;
    private final CamionService camionService;
    private final TarifaCombustibleService tarifaCombustibleService;
    private final TarifaVolumenService tarifaVolumenService;

    // Constantes de tiempo de conducción en segundos
    private static final long minimoConduccionSegundos = 8 * 3600; // 8 horas
    private static final long maximoConduccionSegundos = 12 * 3600; // 12 horas

    // --- LÓGICA DE ESTIMACIÓN DE COSTO ---

    public RutaCalculadaDTO estimarCostoRuta(EstimarCostoRequestDTO request) {
        
        // 1. Obtener datos base para el cálculo (tarifas y ubicaciones)
        TarifaCombustible tarifaComb = tarifaCombustibleService.getPrimeraTarifa();
        if (tarifaComb == null) {
            throw new RuntimeException("No se encontraron tarifas de combustible");
        }

        TarifaVolumen tarifaVol = tarifaVolumenService.findByVolumen(request.getVolumen());
        if (tarifaVol == null) {
            throw new RuntimeException("No se encontró tarifa para el volumen: " + request.getVolumen());
        }

        UbicacionDTO origen = ubicacionService.getUbicacionById(request.getIdOrigen());
        UbicacionDTO destino = ubicacionService.getUbicacionById(request.getIdDestino());

        if (origen == null || destino == null) {
            throw new RuntimeException("Origen o Destino no encontrado");
        }

        // 2. Calcular los tramos, asignando camiones en el proceso
        //    (Este método ahora devuelve la lista de DTOs directamente)
        List<TramoCalcularRutaDTO> tramos = this.calcularTramosConCamiones(
            origen,
            destino,
            request.getPeso(),
            request.getVolumen()
        );

        if (tramos.isEmpty()) {
            throw new RuntimeException("No se pudo calcular la ruta");
        }

        // 3. Calcular costos iterando sobre los tramos
        double costoTotal = 0.0; // Arrancamos a contar

        for (TramoCalcularRutaDTO tramo : tramos) {
            double kilometrosTramo = tramo.getRutaDelTramo().getKilometros();
            double consumoCamionTramo = tramo.getCamionAsignado().getConsumoCombustiblePromedio();
            
            // Calcular costos individuales del tramo
            double costoCombustibleTramo = (kilometrosTramo * consumoCamionTramo) * tarifaComb.getPrecioLitro();
            double costoVolumenTramo = kilometrosTramo * tarifaVol.getCostoKmBase();
            double costoEstadiaTramo = 0.0;

            if (tramo.getDepositoParada() != null) {
                costoEstadiaTramo = tramo.getDepositoParada().getCostoEstadiaDiario();
            }

            // Sumamos los costos del tramo y los guardamos
            double costoTramo = costoCombustibleTramo + costoVolumenTramo + costoEstadiaTramo;
            tramo.setCostoEstimado(costoTramo); // <-- ¡AQUÍ ESTÁ LA LÍNEA CLAVE!

            // Sumamamos el costo del tramo al costo total general
            costoTotal += costoTramo;
        }

        // 4. Devolvemos el DTO final
        return new RutaCalculadaDTO(tramos, costoTotal);
    }


    // --- LÓGICA DE CONSTRUCCIÓN DE RUTA ---
    private List<TramoCalcularRutaDTO> calcularTramosConCamiones(UbicacionDTO origen, UbicacionDTO destino, Double peso, Double volumen) {
        
        List<TramoCalcularRutaDTO> tramosCalculados = new ArrayList<>();
        
        List<DepositoDTO> depositosDisponibles = depositoService.getAllDepositos();
        List<CamionDTO> camionesDisponibles = camionService.findCamionesDisponibles(peso, volumen);
        
        if (camionesDisponibles.isEmpty()) {
            throw new RuntimeException("No hay camiones disponibles que soporten el peso/volumen requerido.");
        }

        String puntoActual = origen.getDireccionTextual();
        String destinoFinal = destino.getDireccionTextual();
        CamionDTO camionEnDescanso = null;

        while (true) {
            RutaRequestDTO requestDirecto = new RutaRequestDTO(puntoActual, destinoFinal, null);
            GeoapiDTO rutaDirecta = geoApiClient.obtenerRuta(requestDirecto);

            if (rutaDirecta == null) {
                throw new RuntimeException("No se pudo calcular la ruta directa desde " + puntoActual);
            }

            CamionDTO camionParaEsteTramo = findSiguienteCamion(camionesDisponibles, camionEnDescanso);
            camionEnDescanso = camionParaEsteTramo;

            if (rutaDirecta.getDuracionSegundos() <= maximoConduccionSegundos) {
                TramoCalcularRutaDTO tramoFinal = new TramoCalcularRutaDTO();
                tramoFinal.setRutaDelTramo(rutaDirecta);
                tramoFinal.setCamionAsignado(camionParaEsteTramo);
                tramoFinal.setDepositoParada(null);
                tramosCalculados.add(tramoFinal);
                break;
            }

            DepositoDTO mejorParada = encontrarMejorParada(
                puntoActual,
                destinoFinal,
                rutaDirecta.getKilometros(),
                depositosDisponibles
            );

            if (mejorParada == null) {
                throw new RuntimeException("No se puede calcular la ruta. No hay depósitos intermedios alcanzables desde " + puntoActual);
            }

            String direccionParada = mejorParada.getUbicacion().getDireccionTextual();
            RutaRequestDTO requestAlDepot = new RutaRequestDTO(puntoActual, direccionParada, null);
            GeoapiDTO rutaAlDepot = geoApiClient.obtenerRuta(requestAlDepot);

            if (rutaAlDepot == null) {
                throw new RuntimeException("No se pudo calcular la ruta al depósito seleccionado: " + direccionParada);
            }

            TramoCalcularRutaDTO tramoIntermedio = new TramoCalcularRutaDTO();
            tramoIntermedio.setRutaDelTramo(rutaAlDepot);
            tramoIntermedio.setCamionAsignado(camionParaEsteTramo);
            tramoIntermedio.setDepositoParada(mejorParada);

            tramosCalculados.add(tramoIntermedio);

            puntoActual = direccionParada;
            depositosDisponibles.remove(mejorParada);
        }

        return tramosCalculados;
    }

    private CamionDTO findSiguienteCamion(List<CamionDTO> camionesDisponibles, CamionDTO camionEnDescanso) {
        if (camionesDisponibles.isEmpty()) {
            throw new RuntimeException("No hay camiones disponibles.");
        }
        
        Optional<CamionDTO> camionNuevo = camionesDisponibles.stream()
            .filter(c -> !c.equals(camionEnDescanso))
            .findFirst();

        if (camionNuevo.isPresent()) {
            return camionNuevo.get();
        }
        
        return camionesDisponibles.get(0);
    }

    private DepositoDTO encontrarMejorParada(String puntoActual, String destinoFinal, double distanciaDirecta, List<DepositoDTO> depositos) {
        
        DepositoDTO mejorParadaOptima = null;
        double minimaDesviacionOptima = Double.MAX_VALUE;

        DepositoDTO mejorParadaSubOptima = null;
        double minimaDesviacionSubOptima = Double.MAX_VALUE;

        for (DepositoDTO depot : depositos) {
            String dirDeposito = depot.getUbicacion().getDireccionTextual();
            if (dirDeposito.equals(puntoActual)) continue;

            RutaRequestDTO requestAlDepot = new RutaRequestDTO(puntoActual, dirDeposito, null);
            GeoapiDTO rutaAlDepot = geoApiClient.obtenerRuta(requestAlDepot);

            if (rutaAlDepot == null) continue;

            long duracionAlDepot = rutaAlDepot.getDuracionSegundos();
            
            if (duracionAlDepot > maximoConduccionSegundos) {
                continue;
            }

            RutaRequestDTO requestDepotAlDestino = new RutaRequestDTO(dirDeposito, destinoFinal, null);
            GeoapiDTO rutaDepotAlDestino = geoApiClient.obtenerRuta(requestDepotAlDestino);

            if (rutaDepotAlDestino == null) continue;

            double distanciaAlDepot = rutaAlDepot.getKilometros();
            double distanciaDepotAlDestino = rutaDepotAlDestino.getKilometros();
            double desviacion = (distanciaAlDepot + distanciaDepotAlDestino) - distanciaDirecta;

            
            if (duracionAlDepot >= minimoConduccionSegundos) {
                if (desviacion < minimaDesviacionOptima) {
                    minimaDesviacionOptima = desviacion;
                    mejorParadaOptima = depot;
                }
            }
            else {
                if (desviacion < minimaDesviacionSubOptima) {
                    minimaDesviacionSubOptima = desviacion;
                    mejorParadaSubOptima = depot;
                }
            }
        }

        return (mejorParadaOptima != null) ? mejorParadaOptima : mejorParadaSubOptima;
    }

    public CalcularDefResponseDTO calcularDefinitivo(CalcularDefRequestDTO request) {
        TarifaCombustible tarifaComb = tarifaCombustibleService.getPrimeraTarifa();
        
        if (tarifaComb == null) {
            throw new RuntimeException("No se encontró tarifa de combustible");
        }
        
        List<TramoDTO> tramosConCostos = new ArrayList<>();
        List<ParadaEnDepositoDTO> paradasConCostos = new ArrayList<>();
        Double costoTotal = 0.0;
        Long segundosTotales = 0L;
        
        // Procesar tramos
        for (TramoDTO tramo : request.getTramos()) {
            CamionDTO camion = camionService.getCamionByDominio(tramo.getCamionDominio());
            
            if (camion == null) {
                throw new RuntimeException("Camión no encontrado: " + tramo.getCamionDominio());
            }
            
            Double costoTramo = camion.getConsumoCombustiblePromedio() * tarifaComb.getPrecioLitro() * tramo.getDistanciaKm();
            tramo.setCostoReal(costoTramo);
            tramosConCostos.add(tramo);
            costoTotal += costoTramo;
            
            // ✅ VALIDAR que las fechas NO sean null
            if (tramo.getFechaHoraInicio() != null && tramo.getFechaHoraFin() != null) {
                Long segundosTramo = ChronoUnit.SECONDS.between(tramo.getFechaHoraInicio(), tramo.getFechaHoraFin());
                segundosTotales += segundosTramo;
            } else {
                System.err.println("⚠️ Tramo sin fechas: " + tramo.getId() + " - Se omite del cálculo de tiempo");
            }
        }
        
        // Procesar paradas en depósitos
        for (ParadaEnDepositoDTO parada : request.getParadasEnDeposito()) {
            DepositoDTO deposito = depositoService.getDepositoById(parada.getDepositoId());
            
            if (deposito == null) {
                throw new RuntimeException("Depósito no encontrado: " + parada.getDepositoId());
            }
            
            Long segundosEstadia = parada.getSegundosEstadia();
            
            // ✅ VALIDAR que segundosEstadia NO sea null
            if (segundosEstadia != null && segundosEstadia > 0) {
                Double costoEstadia = (deposito.getCostoEstadiaDiario() / 24) * (segundosEstadia / 3600.0);
                parada.setCostoTotalEstadia(costoEstadia);
                paradasConCostos.add(parada);
                costoTotal += costoEstadia;
                segundosTotales += segundosEstadia;
            } else {
                System.err.println("⚠️ Parada sin tiempo de estadía: " + parada.getId());
                parada.setCostoTotalEstadia(0.0);
                paradasConCostos.add(parada);
            }
        }
        
        return new CalcularDefResponseDTO(costoTotal, tramosConCostos, segundosTotales, paradasConCostos);
    }
}