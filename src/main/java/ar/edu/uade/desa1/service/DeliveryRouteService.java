package ar.edu.uade.desa1.service;

import ar.edu.uade.desa1.domain.entity.DeliveryRoute;
import ar.edu.uade.desa1.domain.request.CreateRouteRequest;
import ar.edu.uade.desa1.domain.request.UpdateRouteStatusRequest;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;

import java.util.List;

public interface DeliveryRouteService {
    DeliveryRoute createRoute(CreateRouteRequest request);

    List<DeliveryRouteResponse> getAllRoutes();

    List<DeliveryRouteResponse> getAllRoutesByUserId(Long userId);

    DeliveryRoute getRouteById(Long id);

    DeliveryRouteResponse updateRouteStatus(UpdateRouteStatusRequest request);

    List<DeliveryRouteResponse> getCompletedRoutesByUser(Long userId);

    List<DeliveryRouteResponse> getAllRoutesByDeliveryUserId(Long deliveryUserId);
}
