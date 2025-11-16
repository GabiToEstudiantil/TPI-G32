package ar.edu.frc.utn.bda.k7.solicitudes.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.RutasServiceClient;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.CalcularDefRequestDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.CalcularDefResponseDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.CrearSolicitudRequestDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.EstimarCostoRequestDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.RutaCalculadaDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.TramoRutaDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Contenedor;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.ContenedorEstado;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.ParadaEnDeposito;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Ruta;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Solicitud;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.SolicitudEstado;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Tramo;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.TramoEstado;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.TramoTipo;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.ParadaEnDepositoDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.RutaDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.SolicitudDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.TrackingDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.TramoDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.TramoEstadoPatchDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.repositories.RutaRepo;
import ar.edu.frc.utn.bda.k7.solicitudes.repositories.SolicitudRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SolicitudService {
    
    private final SolicitudRepo solicitudRepo;

    private final ContenedorService contenedorService;
    private final RutasServiceClient rutasServiceClient;
    private final RutaRepo rutaRepo;
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

        //Mappers
    public RutaDTO toDto(Ruta ruta) {
        RutaDTO dto = new RutaDTO();
        dto.setId(ruta.getId());
        dto.setCantidadTramos(ruta.getCantidadTramos());
        dto.setCantidadDepositos(ruta.getCantidadDepositos());
        dto.setSolicitud(this.toDto(ruta.getSolicitud()));
        return dto;
    }

    public Ruta toRuta(RutaDTO dto) {
        Ruta ruta = new Ruta();
        ruta.setId(dto.getId());
        ruta.setCantidadTramos(dto.getCantidadTramos());
        ruta.setCantidadDepositos(dto.getCantidadDepositos());
        ruta.setSolicitud(this.toSolicitud(dto.getSolicitud()));
        return ruta;
    }
    //FIN Mappers

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
        
        Ruta rutaGuardada = rutaRepo.save(ruta);

        // 6. Recorremos los tramos recibidos y los guardamos
        int cant = 1;
        for (TramoRutaDTO tramoRuta : rutaCalculada.getTramos()) {
            
            // Guardamos el Tramo
            Tramo tramo = new Tramo();
            tramo.setRuta(rutaGuardada);
            tramo.setDistanciaKm(tramoRuta.getRutaDelTramo().getKilometros());
            tramo.setCostoAproximado(tramoRuta.getCostoEstimado());
            tramo.setCamionDominio(tramoRuta.getCamionAsignado().getDominio());
            tramo.setOrdenEnRuta(cant);
            tramo.setEstado(TramoEstado.ASIGNADO);
            
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

    public TrackingDTO trackingSolicitud(Integer solicitudId) {

        Ruta ruta = rutaRepo.findBySolicitudId(solicitudId);

        List<TramoDTO> tramos = tramoService.getTramosByRutaOrdenados(ruta);
        if (tramos.isEmpty()) {
            throw new RuntimeException("La ruta no tiene tramos...");
        }

        int tramosCompletados = 0;
        TramoDTO tramoActual = null;
        SolicitudEstado estado = SolicitudEstado.PENDIENTE_ASIGNACION;

        //Buscamos el estado actual revisando los estados de los tramos en orden
        for (TramoDTO tramo : tramos) {
            if (tramo.getEstado() == TramoEstado.CANCELADO) {
                estado = SolicitudEstado.CANCELADA;
                tramoActual = tramo;
                break; //Si está cancelado, no importa el resto
            }
            if (tramo.getEstado() == TramoEstado.INICIADO) {
                estado = SolicitudEstado.EN_TRANSITO;
                tramoActual = tramo;
                break; //Si está iniciado, no importa el resto
            }
            if (tramo.getEstado() == TramoEstado.FINALIZADO) {
                tramosCompletados++; //Contamos los tramos finalizados
            }
            //Si no está en ninguno de los otros 3 estados, puede ser ASIGNADO o ESTIMADO
            if (tramoActual == null && (tramo.getEstado() == TramoEstado.ASIGNADO || tramo.getEstado() == TramoEstado.ESTIMADO)) {
                tramoActual = tramo;
                estado = (tramo.getEstado() == TramoEstado.ASIGNADO) ? SolicitudEstado.LISTA_PARA_INICIAR : SolicitudEstado.PENDIENTE_ASIGNACION;
                break; //Si encontramos el primer tramo no iniciado, lo tomamos como actual
            }
        }

        if (tramosCompletados == tramos.size()) {
            estado = SolicitudEstado.FINALIZADA;
            tramoActual = tramos.get(tramos.size() - 1); //El último tramo
        }

        if (tramoActual == null) { // Requete por las dudas, no deberia pasar nunca
            throw new RuntimeException("No se pudo determinar el tramo actual.");
        }

        String desc = "Upsi, perdimos tu contenedor, disculpa :(, pero seguinos eligiendo :D";
        try {
            // Buscamos para poder armar la descripcion linda
            String origen = rutasServiceClient.getUbicacionById(tramoActual.getOrigenId()).getDireccionTextual();
            String destino = rutasServiceClient.getUbicacionById(tramoActual.getDestinoId()).getDireccionTextual();

            if (estado == SolicitudEstado.EN_TRANSITO) {
                desc = "En viaje de " + origen + " a " + destino;
            }
            else if (estado == SolicitudEstado.LISTA_PARA_INICIAR) {
                desc = "Listo para iniciar viaje desde " + origen + " hacia " + destino;
            }
            else if (estado == SolicitudEstado.PENDIENTE_ASIGNACION) {
                desc = "Todavia no lo asignamos, pero está en " + origen;
            }
            else if (estado == SolicitudEstado.FINALIZADA) {
                desc = "Ya llegó a " + destino;
            }
            else if (estado == SolicitudEstado.CANCELADA) {
                desc = "Ya la cancelaste, está en " + origen;
            }

        } catch (Exception e) {
            desc = "No lo perdimos, pero no lo encontramos en este momento, no me anda el servicio, intenta mas tarde. Disculpa :(";
        }
        return new TrackingDTO(
            solicitudId,
            estado,
            desc,
            tramoActual.getCamionDominio(),
            tramosCompletados,
            tramos.size()
        );
    }

    @Transactional
    public TramoDTO actualizarEstadoDeTramo(TramoEstadoPatchDTO tramoEstadoPatchDTO, Integer tramoId, Integer solicitudId) {
        TramoEstado estadoNuevoTramo = tramoEstadoPatchDTO.getNuevoEstado();
        String dominioCamion = tramoEstadoPatchDTO.getDominioCamion();
        Tramo tramo = tramoService.getTramoById(tramoId);
        Solicitud solicitud = solicitudRepo.findById(solicitudId).orElse(null);
        if (tramo == null) { // Validamos si existe tramo
            throw new RuntimeException("Tramo no encontrado");
        }
        if (!tramo.getRuta().getSolicitud().getId().equals(solicitudId)) { // Validamos que el tramo pertenezca a la solicitud
            throw new SecurityException("El tramo no pertenece a la solicitud.");
        }

        Contenedor contenedor = solicitud.getContenedor();

        if (estadoNuevoTramo == TramoEstado.INICIADO && (tramo.getTipo() == TramoTipo.DEPOSITO_DEPOSITO || tramo.getTipo() == TramoTipo.DEPOSITO_DESTINO)) {
            ParadaEnDepositoDTO parada = paradaEnDepositoService.getParadaEnDepositoByRutaAndOrden(tramo.getRuta(), tramo.getOrdenEnRuta() - 1);

            paradaEnDepositoService.guardarTiempoEstadia(parada);
            contenedor.setEstado(ContenedorEstado.EN_TRANSITO);
            contenedorService.save(contenedor);
        }
        if (estadoNuevoTramo == TramoEstado.FINALIZADO) {
            if (tramo.getTipo() == TramoTipo.DEPOSITO_DEPOSITO || tramo.getTipo() == TramoTipo.ORIGEN_DEPOSITO) {
                ParadaEnDepositoDTO parada = paradaEnDepositoService.getParadaEnDepositoByRutaAndOrden(tramo.getRuta(), tramo.getOrdenEnRuta());
                parada.setFechaHoraLlegada(LocalDateTime.now());
                paradaEnDepositoService.save(paradaEnDepositoService.toParada(parada));
                contenedor.setEstado(ContenedorEstado.EN_DEPOSITO);
                contenedorService.save(contenedor);
            }
            if (tramo.getTipo() == TramoTipo.DEPOSITO_DESTINO) {
                CalcularDefResponseDTO defResponse = calcularDef(solicitud);
                solicitud.setTiempoReal(defResponse.getSegundos()/3600 + " horas");
                solicitud.setCostoFinal(defResponse.getCostoTotal());
                for (ParadaEnDepositoDTO paradaDto : defResponse.getParadasEnDepositoConCosto()) {
                    ParadaEnDeposito parada = paradaEnDepositoService.toParada(paradaDto);
                    paradaEnDepositoService.save(parada);
                }
                for (TramoDTO tramoDto : defResponse.getTramosConCosto()) {
                    Tramo tramoAActualizar = tramoService.toTramo(tramoDto);
                    tramoService.save(tramoAActualizar);
                }
                contenedor.setEstado(ContenedorEstado.DISPONIBLE);
                contenedorService.save(contenedor);
            }
        }
        // Le decimos al tramo que se actualice
        TramoDTO tramoActualizado = tramoService.actualizarEstado(tramoId, estadoNuevoTramo, dominioCamion);
        return tramoActualizado;
    }

    public CalcularDefResponseDTO calcularDef(Solicitud solicitud) {
        Ruta ruta = rutaRepo.findBySolicitudId(solicitud.getId());
        List<TramoDTO> tramos = tramoService.getTramosByRutaOrdenados(ruta);
        List<ParadaEnDepositoDTO> paradas = paradaEnDepositoService.getParadaEnDepositoByRuta(ruta);
        CalcularDefRequestDTO peticion = new CalcularDefRequestDTO(tramos, paradas);
        CalcularDefResponseDTO respuesta = rutasServiceClient.calcularCostoDefinitivo(peticion);
        return respuesta;
    }
}
