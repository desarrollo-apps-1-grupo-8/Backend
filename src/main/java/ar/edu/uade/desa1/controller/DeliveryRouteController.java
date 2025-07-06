package ar.edu.uade.desa1.controller;

import ar.edu.uade.desa1.domain.entity.DeliveryRoute;

import ar.edu.uade.desa1.domain.enums.RouteStatus;

import ar.edu.uade.desa1.domain.request.CreateRouteRequest;
import ar.edu.uade.desa1.domain.request.UpdateRouteStatusRequest;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;
import ar.edu.uade.desa1.service.DeliveryRouteService;


import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import ar.edu.uade.desa1.service.TokenStorageService;
import ar.edu.uade.desa1.service.FirebaseMessagingService;



import ar.edu.uade.desa1.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class DeliveryRouteController {

    private final DeliveryRouteService deliveryRouteService;

    private final TokenStorageService tokenStorageService;
    private final FirebaseMessagingService firebaseMessagingService;

    @PostMapping
    public ResponseEntity<DeliveryRoute> createRoute(@RequestBody CreateRouteRequest request) {
    DeliveryRoute createdRoute = deliveryRouteService.createRoute(request);
    return ResponseEntity.ok(createdRoute);
}


    private final QrCodeService qrCodeService;




    @GetMapping
    public ResponseEntity<List<DeliveryRouteResponse>> getAllRoutes() {
        return ResponseEntity.ok(deliveryRouteService.getAllRoutes());
    }

    @GetMapping("/deliveryUser/{deliveryUserId}")
    public ResponseEntity<List<DeliveryRouteResponse>> getAllRoutesByDeliveryUserId(@PathVariable("deliveryUserId") Long deliveryUserId) {
        return ResponseEntity.ok(deliveryRouteService.getAllRoutesByDeliveryUserId(deliveryUserId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DeliveryRouteResponse>> getAllRoutesByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(deliveryRouteService.getAllRoutesByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryRoute> getRouteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(deliveryRouteService.getRouteById(id));
    }

    @PostMapping("/update-status")

    public ResponseEntity<?> updateRouteStatus(@RequestBody UpdateRouteStatusRequest request) {
        return ResponseEntity.ok(deliveryRouteService.updateRouteStatus(
            request.getDeliveryRouteId(), 
            request.getStatus(),
            request.getDeliveryUserId(),
            request.getCompletionCode()
        ));
    }


    @GetMapping("/history/{userId}")
    public ResponseEntity<List<DeliveryRouteResponse>> getCompletedRoutes(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(deliveryRouteService.getCompletedRoutesByUser(userId));
    }


    @GetMapping("/qr")
    public ResponseEntity<Map<String, String>> generateQrCode(@RequestParam("routeId") Long routeId) {

        String base64Image = qrCodeService.generateQRCodeAsBase64(routeId);
        Map<String, String> response = new HashMap<>();
        response.put("qrCodeBase64", base64Image);

        return ResponseEntity.ok(response);
    }

}

