package ar.edu.utn.frc.bda.k7.rutas.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Importar

import org.springframework.stereotype.Service;

import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.GeoApiClient;
import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.GeoapiDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.RutaRequestDTO;
import ar.edu.utn.frc.bda.k7.rutas.dtos.CamionDTO;
import ar.edu.utn.frc.bda.k7.rutas.dtos.DepositoDTO;
import ar.edu.utn.frc.bda.k7.rutas.dtos.EstimarCostoRequestDTO;
import ar.edu.utn.frc.bda.k7.rutas.dtos.RutaCalculadaDTO;
import ar.edu.utn.frc.bda.k7.rutas.dtos.TramoDTO; // Importar
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
        List<TramoDTO> tramos = this.calcularTramosConCamiones(
            origen,
            destino,
            request.getPeso(),
            request.getVolumen()
        );

        if (tramos.isEmpty()) {
            throw new RuntimeException("No se pudo calcular la ruta");
        }

        // 3. Calcular costos iterando sobre los tramos
        double costoCombustibleTotal = 0.0;
        double costoVolumenTotal = 0.0;
        double costoEstadiaTotal = 0.0;
        
        for (TramoDTO tramo : tramos) { // 'tramos' ya es la List<TramoDTO>
            double kilometrosTramo = tramo.getRutaDelTramo().getKilometros();
            double consumoCamionTramo = tramo.getCamionAsignado().getConsumoCombustiblePromedio(); // L/Km
            
            // Costo Combustible: (Km * L/Km) * $/L
            costoCombustibleTotal += (kilometrosTramo * consumoCamionTramo) * tarifaComb.getPrecioLitro();

            // Costo por Volumen (tarifa base): Km * $/Km
            costoVolumenTotal += kilometrosTramo * tarifaVol.getCostoKmBase();

            // Costo Estadía
            if (tramo.getDepositoParada() != null) {
                costoEstadiaTotal += tramo.getDepositoParada().getCostoEstadiaDiario();
            }
        }
        

        // 4. Calcular Costo Total
        double costoTotal = costoCombustibleTotal + costoVolumenTotal + costoEstadiaTotal;

        // 5. Devolver el DTO final con la LISTA DE TRAMOS original
        return new RutaCalculadaDTO(tramos, costoTotal); // <-- Usamos 'tramos'
    }


    // --- LÓGICA DE CONSTRUCCIÓN DE RUTA ---
    private List<TramoDTO> calcularTramosConCamiones(UbicacionDTO origen, UbicacionDTO destino, Double peso, Double volumen) {
        
        List<TramoDTO> tramosCalculados = new ArrayList<>();
        
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
                tramosCalculados.add(new TramoDTO(rutaDirecta, camionParaEsteTramo, null));
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

            tramosCalculados.add(new TramoDTO(rutaAlDepot, camionParaEsteTramo, mejorParada));

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
}