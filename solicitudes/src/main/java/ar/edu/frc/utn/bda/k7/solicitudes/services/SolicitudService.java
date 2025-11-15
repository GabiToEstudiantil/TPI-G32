package ar.edu.frc.utn.bda.k7.solicitudes.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.RutasServiceClient;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.CrearSolicitudRequestDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.EstimarCostoRequestDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.RutaCalculadaDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.TramoRutaDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Contenedor;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.ContenedorEstado;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.ParadaEnDeposito;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Ruta;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Solicitud;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Tramo;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.TramoEstado;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.TramoTipo;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.SolicitudDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.repositories.SolicitudRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SolicitudService {
    
    private final SolicitudRepo solicitudRepo;

    private final ContenedorService contenedorService;
    private final RutasServiceClient rutasServiceClient;
    private final RutaService rutaService;
    private final TramoService tramoService;
    private final ParadaEnDepositoService paradaEnDepositoService;

    //Mappers
    public SolicitudDTO toDto(Solicitud solicitud) {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setId(solicitud.getId());
        dto.setFechaCreacion(solicitud.getFechaCreacion());
        dto.setTiempoEstimado(solicitud.getTiempoEstimado());
        dto.setCostoEstimado(solicitud.getCostoEstimado());
        dto.setTiempoReal(solicitud.getTiempoReal());
        dto.setCostoFinal(solicitud.getCostoFinal());
        dto.setClienteDni(solicitud.getClienteDni());
        dto.setContenedor(contenedorService.toDto(solicitud.getContenedor()));
        return dto;
    }

    public Solicitud toSolicitud(SolicitudDTO dto) {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(dto.getId());
        solicitud.setFechaCreacion(dto.getFechaCreacion());
        solicitud.setTiempoEstimado(dto.getTiempoEstimado());
        solicitud.setCostoEstimado(dto.getCostoEstimado());
        solicitud.setTiempoReal(dto.getTiempoReal());
        solicitud.setCostoFinal(dto.getCostoFinal());
        solicitud.setClienteDni(dto.getClienteDni());
        solicitud.setContenedor(contenedorService.toContenedor(dto.getContenedor()));
        return solicitud;
    }
    //FIN mappers

    public ArrayList<SolicitudDTO> obtenerTodas(){
        ArrayList<SolicitudDTO> solicitudDTOs = new ArrayList<>();
        for (Solicitud solicitud : solicitudRepo.findAll()){
            solicitudDTOs.add(toDto(solicitud));
        }
        return solicitudDTOs;
    }

    public SolicitudDTO buscarPorId(Integer id){
        return toDto(solicitudRepo.findById(id).orElse(null));
    }

    public ArrayList<SolicitudDTO> obtenerPorDniCliente(String dni){
        ArrayList<SolicitudDTO> solicitudDTOs = new ArrayList<>();
        for (Solicitud solicitud : solicitudRepo.findByClienteDni(dni)){
            solicitudDTOs.add(toDto(solicitud));
        }
        return solicitudDTOs;
    }

    @Transactional
    public void eliminar(Integer id){
        SolicitudDTO dto = buscarPorId(id);
        Object estado = dto.getContenedor().getEstado();
        if (estado.equals("EN_TRANSITO") || estado.equals("EN_DEPOSITO")) {
            throw new IllegalStateException("No se puede eliminar una solicitud en tránsito");
        }
        solicitudRepo.deleteById(id);
    }

    // ------------------------------------------------ POST DE SOLICITUD ------------------------------------------------
    @Transactional
    public SolicitudDTO crearSolicitudCompleta(CrearSolicitudRequestDTO request) {
        
        // 1. Creamos el DTO para el servicio de rutas
        EstimarCostoRequestDTO estimarRequest = new EstimarCostoRequestDTO();
        estimarRequest.setIdOrigen(request.getOrigenId());
        estimarRequest.setIdDestino(request.getDestinoId());
        estimarRequest.setPeso(request.getPeso());
        estimarRequest.setVolumen(request.getVolumen());

        // 2. Llamamos al servicio de rutas
        RutaCalculadaDTO rutaCalculada = rutasServiceClient.estimarCosto(estimarRequest);
        if (rutaCalculada == null) {
            throw new RuntimeException("No se pudo calcular la ruta.");
        }

        // 3. Si el contenedor no existe, lo creamos; si existe y está disponible, lo asignamos; si está en tránsito o en depósito, error
        Contenedor contenedor = contenedorService.findByCodigoIdentificacion(request.getCodigoContenedor());
        if (contenedor == null) {
            contenedor = new Contenedor();
            contenedor.setCodigoIdentificacion(request.getCodigoContenedor());
            contenedor.setPeso(request.getPeso());
            contenedor.setVolumen(request.getVolumen());
            contenedor.setEstado(ContenedorEstado.ASIGNADO);
            contenedor.setClienteDni(request.getClienteDni());
        } else {
            if (contenedor.getEstado() == ContenedorEstado.EN_TRANSITO || contenedor.getEstado() == ContenedorEstado.EN_DEPOSITO) {
                throw new IllegalStateException("El contenedor ya está en tránsito o en depósito.");
            }
            contenedor.setPeso(request.getPeso()); // Capaz lleva algo adentro el contenedor
            // No le cambio el volumen pq no tendría sentido q el contenedor cambie de volumen xd
            contenedor.setEstado(ContenedorEstado.ASIGNADO);
        }
        Contenedor contenedorGuardado = contenedorService.save(contenedor);

        // 4. Creamos y guardamos la Solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setClienteDni(request.getClienteDni());
        solicitud.setContenedor(contenedorGuardado);
        solicitud.setCostoEstimado(rutaCalculada.getCostoEstimado());
        long totalSegundos = rutaCalculada.getTramos().stream()
                .mapToLong(t -> t.getRutaDelTramo().getDuracionSegundos())
                .sum();
        solicitud.setTiempoEstimado(String.format("%d horas", totalSegundos / 3600));
        
        Solicitud solicitudGuardada = solicitudRepo.save(solicitud);

        // 5. Creamos y guardamos la Ruta
        Ruta ruta = new Ruta();
        ruta.setSolicitud(solicitudGuardada);
        ruta.setCantidadTramos(rutaCalculada.getTramos().size());
        ruta.setCantidadDepositos((int) rutaCalculada.getTramos().stream() // el (int) es para hacer que el long del count se convierta en int
                .filter(t -> t.getDepositoParada() != null)
                .count());
        
        Ruta rutaGuardada = rutaService.save(ruta);

        // 6. Recorremos los tramos recibidos y los guardamos
        int cant = 1;
        for (TramoRutaDTO tramoRuta : rutaCalculada.getTramos()) {
            
            // Guardamos el Tramo
            Tramo tramo = new Tramo();
            tramo.setRuta(rutaGuardada);
            tramo.setDistanciaKm(tramoRuta.getRutaDelTramo().getKilometros());
            tramo.setCostoAproximado(tramoRuta.getCostoEstimado());
            tramo.setCamionDominio(tramoRuta.getCamionAsignado().getDominio());
            tramo.setEstado(TramoEstado.ESTIMADO);
            
            if (cant == 1) { // Es el primer tramo
                tramo.setOrigenId(request.getOrigenId());
                tramo.setTipo(tramoRuta.getDepositoParada() != null ? TramoTipo.ORIGEN_DEPOSITO : TramoTipo.ORIGEN_DESTINO);
            } else {
                // El origen es el depósito del tramo anterior
                TramoRutaDTO tramoAnterior = rutaCalculada.getTramos().get(cant - 2); // Como arrancamos en 1 hay q restar 2 :b
                tramo.setOrigenId(tramoAnterior.getDepositoParada().getUbicacion().getId());
                tramo.setTipo(tramoRuta.getDepositoParada() != null ? TramoTipo.DEPOSITO_DEPOSITO : TramoTipo.DEPOSITO_DESTINO);
            }
            
            if (tramoRuta.getDepositoParada() != null) {
                // Destino es un depósito
                tramo.setDestinoId(tramoRuta.getDepositoParada().getUbicacion().getId());

                // Guardamos la ParadaEnDeposito
                ParadaEnDeposito parada = new ParadaEnDeposito();
                parada.setRuta(rutaGuardada);
                parada.setDepositoId(tramoRuta.getDepositoParada().getId());
                parada.setOrdenEnRuta(cant); // Por eso no arrancamos con la cant en 0, pq sino te queda que el orden es 0 y cualquier cosa es eso
                paradaEnDepositoService.save(parada);
                
            } else {
                // Destino es el final
                tramo.setDestinoId(request.getDestinoId());
            }

            tramoService.save(tramo);
            cant++;
        }

        // 7. Devolvemos el DTO de la solicitud creada
        return new SolicitudDTO(
            solicitudGuardada.getId(),
            solicitudGuardada.getFechaCreacion(),
            solicitudGuardada.getCostoEstimado(),
            solicitudGuardada.getTiempoEstimado(),
            solicitudGuardada.getCostoFinal(),
            solicitudGuardada.getTiempoReal(),
            solicitudGuardada.getClienteDni(),
            contenedorService.toDto(contenedorGuardado)
        );
    }
    // ------------------------------------------------ FIN POST DE SOLICITUD ------------------------------------------------

}
