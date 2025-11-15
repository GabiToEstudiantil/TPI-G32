package ar.edu.frc.utn.bda.k7.solicitudes.domain;

public enum SolicitudEstado {
    PENDIENTE_ASIGNACION, // Todos los tramos están en ESTIMADO
    LISTA_PARA_INICIAR,   // Todos los tramos están en ASIGNADO
    EN_TRANSITO,          // Al menos un tramo está INICIADO
    FINALIZADA,           // Todos los tramos están FINALIZADO
    CANCELADA             // Al menos un tramo está CANCELADO
}
