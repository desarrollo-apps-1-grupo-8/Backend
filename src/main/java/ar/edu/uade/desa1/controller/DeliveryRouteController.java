package ar.edu.uade.desa1.controller;

import ar.edu.uade.desa1.domain.entity.DeliveryRoute;
import ar.edu.uade.desa1.domain.request.CreateRouteRequest;
import ar.edu.uade.desa1.domain.request.UpdateRouteStatusRequest;
import ar.edu.uade.desa1.service.DeliveryRouteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
public class DeliveryRouteController {

    @Autowired
    private DeliveryRouteService deliveryRouteService;

    @PostMapping
    public ResponseEntity<DeliveryRoute> createRoute(@RequestBody CreateRouteRequest request) {
        return ResponseEntity.ok(deliveryRouteService.createRoute(request));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryRoute>> getAllRoutes() {
        return ResponseEntity.ok(deliveryRouteService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryRoute> getRouteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(deliveryRouteService.getRouteById(id));
    }

    @PostMapping("/update-status")
    public ResponseEntity<DeliveryRoute> updateRouteStatus(@RequestBody UpdateRouteStatusRequest request) {
        return ResponseEntity.ok(deliveryRouteService.updateRouteStatus(
            request.getDeliveryRouteId(), 
            request.getStatus(),
            request.getDeliveryUserId()
        ));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<DeliveryRoute>> getCompletedRoutes(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(deliveryRouteService.getCompletedRoutesByUser(userId));
    }
}
